package com.android.laundry.pelanggan

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.laundry.R
import com.android.laundry.modeldata.ModelPelanggan
import com.google.firebase.database.*

class TambahanPelangganActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pelanggan")

    private lateinit var tvTambahPelanggan: TextView
    private lateinit var etNama: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etNoHp: EditText
    private lateinit var btnSimpan: Button

    private var pelangganId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambahan_pelanggan)

        tvTambahPelanggan = findViewById(R.id.tvTambahPelanggan)
        etNama = findViewById(R.id.etNama)
        etAlamat = findViewById(R.id.etAlamat)
        etNoHp = findViewById(R.id.etNoHp)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Ambil data dari Intent
        pelangganId = intent.getStringExtra("pelangganId")
        val judul = intent.getStringExtra("Judul")
        val nama = intent.getStringExtra("Nama")
        val alamat = intent.getStringExtra("Alamat")
        val noHP = intent.getStringExtra("noHPPelanggan")

        tvTambahPelanggan.text = judul
        etNama.setText(nama)
        etAlamat.setText(alamat)
        etNoHp.setText(noHP)

        if (judul == "Edit Pelanggan") {
            btnSimpan.text = "Sunting"
        } else {
            btnSimpan.text = "Simpan"
        }

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
        val pelangganBaru = myRef.push()
        val id = pelangganBaru.key ?: return
        val data = ModelPelanggan(nama, alamat, noHP, id)

        pelangganBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Pelanggan berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()
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
