// File: ModelPembayaran.kt
package com.android.laundry.model

import com.android.laundry.Tambahan.ModelTambahan

data class ModelPembayaran(
    val transactionId: String = "",
    val namaPelanggan: String = "",
    val noHp: String = "",
    val namaLayanan: String = "",
    val hargaPerKg: Int = 0,
    val berat: Double = 0.0,
    val totalHarga: Double = 0.0,
    val metodePembayaran: String = "",
    val layananTambahan: List<ModelTambahan> = emptyList(),
    val hargaTambahan: Int = 0,
    val timestamp: Long = 0L,
    val tanggal: String = "",
    val status: String = "Pending"
) {
    // Constructor tanpa parameter untuk Firebase
    constructor() : this(
        transactionId = "",
        namaPelanggan = "",
        noHp = "",
        namaLayanan = "",
        hargaPerKg = 0,
        berat = 0.0,
        totalHarga = 0.0,
        metodePembayaran = "",
        layananTambahan = emptyList(),
        hargaTambahan = 0,
        timestamp = 0L,
        tanggal = "",
        status = "Pending"
    )
}