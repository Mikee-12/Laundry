package com.android.laundry.pegawai

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.laundry.R
import com.android.laundry.modeldata.ModelPegawai
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

class Tambahan_pegawai : AppCompatActivity() {

    // Firebase
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pegawai")

    // Views
    private lateinit var tvTambahan_pegawai: TextView
    private lateinit var etNama: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etNoHp: EditText
    private lateinit var btnSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_tambahan_pegawai)

        val mainView = findViewById<View>(R.id.tambah_pegawai)
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


        // Inisialisasi view
        tvTambahan_pegawai = findViewById(R.id.tvTambahPegawai) // ID yang benar dari XML
        etNama = findViewById(R.id.etNama)
        etAlamat = findViewById(R.id.etAlamat)
        etNoHp = findViewById(R.id.etNoHp)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Setup status bar untuk fullscreen
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        // Klik tombol simpan
        btnSimpan.setOnClickListener {
            val namaInput = etNama.text.toString().trim()
            val alamatInput = etAlamat.text.toString().trim()
            val noHPInput = etNoHp.text.toString().trim()

            if (namaInput.isEmpty() || alamatInput.isEmpty() || noHPInput.isEmpty()) {
                Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            simpanData(namaInput, alamatInput, noHPInput)
        }
    }

    private fun simpanData(nama: String, alamat: String, noHP: String) {
        myRef.get().addOnSuccessListener { dataSnapshot ->
            var lastIdNumber = 0

            // Iterasi semua child untuk mencari ID terbesar
            for (child in dataSnapshot.children) {
                val idStr = child.key ?: continue
                val idNumber = idStr.toIntOrNull()
                if (idNumber != null && idNumber > lastIdNumber) {
                    lastIdNumber = idNumber
                }
            }

            val nextIdNumber = lastIdNumber + 1
            val nextIdFormatted = String.format("%05d", nextIdNumber) // Contoh: 00001

            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val tanggalSekarang = sdf.format(Date())
            val waktuMillis = System.currentTimeMillis()

            val pegawai = ModelPegawai(
                nama = nama,
                alamat = alamat,
                noHP = noHP,
                dateRegistered = tanggalSekarang,
                timestamp = waktuMillis
            )

            // Simpan data dengan ID yang sudah diformat
            myRef.child(nextIdFormatted).setValue(pegawai)
                .addOnSuccessListener {
                    Toast.makeText(this, "Pegawai berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menyimpan pegawai", Toast.LENGTH_SHORT).show()
                }

        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mendapatkan data ID terakhir", Toast.LENGTH_SHORT).show()
        }
    }
}