package com.android.laundry.layanan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.laundry.R
import com.google.firebase.database.FirebaseDatabase

class Edit_layanan : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etHarga: EditText
    private lateinit var etCabang: EditText
    private lateinit var btnSimpan: Button

    private var layananId: String? = null
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_edit_layanan)

        val mainView = findViewById<View>(R.id.edit_layanan)
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

        etNama = findViewById(R.id.etNamaLayanan)
        etHarga = findViewById(R.id.etHarga)
        etCabang = findViewById(R.id.etCabang)
        btnSimpan = findViewById(R.id.btnSimpan)

        layananId = intent.getStringExtra("layananId")
        val namaLama = intent.getStringExtra("nama")   // Perbaikan di sini
        val hargaLama = intent.getStringExtra("harga")
        val cabangLama = intent.getStringExtra("cabang")

        etNama.setText(namaLama)
        etHarga.setText(hargaLama)
        etCabang.setText(cabangLama)

        // Awalnya non-editable dan tombol bertuliskan Edit
        setEditable(false)
        btnSimpan.text = "Edit"

        btnSimpan.setOnClickListener {
            if (!isEditMode) {
                // Masuk mode edit
                isEditMode = true
                setEditable(true)
                btnSimpan.text = "Simpan"
            } else {
                val namaBaru = etNama.text.toString().trim()
                val hargaBaru = etHarga.text.toString().trim()
                val cabangBaru = etCabang.text.toString().trim()

                if (namaBaru.isEmpty() || hargaBaru.isEmpty() || cabangBaru.isEmpty()) {
                    Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val hargaAngka = hargaBaru.toIntOrNull()
                if (hargaAngka == null) {
                    Toast.makeText(this, "Harga harus berupa angka!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val id = layananId ?: run {
                    Toast.makeText(this, "ID layanan tidak valid!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val ref = FirebaseDatabase.getInstance().getReference("Layanan").child(id)

                val updateData = mapOf(
                    "nama" to namaBaru,
                    "harga" to hargaAngka,
                    "cabang" to cabangBaru
                )

                ref.updateChildren(updateData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data layanan berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        val resultIntent = Intent().apply {
                            putExtra("id", id)
                            putExtra("nama", namaBaru)
                            putExtra("harga", hargaAngka.toString())
                            putExtra("cabang", cabangBaru)
                        }
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal memperbarui data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun setEditable(enabled: Boolean) {
        etNama.isEnabled = enabled
        etHarga.isEnabled = enabled
        etCabang.isEnabled = enabled
    }
}
