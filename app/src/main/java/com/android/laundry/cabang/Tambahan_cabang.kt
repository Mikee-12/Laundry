package com.android.laundry.cabang

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.laundry.R
import com.android.laundry.modeldata.ModelCabang
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Tambahan_cabang : AppCompatActivity() {

    private lateinit var etNamaCabang: EditText
    private lateinit var etManager: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etNoHp: EditText
    private lateinit var btnSimpan: Button
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambahan_cabang)

        // Inisialisasi View
        etNamaCabang = findViewById(R.id.etNamaCabang)
        etManager = findViewById(R.id.etManager)
        etAlamat = findViewById(R.id.etAlamat)
        etNoHp = findViewById(R.id.etNoHp)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Inisialisasi Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("Cabang")

        btnSimpan.setOnClickListener {
            saveCabang()
        }
    }

    private fun saveCabang() {
        val namaCabang = etNamaCabang.text.toString()
        val manager = etManager.text.toString()
        val alamat = etAlamat.text.toString()
        val noHp = etNoHp.text.toString()

        if (namaCabang.isEmpty() || manager.isEmpty() || alamat.isEmpty() || noHp.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        val cabangId = dbRef.push().key!!

        val cabang = ModelCabang(
            id = cabangId,
            namaCabang = namaCabang,
            manager = manager,
            alamat = alamat,
            noHp = noHp
        )

        dbRef.child(cabangId).setValue(cabang)
            .addOnCompleteListener {
                Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish() // kembali ke halaman sebelumnya
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Gagal: ${err.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
