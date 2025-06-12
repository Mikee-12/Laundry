// File: Konfirmasi_data.kt (Comprehensive Debug Version - No Dialog)
package com.android.laundry.Transaksi.Data_konfirmasi

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
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
import com.android.laundry.Tambahan.ModelTambahan
import com.android.laundry.adapter.adapter_konvirmasi_pelanggan
import com.android.laundry.adapter.adapter_dialog_pembayaran
import com.android.laundry.model.ModelPembayaran
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import android.view.View
import androidx.activity.enableEdgeToEdge
import java.util.*

class Konfirmasi_data : AppCompatActivity() {

    private lateinit var tvNamaPelanggan: TextView
    private lateinit var tvNoHp: TextView
    private lateinit var tvLayanan: TextView
    private lateinit var tvHarga: TextView
    private lateinit var tvTotal: TextView
    private lateinit var rvDataTambahan: RecyclerView
    private lateinit var btnBatal: Button
    private lateinit var btnBayar: Button

    private lateinit var listTambahan: ArrayList<ModelTambahan>
    private lateinit var adapterTambahan: adapter_konvirmasi_pelanggan
    private lateinit var adapterdialogPembayaran: adapter_dialog_pembayaran

    private var namaPelanggan: String = ""
    private var noHp: String = ""
    private var namaLayanan: String = ""
    private var hargaPerKg: Int = 0
    private var berat: Double = 0.0
    private var totalHarga: Double = 0.0

    private lateinit var database: DatabaseReference
    private val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    private val decimalFormat = DecimalFormat("#,##0", symbols)
    private val decimalFormatWithDecimal = DecimalFormat("#,##0.00", symbols)
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    companion object {
        private const val TAG = "Konfirmasi_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_konfirmasi_data)

        val mainView = findViewById<View>(R.id.konvirmasi_data)
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

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().reference

        initViews()
        setupRecyclerView()
        setupPaymentAdapter()
        handleIntentData()
        setupButtonListeners()

        // Debug: Print all available activities
        debugAvailableActivities()
    }

