package com.android.laundry.Transaksi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.laundry.R
import com.android.laundry.pelanggan_transaksi

class Data_transaksi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_transaksi)

        // Menyesuaikan insets untuk perangkat dengan layar penuh (opsional)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_transaksi)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi tombol
        val btnPilihPelanggan = findViewById<Button>(R.id.btnPilihPelanggan)
        btnPilihPelanggan.setOnClickListener {
            val intent = Intent(this, pelanggan_transaksi::class.java)
            startActivity(intent)
        }

        // Aksi ketika tombol diklik
        btnPilihPelanggan.setOnClickListener {
            // Intent menuju halaman Pelanggan_transaksi
            val intent = Intent(this, pelanggan_transaksi::class.java)
            startActivity(intent)
        }
    }
}
