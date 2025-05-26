package com.android.laundry.pelanggan

import android.content.Intent
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

    // ID pelanggan jika mode edit
    private var pelangganId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambahan_pelanggan)

        // Inisialisasi view
        tvTambahPelanggan = findViewById(R.id.tvTambahPelanggan)
        etNama = findViewById(R.id.etNama)
        etAlamat = findViewById(R.id.etAlamat)
        etNoHp = findViewById(R.id.etNoHp)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Ambil data dari intent
        pelangganId = intent.getStringExtra("pelangganId")
        val judul = intent.getStringExtra("Judul")
        val nama = intent.getStringExtra("Nama")
        val alamat = intent.getStringExtra("Alamat")
        val noHP = intent.getStringExtra("noHPPelanggan")

        // Tampilkan judul dan isi field jika data ada
        tvTambahPelanggan.text = judul ?: "Tambah Pelanggan"
        etNama.setText(nama)
        etAlamat.setText(alamat)
        etNoHp.setText(noHP)

        btnSimpan.text = if (judul == "Edit Pelanggan") "Sunting" else "Simpan"

        btnSimpan.setOnClickListener {
            val namaInput = etNama.text.toString().trim()
            val alamatInput = etAlamat.text.toString().trim()
            val noHPInput = etNoHp.text.toString().trim()

            if (namaInput.isEmpty() || alamatInput.isEmpty() || noHPInput.isEmpty()) {
                Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (btnSimpan.text == "Simpan") {
                simpanData(namaInput, alamatInput, noHPInput)
            } else {
                updateData(namaInput, alamatInput, noHPInput)
            }
        }
    }

    private fun simpanData(nama: String, alamat: String, noHP: String) {
        val refBaru = myRef.push()
        val id = refBaru.key ?: return

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

        refBaru.setValue(pelanggan)
            .addOnSuccessListener {
                Toast.makeText(this, "Pelanggan berhasil disimpan", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan pelanggan", Toast.LENGTH_SHORT).show()
            }
    }


    private fun updateData(nama: String, alamat: String, noHP: String) {
        val id = pelangganId ?: return

        val updateMap = mapOf(
            "nama" to nama,
            "alamat" to alamat,
            "noHP" to noHP
            // Tidak update `dateRegistered` dan `timestamp` saat sunting
        )

        myRef.child(id).updateChildren(updateMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Data pelanggan diperbarui", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
            }
    }
}
