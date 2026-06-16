package com.percobaan.me

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tampilan)

        // 1. Inisialisasi Fitur
        checkAndCreateFolder()
        createNotificationChannel()
        checkNotificationPermission()

        // 2. Firebase
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
                        Toast.makeText(this, "Berhasil simpan ke Cloud!", Toast.LENGTH_SHORT).show()
                        inputTeks.setText("")
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Teks tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
        }

        btnHapus.setOnClickListener {
            inputTeks.setText("")
        }
    }

    // --- FUNGSI FOLDER ---
    private fun checkAndCreateFolder() {
        val folder = getExternalFilesDir(null)
        if (folder != null && !folder.exists()) {
            val created = folder.mkdirs()
            Log.d("AppFolder", "Folder dibuat: $created")
        }
    }

    // --- FUNGSI NOTIFIKASI ---
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

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) 
                != PackageManager.PERMISSION_GRANTED) {
                // Meminta izin secara eksplisit
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }
}
