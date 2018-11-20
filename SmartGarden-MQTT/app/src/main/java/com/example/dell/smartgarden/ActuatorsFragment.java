package com.example.dell.smartgarden;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

import static com.example.dell.smartgarden.Constants.PUBLISH_TOPIC_LEFT_FAN;
import static com.example.dell.smartgarden.Constants.PUBLISH_TOPIC_LIGHT;
import static com.example.dell.smartgarden.Constants.PUBLISH_TOPIC_PUMP;
import static com.example.dell.smartgarden.Constants.PUBLISH_TOPIC_RIGHT_FAN;
import static com.example.dell.smartgarden.Constants.QOS;

public class ActuatorsFragment extends Fragment {

    public MqttAndroidClient client;
    private String TAG = "ActuatorsFragment";
    public PahoMqttClient pahoMqttClient;

    LoginActivity m;

    SharedPreferences.Editor editor;

    SharedPreferences test_name;
    private FirebaseUser user;
    private String uid;

    View view;

    private Switch WaterSwitch, LightSwitch, LeftFanSwitch, RightFanSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_actuators, container, false);

        pahoMqttClient = new PahoMqttClient();

        WaterSwitch =  view.findViewById(R.id.waterSwitch);
        LightSwitch =  view.findViewById(R.id.lightSwitch);
        LeftFanSwitch =  view.findViewById(R.id.leftFanSwitch);
        RightFanSwitch =  view.findViewById(R.id.rightFanSwitch);

        test_name = this.getActivity().getSharedPreferences("NAME", 0);
        editor = test_name.edit();

        WaterSwitch.setChecked(test_name.getBoolean("waterSwitch", false)); //false default
        LightSwitch.setChecked(test_name.getBoolean("lightSwitch", false));
        LeftFanSwitch.setChecked(test_name.getBoolean("leftFanSwitch", false));
        RightFanSwitch.setChecked(test_name.getBoolean("rightFanSwitch", false));

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        client = pahoMqttClient.getMqttClient(getActivity().getApplicationContext(), Constants.MQTT_BROKER_URL, uid);
        client.setTraceEnabled(true);

        pahoMqttClient.mqttAndroidClient.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) {

            }
            String m;
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                Log.d(TAG, "massege recived");

                m=message.toString();

                if (topic.equals(PUBLISH_TOPIC_LEFT_FAN) ||  topic.equals(PUBLISH_TOPIC_RIGHT_FAN) || topic.equals(PUBLISH_TOPIC_PUMP) || topic.equals(PUBLISH_TOPIC_LIGHT) ){
                    if(m.length()>1){
                        Toast.makeText(getActivity().getApplicationContext(),
                                m, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege recived");

                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

      //  m = new LoginActivity();
        WaterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){

                    try {
                        pahoMqttClient.publishMessage(client, "1", QOS, Constants.PUBLISH_TOPIC_PUMP);
                        if(!client.isConnected()){
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Water Pump will turned ON after the connection", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "massege published");

                        }

                    } catch (MqttException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    }

                }
                else {

                    try {
                        pahoMqttClient.publishMessage(client, "0", QOS, Constants.PUBLISH_TOPIC_PUMP);

                        if(!client.isConnected()){
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Water Pump will turned OFF after the connection", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "massege not published");
                        }

                    } catch (MqttException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    }
                }

                editor.putBoolean("waterSwitch", isChecked);
                editor.apply();

            }
        });

        LightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    try {
                        pahoMqttClient.publishMessage(client, "1", QOS, PUBLISH_TOPIC_LIGHT);
                        if(!client.isConnected()) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Light will turned ON after the connection", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "massege not published");

                        }


                    } catch (MqttException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    }

                }
                else {

                    try {
                        pahoMqttClient.publishMessage(client, "0", QOS, PUBLISH_TOPIC_LIGHT);
                        if(!client.isConnected()){
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Light will turned OFF after the connection", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "massege not published");

                        }


                    } catch (MqttException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    }
                }

                editor.putBoolean("lightSwitch", isChecked);
                editor.apply();


            }
        });

        LeftFanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    try {
                        pahoMqttClient.publishMessage(client, "1", QOS, PUBLISH_TOPIC_LEFT_FAN);
                        if(!client.isConnected()){
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Left Fan will turned ON after the connection", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "massege not published");

                        }


                    } catch (MqttException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    }

                }
                else {

                    try {
                        pahoMqttClient.publishMessage(client, "0", QOS, PUBLISH_TOPIC_LEFT_FAN);
                        if(!client.isConnected()){
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Left Fan will turned OFF after the connection", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "massege not published");

                        }


                    } catch (MqttException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    }
                }

                editor.putBoolean("leftFanSwitch", isChecked);
                editor.apply();


            }
        });


        RightFanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){

                    try {
                        pahoMqttClient.publishMessage(client, "1", QOS, PUBLISH_TOPIC_RIGHT_FAN);
                        if(!client.isConnected()){
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Right Fan will turned ON after the connection", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "massege not published");

                        }



                    } catch (MqttException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    }

                }
                else {

                    try {
                        pahoMqttClient.publishMessage(client, "0", QOS, PUBLISH_TOPIC_RIGHT_FAN);
                        if(!client.isConnected()){
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Right Fan will turned OFF after the connection", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "massege not published");

                        }


                    } catch (MqttException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "ERROR!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "massege did not published");
                    }
                }

                editor.putBoolean("rightFanSwitch", isChecked);
                editor.apply();


            }
        });

        return view;
    }
}
