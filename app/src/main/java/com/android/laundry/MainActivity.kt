package com.android.laundry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import com.android.laundry.pelanggan.DataPelangganActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sembunyikan status bar (bar hitam atas)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
        actionBar?.hide()
        supportActionBar?.hide()

        setContentView(R.layout.activity_main)

        // Atur mode malam sesuai sistem
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        // Referensi TextView
        val welcomeTextView: TextView = findViewById(R.id.Welcome)
        val calendarTextView: TextView = findViewById(R.id.Calendar)

        val language = Locale.getDefault().language
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val greeting = when (hour) {
            in 5..10 -> if (language == "id" || language == "in") "Selamat Pagi, Mike" else "Good Morning, Mike"
            in 11..14 -> if (language == "id" || language == "in") "Selamat Siang, Mike" else "Good Afternoon, Mike"
            in 15..17 -> if (language == "id" || language == "in") "Selamat Sore, Mike" else "Good Evening, Mike"
            else -> if (language == "id" || language == "in") "Selamat Malam, Mike" else "Good Evening, Mike"
        }
        welcomeTextView.text = greeting

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        calendarTextView.text = dateFormat.format(Date())

        findViewById<CardView>(R.id.cardPelanggan).setOnClickListener {
            startActivity(Intent(this, DataPelangganActivity::class.java))
        }
        findViewById<CardView>(R.id.cardlayanan).setOnClickListener {
            startActivity(Intent(this, com.android.laundry.layanan.Data_layanan::class.java))
        }
        findViewById<CardView>(R.id.cardTransaksi)?.setOnClickListener {
            // startActivity(Intent(this, TransaksiActivity::class.java))
        }
        findViewById<CardView>(R.id.cardCabang).setOnClickListener {
            startActivity(Intent(this, com.android.laundry.cabang.Data_cabang::class.java))
        }
        findViewById<CardView>(R.id.cardTambahan) .setOnClickListener{
            startActivity(Intent(this, com.android.laundry.Tambahan.Data_tambahan::class.java))
        }
        findViewById<CardView>(R.id.cardTransaksi) .setOnClickListener{
            startActivity(Intent(this, com.android.laundry.Transaksi.Data_transaksi::class.java))
        }
    }
}
