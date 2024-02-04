/*
 * This file is generated by jOOQ.
 */
package db.indexes


import db.tables.FlywaySchemaHistory

import org.jooq.Index
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// INDEX definitions
// -------------------------------------------------------------------------

val FLYWAY_SCHEMA_HISTORY_S_IDX: Index = Internal.createIndex(DSL.name("flyway_schema_history_s_idx"), FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, arrayOf(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.SUCCESS), false)
