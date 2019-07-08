package com.comp259app2.berezowskib.mapsdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class FirebaseMessaging extends FirebaseMessagingService {


    private static final String TAG = FirebaseMessaging.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

           String dataPayload = remoteMessage.getData().toString();

            Map<String, String> DataPayload = remoteMessage.getData();
            //grabs the date from firebase
            Long ddate = remoteMessage.getSentTime();

            String Lat = DataPayload.get("Lat");
            String Lon = DataPayload.get("Lon");
            String Description = DataPayload.get("Description");

          //  Sets the date format
            String format = "dd-MM-yyyy";
//creates an istance of SimpleDateFormat
            SimpleDateFormat formatter = new SimpleDateFormat(format);

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ddate);
            String FinalDate = formatter.format(calendar.getTime());


//creates an intent

            Intent intent = new Intent(Global.PUSH_NOTIFICATION);
//creates a bundle that contains the information sent from firebase
            Bundle data = new Bundle();
            data.putString("Lat", Lat);
            data.putString("Lon", Lon);
            data.putString("Description", Description);
            data.putString("Date",FinalDate);
            //adds the bundle to the intent
            intent.putExtras(data);
          //broadcast the intent
             LocalBroadcastManager.getInstance(this).sendBroadcast(intent);


            Log.i(TAG, "Data Message= " + dataPayload);
        }
        // Check if message contains a notification payload and send broadcast to mainactivity
//        if (remoteMessage.getNotification() != null) {
//
//            String notificationPayload = remoteMessage.getNotification().getBody();
//
//            Intent intent = new Intent(Global.PUSH_NOTIFICATION);
//            intent.putExtra(Global.EXTRA_MESSAGE, notificationPayload);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//
//            Log.i(TAG, "Notification Message= " + notificationPayload);
//        }
    }

    //old code

//    private static String zzTAG = "FirebaseMessaging";
//
//    public FirebaseMessaging() {
//    }
//
////    @Override
////    public IBinder onBind(Intent intent) {
////        // TODO: Return the communication channel to the service.
////        throw new UnsupportedOperationException("Not yet implemented");
////    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // ...
//
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(zzTAG, "From: " + remoteMessage.getFrom());
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(zzTAG, "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                //scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                //handleNow();
//
//            }
//
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(zzTAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
//    }
//
//
//    /**
//     * Called if InstanceID token is updated. This may occur if the security of
//     * the previous token had been compromised. Note that this is called when the InstanceID token
//     * is initially generated so this is where you would retrieve the token.
//     */
//    @Override
//    public void onNewToken(String token) {
//        Log.d(zzTAG, "Refreshed token: " + token);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        //sendRegistrationToServer(token);
//    }

}
