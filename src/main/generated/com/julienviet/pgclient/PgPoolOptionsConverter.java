/*
 * Copyright (c) 2014 Red Hat, Inc. and others
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.julienviet.pgclient;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link com.julienviet.pgclient.PgPoolOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link com.julienviet.pgclient.PgPoolOptions} original class using Vert.x codegen.
 */
public class PgPoolOptionsConverter {

  public static void fromJson(JsonObject json, PgPoolOptions obj) {
    if (json.getValue("maxSize") instanceof Number) {
      obj.setMaxSize(((Number)json.getValue("maxSize")).intValue());
    }
  }

  public static void toJson(PgPoolOptions obj, JsonObject json) {
    json.put("maxSize", obj.getMaxSize());
  }
}