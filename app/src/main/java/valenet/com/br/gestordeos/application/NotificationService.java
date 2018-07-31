package valenet.com.br.gestordeos.application;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {
    private static final String TAG = "NOTIFICATION";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getNotification().getBody() != null) {
            Log.d(TAG, "Notification Message Title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        } else
            Log.d(TAG, "Notification Message Body: NULL");
    }

    @Override
    public void onNewToken(String s) {
        // Get updated InstanceID token.
        String refreshedToken = s;
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        //TODO: Implementar envio do token para o server
    }
}
