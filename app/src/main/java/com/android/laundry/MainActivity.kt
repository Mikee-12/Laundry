package com.android.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.android.laundry.pelanggan.DataPelangganActivity
import java.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatDelegate
import com.android.laundry.Layanan.tambah_layanan
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Mengatur mode malam sesuai dengan sistem
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        // Inisialisasi TextView untuk tampilan pesan selamat dan kalender
        val welcomeTextView: TextView = findViewById(R.id.Welcome)
        val calendarTextView: TextView = findViewById(R.id.Calendar)

        // Menentukan bahasa perangkat
        val language = Locale.getDefault().language

        // Mendapatkan jam saat ini
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        // Menentukan pesan selamat berdasarkan waktu dan bahasa perangkat
        val greeting = when (hour) {
            in 5..10 -> if (language == "id" || language == "in") "Selamat Pagi, Mike" else "Good Morning, Mike"
            in 11..14 -> if (language == "id" || language == "in") "Selamat Siang, Mike" else "Good Afternoon, Mike"
            in 15..17 -> if (language == "id" || language == "in") "Selamat Sore, Mike" else "Good Evening, Mike"
            else -> if (language == "id" || language == "in") "Selamat Malam, Mike" else "Good Evening, Mike"
        }
        welcomeTextView.text = greeting

        // Menampilkan tanggal saat ini
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        calendarTextView.text = dateFormat.format(Date())

        // Inisialisasi CardView untuk navigasi ke aktivitas lain (DataPelangganActivity)
        findViewById<CardView>(R.id.cardPelanggan).setOnClickListener {
            startActivity(Intent(this, DataPelangganActivity::class.java))
        }

        findViewById<CardView>(R.id.cardlayanan).setOnClickListener {
            startActivity(Intent(this, tambah_layanan::class.java))
        }

    }

}
