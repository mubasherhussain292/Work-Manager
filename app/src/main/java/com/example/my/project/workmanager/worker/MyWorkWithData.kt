package com.example.my.project.workmanager.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.my.project.workmanager.R
import com.example.my.project.workmanager.utils.Utils.EXTRA_OUTPUT_MESSAGE
import com.example.my.project.workmanager.utils.Utils.EXTRA_TEXT
import com.example.my.project.workmanager.utils.Utils.EXTRA_TITLE

class MyWorkWithData(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {

        val title = inputData.getString(EXTRA_TITLE)
        val text = inputData.getString(EXTRA_TEXT)

        sendNotification(title, text)

        val output = Data.Builder()
            .putString(EXTRA_OUTPUT_MESSAGE, "Passing Data")
            .build()

        return Result.success(output)
    }

    private fun sendNotification(title: String?, message: String?) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "default")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)
        notificationManager.notify(1, notification.build())
    }

}


private const val TAG = "MyWorkWithData"
