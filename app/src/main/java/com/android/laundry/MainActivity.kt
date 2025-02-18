package com.android.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.android.laundry.pelanggan.DataPelangganActivity
import java.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatDelegate
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        setContentView(R.layout.activity_main)

        // Inisialisasi TextView
        val welcomeTextView: TextView = findViewById(R.id.Welcome)
        val calendarTextView: TextView = findViewById(R.id.Calendar)

        // Menentukan bahasa perangkat
        val language = Locale.getDefault().language

        // Mendapatkan jam saat ini
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        // Menampilkan pesan selamat berdasarkan waktu dan bahasa perangkat
        val greeting = when (hour) {
            in 5..10 -> if (language == "id") "Selamat Pagi, Mike" else "Good Morning, Mike"
            in 11..14 -> if (language == "id") "Selamat Siang, Mike" else "Good Afternoon, Mike"
            in 15..17 -> if (language == "id") "Selamat Sore, Mike" else "Good Evening, Mike"
            else -> if (language == "id") "Selamat Malam, Mike" else "Good Evening, Mike"
        }
        welcomeTextView.text = greeting

        // Menampilkan tanggal saat ini
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        calendarTextView.text = dateFormat.format(Date())

        // Inisialisasi CardView dan navigasi ke aktivitas lain
        findViewById<CardView>(R.id.cardPelanggan).setOnClickListener {
            startActivity(Intent(this, DataPelangganActivity::class.java))
        }
    }
}
