package com.android.laundry.pegawai

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.laundry.R
import com.android.laundry.pelanggan.TambahanPelangganActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class data_pegawai : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pegawai)

        // Menghubungkan Floating Action Button dari XML
        val fab: FloatingActionButton = findViewById(R.id.fabData_Pegawai_Tambah)

        // Ketika FAB diklik, pindah ke Activity lain
        fab.setOnClickListener {
            val intent = Intent(this, tambahan_pegawai::class.java) // Ganti dengan Activity tujuan
            startActivity(intent)
        }

        // Mengatur padding agar sesuai dengan insets sistem
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_pegawai)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}