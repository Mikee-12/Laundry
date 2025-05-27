package com.android.laundry.modeldata

data class ModelPelanggan(
    val nama: String = "",
    val alamat: String = "",
    val noHP: String = "",
    val dateRegistered: String = "",    // tanggal pendaftaran (user-friendly)
    val timestamp: Long? = null         // untuk sorting dan keperluan internal
)
