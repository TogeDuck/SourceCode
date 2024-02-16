package com.idle.togeduck.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class GPSWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    companion object {
        private val TAG = GPSWorker::class.java.name
    }

    private val mFusedLocationClient: FusedLocationProviderClient by lazy { 
        LocationServices.getFusedLocationProviderClient(context)
    }
    
    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        return try {
            mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("로그", "GPSWorker - doWork() 성공 : ${task.result}")
                } else {
                    Log.d("로그", "GPSWorker - doWork() 실패 : ${task.result}")
                }
            }
            Result.success()
        } catch (e: Exception) {
            Log.d("로그", "GPSWorker - doWork() 에러 : ${e}")
            Result.failure()
        }
    }
}