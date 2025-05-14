package com.android.laundry.Layanan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.adapter.adapter_layanan
import com.android.laundry.modeldata.ModelLayanan
import com.google.firebase.database.*

class Layanan : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layananAdapter: adapter_layanan
    private lateinit var database: DatabaseReference
    private lateinit var layananList: MutableList<ModelLayanan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layanan)

        // Ambil data dari intent (ID dan nama pelanggan)
        val idPelanggan = intent.getStringExtra("idPelanggan") ?: "ID Tidak Tersedia"
        val namaPelanggan = intent.getStringExtra("namaPelanggan") ?: "Nama Tidak Tersedia"

        // Tampilkan data pelanggan dalam Toast
        Toast.makeText(this, "ID: $idPelanggan\nNama: $namaPelanggan", Toast.LENGTH_LONG).show()

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerViewLayanan)
        recyclerView.layoutManager = LinearLayoutManager(this)
        layananList = mutableListOf()
        layananAdapter = adapter_layanan(layananList)  // Gunakan LayananAdapter
        recyclerView.adapter = layananAdapter

        // Ambil data layanan dari Firebase
        getDataLayanan()
    }

    private fun getDataLayanan() {
        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().getReference("layanan")

        // Mengambil data layanan
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    layananList.clear()
                    for (layananSnapshot in snapshot.children) {
                        val layanan = layananSnapshot.getValue(ModelLayanan::class.java)
                        if (layanan != null) {
                            layananList.add(layanan)
                        }
                    }
                    layananAdapter.notifyDataSetChanged() // Memberitahu adapter bahwa data telah berubah
                } else {
                    Toast.makeText(this@Layanan, "Data layanan tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Layanan, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
