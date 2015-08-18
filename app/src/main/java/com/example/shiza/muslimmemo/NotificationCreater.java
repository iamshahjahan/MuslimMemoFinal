package com.example.shiza.muslimmemo;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Random;


/**
 * Created by Shiza on 16-06-2015.
 */
public class NotificationCreater extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SETTINGS", Activity.MODE_PRIVATE);
        Log.d("createNotification", "I am in on recieve");

        if ( sharedPreferences.getBoolean("NOTIFICATIONS",true))
        {
            checkForNewEntry task = new checkForNewEntry(context);
            if(Build.VERSION.SDK_INT >= 11)
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                task.execute();
        }
        else
        {
            Log.d("createNotification","You switched off the notification.");
        }
//        createNotification(context);
    }

    public static void createNotification(Context context, String heading) {
        Random random = new Random();
        int notificationID = random.nextInt(1000) + 1;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Log.d("createNotification","I am notification builder try");
        mBuilder.setSmallIcon(R.drawable.bg);
        mBuilder.setContentTitle("MUSLIM MEMO");
        mBuilder.setContentText(heading);

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
// notificationID allows you to update the notification later on.
        mNotificationManager.notify(notificationID, mBuilder.build());
        mBuilder.setAutoCancel(true);

    }

}

class checkForNewEntry extends AsyncTask<Void, Void, Void> {
    Context mContext;
    SharedPreferences sharedPreferences;

    public checkForNewEntry(Context context) {
        mContext = context;
        Log.d("createNotification", "I am in checkfornwentry");
    }



    @Override
    protected Void doInBackground(Void... params) {
        sharedPreferences = mContext.getSharedPreferences("NOTIFICATION", Activity.MODE_PRIVATE);
        try {
            Log.d("createNotification","I am notification creator try");

            Document document = Jsoup.connect("https://muslimmemo.com").get();
            Elements heading = document.getElementsByClass("entry-title");
            for (Element headings : heading) {
                if (!sharedPreferences.getBoolean(headings.text(), false)) {
                    NotificationCreater.createNotification(mContext, headings.text());
                    sharedPreferences.edit().putBoolean(headings.text(), true).apply();
                    Log.d("createNotification","I am in notification creator");
                    break;
                }

            }

        } catch (IOException e) {
            Log.d("createNotification","I am notification creator catch");

            e.printStackTrace();
        }
        return null;
    }
}


