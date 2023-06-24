package in.happiness.groceryapp.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.activity.MainActivity;
import in.happiness.groceryapp.activity.SplashActivity;

import in.happiness.groceryapp.utils.AppConstant;
import in.happiness.groceryapp.utils.AppSharedPreferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static String TAG = "MyFirebaseMessagingService";
    Context mContext;
    int count = 0;
    int NOTIFICATION_ID = 100;
    TextView tvBadge;
    Handler mHandler;
    AppSharedPreferences sharedPreferences;
    private LocalBroadcastManager broadcaster;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "in.happiness.groceryapp";

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        sharedPreferences = new AppSharedPreferences(this);
        mContext = this;
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            /*if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }*/

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

            switch (am.getRingerMode()) {
                case AudioManager.RINGER_MODE_NORMAL:
                    showNotification(remoteMessage, remoteMessage.getNotification().getClickAction());
                    break;
                default:
                    Log.i("phone is silent","check");
                    break;
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void showNotification(RemoteMessage remoteMessage, String Action) {
        Intent resultIntent = null;
        if (sharedPreferences.doGetFromSharedPreferences(AppConstant.firstTimeLaunch).equals("true")) {
            if (Action != null) {
                if (Action.equals("order_details")) {
                    sharedPreferences.doSaveToSharedPreferences(AppConstant.click_action, Action);
                    resultIntent = new Intent(this, MainActivity.class);

                    Intent intent = new Intent("NotificationCount");
                    intent.putExtra("notify_count", "1");
                    broadcaster.sendBroadcast(intent);
                }
            } else {
                resultIntent = new Intent(this, MainActivity.class);
            }
        }

        if (resultIntent == null) {
            resultIntent = new Intent(this, SplashActivity.class);
        }

        String channel_id = "";
        String group_id = "";
        PendingIntent contentIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_MUTABLE);
        } else {
            contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);

        }
        //  PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Uri Emergency_sound_uri = Uri.parse("android.resource://" + getPackageName() + "/raw/audio3");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = getSystemService(NotificationManager.class);
            channel_id = notificationManager.getNotificationChannel("in.happiness.groceryapp").getId();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class).putExtra("importance", notificationManager.getNotificationChannel(channel_id).getImportance()).putExtra("channel_id", channel_id), PendingIntent.FLAG_MUTABLE);
            } else {
                contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class).putExtra("importance", notificationManager.getNotificationChannel(channel_id).getImportance()).putExtra("channel_id", channel_id), PendingIntent.FLAG_UPDATE_CURRENT);

            }
        }
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, channel_id)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSound(Emergency_sound_uri, AudioManager.STREAM_NOTIFICATION)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher);

        count++;
        notificationManager.notify(count, notification.build());
    }


    private void showNotification2(RemoteMessage remoteMessage, String Action) {
        Intent resultIntent = null;
        if (sharedPreferences.doGetFromSharedPreferences(AppConstant.firstTimeLaunch).equals("true")) {
            if (Action != null) {
                if (Action.equals("order_details")) {
                    sharedPreferences.doSaveToSharedPreferences(AppConstant.click_action, Action);
                    resultIntent = new Intent(this, MainActivity.class);

                    Intent intent = new Intent("NotificationCount");
                    intent.putExtra("notify_count", "1");
                    broadcaster.sendBroadcast(intent);
                }
            } else {
                resultIntent = new Intent(this, MainActivity.class);
            }
        }

        if (resultIntent == null) {
            resultIntent = new Intent(this, SplashActivity.class);
        }

        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            resultPendingIntent =
                    PendingIntent.getActivity(this, 0, resultIntent,
                            PendingIntent.FLAG_MUTABLE);

        } else {
            resultPendingIntent =
                    PendingIntent.getActivity(this, 0, resultIntent,
                            PendingIntent.FLAG_ONE_SHOT);

        }
        final int NOTIFICATION_COLOR = this.getResources().getColor(R.color.colorPrimary);
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/audio3");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), default_notification_channel_id)
                .setSmallIcon(R.mipmap.ic_app_logo)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setColor(NOTIFICATION_COLOR)
                .setSound(sound);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationChannel.setSound(sound, audioAttributes);
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify((int) System.currentTimeMillis(),
                mBuilder.build());
    }

    private void showNotification1(RemoteMessage remoteMessage, String Action) {
//        Uri alarmSound =
//                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION );
        Uri Emergency_sound_uri = Uri.parse("android.resource://" + getPackageName() + "/raw/audio3");
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), Emergency_sound_uri);
        mp.start();
        long[] v = {500, 1000};
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setDefaults(Notification.DEFAULT_LIGHTS | Emergency_sound_uri)
                .setColor(Color.parseColor("#FFE74C3C"))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(Emergency_sound_uri, AudioManager.STREAM_NOTIFICATION)
                .setSmallIcon(R.mipmap.ic_app_logo)
                .setVibrate(v)
                // .setBadgeIconType( NotificationCompat.BADGE_ICON_SMALL )
                // .setNumber( 2 )
                .setAutoCancel(true);

        //**add this line**
        int requestID = (int) System.currentTimeMillis();

        Intent resultIntent = null;
        if (sharedPreferences.doGetFromSharedPreferences(AppConstant.firstTimeLaunch).equals("true")) {
            if (Action != null) {
                if (Action.equals("order_details")) {
                    sharedPreferences.doSaveToSharedPreferences(AppConstant.click_action, Action);
                    resultIntent = new Intent(this, MainActivity.class);

                    Intent intent = new Intent("NotificationCount");
                    intent.putExtra("notify_count", "1");
                    broadcaster.sendBroadcast(intent);
                }
            } else {
                resultIntent = new Intent(this, MainActivity.class);
            }
        }

        if (resultIntent == null) {
            resultIntent = new Intent(this, SplashActivity.class);
        }

        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, 0, resultIntent,
                        PendingIntent.FLAG_ONE_SHOT);

//        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(resultPendingIntent);
        mp.stop();
        //**add this line**
        /*notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this, requestID,notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(contentIntent);*/


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

        /*NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());*/

    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(token);
    }
}
