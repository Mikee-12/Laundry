package com.android.laundry

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Menyambungkan TextView dari layout XML untuk pesan selamat
        val welcomeTextView: TextView = findViewById(R.id.Welcome)

        // Menyambungkan TextView dari layout XML untuk kalender
        val calendarTextView: TextView = findViewById(R.id.Calendar)

        // Mendapatkan waktu saat ini
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        // Menentukan pesan berdasarkan jam
        val greeting = when {
            hour in 5..10 -> "Selamat Pagi, Mike"
            hour in 11..14 -> "Selamat Siang, Mike"
            hour in 15..17 -> "Selamat Sore, Mike"
            else -> "Selamat Malam, Mike"
        }

        // Menampilkan pesan waktu pada TextView
        welcomeTextView.text = greeting

        // Format untuk tanggal (dd MMMM yyyy)
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        // Mendapatkan tanggal saat ini
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)

        // Menampilkan tanggal pada TextView
        calendarTextView.text = formattedDate}
}