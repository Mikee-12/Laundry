package com.android.laundry.Transaksi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.adapter.adapter_tambahan_transaksi
import com.android.laundry.Tambahan.ModelTambahan
import com.google.firebase.database.*

class Tambahan_transaksi : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var tambahanRecyclerView: RecyclerView
    private lateinit var tambahanList: ArrayList<ModelTambahan>
    private lateinit var allTambahanList: ArrayList<ModelTambahan>  // untuk filter
    private lateinit var adapter: adapter_tambahan_transaksi

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_NAMA = "EXTRA_NAMA"
        const val EXTRA_HARGA = "EXTRA_HARGA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_tambahan_transaksi)

        val mainView = findViewById<View>(R.id.transaksi_tambahan)
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

        // Inisialisasi RecyclerView
        tambahanRecyclerView = findViewById(R.id.rvDataTambahan)
        tambahanRecyclerView.layoutManager = LinearLayoutManager(this)
        tambahanRecyclerView.setHasFixedSize(true)

        // Inisialisasi ArrayList
        tambahanList = arrayListOf()
        allTambahanList = arrayListOf()

        // Setup adapter dengan callback untuk item click
        adapter = adapter_tambahan_transaksi(
            tambahanList,
            object : adapter_tambahan_transaksi.OnItemClickListener {
                override fun onItemClick(item: ModelTambahan) {
                    // Kirim data kembali ke Data_transaksi
                    val resultIntent = Intent()
                    resultIntent.putExtra(EXTRA_ID, item.id)
                    resultIntent.putExtra(EXTRA_NAMA, item.nama)
                    resultIntent.putExtra(EXTRA_HARGA, item.harga ?: 0)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            }
        )
        tambahanRecyclerView.adapter = adapter

        // Setup SearchView
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTambahan(newText)
                return true
            }
        })

        // Setup Firebase Database Reference
        dbRef = FirebaseDatabase.getInstance().getReference("Tambahan")

        // Load data dari Firebase
        loadDataFromFirebase()
    }

    private fun loadDataFromFirebase() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = arrayListOf<ModelTambahan>()
                if (snapshot.exists()) {
                    for (tambahanSnap in snapshot.children) {
                        val tambahan = tambahanSnap.getValue(ModelTambahan::class.java)
                        tambahan?.let {
                            // Set ID dari key Firebase jika tidak ada
                            if (it.id.isEmpty()) {
                                it.id = tambahanSnap.key ?: ""
                            }
                            tempList.add(it)
                        }
                    }

                    // Update data
                    allTambahanList.clear()
                    allTambahanList.addAll(tempList)

                    // Tampilkan semua data awalnya
                    tambahanList.clear()
                    tambahanList.addAll(allTambahanList)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error jika perlu
                // Bisa tambahkan Toast atau Log untuk debugging
            }
        })
    }

    private fun filterTambahan(query: String?) {
        if (query.isNullOrEmpty()) {
            // Tampilkan semua data jika query kosong
            tambahanList.clear()
            tambahanList.addAll(allTambahanList)
        } else {
            // Filter berdasarkan nama tambahan
            val filteredList = allTambahanList.filter { tambahan ->
                tambahan.nama.contains(query, ignoreCase = true)
            }
            tambahanList.clear()
            tambahanList.addAll(filteredList)
        }
        adapter.notifyDataSetChanged()
    }
}