package com.android.laundry.layanan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.adapter.adapter_layanan
import com.android.laundry.modeldata.ModelLayanan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class Data_layanan : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layananList: ArrayList<ModelLayanan>
    private lateinit var layananAdapter: adapter_layanan
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Aktifkan edge-to-edge layout
        setContentView(R.layout.activity_data_layanan)

        // Atur padding agar konten tidak tertutup status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_layanan)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.rvDataLayanan)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        layananList = ArrayList()
        layananAdapter = adapter_layanan(layananList)
        recyclerView.adapter = layananAdapter

        // Ambil data dari Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference("Layanan")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                layananList.clear()
                for (layananSnapshot in snapshot.children) {
                    val layanan = layananSnapshot.getValue(ModelLayanan::class.java)
                    layanan?.let { layananList.add(it) }
                }
                layananAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Tambahkan log atau toast jika perlu
            }
        })

        // Tombol tambah
        val fabTambah = findViewById<FloatingActionButton>(R.id.fabData_Layanan_Tambah)
        fabTambah.setOnClickListener {
            startActivity(Intent(this, tambahan_layanan::class.java))
        }
    }
}
