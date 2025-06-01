package com.android.laundry.Transaksi

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
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
    private lateinit var adapter: adapter_transaksi_layanan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layanan_transaksi)

        layananRecyclerView = findViewById(R.id.rvDataLayanan)
        layananRecyclerView.layoutManager = LinearLayoutManager(this)
        layananRecyclerView.setHasFixedSize(true)

        layananList = arrayListOf()
        adapter = adapter_transaksi_layanan(layananList) { selectedLayanan ->
            val intent = Intent(this, Data_transaksi::class.java)
            intent.putExtra("NAMA_LAYANAN", selectedLayanan.nama)
            intent.putExtra("HARGA_LAYANAN", selectedLayanan.harga)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish() // optional, supaya Layanan_transaksi hilang dari stack
        }


        layananRecyclerView.adapter = adapter

        // Setup SearchView
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        // Ambil data dari Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("layanan")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = ArrayList<ModelLayanan>()
                if (snapshot.exists()) {
                    for (layananSnap in snapshot.children) {
                        val layanan = layananSnap.getValue(ModelLayanan::class.java)
                        layanan?.let { tempList.add(it) }
                    }
                    adapter.updateData(tempList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
