package com.android.laundry.Layanan

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.laundry.R
import com.android.laundry.modeldata.ModelLayanan
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class tambah_layanan : AppCompatActivity() {

    // Deklarasi EditText dan Button
    private lateinit var etNamaLayanan: EditText
    private lateinit var etHarga: EditText
    private lateinit var etNamaCabang: EditText
    private lateinit var btnSimpan: Button

    // Referensi ke Firebase Database
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_layanan)

        // Menghubungkan komponen XML ke variabel Kotlin
        etNamaLayanan = findViewById(R.id.etNamaLayanan)
        etHarga = findViewById(R.id.etAlamat)
        etNamaCabang = findViewById(R.id.etNamaCabang)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().getReference("Layanan")

        // Menangani event klik tombol Simpan
        btnSimpan.setOnClickListener {
            simpanLayanan()
        }
    }

    private fun simpanLayanan() {
        val namaLayanan = etNamaLayanan.text.toString().trim()
        val harga = etHarga.text.toString().trim()
        val namaCabang = etNamaCabang.text.toString().trim()

        if (namaLayanan.isEmpty() || harga.isEmpty() || namaCabang.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        // Membuat ID unik untuk setiap layanan
        val layananId = database.push().key!!

        // Menyimpan data ke Firebase Realtime Database
        val layanan = ModelLayanan(namaLayanan, harga, namaCabang)
        database.child(layananId).setValue(layanan)
            .addOnSuccessListener {
                Toast.makeText(this, "Layanan berhasil disimpan", Toast.LENGTH_SHORT).show()
                clearFields()  // Menghapus input setelah berhasil disimpan
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan layanan", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        etNamaLayanan.text.clear()
        etHarga.text.clear()
        etNamaCabang.text.clear()
    }
}
