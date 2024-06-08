package com.v2ray.ang.service

import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.v2ray.ang.AngApplication
import com.v2ray.ang.connection.Repository
import com.v2ray.ang.util.MmkvManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val repository = Repository(AngApplication.apiService)
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Handle data payload of FCM messages.
        if (remoteMessage.data.isNotEmpty()) {
            // Handle the data message here.
            Log.i("Firebase", "Message data payload: ${remoteMessage.data}")
        }

        val service = V2RayServiceManager.serviceControl?.get()?.getService() ?: return

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                V2RayServiceManager.createNotificationChannel()
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }
        // Handle notification payload of FCM messages.
        remoteMessage.notification?.let {

            Log.i("Firebase", "Message Notification Title: ${it.title}")
            Log.i("Firebase", "Message Notification Image: ${it.imageUrl}")
            Log.i("Firebase", "Message Notification Body: ${it.body}")

            NotificationCompat.Builder(service, channelId)
                .setContentTitle(it.title)
                .setContentText(it.body.toString())
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setOngoing(true)
                .setShowWhen(false)
                .setOnlyAlertOnce(true).build()
        }
    }

    override fun onNewToken(token: String) {
        Log.i("Firebase", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        MmkvManager.setRegistrationId(token)
        try {
            scope.launch {
                repository.registrationTokenUpdate()
            }
        } catch (e: Exception) {
            // ignore
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}