package com.android.laundry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.activity.enableEdgeToEdge
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.laundry.pelanggan.DataPelangganActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val mainView = findViewById<View>(R.id.main)
        val initialPadding = mainView.run {
            Triple(paddingLeft, paddingTop, paddingRight)
        }
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                initialPadding.first + systemBars.left,
                initialPadding.second + systemBars.top,
                initialPadding.third + systemBars.right,
                systemBars.bottom
            )
            insets
        }

        // Atur mode malam sesuai sistem
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        // Gunakan safe call untuk TextView
        val welcomeTextView: TextView? = findViewById(R.id.Welcome)
        val calendarTextView: TextView? = findViewById(R.id.Calendar)

        // Set greeting dan tanggal dengan null check
        welcomeTextView?.let { setGreeting(it) }
        calendarTextView?.let { setCurrentDate(it) }

        // Setup click listeners
        setupClickListeners()
    }

    private fun setGreeting(textView: TextView) {
        val language = Locale.getDefault().language
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val greeting = when (hour) {
            in 5..10 -> if (language == "id" || language == "in") "Selamat Pagi, Mike" else "Good Morning, Mike"
            in 11..14 -> if (language == "id" || language == "in") "Selamat Siang, Mike" else "Good Afternoon, Mike"
            in 15..17 -> if (language == "id" || language == "in") "Selamat Sore, Mike" else "Good Evening, Mike"
            else -> if (language == "id" || language == "in") "Selamat Malam, Mike" else "Good Evening, Mike"
        }
        textView.text = greeting
    }

    private fun setCurrentDate(textView: TextView) {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        textView.text = dateFormat.format(Date())
    }

    private fun setupClickListeners() {
        // Gunakan safe call operator (?.) untuk menghindari NPE
        findViewById<CardView>(R.id.cardPelanggan)?.setOnClickListener {
            startActivity(Intent(this, DataPelangganActivity::class.java))
        }

        findViewById<CardView>(R.id.cardlayanan)?.setOnClickListener {
            startActivity(Intent(this, com.android.laundry.layanan.Data_layanan::class.java))
        }

        findViewById<CardView>(R.id.cardCabang)?.setOnClickListener {
            startActivity(Intent(this, com.android.laundry.cabang.Data_cabang::class.java))
        }

        findViewById<CardView>(R.id.cardTambahan)?.setOnClickListener {
            startActivity(Intent(this, com.android.laundry.Tambahan.Data_tambahan::class.java))
        }

        findViewById<CardView>(R.id.cardTransaksi)?.setOnClickListener {
            startActivity(Intent(this, com.android.laundry.Transaksi.Data_transaksi::class.java))
        }

        findViewById<CardView>(R.id.cardPegawai)?.setOnClickListener {
            startActivity(Intent(this, com.android.laundry.pegawai.Data_pegawai::class.java))
        }
    }
}