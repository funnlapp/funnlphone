package com.example.funnl

import android.annotation.SuppressLint
import android.app.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import android.support.v4.content.ContextCompat
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("ByteOrderMark")
class MainActivity: AppCompatActivity() {

    private val notificationId = 101
    private val KEY_TEXT_REPLY = "key_text_reply"

    private var notificationManager: NotificationManager? = null
    private val channelIDOngoing = "app.funnl.persist"
    private val channelID =  "app.funnl.info"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager =
            getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(channelIDOngoing,
            "Funnl Persistent Notification", "To Add Text Anywhere")
        createNotificationChannel(channelIDOngoing,
            "Funnl Status Notifications", "To Pass Info To Users")

        handleIntent()
    }

    private fun createNotificationChannel(id: String, name: String, description: String) {

        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
//        channel.enableLights(true)
//        channel.lightColor = Color.RED
//        channel.enableVibration(true)
//        channel.vibrationPattern =
//            longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)


        notificationManager?.createNotificationChannel(channel)
    }

    fun sendNotification(view: View?) {
        val replyLabel = "Save Thing"
        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel(replyLabel)
            .build()

        val resultIntent = Intent(this, MainActivity::class.java)

        val resultPendingIntent = PendingIntent.getActivity(
            this,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val icon = Icon.createWithResource(this@MainActivity,
            android.R.drawable.ic_dialog_info)

        val replyAction = Notification.Action.Builder(
            icon,
            "Save Thing", resultPendingIntent)
            .addRemoteInput(remoteInput)
            .build()

        val newMessageNotification = Notification.Builder(this, channelIDOngoing)
            .setColor(
                ContextCompat.getColor(this,
                R.color.colorPrimary))
            .setSmallIcon(
                android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .setContentTitle("Funnl")
            .setContentText("Save your thoughts")
            .addAction(replyAction).build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(notificationId, newMessageNotification)

    }

//
    private fun handleIntent() {

        val intent = this.intent

        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        if (remoteInput != null) {

            val inputString = remoteInput.getCharSequence(KEY_TEXT_REPLY).toString()

            inputTxt.text = inputString
            val repliedNotification = Notification.Builder(this, channelID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentText("Thought saved")
                .build()
            // TODO timer to replace ongoing notification with 'success' or 'failure'

            sendNotification(View(this))



        }
    }

}