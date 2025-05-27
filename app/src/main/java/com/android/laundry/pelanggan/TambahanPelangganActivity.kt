package com.android.laundry.pelanggan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.laundry.R
import com.android.laundry.modeldata.ModelPelanggan
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class TambahanPelangganActivity : AppCompatActivity() {

    // Firebase
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pelanggan")

    // Views
    private lateinit var tvTambahPelanggan: TextView
    private lateinit var etNama: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etNoHp: EditText
    private lateinit var btnSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambahan_pelanggan)

        // Inisialisasi view
        tvTambahPelanggan = findViewById(R.id.tvTambahPelanggan)
        etNama = findViewById(R.id.etNama)
        etAlamat = findViewById(R.id.etAlamat)
        etNoHp = findViewById(R.id.etNoHp)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Judul
        tvTambahPelanggan.text = "Tambah Pelanggan"
        btnSimpan.text = "Simpan"

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
        // Dapatkan seluruh data pelanggan untuk mengetahui ID terakhir
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

            val pelanggan = ModelPelanggan(
                nama = nama,
                alamat = alamat,
                noHP = noHP,
                dateRegistered = tanggalSekarang,
                timestamp = waktuMillis
            )

            // Simpan data dengan ID yang sudah diformat
            myRef.child(nextIdFormatted).setValue(pelanggan)
                .addOnSuccessListener {
                    Toast.makeText(this, "Pelanggan berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menyimpan pelanggan", Toast.LENGTH_SHORT).show()
                }

        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mendapatkan data ID terakhir", Toast.LENGTH_SHORT).show()
        }
    }
}
