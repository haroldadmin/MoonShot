package com.haroldadmin.moonshot.database.test

import com.haroldadmin.moonshot.database.LaunchDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.LaunchPictures
import javax.inject.Inject

class FakeLaunchDao @Inject constructor(): LaunchDao() {

    private val launches = mutableListOf<Launch>()

    fun seedWith(vararg seedData: Launch) {
        launches.addAll(seedData)
    }

    fun clear() {
        launches.clear()
    }

    override suspend fun details(flightNumber: Int): Launch? {
        return launches.find { it.flightNumber == flightNumber }
    }

    override suspend fun all(limit: Int, offset: Int): List<Launch> {
        if (offset > launches.size) { return emptyList() }
        return launches.subList(offset, (offset + limit).coerceAtMost(launches.size))
    }

    override suspend fun upcoming(limit: Int, offset: Int): List<Launch> {
        return all(limit, offset).filter { it.isUpcoming == true }
    }

    override suspend fun upcoming(until: Long, limit: Int, offset: Int): List<Launch> {
        return upcoming(limit, offset).filter { it.launchDateUtc.time < until }
    }

    override suspend fun recent(limit: Int, offset: Int): List<Launch> {
        return all(limit, offset).filter { it.isUpcoming == false }
    }

    override suspend fun forLaunchPad(siteId: String, limit: Int, offset: Int): List<Launch> {
        return all(limit, offset).filter { it.launchSite?.siteId == siteId }
    }

    override suspend fun forLaunchPad(siteId: String, isUpcoming: Boolean, limit: Int, offset: Int): List<Launch> {
        return forLaunchPad(siteId, limit, offset).filter { it.isUpcoming == isUpcoming }
    }

    override suspend fun pictures(flightNumber: Int): LaunchPictures? {
        return LaunchPictures(details(flightNumber)?.links?.flickrImages)
    }

    override suspend fun forQuery(query: String, limit: Int, offset: Int): List<Launch> {
        return all(limit, offset).filter { it.missionName.contains(query) }
    }

    override suspend fun next(): Launch? {
        return upcoming(Int.MAX_VALUE, 0).firstOrNull()
    }

    override suspend fun clearTable() {
        launches.clear()
    }

    override suspend fun save(obj: Launch) {
        launches.add(obj)
    }

    override suspend fun saveAll(objs: List<Launch>) {
        launches.addAll(objs)
    }

    override suspend fun update(obj: Launch) {
        val index = launches.indexOfFirst { it.flightNumber == obj.flightNumber }
        launches[index] = obj
    }

    override suspend fun updateAll(objs: List<Launch>) {
        objs.forEach { update(it) }
    }

    override suspend fun delete(obj: Launch) {
        launches.remove(obj)
    }

    override suspend fun deleteAll(objs: List<Launch>) {
        launches.removeAll(objs)
    }
}