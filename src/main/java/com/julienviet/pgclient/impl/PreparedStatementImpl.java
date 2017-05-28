package com.julienviet.pgclient.impl;

import com.julienviet.pgclient.PgBatch;
import com.julienviet.pgclient.PgPreparedStatement;
import com.julienviet.pgclient.PgQuery;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.sql.UpdateResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class PreparedStatementImpl implements PgPreparedStatement {

  private final DbConnection conn;
  final String sql;
  final AtomicBoolean closed = new AtomicBoolean();
  boolean parsed;
  final String stmt;

  PreparedStatementImpl(DbConnection conn, String sql, String stmt) {
    this.conn = conn;
    this.sql = sql;
    this.stmt = stmt;
  }

  @Override
  public PgQuery query() {
    return new QueryImpl(this, Collections.emptyList());
  }

  @Override
  public PgQuery query(Object param1) {
    return new QueryImpl(this, Collections.singletonList(param1));
  }

  @Override
  public PgQuery query(Object param1, Object param2) {
    return new QueryImpl(this, Arrays.asList(param1, param2));
  }

  @Override
  public PgQuery query(Object param1, Object param2, Object param3) {
    return new QueryImpl(this, Arrays.asList(param1, param2, param3));
  }

  @Override
  public PgQuery query(Object param1, Object param2, Object param3, Object param4) {
    return new QueryImpl(this, Arrays.asList(param1, param2, param3, param4));
  }

  @Override
  public PgQuery query(Object param1, Object param2, Object param3, Object param4, Object param5) {
    return new QueryImpl(this, Arrays.asList(param1, param2, param3, param4, param5));
  }

  @Override
  public PgQuery query(Object param1, Object param2, Object param3, Object param4, Object param5, Object param6) {
    return new QueryImpl(this, Arrays.asList(param1, param2, param3, param4, param5, param6));
  }

  @Override
  public PgQuery query(List<Object> params) {
    return new QueryImpl(this, params);
  }

  @Override
  public PgBatch batch() {
    return new BatchImpl(this);
  }

  @Override
  public void close() {
    close(ar -> {
    });
  }

  void execute(List<Object> params,
               int fetch,
               String portal,
               boolean suspended,
               QueryResultHandler handler) {
    boolean parse;
    if (!parsed) {
      parsed = true;
      parse = true;
    } else {
      parse = false;
    }
    conn.schedule(new PreparedQueryCommand(parse, sql, params, fetch, stmt, portal, suspended, handler));
  }

  void update(List<List<Object>> paramsList, Handler<AsyncResult<List<UpdateResult>>> handler) {
    boolean parse;
    if (!parsed) {
      parsed = true;
      parse = true;
    } else {
      parse = false;
    }
    conn.schedule(new PreparedUpdateCommand(parse, sql, stmt, paramsList, handler));
  }

  @Override
  public void close(Handler<AsyncResult<Void>> completionHandler) {
    if (closed.compareAndSet(false, true)) {
      conn.schedule(new CloseStatementCommand(this, completionHandler));
    } else {
      completionHandler.handle(Future.failedFuture("Already closed"));
    }
  }
}
