package com.percobaan.me

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tampilan)

        checkAndCreateFolder()
        createNotificationChannel()
        checkNotificationPermission()

        val db = FirebaseFirestore.getInstance()
        val inputTeks = findViewById<EditText>(R.id.inputTeks)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnHapus = findViewById<Button>(R.id.btnHapus)

        btnSimpan.setOnClickListener {
            val isiTeks = inputTeks.text.toString()
            if (isiTeks.isNotEmpty()) {
                db.collection("users").document("YpabdicodRZLzZ1vVRS5")
                    .update("usage_history", isiTeks)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Berhasil!", Toast.LENGTH_SHORT).show()
                        sendTestNotification()
                        inputTeks.setText("")
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Teks kosong!", Toast.LENGTH_SHORT).show()
            }
        }

        btnHapus.setOnClickListener {
            inputTeks.setText("")
        }
    }

    private fun checkAndCreateFolder() {
        val folder = getExternalFilesDir(null)
        if (folder != null && !folder.exists()) {
            folder.mkdirs()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID_AERA", 
                "Update Info", 
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifikasi dari Cloud"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun sendTestNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, "CHANNEL_ID_AERA")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Sukses!")
            .setContentText("Data berhasil dikirim ke Cloud.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.POST_NOTIFICATIONS) 
                == PackageManager.PERMISSION_GRANTED) {
                notify(1, builder.build())
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) 
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }
}
