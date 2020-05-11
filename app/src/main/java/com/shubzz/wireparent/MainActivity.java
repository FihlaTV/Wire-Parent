package com.shubzz.wireparent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView uq1, uq2, uq3, uq4;
    private SessionHandler session;
    private Button logout;
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_uq = "key";
    private static final String KEY_Longitude = "Longitude";
    private static final String KEY_Latitude = "Latitude";
    private String get_location = "http://34.93.78.17/project/getLocation.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionHandler(getApplicationContext());
        initGUI();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(MainActivity.this, Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        uq1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), uq1.getText().toString() + " Clicked", Toast.LENGTH_LONG).show();
                getLocation(1);

                //startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });
        uq2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation(2);
                //Toast.makeText(getApplicationContext(), uq2.getText().toString() + " Clicked", Toast.LENGTH_LONG).show();
            }
        });
        uq3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation(3);
                //Toast.makeText(getApplicationContext(), uq3.getText().toString() + " Clicked", Toast.LENGTH_LONG).show();
            }
        });
        uq4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation(4);
                //Toast.makeText(getApplicationContext(), uq4.getText().toString() + " Clicked", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initGUI() {
        uq1 = findViewById(R.id.uq1);
        uq2 = findViewById(R.id.uq2);
        uq3 = findViewById(R.id.uq3);
        uq4 = findViewById(R.id.uq4);
        String[] key = session.getKey();
        uq1.setText(key[0]);
        uq2.setText(key[1]);
        uq3.setText(key[2]);
        uq4.setText(key[3]);
        logout = findViewById(R.id.log);
    }

    private void getLocation(int loc) {
        String[] details = session.getDetails(loc);
        JSONObject request = new JSONObject();
        try {
            request.put(KEY_FULL_NAME, details[0]);
            request.put(KEY_USERNAME, details[1]);
            request.put(KEY_uq, details[2]);
            Log.d("request",details[2]);
            Log.d("request",request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, get_location, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt(KEY_STATUS) == 0) {
//                        Log.d("request",response.toString());
//                        Log.d("request",response.getString(KEY_Longitude)+" "+response.getString(KEY_Latitude));

                        if(response.getString(KEY_Longitude).equals("null")  || response.getString(KEY_Longitude).equals("null")){
                            Toast.makeText(getApplicationContext(),"Key Not used", Toast.LENGTH_SHORT).show();
                        }else{
                            session.setLocation(response.getString(KEY_Longitude),response.getString(KEY_Latitude));
                            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                            intent.putExtra("Latitude",response.getString(KEY_Latitude));
                            intent.putExtra("Longitude",response.getString(KEY_Longitude));
                            startActivity(intent);
                            //Toast.makeText(getApplicationContext(),response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }


}

