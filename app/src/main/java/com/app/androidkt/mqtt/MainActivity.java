package com.app.androidkt.mqtt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


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
    private TextView text_t,text_humi,text_moisture,text_temp, text_bright ;
    private MqttCallback mqtt_callback;
    private Switch sw1, sw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pahoMqttClient = new PahoMqttClient();

        textMessage = (EditText) findViewById(R.id.textMessage);
        publishMessage = (Button) findViewById(R.id.publishMessage);
//        text_t   = (TextView) findViewById(R.id.textView) ;
        text_moisture  =  (TextView) findViewById(R.id.text_moisture_gui);
        text_humi  =  (TextView) findViewById(R.id.text_humi_gui);
        text_temp  =  (TextView) findViewById(R.id.text_temp_gui);
        text_bright  =  (TextView) findViewById(R.id.text_bright_gui);
        sw1 = (Switch) findViewById(R.id.switch_1);
        sw2 = (Switch) findViewById(R.id.switch_2);
        boolean sw1_state = sw1.isChecked();
        boolean sw2_state = sw2.isChecked();

    //
    //        relay1on = (Button) findViewById(R.id.relay1on) ;
    //        relay2on = (Button) findViewById(R.id.relay2on);
    //        relay1off= (Button) findViewById(R.id.relay1off);
    //        relay2off =(Button) findViewById(R.id.relay2off);

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
                String mess = topic + ' ' + message.toString();
                String topic_received = topic.toString();
                if (topic_received != "Sensors") {
                    //                text_t.setText(mess);
                    JSONObject Jsondata = new JSONObject(message.toString());
                    //                JSONObject Jsondata = (JSONObject) message.toString();
                    String Moisture =   "Moisture:   " + ' ' + Jsondata.getString("Moisture");
                    String Brightness = "Brightness: " + ' ' + Jsondata.getString("Brightness");
                    String Temp =       "Temp:       " + ' ' + Jsondata.getString("Temperature");
                    String Humi =       "Humi:       " + ' ' + Jsondata.getString("Humidity");

                    text_moisture.setText(Moisture);
                    text_bright.setText(Brightness);
                    text_temp.setText(Temp);
                    text_humi.setText(Humi);
                }
                else {

                    JSONObject Jsondata = new JSONObject(message.toString());
                    String Light = Jsondata.getString("Light");
                    String Pump = Jsondata.getString("Pump");
                    if (Light == "ON") {
                        sw1.setChecked(true);
                    }
                    else {
                        sw1.setChecked(false);
                    }
                    if (Pump == "OFF") {
                        sw2.setChecked(false);
                    }
                    else {
                        sw2.setChecked(true);
                    }
                }

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

        sw1.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {

                                       if (sw1.isChecked()) {
                                           String msg = "{\"Light\":\"ON\",\"Pump\":\"UNCHANGED\"}";
                                           try {
                                               pahoMqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC);
                                           } catch (MqttException e) {
                                               e.printStackTrace();
                                           } catch (UnsupportedEncodingException e) {
                                               e.printStackTrace();
                                           }
                                       } else {
                                           String msg = "{\"Light\":\"OFF\",\"Pump\":\"UNCHANGED\"}";
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
            sw2.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {

                                           if (sw2.isChecked()) {
                                               String msg = "{\"Light\":\"UNCHANGED\",\"Pump\":\"ON\"}";
                                               try {
                                                   pahoMqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC);
                                               } catch (MqttException e) {
                                                   e.printStackTrace();
                                               } catch (UnsupportedEncodingException e) {
                                                   e.printStackTrace();
                                               }
                                           } else {
                                               String msg = "{\"Light\":\"UNCHANGED\",\"Pump\":\"OFF\"}";
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
//        relay1on.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String msg = "{\"Light\":\"ON\",\"Pump\":\"UNCHANGED\"}" ;
//                try {
//                    pahoMqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC);
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        relay1off.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String msg = "{\"Light\":\"OFF\",\"Pump\":\"UNCHANGED\"}";
//                try {
//                    pahoMqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC);
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        relay2on.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String msg ="{\"Light\":\"UNCHANGED\",\"Pump\":\"ON\"}";
//                try {
//                    pahoMqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC);
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        relay2off.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String msg ="{\"Light\":\"UNCHANGED\",\"Pump\":\"OFF\"}";
//                try {
//                    pahoMqttClient.publishMessage(client, msg, 1, Constants.PUBLISH_TOPIC);
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String URL = subscribeTopic.getText().toString().trim();
                String topic1 = "Sensors";
                String topic2 = "Relays";
                if (!topic1.isEmpty()) {
                    try {

                        pahoMqttClient.subscribe(client, topic1, 1);
                        pahoMqttClient.subscribe(client, topic2, 1);

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
