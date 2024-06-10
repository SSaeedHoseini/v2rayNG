package com.v2ray.ang.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.v2ray.ang.AngApplication
import com.v2ray.ang.R
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

        // Handle notification payload of FCM messages.
        remoteMessage.notification?.let {

            Log.i("Firebase", "Message Notification Title: ${it.title}")
            Log.i("Firebase", "Message Notification Image: ${it.imageUrl}")
            Log.i("Firebase", "Message Notification Body: ${it.body}")

            val notificationChannel = "message_received_for_server_channel"

            val notificationManager = NotificationManagerCompat.from(applicationContext)
            val notification =
                NotificationCompat.Builder(applicationContext, notificationChannel)
                    .setContentTitle(it.title)
                    .setContentText(it.body.toString())
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setPriority(NotificationCompat.PRIORITY_MIN)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification.setChannelId(notificationChannel)
                val channel =
                    NotificationChannel(
                        notificationChannel,
                        "Notification Receiver Service",
                        NotificationManager.IMPORTANCE_MIN
                    )
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(4, notification.build())
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