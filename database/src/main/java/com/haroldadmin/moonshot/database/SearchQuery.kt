package com.haroldadmin.moonshot.database

/**
 * Wraps a regular string to produce a SQLite compatible `LIKE` query
 *
 * For example, SELECT * FROM launches WHERE mission_name LIKE %starlink%
 */
inline class SearchQuery(val query: String) {
    fun sqlQuery(): String = "%$query%"
}