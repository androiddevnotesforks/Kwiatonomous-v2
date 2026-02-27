package com.corrot

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default

object Args {

    fun parse(args: Array<String>) {
        val parser = ArgParser("kwiatonomous-backend")

        val baseUrl by parser.option(
            type = ArgType.String,
            shortName = "url",
            description = "Base URL"
        ).default(Constants.DEFAULT_BASE_URL)

        val port by parser.option(
            type = ArgType.Int,
            shortName = "p",
            description = "Port number"
        ).default(Constants.DEFAULT_PORT)

        val mqttBrokerUrl by parser.option(
            type = ArgType.String,
            shortName = "mqtt",
            description = "MQTT Broker URL"
        ).default(Constants.DEFAULT_MQTT_BROKER_URL)

        val dbFilePath by parser.option(
            type = ArgType.String,
            shortName = "db",
            description = "Database file path"
        ).default(Constants.DEFAULT_DB_FILE_PATH)

        parser.parse(args)

        Configuration.baseUrl = baseUrl
        Configuration.port = port
        Configuration.mqttBrokerUrl = mqttBrokerUrl
        Configuration.dbFilePath = dbFilePath
    }
}
