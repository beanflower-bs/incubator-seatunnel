/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.seatunnel.api.table.catalog;

import java.io.Serializable;
import java.util.Objects;

public final class TablePath implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String databaseName;
    private final String schemaName;
    private final String tableName;

    private TablePath(String databaseName, String tableName) {
        this(databaseName, null, tableName);
    }

    private TablePath(String databaseName, String schemaName, String tableName) {
        this.databaseName = databaseName;
        this.schemaName = schemaName;
        this.tableName = tableName;
    }

    public static TablePath of(String fullName) {
        String[] paths = fullName.split("\\.");

        if (paths.length == 2) {
            return new TablePath(paths[0], paths[1]);
        }
        if (paths.length == 3) {
            return new TablePath(paths[0], paths[1], paths[2]);
        }
        throw new IllegalArgumentException(
                String.format("Cannot get split '%s' to get databaseName and tableName", fullName));
    }

    public static TablePath of(String databaseName, String tableName) {
        return of(databaseName, null, tableName);
    }

    public static TablePath of(String databaseName, String schemaName, String tableName) {
        return new TablePath(databaseName, schemaName, tableName);
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getTableName() {
        if (schemaName == null) {
            return tableName;
        }
        return String.format("%s.%s", schemaName, tableName);
    }

    public String getFullName() {
        if (schemaName == null) {
            return String.format("%s.%s", databaseName, tableName);
        }
        return String.format("%s.%s.%s", databaseName, schemaName, tableName);
    }

    public String getFullNameWithQuoted() {
        return getFullNameWithQuoted("`");
    }

    public String getFullNameWithQuoted(String quote) {
        if (schemaName == null) {
            return String.format(
                    "%s%s%s.%s%s%s", quote, databaseName, quote, quote, tableName, quote);
        }
        return String.format(
                "%s%s%s.%s%s%s.%s%s%s",
                quote, databaseName, quote, quote, schemaName, quote, quote, tableName, quote);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TablePath that = (TablePath) o;

        return Objects.equals(databaseName, that.databaseName)
                && Objects.equals(schemaName, that.schemaName)
                && Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(databaseName, schemaName, tableName);
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
