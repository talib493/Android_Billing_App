package com.example.billingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.billingapp.database.DatabaseConnect
import com.example.billingapp.navigation.NavGraph
import com.example.billingapp.screens.BillHistory
import com.example.billingapp.screens.BillInfoScreen
import com.example.billingapp.ui.theme.BillingAppTheme
import com.example.billingapp.workManager.autoBackupWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val backupConstraints = Constraints.Builder()
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val autoBackupRequest  = PeriodicWorkRequestBuilder<autoBackupWorker>(30, TimeUnit.MINUTES)
            .setConstraints(backupConstraints).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "HalfHourlyAutoBackup",
            ExistingPeriodicWorkPolicy.KEEP,
            autoBackupRequest
        )

        val databaseConnect = DatabaseConnect(this)

        enableEdgeToEdge()
        setContent {
            BillingAppTheme {
                NavGraph(databaseConnect)
            }
        }
    }
}
