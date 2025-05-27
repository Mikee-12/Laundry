package com.android.laundry.layanan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.laundry.R
import com.android.laundry.modeldata.ModelLayanan
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.view.View

class tambahan_layanan : AppCompatActivity() {

    private lateinit var etNamaLayanan: EditText
    private lateinit var etHargaLayanan: EditText
    private lateinit var etCabang: EditText
    private lateinit var btnSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hapus bar hitam status bar, fullscreen dan transparan
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        enableEdgeToEdge()
        setContentView(R.layout.activity_tambahan_layanan)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tambah_layanan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val defaultPadding = 18  // padding dalam pixel, bukan dp

            val scale = resources.displayMetrics.density
            val paddingPx = (defaultPadding * scale).toInt()  // konversi dp ke px

            v.setPadding(
                systemBars.left + paddingPx,
                systemBars.top + paddingPx,
                systemBars.right + paddingPx,
                systemBars.bottom + paddingPx
            )
            insets
        }

        etNamaLayanan = findViewById(R.id.etNamaLayanan)
        etHargaLayanan = findViewById(R.id.etHarga)
        etCabang = findViewById(R.id.etCabang)
        btnSimpan = findViewById(R.id.btnSimpan)

        val ref = FirebaseDatabase.getInstance().getReference("Layanan")

        btnSimpan.setOnClickListener {
            val namaLayanan = etNamaLayanan.text.toString().trim()
            val hargaLayananStr = etHargaLayanan.text.toString().trim()
            val cabang = etCabang.text.toString().trim()

            if (namaLayanan.isEmpty() || hargaLayananStr.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hargaLayanan = hargaLayananStr.toIntOrNull()
            if (hargaLayanan == null) {
                Toast.makeText(this, "Harga harus berupa angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            generateNewId(ref) { newId ->
                val layananBaru = ModelLayanan(newId, namaLayanan, hargaLayanan, cabang)
                ref.child(newId).setValue(layananBaru)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Layanan berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Gagal menambahkan layanan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun generateNewId(ref: DatabaseReference, callback: (String) -> Unit) {
        ref.get().addOnSuccessListener { snapshot ->
            var maxId = 0
            for (child in snapshot.children) {
                val key = child.key ?: continue
                val number = key.toIntOrNull()
                if (number != null && number > maxId) {
                    maxId = number
                }
            }
            val newId = String.format("%05d", maxId + 1)
            callback(newId)
        }.addOnFailureListener {
            callback("00001")
        }
    }
}
