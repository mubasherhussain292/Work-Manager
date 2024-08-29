package com.example.my.project.workmanager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType.CONNECTED
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.my.project.workmanager.databinding.ActivityMainBinding
import com.example.my.project.workmanager.utils.Utils.EXTRA_OUTPUT_MESSAGE
import com.example.my.project.workmanager.utils.Utils.EXTRA_TEXT
import com.example.my.project.workmanager.utils.Utils.EXTRA_TITLE
import com.example.my.project.workmanager.worker.AWorkManager
import com.example.my.project.workmanager.worker.FirstWork
import com.example.my.project.workmanager.worker.MyPeriodicWorkRequest
import com.example.my.project.workmanager.worker.MyWorkWithData
import com.example.my.project.workmanager.worker.SecondWork
import com.example.my.project.workmanager.worker.ThirdWork
import java.util.UUID
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var mPeriodicWorkRequest: PeriodicWorkRequest? = null

    private var getId: UUID? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.OneTimeWork.setOnClickListener {

            val forOneTimeRequest = OneTimeWorkRequest.Builder(AWorkManager::class.java).build()

            WorkManager.getInstance(this).enqueue(forOneTimeRequest)

        }



        binding.PeriodicWork.setOnClickListener {
            //if you want to set constraint but I comment here but you can simple use by removing from the comments
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .setRequiresDeviceIdle(true)
                .build()


            mPeriodicWorkRequest = PeriodicWorkRequest.Builder(MyPeriodicWorkRequest::class.java, 2, TimeUnit.HOURS)/*.setConstraints(constraints)*/
                .addTag("periodicWorkRequest").build()

            WorkManager.getInstance(this).enqueue(mPeriodicWorkRequest!!)

        }







        binding.ChainableWork.setOnClickListener {

            val firstWorkRequest = OneTimeWorkRequest.Builder(FirstWork::class.java).build()
            val secondWorkRequest = OneTimeWorkRequest.Builder(SecondWork::class.java).build()
            val thirdWorkRequest = OneTimeWorkRequest.Builder(ThirdWork::class.java).build()


            WorkManager.getInstance(this).beginWith(firstWorkRequest).then(secondWorkRequest).then(thirdWorkRequest).enqueue()


        }






        binding.parallelwork.setOnClickListener {


            val firstWorkRequest = OneTimeWorkRequest.Builder(FirstWork::class.java).build()
            val secondWorkRequest = OneTimeWorkRequest.Builder(SecondWork::class.java).build()
            val thirdWorkRequest = OneTimeWorkRequest.Builder(ThirdWork::class.java).build()

            val beginWithAB: MutableList<OneTimeWorkRequest> = ArrayList()
            beginWithAB.add(firstWorkRequest)
            beginWithAB.add(secondWorkRequest)

            WorkManager.getInstance(this).beginWith(beginWithAB).then(thirdWorkRequest).enqueue()

        }






        binding.CancelPeriodicWork.setOnClickListener {

            getId = mPeriodicWorkRequest!!.id
            WorkManager.getInstance(this).cancelWorkById(getId!!)
            Log.d(TAG, "PeriodicWork Cancel By Id: ")

        }



        binding.workwithconstraints.setOnClickListener {

            val constraints = Constraints.Builder().setRequiredNetworkType(CONNECTED).build()
            val oneTimeWorkRequest = OneTimeWorkRequest.Builder(AWorkManager::class.java).setConstraints(constraints).build()
            WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)



            WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this) { workInfo ->

                if (workInfo != null) {
                    Log.d(TAG, "one time request: ")
                }


                if (workInfo != null && workInfo.state.isFinished) {
                    Log.d(TAG, "Work Finished ")
                }

            }


        }




        binding.WorkWithData.setOnClickListener {


            val data = Data.Builder().putString(EXTRA_TITLE, "Coming from activity").putString(EXTRA_TEXT, "Message").build()
            val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorkWithData::class.java).setInputData(data).build()
            WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)


            WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this@MainActivity) { workInfo ->
                if (workInfo != null && workInfo.state.isFinished) {
                    val message = workInfo.outputData.getString(EXTRA_OUTPUT_MESSAGE)
                    binding.textView.text = message
                }
            }
        }


    }
}


private const val TAG = "MainActivity"