package com.android.laundry.Transaksi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView  // Changed from android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.adapter.adapter_transaksi_pelanggan
import com.android.laundry.modeldata.ModelPelanggan
import com.google.firebase.database.*

class Pelanggan_transaksi : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var pelangganRecyclerView: RecyclerView
    private lateinit var pelangganList: ArrayList<ModelPelanggan>
    private lateinit var adapter: adapter_transaksi_pelanggan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelanggan_transaksi)

        pelangganRecyclerView = findViewById(R.id.rvDataPelanggan)
        pelangganRecyclerView.layoutManager = LinearLayoutManager(this)
        pelangganRecyclerView.setHasFixedSize(true)

        pelangganList = arrayListOf()
        adapter = adapter_transaksi_pelanggan(pelangganList) { selectedPelanggan ->
            // Kirim data kembali ke Data_transaksi via setResult
            val resultIntent = Intent()
            resultIntent.putExtra("namaPelanggan", selectedPelanggan.nama)
            resultIntent.putExtra("noHp", selectedPelanggan.noHP)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        pelangganRecyclerView.adapter = adapter

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

        dbRef = FirebaseDatabase.getInstance().getReference("pelanggan")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = ArrayList<ModelPelanggan>()
                if (snapshot.exists()) {
                    for (pelangganSnap in snapshot.children) {
                        val pelanggan = pelangganSnap.getValue(ModelPelanggan::class.java)
                        pelanggan?.let { tempList.add(it) }
                    }
                    adapter.updateData(tempList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error jika perlu
            }
        })
    }
}