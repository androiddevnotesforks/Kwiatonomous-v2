package com.corrot

import com.corrot.Constants.DEFAULT_BASE_URL
import com.corrot.Constants.DEFAULT_DB_FILE_PATH
import com.corrot.Constants.DEFAULT_MQTT_BROKER_URL
import com.corrot.Constants.DEFAULT_PORT

object Configuration {
    var baseUrl = DEFAULT_BASE_URL
    var port = DEFAULT_PORT
    var mqttBrokerUrl = DEFAULT_MQTT_BROKER_URL
    var dbFilePath = DEFAULT_DB_FILE_PATH
}
