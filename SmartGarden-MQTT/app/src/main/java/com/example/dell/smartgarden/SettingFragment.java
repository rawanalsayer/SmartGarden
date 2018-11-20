package com.example.dell.smartgarden;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class SettingFragment extends Fragment {

    Button btnSignOut,exitButton;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog PD;

    private static final String TAG = "EmailPassword";
    PahoMqttClient pahoMqttClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false); }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSignOut = (Button) getActivity().findViewById(R.id.sign_out_button);
        exitButton= (Button) getActivity().findViewById(R.id.exit_button);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    auth.signOut();
                    Toast.makeText(getActivity(), "User Sign out!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);

                    Intent intent2 = new Intent(getActivity(), MqttMessageService.class);
                    getActivity().stopService(intent2);

                    MqttMessageService m = new MqttMessageService();

                    if (m.mqttAndroidClient != null) {
                        m.mqttAndroidClient.unregisterResources();
                        m.mqttAndroidClient.close();
                        m.mqttAndroidClient = null;
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onClick: Exception " + e.getMessage(), e);
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        PD = new ProgressDialog(getActivity());
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        getActivity().findViewById(R.id.change_password_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 1));
            }
        });

        getActivity().findViewById(R.id.change_email_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 2));
            }
        });

        getActivity().findViewById(R.id.delete_user_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 3));
            }
        });

        getActivity().findViewById(R.id.about_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), about_us.class));
            }
        });

        getActivity().findViewById(R.id.contact_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:smartgardenproject@gmail.com");
                intent.setData(data);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        super.onResume();
    }
}