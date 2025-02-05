package com.android.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.android.laundry.pegawai.data_pegawai
import com.android.laundry.pelanggan.DataPelangganActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi TextView
        val welcomeTextView: TextView = findViewById(R.id.Welcome)
        val calendarTextView: TextView = findViewById(R.id.Calendar)

        // Menampilkan pesan selamat berdasarkan waktu
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 5..10 -> "Selamat Pagi, Mike"
            in 11..14 -> "Selamat Siang, Mike"
            in 15..17 -> "Selamat Sore, Mike"
            else -> "Selamat Malam, Mike"
        }
        welcomeTextView.text = greeting

        // Menampilkan tanggal saat ini
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        calendarTextView.text = dateFormat.format(Date())

        // Inisialisasi CardView dan navigasi ke aktivitas lain
        findViewById<CardView>(R.id.cardPelanggan).setOnClickListener {
            startActivity(Intent(this, DataPelangganActivity::class.java))
        }
        findViewById<CardView>(R.id.cardPegawai).setOnClickListener {
            startActivity(Intent(this, data_pegawai::class.java))
        }
    }
}
