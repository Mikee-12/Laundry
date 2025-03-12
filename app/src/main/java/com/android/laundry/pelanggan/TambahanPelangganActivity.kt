package com.android.laundry.pelanggan

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambahan_pelanggan)

        // Initialize views with their IDs from the layout XML
        tvTambahPelanggan = findViewById(R.id.tvTambahPelanggan)
        etNama = findViewById(R.id.etNama)
        etAlamat = findViewById(R.id.etAlamat)
        etNoHp = findViewById(R.id.etNoHp)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Set onClickListener for the "Simpan" button
        btnSimpan.setOnClickListener {
            cekValidasi()
        }
    }

    // Validation function before saving data
    private fun cekValidasi() {
        val nama = etNama.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()
        val noHP = etNoHp.text.toString().trim()

        // Check if any field is empty and set error message accordingly
        if (nama.isEmpty()) {
            etNama.error = getString(R.string.pelanggan_nama)
            etNama.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            etAlamat.error = getString(R.string.alamat)
            etAlamat.requestFocus()
            return
        }
        if (noHP.isEmpty()) {
            etNoHp.error = getString(R.string.nohp)
            etNoHp.requestFocus()
            return
        }

        // Proceed to save data if all fields are valid
        simpan(nama, alamat, noHP)
    }

    // Function to save the data to Firebase
    private fun simpan(nama: String, alamat: String, noHP: String) {
        val pelangganBaru = myRef.push()
        val pelangganId = pelangganBaru.key ?: return
        val timestamp = System.currentTimeMillis().toString()

        // Create a new ModelPelanggan object
        val data = ModelPelanggan(
            nama,
            alamat,
            noHP,
            timestamp
        )

        // Save the data to Firebase
        pelangganBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.validasi_sukses), Toast.LENGTH_SHORT).show()
                finish() // Close the activity after successful save
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.validasi_gagal), Toast.LENGTH_SHORT).show()
            }

    }
}
