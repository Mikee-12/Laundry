package com.android.laundry.Tambahan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.laundry.R
import android.content.Intent
import com.android.laundry.Tambahan.Data_tambahan
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Tambah_tambahan : AppCompatActivity() {

    private lateinit var etNamaTambahan: EditText
    private lateinit var etHarga: EditText
    private lateinit var btnSimpan: Button
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_tambahan)

        etNamaTambahan = findViewById(R.id.etNamaTambahan)
        etHarga = findViewById(R.id.etHarga)
        btnSimpan = findViewById(R.id.btnSimpan)

        dbRef = FirebaseDatabase.getInstance().getReference("Tambahan")

        btnSimpan.setOnClickListener {
            simpanDataTambahan()
        }
    }

    private fun simpanDataTambahan() {
        val nama = etNamaTambahan.text.toString().trim()
        val harga = etHarga.text.toString().trim()

        if (nama.isEmpty()) {
            etNamaTambahan.error = "Nama tambahan harus diisi"
            etNamaTambahan.requestFocus()
            return
        }

        if (harga.isEmpty()) {
            etHarga.error = "Harga harus diisi"
            etHarga.requestFocus()
            return
        }

        // Ambil jumlah data saat ini untuk menentukan ID berikutnya
        dbRef.get().addOnSuccessListener { snapshot ->
            val nextId = snapshot.childrenCount.toInt() + 1
            val idFormatted = String.format("%05d", nextId) // format jadi 00001, 00002, dst

            val tambahan = ModelTambahan(idFormatted, nama, harga)

            dbRef.child(idFormatted).setValue(tambahan)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Data tambahan berhasil disimpan", Toast.LENGTH_SHORT).show()
                        etNamaTambahan.text.clear()
                        etHarga.text.clear()

                        // Kembali ke DataTambahanActivity
                        val intent = Intent(this, Data_tambahan::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                    }
                }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mengambil data dari database", Toast.LENGTH_SHORT).show()
        }
    }

}
