package com.android.laundry.pelanggan

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.laundry.R

class activity_tambahan_pelanggan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambahan_pelanggan)

        val etNama = findViewById<EditText>(R.id.etNama)
        val etAlamat = findViewById<EditText>(R.id.etAlamat)
        val etNoHp = findViewById<EditText>(R.id.etNoHp)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        // Filter hanya huruf dan spasi untuk etNama
        val hurufFilter = object : InputFilter {
            override fun filter(
                source: CharSequence?,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                if (source != null && !source.matches(Regex("^[a-zA-Z\\s]+$"))) {
                    Toast.makeText(applicationContext, "Hanya boleh huruf!", Toast.LENGTH_SHORT).show()
                    return ""
                }
                return null
            }
        }

        etNama.filters = arrayOf(hurufFilter, InputFilter.LengthFilter(50))

        // Kapitalisasi setiap kata
        etNama.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) return

                val words = s.toString().split(" ").map { word ->
                    word.replaceFirstChar { it.uppercaseChar() }
                }
                etNama.removeTextChangedListener(this)
                etNama.setText(words.joinToString(" "))
                etNama.setSelection(etNama.text.length)
                etNama.addTextChangedListener(this)
            }
        })

        // Filter angka dan panjang maksimal 14 digit untuk noHp
        val angkaFilter = object : InputFilter {
            override fun filter(
                source: CharSequence?,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                return if (source != null && source.matches(Regex("^[0-9]+$"))) {
                    source
                } else {
                    Toast.makeText(applicationContext, "Hanya boleh angka!", Toast.LENGTH_SHORT).show()
                    ""
                }
            }
        }

        etNoHp.filters = arrayOf(angkaFilter, InputFilter.LengthFilter(14))

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()
            val noHp = etNoHp.text.toString().trim()

            if (nama.isEmpty() || alamat.isEmpty() || noHp.isEmpty()) {
                showToast("Harap isi semua data!")
            } else {
                showToast("Data pelanggan berhasil disimpan!")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
