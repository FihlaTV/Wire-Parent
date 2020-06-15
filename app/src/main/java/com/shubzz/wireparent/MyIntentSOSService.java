package com.shubzz.wireparent;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class MyIntentSOSService extends IntentService {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    private SessionHandler sessionHandler;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentSOSService(String name) {
        super("MyIntentSOSService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String KEY_STATUS = "status";
        final String KEY_MESSAGE = "message";
        final String KEY_USERNAME = "username";
        final String KEY_FULL_NAME = "full_name";
        final String KEY_uq = "key";
        final String KEY_sos = "SOSkey";
        String update_location = "http://192.168.43.98/wire/SOSgetUpdate.php";
        sessionHandler = new SessionHandler(this);
        String[] details = sessionHandler.getDetails(1);
        JSONObject request = new JSONObject();
        try {
            request.put(KEY_FULL_NAME, details[0]);
            request.put(KEY_USERNAME, details[1]);
            request.put(KEY_uq, details[2]);
            Log.d("request", details[2]);
            Log.d("request", request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, update_location, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt(KEY_STATUS) == 0) {
//                        Log.d("request",response.toString());
//                        Log.d("request",response.getString(KEY_Longitude)+" "+response.getString(KEY_Latitude));

                        if (response.getString(KEY_sos).equals("1")) {
                            Intent notificationIntent = new Intent(getApplicationContext(), SOSNotification.class);
                            notificationIntent.putExtra(SOSNotification.NOTIFICATION_ID, 1);
                            notificationIntent.putExtra(SOSNotification.NOTIFICATION, getNotification(getApplicationContext()));
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            long futureInMillis = SystemClock.elapsedRealtime();
                            AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
                            assert alarmManager != null;
                            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private Notification getNotification(Context c) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(c, default_notification_channel_id);
        builder.setContentTitle("SOS Notification");
        builder.setContentText("Your Child Need Help");
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }

}
