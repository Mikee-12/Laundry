// File: Pembayaran.kt
package com.android.laundry.Transaksi.Data_konfirmasi

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.adapter.adapter_pembayaran
import com.android.laundry.model.ModelPembayaran
import com.google.firebase.database.*
import java.text.NumberFormat
import java.util.*

class Pembayaran : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var adapter: adapter_pembayaran
    private lateinit var rvDataTambahan: RecyclerView
    private var valueEventListener: ValueEventListener? = null

    // Views
    private lateinit var tvIdTransaksi: TextView
    private lateinit var tvTanggal: TextView
    private lateinit var tvNamaPelanggan: TextView
    private lateinit var tvLayanan: TextView
    private lateinit var tvHarga: TextView
    private lateinit var tvMetode: TextView
    private lateinit var tvTotal: TextView
    private lateinit var cardKirim: View

    private var transactionId: String = ""
    private var currentPembayaran: ModelPembayaran? = null

    companion object {
        private const val TAG = "Pembayaran"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_pembayaran)

        val mainView = findViewById<View>(R.id.Pembayaran)
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

        try {
            Log.d(TAG, "=== PEMBAYARAN ACTIVITY STARTED ===")

            enableEdgeToEdge()
            setContentView(R.layout.activity_pembayaran)

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Pembayaran)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            if (initViews()) {
                initRealtimeDatabase()
                setupRecyclerView()
                setupKirimWhatsApp()
                if (getTransactionIdFromIntent()) {
                    loadDataFromFirebase()
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}")
            showError("Terjadi kesalahan saat memuat halaman: ${e.message}")
        }
    }

    private fun initViews(): Boolean {
        return try {
            tvIdTransaksi = findViewById(R.id.tvIdTransaksi)
            tvTanggal = findViewById(R.id.tvTanggal)
            tvNamaPelanggan = findViewById(R.id.tvNamaPelanggan)
            tvLayanan = findViewById(R.id.tvLayanan)
            tvHarga = findViewById(R.id.tvHarga)
            tvMetode = findViewById(R.id.tvMetode)
            tvTotal = findViewById(R.id.tvTotal)
            rvDataTambahan = findViewById(R.id.rvDataTambahan)
            cardKirim = findViewById(R.id.cardKirim)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing views: ${e.message}")
            showError("Gagal menginisialisasi tampilan")
            false
        }
    }

    private fun initRealtimeDatabase() {
        try {
            database = FirebaseDatabase.getInstance().reference
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing Firebase: ${e.message}")
            showError("Gagal menghubungkan ke database")
        }
    }

    private fun setupRecyclerView() {
        try {
            adapter = adapter_pembayaran(emptyList())
            rvDataTambahan.apply {
                layoutManager = LinearLayoutManager(this@Pembayaran)
                adapter = this@Pembayaran.adapter
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up RecyclerView: ${e.message}")
            showError("Gagal mengatur daftar layanan")
        }
    }

    private fun getTransactionIdFromIntent(): Boolean {
        return try {
            transactionId = intent.getStringExtra("TRANSACTION_ID") ?: ""
            if (transactionId.isEmpty()) {
                showError("ID Transaksi tidak ditemukan")
                finish()
                false
            } else {
                tvIdTransaksi.text = transactionId
                true
            }
        } catch (e: Exception) {
            showError("Gagal mendapatkan ID transaksi")
            finish()
            false
        }
    }

    private fun loadDataFromFirebase() {
        try {
            valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        if (dataSnapshot.exists()) {
                            val pembayaran = dataSnapshot.getValue(ModelPembayaran::class.java)
                            if (pembayaran != null) {
                                currentPembayaran = pembayaran
                                displayData(pembayaran)
                                showSuccess("Data berhasil dimuat")
                            } else {
                                showError("Data pembayaran tidak valid")
                            }
                        } else {
                            showError("Data pembayaran tidak ditemukan")
                        }
                    } catch (e: Exception) {
                        showError("Error parsing data: ${e.message}")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    showError("Error: ${databaseError.message}")
                }
            }

            database.child("pembayaran").child(transactionId)
                .addValueEventListener(valueEventListener!!)

        } catch (e: Exception) {
            showError("Gagal memuat data dari database")
        }
    }

    private fun displayData(pembayaran: ModelPembayaran) {
        try {
            tvIdTransaksi.text = pembayaran.transactionId ?: "N/A"
            tvTanggal.text = pembayaran.tanggal ?: "N/A"
            tvNamaPelanggan.text = pembayaran.namaPelanggan ?: "N/A"
            tvLayanan.text = "${pembayaran.namaLayanan ?: "N/A"} (${pembayaran.berat} kg)"
            tvMetode.text = pembayaran.metodePembayaran ?: "N/A"

            val hargaLayanan = (pembayaran.hargaPerKg?.toDouble() ?: 0.0) * (pembayaran.berat ?: 0.0)
            tvHarga.text = formatCurrency(hargaLayanan)

            val layananTambahan = pembayaran.layananTambahan ?: emptyList()
            adapter.updateData(layananTambahan)

            val totalTambahan = layananTambahan.sumOf { it.harga?.toDouble() ?: 0.0 }
            val totalKeseluruhan = hargaLayanan + totalTambahan
            tvTotal.text = formatCurrency(totalKeseluruhan)

        } catch (e: Exception) {
            showError("Gagal menampilkan data")
        }
    }

    // Fungsi untuk memformat nomor telepon
    private fun formatPhoneNumber(phoneNumber: String): String? {
        return try {
            // Hapus semua karakter non-numerik (spasi, tanda hubung, dll)
            val cleanNumber = phoneNumber.replace(Regex("[^0-9]"), "")

            when {
                // Jika sudah dimulai dengan 62, kembalikan apa adanya
                cleanNumber.startsWith("62") -> cleanNumber

                // Jika dimulai dengan 0, ganti dengan 62
                cleanNumber.startsWith("0") -> "62" + cleanNumber.substring(1)

                // Jika dimulai dengan 8 (tanpa 0 di depan), tambah 62
                cleanNumber.startsWith("8") -> "62$cleanNumber"

                // Format lain yang tidak dikenali
                else -> {
                    Log.w(TAG, "Format nomor tidak dikenali: $phoneNumber")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error formatting phone number: ${e.message}")
            null
        }
    }

    // Contoh penggunaan dalam setupKirimWhatsApp()
    private fun setupKirimWhatsApp() {
        cardKirim.setOnClickListener {
            val pembayaran = currentPembayaran
            if (pembayaran != null) {
                val noHp = pembayaran.noHp
                if (!noHp.isNullOrEmpty()) {
                    // Format nomor telepon
                    val formattedNumber = formatPhoneNumber(noHp)

                    if (formattedNumber != null) {
                        Log.d(TAG, "Nomor asli: $noHp")
                        Log.d(TAG, "Nomor setelah format: $formattedNumber")

                        // Buat pesan WhatsApp
                        val hargaLayanan = (pembayaran.hargaPerKg?.toDouble() ?: 0.0) * (pembayaran.berat ?: 0.0)
                        val layananTambahan = pembayaran.layananTambahan ?: emptyList()

                        val detailTambahan = if (layananTambahan.isNotEmpty()) {
                            val sb = StringBuilder()
                            sb.append("\n\nLayanan Tambahan:\n")
                            layananTambahan.forEachIndexed { _, item ->
                                val nama = item.nama ?: "Tidak diketahui"
                                val hargaDouble = item.harga?.toDouble() ?: 0.0
                                sb.append("- $nama: ${formatCurrency(hargaDouble)}\n")
                            }
                            sb.toString()
                        } else {
                            "\n\n(Tidak ada layanan tambahan)"
                        }

                        val totalTambahan = layananTambahan.sumOf { it.harga?.toDouble() ?: 0.0 }
                        val totalKeseluruhan = hargaLayanan + totalTambahan

                        val pesan = """
Halo ${pembayaran.namaPelanggan}

Berikut detail transaksi laundry Anda:

Layanan: ${pembayaran.namaLayanan ?: "N/A"}
Berat: ${pembayaran.berat} Kg
Harga per Kg: ${formatCurrency(pembayaran.hargaPerKg?.toDouble() ?: 0.0)}
Metode Pembayaran: ${pembayaran.metodePembayaran}
$detailTambahan
Total Harga: ${formatCurrency(totalKeseluruhan)}

Terima kasih telah menggunakan layanan kami!
""".trimIndent()

                        // Buat URI WhatsApp dengan nomor yang sudah diformat
                        val uri = Uri.parse("https://wa.me/$formattedNumber?text=" + Uri.encode(pesan))
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.setPackage("com.whatsapp")

                        try {
                            startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(this, getString(R.string.watidak), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.nohptdk), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.nohptdktersedia), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.databelum), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showError(message: String) {
    }

    private fun showSuccess(message: String) {
    }

    private fun formatCurrency(amount: Double): String {
        return try {
            val localeID = Locale("id", "ID")
            val formatter = NumberFormat.getCurrencyInstance(localeID).apply {
                maximumFractionDigits = 2
                minimumFractionDigits = 2
            }
            formatter.format(amount)
        } catch (e: Exception) {
            "Rp ${String.format("%,.2f", amount)}"
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        valueEventListener?.let {
            database.child("pembayaran").child(transactionId).removeEventListener(it)
        }
    }

    override fun onPause() {
        super.onPause()
        valueEventListener?.let {
            database.child("pembayaran").child(transactionId).removeEventListener(it)
        }
    }

    override fun onResume() {
        super.onResume()
        valueEventListener?.let {
            database.child("pembayaran").child(transactionId).addValueEventListener(it)
        }
    }
}
