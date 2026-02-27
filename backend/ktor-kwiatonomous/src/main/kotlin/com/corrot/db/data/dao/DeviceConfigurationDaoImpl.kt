package com.corrot.db.data.dao

import com.corrot.db.DevicesConfigurations
import com.corrot.db.KwiatonomousDatabase
import com.corrot.db.data.model.DeviceConfiguration
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

class DeviceConfigurationDaoImpl(private val database: KwiatonomousDatabase) : DeviceConfigurationDao {

    init {
        transaction(database.db) {
            arrayOf<Table>(DevicesConfigurations)
            SchemaUtils.create(DevicesConfigurations)
        }
    }

    override fun getDeviceConfiguration(deviceId: String): DeviceConfiguration? = transaction(database.db) {
        DevicesConfigurations.selectAll().where { DevicesConfigurations.deviceId eq deviceId }.map {
            DeviceConfiguration(
                deviceId = it[DevicesConfigurations.deviceId],
                sleepTimeMinutes = it[DevicesConfigurations.sleepTimeMinutes],
                timeZoneOffset = it[DevicesConfigurations.timeZoneOffset],
                wateringOn = it[DevicesConfigurations.wateringOn],
                wateringIntervalDays = it[DevicesConfigurations.wateringIntervalDays],
                wateringAmount = it[DevicesConfigurations.wateringAmount],
                wateringTime = it[DevicesConfigurations.wateringTime]
            )
        }.singleOrNull()
    }

    override fun createDeviceConfiguration(
        deviceId: String,
        sleepTimeMinutes: Int,
        timeZoneOffset: Int,
        wateringOn: Boolean,
        wateringIntervalDays: Int,
        wateringAmount: Int,
        wateringTime: String
    ): Unit = transaction(database.db) {
        DevicesConfigurations.insert {
            it[DevicesConfigurations.deviceId] = deviceId
            it[DevicesConfigurations.sleepTimeMinutes] = sleepTimeMinutes
            it[DevicesConfigurations.timeZoneOffset] = timeZoneOffset
            it[DevicesConfigurations.wateringOn] = wateringOn
            it[DevicesConfigurations.wateringIntervalDays] = wateringIntervalDays
            it[DevicesConfigurations.wateringAmount] = wateringAmount
            it[DevicesConfigurations.wateringTime] = wateringTime
        }
    }

    override fun updateDeviceConfiguration(deviceConfiguration: DeviceConfiguration): Unit = transaction(database.db) {
        DevicesConfigurations.update(
            where = { DevicesConfigurations.deviceId eq deviceConfiguration.deviceId },
            body = {
                it[deviceId] = deviceConfiguration.deviceId
                it[sleepTimeMinutes] = deviceConfiguration.sleepTimeMinutes
                it[timeZoneOffset] = deviceConfiguration.timeZoneOffset
                it[wateringOn] = deviceConfiguration.wateringOn
                it[wateringIntervalDays] = deviceConfiguration.wateringIntervalDays
                it[wateringAmount] = deviceConfiguration.wateringAmount
                it[wateringTime] = deviceConfiguration.wateringTime
            })
    }
}