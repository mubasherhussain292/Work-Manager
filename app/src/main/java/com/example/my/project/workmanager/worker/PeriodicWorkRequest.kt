package com.example.my.project.workmanager.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyPeriodicWorkRequest(context: Context, workParams: WorkerParameters) : Worker(context, workParams) {


    override fun doWork(): Result {
        return Result.success()
    }


}