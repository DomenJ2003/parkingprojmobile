package com.example.parkingprojmobile.api

import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttProvider(
    private val clientId: String,
    private val token: String
) {
    private var mqttClient: MqttClient? = null
    var isConnected: Boolean = false

    fun connect(): Boolean {
        return try {
            mqttClient = MqttClient(Constants.MQTT_URL, clientId, MemoryPersistence())

            val connOpts = MqttConnectOptions().apply {
                isCleanSession = true
                userName = "Bearer" // Add any username if required, often optional for token-based auth
                password = token.toCharArray() // Add token in the password field
            }

            // Connect to the broker
            mqttClient?.connect(connOpts)
            println("Connected to MQTT Broker: ${Constants.MQTT_URL}")
            isConnected = true
            true
        } catch (e: MqttException) {
            e.printStackTrace()
            isConnected = false
            false
        }
    }

    fun publish(message: String, qos: Int = 1): Boolean {
        return try {
            val mqttMessage = MqttMessage(message.toByteArray()).apply {
                this.qos = qos
            }
            mqttClient?.publish(Constants.MQTT_TOPIC, mqttMessage)
            println("Message published: $message")
            true
        } catch (e: MqttException) {
            e.printStackTrace()
            false
        }
    }

    fun subscribe(qos: Int = 1, callback: (message: String) -> Unit): Boolean {
        return try {
            mqttClient?.setCallback(object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    println("Connection lost: ${cause?.message}")
                }

                override fun messageArrived(topic: String, message: MqttMessage) {
                    callback(message.toString())
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    println("Delivery complete: ${token?.message}")
                }
            })

            mqttClient?.subscribe(Constants.MQTT_TOPIC, qos)
            println("Subscribed to topic ${Constants.MQTT_TOPIC} with QoS $qos")
            true
        } catch (e: MqttException) {
            e.printStackTrace()
            false
        }
    }

    fun disconnect(): Boolean {
        return try {
            mqttClient?.disconnect()
            println("Disconnected from MQTT Broker")
            isConnected = false
            true
        } catch (e: MqttException) {
            e.printStackTrace()
            false
        }
    }
}