package com.android.laundry.pegawai

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

class Edit_pegawai : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etNoHP: EditText
    private lateinit var btnSimpan: Button

    private var idPegawai: String? = null
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_edit_pegawai)

        val mainView = findViewById<View>(R.id.edit_pegawai)
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

        // Inisialisasi views
        etNama = findViewById(R.id.etNama)
        etAlamat = findViewById(R.id.etAlamat)
        etNoHP = findViewById(R.id.etNoHp)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Ambil data dari intent
        idPegawai = intent.getStringExtra("pegawaiId")
        val namaLama = intent.getStringExtra("Nama")
        val alamatLama = intent.getStringExtra("Alamat")
        val noHPLama = intent.getStringExtra("noHPPegawai")

        // Set data ke EditText
        etNama.setText(namaLama)
        etAlamat.setText(alamatLama)
        etNoHP.setText(noHPLama)

        // Set awal dalam mode freeze (tidak bisa edit)
        setEditable(false)
        btnSimpan.text = "Edit"

        btnSimpan.setOnClickListener {
            if (!isEditMode) {
                // Mode freeze -> Mode edit
                isEditMode = true
                setEditable(true)
                btnSimpan.text = "Simpan"
            } else {
                // Mode edit -> Simpan data
                val namaBaru = etNama.text.toString().trim()
                val alamatBaru = etAlamat.text.toString().trim()
                val noHPBaru = etNoHP.text.toString().trim()

                if (namaBaru.isEmpty() || alamatBaru.isEmpty() || noHPBaru.isEmpty()) {
                    Toast.makeText(this, getString(R.string.harapisidata), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val id = idPegawai ?: run {
                    return@setOnClickListener
                }

                val ref = FirebaseDatabase.getInstance()
                    .getReference("pegawai")
                    .child(id)

                val updateData = mapOf(
                    "nama" to namaBaru,
                    "alamat" to alamatBaru,
                    "noHP" to noHPBaru
                )

                ref.updateChildren(updateData)
                    .addOnSuccessListener {
                        Toast.makeText(this, getString(R.string.datadiperbarui), Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this, getString(R.string.datagagaldiperbarui), Toast.LENGTH_SHORT).show()
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