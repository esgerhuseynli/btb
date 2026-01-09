package az.btb.mobilebanking;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import az.btb.mobilebanking.utils.Constants;

import static az.btb.mobilebanking.utils.Constants.FCM_NOTIFICATION_TOKEN;

public class AppFirebaseMessagingService extends FirebaseMessagingService {

	@Override
	public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
		// [START_EXCLUDE]
		// There are two types of messages data messages and notification messages. Data messages
		// are handled
		// here in onMessageReceived whether the app is in the foreground or background. Data
		// messages are the type
		// traditionally used with GCM. Notification messages are only received here in
		// onMessageReceived when the app
		// is in the foreground. When the app is in the background an automatically generated
		// notification is displayed.
		// When the user taps on the notification they are returned to the app. Messages
		// containing both notification
		// and data payloads are treated as notification messages. The Firebase console always
		// sends notification
		// messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
		// [END_EXCLUDE]

		// TODO(developer): Handle FCM messages here.
		// Not getting messages here? See why this may be: https://goo.gl/39bRNJ
		//Log.d(TAG, "From: " + remoteMessage.getFrom());

		// Check if message contains a data payload.
		if (remoteMessage.getData().size() > 0) {
			//Log.d(TAG, "Message data payload: " + remoteMessage.getData());

			if (/* Check if data needs to be processed by long running job */ true) {
				// For long-running tasks (10 seconds or more) use WorkManager.
				//scheduleJob();
			} else {
				// Handle message within 10 seconds
				//handleNow();
			}

		}

		// Check if message contains a notification payload.
		if (remoteMessage.getNotification() != null) {
			final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

			// Show the notification IF AND ONLY IF active session is exists.
			if (prefs.getBoolean(Constants.HAS_ACTIVE_SESSION, false))
				sendNotification(remoteMessage.getNotification());

			//Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
		}

		// Also if you intend on generating your own notifications as a result of a received FCM
		// message, here is where that should be initiated. See sendNotification method below.
	}

	/**
	 * There are two scenarios when onNewToken is called:
	 * 1) When a new token is generated on initial app startup
	 * 2) Whenever an existing token is changed
	 * Under #2, there are three scenarios when the existing token is changed:
	 * A) App is restored to a new device
	 * B) User uninstalls/reinstalls the app
	 * C) User clears app data
	 */
	@Override
	public void onNewToken(@NonNull String token) {
		// If you want to send messages to this application instance or
		// manage this apps subscriptions on the server side, send the
		// FCM registration token to our app server.
		PreferenceManager
			.getDefaultSharedPreferences(getApplicationContext())
			.edit()
			.putString(FCM_NOTIFICATION_TOKEN, token)
			.apply();
	}

	/**
	 * Create and show a simple notification containing the received FCM message.
	 *
	 * @param fcmNotification FCM notification received.
	 */
	private void sendNotification(RemoteMessage.Notification fcmNotification) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_MUTABLE);

		String channelId = "_btb_mobile_notification_channel_main";
		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder notificationBuilder =
			new NotificationCompat.Builder(this, channelId)
				.setSmallIcon(R.drawable.ic_notification)
				.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme()))
				.setContentTitle(fcmNotification.getTitle())
				.setContentText(fcmNotification.getBody())
				.setAutoCancel(true)
				.setSound(defaultSoundUri)
				.setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
				.setContentIntent(pendingIntent);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Since android Oreo notification channel is needed.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(
				channelId,
				"BTB Mobile bildiriş kanalı",
				NotificationManager.IMPORTANCE_HIGH);
			notificationManager.createNotificationChannel(channel);
		}

		notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
	}
}
