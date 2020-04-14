package com.haroldadmin.moonshot.database.test

import com.haroldadmin.moonshot.database.LaunchDao
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.LaunchPictures
import javax.inject.Inject

private typealias FlightNumber = Int

class FakeLaunchDao @Inject constructor() : LaunchDao() {

    private val launches = mutableMapOf<FlightNumber, Launch>()

    fun seedWith(vararg seedData: Launch) {
        seedData
            .map { it.flightNumber to it }
            .forEach { (flightNumber, launch) ->
                launches[flightNumber] = launch
            }
    }

    fun clear() {
        launches.clear()
    }

    override suspend fun details(flightNumber: FlightNumber): Launch? {
        return launches[flightNumber]
    }

    override suspend fun all(limit: Int, offset: Int): List<Launch> {
        if (offset > launches.size) {
            return emptyList()
        }
        return launches.values
            .toList()
            .subList(offset, (offset + limit).coerceAtMost(launches.size))
    }

    override suspend fun upcoming(limit: Int, offset: Int): List<Launch> {
        return all(limit, offset)
            .filter { it.isUpcoming == true }
    }

    override suspend fun upcoming(until: Long, limit: Int, offset: Int): List<Launch> {
        return upcoming(limit, offset)
            .filter { it.launchDateUtc.time < until }
    }

    override suspend fun recent(limit: Int, offset: Int): List<Launch> {
        return all(limit, offset)
            .filter { it.isUpcoming == false }
    }

    override suspend fun forLaunchPad(siteId: String, limit: Int, offset: Int): List<Launch> {
        return all(limit, offset)
            .filter { it.launchSite?.siteId == siteId }
    }

    override suspend fun forLaunchPad(siteId: String, isUpcoming: Boolean, limit: Int, offset: Int): List<Launch> {
        return forLaunchPad(siteId, limit, offset)
            .filter { it.isUpcoming == isUpcoming }
    }

    override suspend fun pictures(flightNumber: FlightNumber): LaunchPictures? {
        return LaunchPictures(details(flightNumber)?.links?.flickrImages)
    }

    override suspend fun forQuery(query: String, limit: Int, offset: Int): List<Launch> {
        return all(limit, offset)
            .filter { it.missionName.contains(query) }
    }

    override suspend fun next(): Launch? {
        return upcoming(
            limit = Int.MAX_VALUE,
            offset = 0
        ).firstOrNull()
    }

    override suspend fun clearTable() {
        launches.clear()
    }

    override suspend fun save(obj: Launch) {
        launches[obj.flightNumber] = obj
    }

    override suspend fun saveAll(objs: List<Launch>) {
        objs.forEach { save(it) }
    }

    override suspend fun update(obj: Launch) {
        save(obj)
    }

    override suspend fun updateAll(objs: List<Launch>) {
        objs.forEach { update(it) }
    }

    override suspend fun delete(obj: Launch) {
        launches.remove(obj.flightNumber)
    }

    override suspend fun deleteAll(objs: List<Launch>) {
        objs.forEach { delete(it) }
    }
}