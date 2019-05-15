package com.app.androidkt.mqtt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.anychart.anychart.AnyChart;
//import com.anychart.anychart.AnyChartView;
//import com.anychart.anychart.DataEntry;
//import com.anychart.anychart.Pie;
//import com.anychart.anychart.ValueDataEntry;
//
//import java.util.ArrayList;
//import java.util.List;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.MqttCallback;

public class MainActivity extends AppCompatActivity {

    private MqttAndroidClient client;
    private String TAG = "MainActivity";
    private PahoMqttClient pahoMqttClient;

    private EditText textMessage, subscribeTopic, unSubscribeTopic;
    private Button publishMessage, subscribe, unSubscribe, relay1on, relay2on,relay1off,relay2off;
    private TextView text_t,text_humi ;
    private MqttCallback mqtt_callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pahoMqttClient = new PahoMqttClient();

        textMessage = (EditText) findViewById(R.id.textMessage);
        publishMessage = (Button) findViewById(R.id.publishMessage);
//        text_t   = (TextView) findViewById(R.id.textView) ;
        text_humi   =  (TextView) findViewById(R.id.text_humi);
        relay1on = (Button) findViewById(R.id.relay1on) ;
        relay2on = (Button) findViewById(R.id.relay2on);
        relay1off= (Button) findViewById(R.id.relay1off);
        relay2off =(Button) findViewById(R.id.relay2off);

        subscribe = (Button) findViewById(R.id.subscribe);
//        unSubscribe = (Button) findViewById(R.id.unSubscribe);
//
        subscribeTopic = (EditText) findViewById(R.id.subscribeTopic);
//        unSubscribeTopic = (EditText) findViewById(R.id.unSubscribeTopic);



//        Pie pie = AnyChart.pie();
//
//        List<DataEntry> data = new ArrayList<>();
//        data.add(new ValueDataEntry("John", 10000));
//        data.add(new ValueDataEntry("Jake", 12000));
//        data.add(new ValueDataEntry("Peter", 18000));
//
//        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
//        anyChartView.setChart(pie);

        mqtt_callback = new MqttCallback(){
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String mess = topic +' '+ message.toString();
//                text_t.setText(mess);
                JSONObject Jsondata = new JSONObject(message.toString());
//                JSONObject Jsondata = (JSONObject) message.toString();
                String Moisture     =  "Humi"+' '+ Jsondata.getString("Moisture") ;
                text_humi.setText(Moisture);


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        };

        client = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID, mqtt_callback);

        publishMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textMessage.getText().toString().trim();
                if (!msg.isEmpty()) {
                    try {
                        pahoMqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        relay1on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "{\"Light\":\"ON\",\"Pump\":\"UNCHANGED\"}" ;
                try {
                    pahoMqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC);
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        relay1off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "{\"Light\":\"OFF\",\"Pump\":\"UNCHANGED\"}";
                try {
                    pahoMqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC);
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        relay2on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg ="{\"Light\":\"UNCHANGED\",\"Pump\":\"ON\"}";
                try {
                    pahoMqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC);
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        relay2off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg ="{\"Light\":\"UNCHANGED\",\"Pump\":\"OFF\"}";
                try {
                    pahoMqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC);
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String topic = subscribeTopic.getText().toString().trim();
                String topic = "Sensors";
                if (!topic.isEmpty()) {
                    try {
                        pahoMqttClient.subscribe(client, topic, 1);


                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//        unSubscribe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String topic = unSubscribeTopic.getText().toString().trim();
//                if (!topic.isEmpty()) {
//                    try {
//                        pahoMqttClient.unSubscribe(client, topic);
//                    } catch (MqttException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

        Intent intent = new Intent(MainActivity.this, MqttMessageService.class);
        startService(intent);
    }
}
