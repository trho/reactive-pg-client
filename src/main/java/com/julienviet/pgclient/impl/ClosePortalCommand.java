/*
 * Copyright (C) 2017 Julien Viet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.julienviet.pgclient.impl;

import com.julienviet.pgclient.impl.codec.decoder.DecodeContext;
import com.julienviet.pgclient.impl.codec.decoder.InboundMessage;
import com.julienviet.pgclient.impl.codec.decoder.message.CloseComplete;
import com.julienviet.pgclient.impl.codec.encoder.message.Close;
import com.julienviet.pgclient.impl.codec.encoder.message.Sync;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class ClosePortalCommand extends CommandBase<Void> {

  private final String portal;

  ClosePortalCommand(String portal, Handler<AsyncResult<Void>> handler) {
    super(handler);
    this.portal = portal;
  }

  @Override
  void exec(SocketConnection conn) {
    conn.decodeQueue.add(new DecodeContext(false, null, null, null));
    conn.writeMessage(new Close().setPortal(portal));
    conn.writeMessage(Sync.INSTANCE);
  }

  @Override
  public void handleMessage(InboundMessage msg) {
    if (msg.getClass() == CloseComplete.class) {
      handler.handle(Future.succeededFuture());
    } else {
      super.handleMessage(msg);
    }
  }

  @Override
  void fail(Throwable err) {
    handler.handle(Future.failedFuture(err));
  }
}
