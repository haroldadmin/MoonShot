package com.haroldadmin.moonshot.db

import com.squareup.sqldelight.ColumnAdapter

internal class LaunchIDAdapter : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String): List<String> {
        if (databaseValue == "") {
            return emptyList()
        }
        return databaseValue.split(',')
    }

    override fun encode(value: List<String>): String {
        return value.joinToString(",")
    }

}