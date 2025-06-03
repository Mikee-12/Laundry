package com.android.laundry.Transaksi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView  // Changed from android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.adapter.adapter_transaksi_layanan
import com.android.laundry.modeldata.ModelLayanan
import com.google.firebase.database.*

class Layanan_transaksi : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var layananRecyclerView: RecyclerView
    private lateinit var layananList: ArrayList<ModelLayanan>
    private lateinit var allLayananList: ArrayList<ModelLayanan>  // data lengkap, untuk filter
    private lateinit var adapter: adapter_transaksi_layanan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layanan_transaksi)

        layananRecyclerView = findViewById(R.id.rvDataLayanan)
        layananRecyclerView.layoutManager = LinearLayoutManager(this)
        layananRecyclerView.setHasFixedSize(true)

        layananList = arrayListOf()
        allLayananList = arrayListOf()  // inisialisasi list lengkap

        adapter = adapter_transaksi_layanan(layananList) { selectedLayanan ->
            val resultIntent = Intent()
            resultIntent.putExtra("NAMA_LAYANAN", selectedLayanan.nama)
            resultIntent.putExtra("HARGA_LAYANAN", selectedLayanan.harga)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        layananRecyclerView.adapter = adapter

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterLayanan(newText)
                return true
            }
        })

        dbRef = FirebaseDatabase.getInstance().getReference("Layanan")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = arrayListOf<ModelLayanan>()
                if (snapshot.exists()) {
                    for (layananSnap in snapshot.children) {
                        val layanan = layananSnap.getValue(ModelLayanan::class.java)
                        layanan?.let { tempList.add(it) }
                    }
                    allLayananList.clear()
                    allLayananList.addAll(tempList)

                    // Awalnya tampilkan semua data
                    adapter.updateData(allLayananList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Tangani error jika perlu
            }
        })
    }

    private fun filterLayanan(query: String?) {
        if (query.isNullOrEmpty()) {
            adapter.updateData(allLayananList)  // reset tampilkan semua jika kosong
        } else {
            val filteredList = allLayananList.filter { layanan ->
                layanan.nama?.contains(query, ignoreCase = true) == true
                // Kalau mau filter berdasarkan cabang juga bisa ditambahkan OR:
                // || layanan.cabang?.contains(query, ignoreCase = true) == true
            }
            adapter.updateData(filteredList)
        }
    }
}