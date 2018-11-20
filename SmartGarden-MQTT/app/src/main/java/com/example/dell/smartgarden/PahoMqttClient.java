package com.example.dell.smartgarden;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;

import static com.example.dell.smartgarden.Constants.MQTT_USERNAME;
import static com.example.dell.smartgarden.Constants.MQTT_PASSWORD;
import static com.example.dell.smartgarden.Constants.PUBLISH_TOPIC_LEFT_FAN;
import static com.example.dell.smartgarden.Constants.PUBLISH_TOPIC_LIGHT;
import static com.example.dell.smartgarden.Constants.PUBLISH_TOPIC_PUMP;
import static com.example.dell.smartgarden.Constants.PUBLISH_TOPIC_RIGHT_FAN;
import static com.example.dell.smartgarden.Constants.QOS;
import static com.example.dell.smartgarden.Constants.SUBSCRIBE_TOPIC_HUMIDITY;
import static com.example.dell.smartgarden.Constants.SUBSCRIBE_TOPIC_LIGHT_SENSOR;
import static com.example.dell.smartgarden.Constants.SUBSCRIBE_TOPIC_SOILMOISTURE;
import static com.example.dell.smartgarden.Constants.SUBSCRIBE_TOPIC_TEMPERTURE;
import static com.example.dell.smartgarden.Constants.SUBSCRIBE_TOPIC_WATER_LEVEL;
public class PahoMqttClient {

    private static final String TAG = "PahoMqttClient";
    public MqttAndroidClient mqttAndroidClient;

    // Return the Mqtt client
    public MqttAndroidClient getMqttClient(Context context, String brokerUrl, String clientId) {

        mqttAndroidClient = new MqttAndroidClient(context, brokerUrl, clientId);
        MqttConnectOptions connOpts;
        try {
            connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);
            connOpts.setAutomaticReconnect(true);
            connOpts.setKeepAliveInterval(9000);
            connOpts.setUserName(MQTT_USERNAME);
            connOpts.setPassword(MQTT_PASSWORD.toCharArray());
            //Connect to the broker
            IMqttToken token = mqttAndroidClient.connect(connOpts);
            //If the client connected then subscribe to the topics
            if (mqttAndroidClient.isConnected()) {
                subscribe();
            }

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Successfuly connected");
                    // use a buffer for the offline client$
                    mqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions());
                    subscribe();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Failure  to connect" + exception.toString());
                }
            });

        } catch (MqttException ex) {
            ex.printStackTrace();
        }
        return mqttAndroidClient;
    }

    // disconnect the client from broker
    public void disconnect(@NonNull MqttAndroidClient client) throws MqttException {
        IMqttToken mqttToken = client.disconnect();
        mqttToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "Successfully disconnected");
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.d(TAG, "Failed to disconnected " + throwable.toString());
            }
        });
    }
    // offline buffer specification
    @NonNull
    private DisconnectedBufferOptions getDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(false);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }

    //publish to the broker
    public void publishMessage(@NonNull MqttAndroidClient client, @NonNull String msg, int qos, @NonNull String topic)
            throws MqttException, UnsupportedEncodingException {
        byte[] encodedPayload = new byte[0];
        //convert the mesage from string to byte
        encodedPayload = msg.getBytes("UTF-8");
        MqttMessage message = new MqttMessage(encodedPayload);
        message.setRetained(true);
        message.setQos(qos);
        client.publish(topic, message);
    }

    //subscribe
    public void subscribe() {
        try {
            mqttAndroidClient.subscribe(SUBSCRIBE_TOPIC_LIGHT_SENSOR, QOS);
            mqttAndroidClient.subscribe(SUBSCRIBE_TOPIC_WATER_LEVEL, QOS);
            mqttAndroidClient.subscribe(SUBSCRIBE_TOPIC_SOILMOISTURE, QOS);
            mqttAndroidClient.subscribe(SUBSCRIBE_TOPIC_TEMPERTURE, QOS);
            mqttAndroidClient.subscribe(SUBSCRIBE_TOPIC_HUMIDITY, QOS);

            mqttAndroidClient.subscribe(PUBLISH_TOPIC_LEFT_FAN, QOS);
            mqttAndroidClient.subscribe(PUBLISH_TOPIC_RIGHT_FAN, QOS);
            mqttAndroidClient.subscribe(PUBLISH_TOPIC_PUMP, QOS);
            mqttAndroidClient.subscribe(PUBLISH_TOPIC_LIGHT, QOS);


            Log.d(TAG, "Subscribe Successfully");
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


    public void Sub(@NonNull MqttAndroidClient client, @NonNull final String topic, int qos)throws MqttException {
        IMqttToken token = client.subscribe(topic, qos);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "Subscribe Successfully " + topic);
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.e(TAG, "Subscribe Failed " + topic);

            }
        });
    }

}