package com.android.laundry.Transaksi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.adapter.adapter_transaksi_tambahan
import com.android.laundry.Tambahan.ModelTambahan

class Data_transaksi : AppCompatActivity() {

    private lateinit var tvPelanggan: TextView
    private lateinit var tvNoHp: TextView
    private lateinit var tvLayanan: TextView
    private lateinit var tvHarga: TextView
    // Hapus deklarasi tvTotalHarga karena tidak digunakan di layout

    private lateinit var rvDataTambahan: RecyclerView
    private lateinit var listTambahan: ArrayList<ModelTambahan>
    private lateinit var adapterTambahan: adapter_transaksi_tambahan

    // Variabel untuk menyimpan total harga (opsional)
    private var totalHarga: Int = 0

    companion object {
        const val REQUEST_CODE_PELANGGAN = 101
        const val REQUEST_CODE_TAMBAHAN = 100
        const val REQUEST_CODE_LAYANAN = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_transaksi)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_transaksi)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi views
        initViews()

        // Setup RecyclerView untuk tambahan
        setupRecyclerView()

        // Handle data dari intent (jika ada)
        handleIntent(intent)

        // Setup button click listeners
        setupButtonListeners()
    }

    private fun initViews() {
        tvPelanggan = findViewById(R.id.tvNamaPelanggan)
        tvNoHp = findViewById(R.id.tvNoHp)
        tvLayanan = findViewById(R.id.tvLayanan)
        tvHarga = findViewById(R.id.tvHarga)

        // Set default text
        tvPelanggan.text = "-"
        tvNoHp.text = "-"
        tvLayanan.text = "-"
        tvHarga.text = "-"
        // Hapus baris tvTotalHarga.text = "Rp 0"
    }

    private fun setupRecyclerView() {
        rvDataTambahan = findViewById(R.id.rvDataTambahan)
        listTambahan = arrayListOf()

        adapterTambahan = adapter_transaksi_tambahan(
            listTambahan,
            object : adapter_transaksi_tambahan.OnItemClickListener {
                override fun onItemClick(item: ModelTambahan) {
                    // Optional: aksi saat item tambahan diklik
                    Toast.makeText(this@Data_transaksi, "Klik: ${item.nama}", Toast.LENGTH_SHORT).show()
                }

                override fun onDeleteClick(item: ModelTambahan, position: Int) {
                    // Hapus item dari list
                    adapterTambahan.removeItem(position)
                    updateTotalHarga()
                    Toast.makeText(this@Data_transaksi, "${item.nama} dihapus", Toast.LENGTH_SHORT).show()
                }
            }
        )

        rvDataTambahan.layoutManager = LinearLayoutManager(this)
        rvDataTambahan.adapter = adapterTambahan
    }

    private fun setupButtonListeners() {
        findViewById<Button>(R.id.btnPilihPelanggan).setOnClickListener {
            val intent = Intent(this, Pelanggan_transaksi::class.java)
            startActivityForResult(intent, REQUEST_CODE_PELANGGAN)
        }

        findViewById<Button>(R.id.btnPilihLayanan).setOnClickListener {
            val intent = Intent(this, Layanan_transaksi::class.java)
            startActivityForResult(intent, REQUEST_CODE_LAYANAN)
        }

        findViewById<Button>(R.id.btnTambahan).setOnClickListener {
            val intent = Intent(this, Tambahan_transaksi::class.java)
            startActivityForResult(intent, REQUEST_CODE_TAMBAHAN)
        }

        findViewById<Button>(R.id.btnProses).setOnClickListener {
            prosesTransaksi()
        }
    }

    private fun handleIntent(intent: Intent) {
        val namaPelanggan = intent.getStringExtra("namaPelanggan")
        val noHp = intent.getStringExtra("noHp")
        val namaLayanan = intent.getStringExtra("namaLayanan")
        val harga = intent.getStringExtra("hargaLayanan")

        if (!namaPelanggan.isNullOrEmpty()) tvPelanggan.text = namaPelanggan
        if (!noHp.isNullOrEmpty()) tvNoHp.text = noHp
        if (!namaLayanan.isNullOrEmpty()) tvLayanan.text = namaLayanan
        if (!harga.isNullOrEmpty()) tvHarga.text = harga
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_PELANGGAN -> {
                    val namaPelanggan = data.getStringExtra("namaPelanggan") ?: ""
                    val noHp = data.getStringExtra("noHp") ?: ""

                    Log.d("Data_transaksi", "Received - Nama: $namaPelanggan, No HP: $noHp")

                    if (namaPelanggan.isNotEmpty()) {
                        tvPelanggan.text = namaPelanggan
                    }
                    if (noHp.isNotEmpty()) {
                        tvNoHp.text = noHp
                    }
                }

                REQUEST_CODE_LAYANAN -> {
                    val namaLayanan = data.getStringExtra("NAMA_LAYANAN") ?: ""
                    val hargaLayanan = data.getIntExtra("HARGA_LAYANAN", 0)

                    Log.d("Data_transaksi", "Received - Layanan: $namaLayanan, Harga: $hargaLayanan")

                    if (namaLayanan.isNotEmpty()) {
                        tvLayanan.text = namaLayanan
                    }
                    tvHarga.text = "Rp ${String.format("%,d", hargaLayanan)}"
                    updateTotalHarga()
                }

                REQUEST_CODE_TAMBAHAN -> {
                    val idTambahan = data.getStringExtra(Tambahan_transaksi.EXTRA_ID) ?: ""
                    val namaTambahan = data.getStringExtra(Tambahan_transaksi.EXTRA_NAMA) ?: ""
                    val hargaTambahan = data.getIntExtra(Tambahan_transaksi.EXTRA_HARGA, 0)

                    Log.d("Data_transaksi", "Received - Tambahan: $namaTambahan, Harga: $hargaTambahan")

                    if (idTambahan.isNotEmpty() && namaTambahan.isNotEmpty()) {
                        // Cek apakah item sudah ada dalam list (untuk menghindari duplikasi)
                        val existingItem = listTambahan.find { it.id == idTambahan }
                        if (existingItem == null) {
                            val tambahan = ModelTambahan(idTambahan, namaTambahan, hargaTambahan)
                            listTambahan.add(tambahan)
                            adapterTambahan.notifyItemInserted(listTambahan.size - 1)
                            updateTotalHarga()
                            Toast.makeText(this, "$namaTambahan ditambahkan", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "$namaTambahan sudah ada dalam daftar", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } else {
            Log.d("Data_transaksi", "Result not OK or data is null - RequestCode: $requestCode, ResultCode: $resultCode")
        }
    }

    private fun updateTotalHarga() {
        // Ambil harga layanan
        val hargaLayananText = tvHarga.text.toString()
        val hargaLayanan = if (hargaLayananText != "-" && hargaLayananText.isNotEmpty()) {
            // Extract angka dari format "Rp 123,456"
            hargaLayananText.replace("Rp ", "").replace(",", "").toIntOrNull() ?: 0
        } else {
            0
        }

        // Hitung total harga tambahan
        val totalHargaTambahan = adapterTambahan.getTotalHarga()

        // Total keseluruhan
        totalHarga = hargaLayanan + totalHargaTambahan

        // Log total harga (menggantikan tvTotalHarga.text)
        Log.d("Data_transaksi", "Total harga: Rp ${String.format("%,d", totalHarga)}")

        // Jika Anda ingin menampilkan total harga di UI, bisa menggunakan Toast
        // Toast.makeText(this, "Total: Rp ${String.format("%,d", totalHarga)}", Toast.LENGTH_SHORT).show()
    }

    private fun prosesTransaksi() {
        val namaPelanggan = tvPelanggan.text.toString()
        val noHp = tvNoHp.text.toString()
        val namaLayanan = tvLayanan.text.toString()
        val harga = tvHarga.text.toString()

        // Validasi data
        if (namaPelanggan == "-" || namaPelanggan.isEmpty()) {
            Toast.makeText(this, "Silakan pilih pelanggan terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        if (namaLayanan == "-" || namaLayanan.isEmpty()) {
            Toast.makeText(this, "Silakan pilih layanan terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        // Lanjutkan dengan proses transaksi
        Log.d("Data_transaksi", "Processing transaction for: $namaPelanggan")
        Log.d("Data_transaksi", "Total tambahan: ${listTambahan.size}")
        Log.d("Data_transaksi", "Total harga: Rp ${String.format("%,d", totalHarga)}")

        Toast.makeText(this, "Transaksi berhasil diproses! Total: Rp ${String.format("%,d", totalHarga)}", Toast.LENGTH_LONG).show()
    }}
