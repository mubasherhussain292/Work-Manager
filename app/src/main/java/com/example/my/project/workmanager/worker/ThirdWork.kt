package com.example.my.project.workmanager.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class ThirdWork(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {


    override fun doWork(): Result {
        Log.d(TAG, "My WorkC")
        return Result.success()
    }

}

private const val TAG = "MyWorkC"
