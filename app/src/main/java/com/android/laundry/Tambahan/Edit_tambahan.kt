    package com.android.laundry.Tambahan

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

    class Edit_tambahan : AppCompatActivity() {

        private lateinit var etNamaTambahan: EditText
        private lateinit var etHarga: EditText
        private lateinit var btnSimpan: Button

        private var tambahanId: String? = null
        private var isEditMode = false

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            enableEdgeToEdge()

            setContentView(R.layout.activity_edit_tambahan)

            val mainView = findViewById<View>(R.id.edit_tambahan)
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

            etNamaTambahan = findViewById(R.id.etNamaTambahan)
            etHarga = findViewById(R.id.etHarga)
            btnSimpan = findViewById(R.id.btnSimpan)

            // Ambil data lama dari intent
            tambahanId = intent.getStringExtra("tambahanId")
            val namaLama = intent.getStringExtra("nama")
            val hargaLama = intent.getStringExtra("harga")

            // Tampilkan data lama di input
            etNamaTambahan.setText(namaLama)
            etHarga.setText(hargaLama)

            // Set awal: hanya lihat (read-only)
            setEditable(false)
            btnSimpan.text = "Edit"

            btnSimpan.setOnClickListener {
                if (!isEditMode) {
                    // Masuk ke mode edit
                    isEditMode = true
                    setEditable(true)
                    btnSimpan.text = "Simpan"
                } else {
                    // Ambil input terbaru
                    val namaBaru = etNamaTambahan.text.toString().trim()
                    val hargaBaru = etHarga.text.toString().trim()

                    // Validasi input
                    if (namaBaru.isEmpty() || hargaBaru.isEmpty()) {
                        Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val hargaAngka = hargaBaru.toIntOrNull()
                    if (hargaAngka == null) {
                        Toast.makeText(this, "Harga harus berupa angka!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    // Pastikan id tidak null
                    val id = tambahanId ?: run {
                        Toast.makeText(this, "ID tidak ditemukan!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    // Update data ke Firebase
                    val ref = FirebaseDatabase.getInstance().getReference("Tambahan").child(id)
                    val updateData = mapOf(
                        "nama" to namaBaru,
                        "harga" to hargaAngka
                    )

                    ref.updateChildren(updateData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                            setEditable(false)
                            isEditMode = false
                            btnSimpan.text = "Edit"

                            // Kirim hasil kembali ke activity sebelumnya
                            val resultIntent = Intent().apply {
                                putExtra("id", id)
                                putExtra("nama", namaBaru)
                                putExtra("harga", hargaAngka.toString())
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish() // tutup activity setelah simpan
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Gagal update: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        private fun setEditable(enabled: Boolean) {
            etNamaTambahan.isEnabled = enabled
            etHarga.isEnabled = enabled
        }
    }

