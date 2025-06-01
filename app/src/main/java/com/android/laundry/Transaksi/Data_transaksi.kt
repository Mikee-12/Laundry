package com.android.laundry.Transaksi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.laundry.R

class Data_transaksi : AppCompatActivity() {

    private lateinit var tvPelanggan: TextView
    private lateinit var tvLayanan: TextView
    private lateinit var tvHarga: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_transaksi)

        // Menyesuaikan insets untuk perangkat dengan layar penuh (opsional)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_transaksi)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi TextView untuk tampilkan data pelanggan dan layanan
        tvPelanggan = findViewById(R.id.tvNamaPelanggan)  // Pastikan ada di layout
        tvLayanan = findViewById(R.id.tvLayanan)      // Pastikan ada di layout
        tvHarga = findViewById(R.id.tvHarga)          // Pastikan ada di layout

        // Tangani data yang dikirim lewat Intent saat activity dibuat
        handleIntent(intent)

        // Tombol pilih pelanggan
        val btnPilihPelanggan = findViewById<Button>(R.id.btnPilihPelanggan)
        btnPilihPelanggan.setOnClickListener {
            val intent = Intent(this, Pelanggan_transaksi::class.java)
            startActivity(intent)
        }

        // Tombol pilih layanan
        val btnPilihLayanan = findViewById<Button>(R.id.btnPilihLayanan)
        btnPilihLayanan.setOnClickListener {
            val intent = Intent(this, Layanan_transaksi::class.java)
            startActivity(intent)
        }
    }

    // Jika activity ini dipanggil ulang lewat Intent baru (misal dari Layanan_transaksi atau Pelanggan_transaksi)
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }


    private fun handleIntent(intent: Intent) {
        val namaPelanggan = intent.getStringExtra("NAMA_PELANGGAN")
        val namaLayanan = intent.getStringExtra("NAMA_LAYANAN")
        val hargaLayanan = intent.getStringExtra("HARGA_LAYANAN")

        if (namaPelanggan != null) {
            tvPelanggan.text = namaPelanggan
        }
        if (namaLayanan != null) {
            tvLayanan.text = namaLayanan
        }
        if (hargaLayanan != null) {
            tvHarga.text = "Rp $hargaLayanan"
        }
    }
}
