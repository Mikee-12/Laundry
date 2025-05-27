package com.android.laundry.pelanggan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.laundry.R
import com.google.firebase.database.FirebaseDatabase

class Edit_pelanggan : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etNoHP: EditText
    private lateinit var btnEdit: Button

    private var idPelanggan: String? = null
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pelanggan)

        etNama = findViewById(R.id.etNama)
        etAlamat = findViewById(R.id.etAlamat)
        etNoHP = findViewById(R.id.etNoHP)
        btnEdit = findViewById(R.id.btnEdit)

        // Sesuaikan key dengan yang dikirim dari Adapter
        idPelanggan = intent.getStringExtra("pelangganId")
        val namaLama = intent.getStringExtra("Nama")
        val alamatLama = intent.getStringExtra("Alamat")
        val noHPLama = intent.getStringExtra("noHPPelanggan")

        etNama.setText(namaLama)
        etAlamat.setText(alamatLama)
        etNoHP.setText(noHPLama)

        setEditable(false)

        btnEdit.setOnClickListener {
            if (!isEditMode) {
                isEditMode = true
                setEditable(true)
                btnEdit.text = getString(R.string.simpan)
            } else {
                val namaBaru = etNama.text.toString().trim()
                val alamatBaru = etAlamat.text.toString().trim()
                val noHPBaru = etNoHP.text.toString().trim()

                if (namaBaru.isEmpty() || alamatBaru.isEmpty() || noHPBaru.isEmpty()) {
                    Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val id = idPelanggan ?: run {
                    Toast.makeText(this, "ID pelanggan tidak valid!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val ref = FirebaseDatabase.getInstance()
                    .getReference("pelanggan")
                    .child(id)

                val updateData = mapOf(
                    "nama" to namaBaru,
                    "alamat" to alamatBaru,
                    "noHP" to noHPBaru
                )

                ref.updateChildren(updateData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data pelanggan berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        val resultIntent = Intent().apply {
                            putExtra("id", id)
                            putExtra("nama", namaBaru)
                            putExtra("alamat", alamatBaru)
                            putExtra("noHP", noHPBaru)
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
        etAlamat.isEnabled = enabled
        etNoHP.isEnabled = enabled
    }
}
