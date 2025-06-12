package com.android.laundry.Transaksi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.adapter.adapter_transaksi_tambahan
import com.android.laundry.Tambahan.ModelTambahan
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import com.android.laundry.Transaksi.Data_konfirmasi.Konfirmasi_data

class Data_transaksi : AppCompatActivity() {

    private lateinit var tvPelanggan: TextView
    private lateinit var tvNoHp: TextView
    private lateinit var tvLayanan: TextView
    private lateinit var tvHarga: TextView
    private lateinit var etBerat: EditText

    private lateinit var rvDataTambahan: RecyclerView
    private lateinit var listTambahan: ArrayList<ModelTambahan>
    private lateinit var adapterTambahan: adapter_transaksi_tambahan

    // Variabel untuk menyimpan harga per kg dan total harga
    private var hargaPerKg: Int = 0
    private var totalHarga: Double = 0.0

    // DecimalFormat untuk formatting angka
    private val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }

    private val decimalFormat = DecimalFormat("#,##0", symbols)
    private val decimalFormatWithDecimal = DecimalFormat("#,##0.00", symbols)

    private val weightFormat = DecimalFormat("#.##")

    companion object {
        const val REQUEST_CODE_PELANGGAN = 101
        const val REQUEST_CODE_TAMBAHAN = 100
        const val REQUEST_CODE_LAYANAN = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            enableEdgeToEdge()

            setContentView(R.layout.activity_data_transaksi)

            val mainView = findViewById<View>(R.id.data_transaksi)
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
        initViews()

        // Setup RecyclerView untuk tambahan
        setupRecyclerView()

        // Setup TextWatcher untuk EditText berat
        setupBeratTextWatcher()

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
        etBerat = findViewById(R.id.etBerat)

        // Set default text
        tvPelanggan.text = "-"
        tvNoHp.text = "-"
        tvLayanan.text = "-"
        tvHarga.text = "-"
    }

    private fun setupBeratTextWatcher() {
        etBerat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                updateTotalHarga()
            }
        })
    }

    private fun setupRecyclerView() {
        rvDataTambahan = findViewById(R.id.rvDataTambahan)
        listTambahan = arrayListOf()

        adapterTambahan = adapter_transaksi_tambahan(
            listTambahan,
            object : adapter_transaksi_tambahan.OnItemClickListener {
                override fun onItemClick(item: ModelTambahan) {
                    // Toast dihapus - tidak ada aksi saat item tambahan diklik
                }

                override fun onDeleteClick(item: ModelTambahan, position: Int) {
                    // Hapus item dari list
                    adapterTambahan.removeItem(position)
                    updateTotalHarga()
                    Toast.makeText(this@Data_transaksi, getString(R.string.dihapus), Toast.LENGTH_SHORT)
                        .show()
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
        if (!harga.isNullOrEmpty()) {
            // Extract harga per kg dan simpan
            hargaPerKg = harga.replace("Rp ", "").replace(",", "").toIntOrNull() ?: 0
            updateTotalHarga()
        }
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

                    Log.d(
                        "Data_transaksi",
                        "Received - Layanan: $namaLayanan, Harga: $hargaLayanan"
                    )

                    if (namaLayanan.isNotEmpty()) {
                        tvLayanan.text = namaLayanan
                    }

                    // Simpan harga per kg
                    hargaPerKg = hargaLayanan
                    updateTotalHarga()
                }

                REQUEST_CODE_TAMBAHAN -> {
                    val idTambahan = data.getStringExtra(Tambahan_transaksi.EXTRA_ID) ?: ""
                    val namaTambahan = data.getStringExtra(Tambahan_transaksi.EXTRA_NAMA) ?: ""
                    val hargaTambahan = data.getIntExtra(Tambahan_transaksi.EXTRA_HARGA, 0)

                    Log.d(
                        "Data_transaksi",
                        "Received - Tambahan: $namaTambahan, Harga: $hargaTambahan"
                    )

                    if (idTambahan.isNotEmpty() && namaTambahan.isNotEmpty()) {
                        // Cek apakah item sudah ada dalam list (untuk menghindari duplikasi)
                        val existingItem = listTambahan.find { it.id == idTambahan }
                        if (existingItem == null) {
                            val tambahan = ModelTambahan(idTambahan, namaTambahan, hargaTambahan)
                            listTambahan.add(tambahan)
                            adapterTambahan.notifyItemInserted(listTambahan.size - 1)
                            updateTotalHarga()
                            Toast.makeText(this, getString(R.string.ditambahkan, namaTambahan), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, getString(R.string.sudahada, namaTambahan), Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        } else {
            Log.d(
                "Data_transaksi",
                "Result not OK or data is null - RequestCode: $requestCode, ResultCode: $resultCode"
            )
        }
    }

    private fun updateTotalHarga() {
        // Ambil nilai berat dari EditText
        val beratText = etBerat.text.toString().trim()
        val berat = if (beratText.isNotEmpty()) {
            beratText.toDoubleOrNull() ?: 0.0
        } else {
            0.0
        }

        // Hitung harga berdasarkan berat (harga per kg × berat)
        val hargaLayanan = hargaPerKg * berat

        // Hitung total harga tambahan
        val totalHargaTambahan = adapterTambahan.getTotalHarga()

        // Total keseluruhan = (harga per kg × berat) + harga tambahan
        totalHarga = hargaLayanan + totalHargaTambahan

        // Update tampilan harga (hanya menampilkan harga per kg)
        if (hargaPerKg > 0) {
            tvHarga.text = "Rp ${decimalFormat.format(hargaPerKg)}/kg"
        } else {
            tvHarga.text = "-"
        }

        Log.d(
            "Data_transaksi",
            "Berat: ${weightFormat.format(berat)} kg, Harga per kg: Rp ${
                decimalFormat.format(hargaPerKg)
            }, Total harga: Rp ${decimalFormat.format(totalHarga.toLong())}"
        )
    }

    // Di dalam fungsi prosesTransaksi() pada Data_transaksi.kt
// Ganti bagian pengiriman data tambahan

    private fun prosesTransaksi() {
        val namaPelanggan = tvPelanggan.text.toString()
        val noHp = tvNoHp.text.toString()
        val namaLayanan = tvLayanan.text.toString()
        val beratText = etBerat.text.toString().trim()

        // Validasi data (tetap sama seperti sebelumnya)
        if (namaPelanggan == "-" || namaPelanggan.isEmpty()) {
            Toast.makeText(this, getString(R.string.silahkanppelanggan), Toast.LENGTH_SHORT).show()
            return
        }

        if (namaLayanan == "-" || namaLayanan.isEmpty()) {
            Toast.makeText(this, getString(R.string.silahkanplayanan), Toast.LENGTH_SHORT).show()
            return
        }

        if (beratText.isEmpty()) {
            Toast.makeText(this, getString(R.string.silahkanberat), Toast.LENGTH_SHORT).show()
            return
        }

        val berat = beratText.toDoubleOrNull()
        if (berat == null || berat <= 0) {
            Toast.makeText(this, getString(R.string.beratharus), Toast.LENGTH_SHORT).show()
            return
        }

        if (berat > 1000) {
            Toast.makeText(this, getString(R.string.terlaluberat), Toast.LENGTH_SHORT).show()
            return
        }

        // Hitung ulang total harga untuk dikirim
        val hargaLayanan = hargaPerKg * berat
        val totalHargaTambahan = adapterTambahan.getTotalHarga()
        totalHarga = hargaLayanan + totalHargaTambahan

        // PERBAIKAN: Siapkan data tambahan untuk dikirim
        val tambahanIds = arrayListOf<String>()
        val tambahanNama = arrayListOf<String>()
        val tambahanHarga = arrayListOf<Int>()

        // Ambil data dari listTambahan dan pisahkan ke dalam array terpisah
        for (tambahan in listTambahan) {
            tambahanIds.add(tambahan.id)
            tambahanNama.add(tambahan.nama)
            tambahanHarga.add(tambahan.harga ?: 0)
        }

        // Buat intent untuk halaman konfirmasi
        val intent = Intent(this, Konfirmasi_data::class.java).apply {
            putExtra("namaPelanggan", namaPelanggan)
            putExtra("noHp", noHp)
            putExtra("namaLayanan", namaLayanan)
            putExtra("berat", berat)
            putExtra("hargaPerKg", hargaPerKg)
            putExtra("totalHargaTambahan", totalHargaTambahan)
            putExtra("totalHarga", totalHarga)

            // PERBAIKAN: Kirim data tambahan dengan format yang benar
            putStringArrayListExtra("tambahanIds", tambahanIds)
            putStringArrayListExtra("tambahanNama", tambahanNama)
            putIntegerArrayListExtra("tambahanHarga", tambahanHarga)
        }

        startActivity(intent)
    }
}