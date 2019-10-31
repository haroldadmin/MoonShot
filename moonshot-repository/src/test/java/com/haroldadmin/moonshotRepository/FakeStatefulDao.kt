package com.haroldadmin.moonshotRepository

import com.haroldadmin.moonshot.database.BaseDao

internal interface FakeStatefulDao<T>: BaseDao<T> {

    val items: MutableList<T>

    override suspend fun save(obj: T) {
        items.add(obj)
    }

    override suspend fun saveAll(objs: List<T>) {
        items.addAll(objs)
    }

    override suspend fun update(obj: T) {
        val index = items.indexOfFirst { it == obj }
        items[index] = obj
    }

    override suspend fun updateAll(objs: List<T>) {
        objs.forEach { update(it) }
    }

    override suspend fun delete(obj: T) {
        items.remove(obj)
    }

    override suspend fun deleteAll(objs: List<T>) {
        items.removeAll(objs)
    }
}