    private fun debugAvailableActivities() {
        try {
            val packageManager = packageManager
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)

            Log.d(TAG, "=== AVAILABLE ACTIVITIES ===")
            packageInfo.activities?.forEach { activityInfo ->
                Log.d(TAG, "Activity: ${activityInfo.name}")
            }
            Log.d(TAG, "=== END ACTIVITIES ===")

        } catch (e: Exception) {
            Log.e(TAG, "Error getting activities: ${e.message}")
        }
    }

    private fun initViews() {
        tvNamaPelanggan = findViewById(R.id.tvNamaPelanggan)
        tvNoHp = findViewById(R.id.tvNoHp)
        tvLayanan = findViewById(R.id.tvLayanan)
        tvHarga = findViewById(R.id.tvHarga)
        tvTotal = findViewById(R.id.tvTotal)
        rvDataTambahan = findViewById(R.id.rvDataTambahan)
        btnBatal = findViewById(R.id.btnBatal)
        btnBayar = findViewById(R.id.btnBayar)
    }

    private fun setupRecyclerView() {
        listTambahan = arrayListOf()
        adapterTambahan = adapter_konvirmasi_pelanggan(listTambahan) {}
        rvDataTambahan.layoutManager = LinearLayoutManager(this)
        rvDataTambahan.adapter = adapterTambahan
    }

    private fun setupPaymentAdapter() {
        adapterdialogPembayaran = adapter_dialog_pembayaran(this) { paymentType ->
            sendDataToFirebase(paymentType)
        }
    }

    private fun handleIntentData() {
        namaPelanggan = intent.getStringExtra("namaPelanggan") ?: ""
        noHp = intent.getStringExtra("noHp") ?: ""
        namaLayanan = intent.getStringExtra("namaLayanan") ?: ""
        hargaPerKg = intent.getIntExtra("hargaPerKg", 0)
        berat = intent.getDoubleExtra("berat", 0.0)
        totalHarga = intent.getDoubleExtra("totalHarga", 0.0)

        val tambahanIds = intent.getStringArrayListExtra("tambahanIds") ?: arrayListOf()
        val tambahanNama = intent.getStringArrayListExtra("tambahanNama") ?: arrayListOf()
        val hargaTambahan = intent.getIntegerArrayListExtra("tambahanHarga") ?: arrayListOf()

        tvNamaPelanggan.text = namaPelanggan
        tvNoHp.text = noHp
        tvLayanan.text = "$namaLayanan ($berat kg)"
        tvHarga.text = "Rp ${decimalFormat.format(hargaPerKg)}/kg"
        tvTotal.text = "Rp ${decimalFormat.format(totalHarga.toLong())}"

        if (tambahanIds.size == tambahanNama.size && tambahanNama.size == hargaTambahan.size) {
            listTambahan.clear()
            for (i in tambahanIds.indices) {
                val tambahan = ModelTambahan(
                    id = tambahanIds[i],
                    nama = tambahanNama[i],
                    harga = hargaTambahan[i]
                )
                listTambahan.add(tambahan)
            }
            adapterTambahan.notifyDataSetChanged()

            Log.d(TAG, "Data tambahan berhasil dimuat: ${listTambahan.size} item")
        }
    }

    private fun setupButtonListeners() {
        btnBatal.setOnClickListener {
            finish()
        }

        btnBayar.setOnClickListener {
            adapterdialogPembayaran.showPaymentDialog()
        }
    }

    private fun sendDataToFirebase(metodePembayaran: String) {
        val transactionId = database.child("pembayaran").push().key ?: UUID.randomUUID().toString()
        val layananTambahanFromView = getLayananTambahanFromRecyclerView()
        val currentTime = System.currentTimeMillis()
        val tanggalTransaksi = dateFormat.format(Date(currentTime))

        val pembayaran = ModelPembayaran(
            transactionId = transactionId,
            namaPelanggan = namaPelanggan,
            noHp = noHp,
            namaLayanan = namaLayanan,
            hargaPerKg = hargaPerKg,
            berat = berat,
            totalHarga = totalHarga,
            metodePembayaran = metodePembayaran,
            layananTambahan = layananTambahanFromView,
            hargaTambahan = 0,
            timestamp = currentTime,
            tanggal = tanggalTransaksi,
            status = "Pending"
        )

        btnBayar.isEnabled = false
        btnBayar.text = "Memproses..."

        database.child("pembayaran").child(transactionId).setValue(pembayaran)
            .addOnSuccessListener {
                Log.d(TAG, "Data berhasil disimpan dengan ID: $transactionId")
                Toast.makeText(this, getString(R.string.transaksiberhasildibuat), Toast.LENGTH_SHORT).show()

                // Langsung navigate ke Pembayaran activity tanpa dialog
                navigateToPembayaran(transactionId)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Gagal menyimpan data: ${exception.message}")
                Toast.makeText(this, getString(R.string.transaksigagaldibuat), Toast.LENGTH_SHORT).show()
                btnBayar.isEnabled = true
                btnBayar.text = "Bayar"
            }
    }

    // Navigation dengan multiple fallback
    private fun navigateToPembayaran(transactionId: String) {
        Log.d(TAG, "=== STARTING NAVIGATION DEBUG ===")
        Log.d(TAG, "Transaction ID: $transactionId")
        Log.d(TAG, "Package name: $packageName")

        // Method 1: Try class reference
        if (tryNavigateWithClassReference(transactionId)) {
            Log.d(TAG, "Success: Class reference method")
            return
        }

        // Method 2: Try setClassName with different variations
        if (tryNavigateWithClassName(transactionId)) {
            Log.d(TAG, "Success: ClassName method")
            return
        }

        // Method 3: Try explicit component
        if (tryNavigateWithComponent(transactionId)) {
            Log.d(TAG, "Success: Component method")
            return
        }

        // Method 4: Fallback - show simple toast and finish
        Log.e(TAG, "All navigation methods failed")
        showNavigationFailure(transactionId)
    }

    private fun tryNavigateWithClassReference(transactionId: String): Boolean {
        return try {
            Log.d(TAG, "Trying class reference method...")

            // Check if Pembayaran class exists
            val pembayaranClass = Class.forName("com.android.laundry.Transaksi.Data_konfirmasi.Pembayaran")
            Log.d(TAG, "Pembayaran class found: ${pembayaranClass.name}")

            val intent = Intent(this, pembayaranClass).apply {
                putExtra("TRANSACTION_ID", transactionId)
                putExtra("NAMA_PELANGGAN", namaPelanggan)
                putExtra("NO_HP", noHp)
                putExtra("NAMA_LAYANAN", namaLayanan)
                putExtra("TOTAL_HARGA", totalHarga)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
                finish()
                true
            } else {
                Log.e(TAG, "Intent cannot be resolved")
                false
            }

        } catch (e: Exception) {
            Log.e(TAG, "Class reference failed: ${e.message}")
            false
        }
    }

    private fun tryNavigateWithClassName(transactionId: String): Boolean {
        val classNames = listOf(
            "com.android.laundry.Transaksi.Data_konfirmasi.Pembayaran",
            ".Transaksi.Data_konfirmasi.Pembayaran",
            "Pembayaran"
        )

        for (className in classNames) {
            try {
                Log.d(TAG, "Trying className: $className")

                val intent = Intent().apply {
                    setClassName(packageName, className)
                    putExtra("TRANSACTION_ID", transactionId)
                    putExtra("NAMA_PELANGGAN", namaPelanggan)
                    putExtra("NO_HP", noHp)
                    putExtra("NAMA_LAYANAN", namaLayanan)
                    putExtra("TOTAL_HARGA", totalHarga)
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                    finish()
                    return true
                }

            } catch (e: Exception) {
                Log.e(TAG, "ClassName $className failed: ${e.message}")
            }
        }

        return false
    }

    private fun tryNavigateWithComponent(transactionId: String): Boolean {
        return try {
            Log.d(TAG, "Trying component method...")

            val component = ComponentName(
                packageName,
                "com.android.laundry.Transaksi.Data_konfirmasi.Pembayaran"
            )

            val intent = Intent().apply {
                putExtra("TRANSACTION_ID", transactionId)
                putExtra("NAMA_PELANGGAN", namaPelanggan)
                putExtra("NO_HP", noHp)
                putExtra("NAMA_LAYANAN", namaLayanan)
                putExtra("TOTAL_HARGA", totalHarga)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
                finish()
                true
            } else {
                Log.e(TAG, "Component cannot be resolved")
                false
            }

        } catch (e: Exception) {
            Log.e(TAG, "Component method failed: ${e.message}")
            false
        }
    }

    private fun showNavigationFailure(transactionId: String) {
        btnBayar.isEnabled = true
        btnBayar.text = "Bayar"

        // Simple toast instead of dialog
        Toast.makeText(this, getString(R.string.transaksiberhasildibuat), Toast.LENGTH_SHORT).show()

        // Just finish the activity
        finish()
    }

    private fun getLayananTambahanFromRecyclerView(): List<ModelTambahan> {
        val layananTambahan = mutableListOf<ModelTambahan>()

        try {
            for (i in 0 until rvDataTambahan.childCount) {
                val cardView = rvDataTambahan.getChildAt(i)
                val tvTambahan = cardView.findViewById<TextView>(R.id.tvTambahan)
                val tvHarga = cardView.findViewById<TextView>(R.id.tvHarga)

                if (tvTambahan != null && tvHarga != null) {
                    val namaTambahan = tvTambahan.text.toString()
                    val hargaText = tvHarga.text.toString()
                    val harga = parseHargaFromText(hargaText)

                    val tambahan = ModelTambahan(
                        id = (i + 1).toString(),
                        nama = namaTambahan,
                        harga = harga
                    )

                    layananTambahan.add(tambahan)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error mengambil data dari RecyclerView: ${e.message}")
            return listTambahan.toList()
        }

        return layananTambahan
    }

    private fun parseHargaFromText(hargaText: String): Int {
        return try {
            val cleanText = hargaText.replace(Regex("[^\\d]"), "")
            if (cleanText.isNotEmpty()) {
                cleanText.toInt()
            } else {
                0
            }
        } catch (e: NumberFormatException) {
            Log.e(TAG, "Error parsing harga: $hargaText")
            0
        }
    }
}