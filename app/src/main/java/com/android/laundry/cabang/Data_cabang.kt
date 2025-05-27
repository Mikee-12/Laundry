package com.android.laundry.cabang

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.adapter.adapter_cabang
import com.android.laundry.modeldata.ModelCabang
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class Data_cabang : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cabangList: ArrayList<ModelCabang>
    private lateinit var adapter: adapter_cabang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_cabang)

        // Atur padding agar tidak terlalu menempel ke atas (menyesuaikan seperti pelanggan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_cabang)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.rvDataCabang)
        recyclerView.layoutManager = LinearLayoutManager(this)
        cabangList = ArrayList()
        adapter = adapter_cabang(cabangList)
        recyclerView.adapter = adapter

        val fabTambah = findViewById<FloatingActionButton>(R.id.fabData_Cabang_Tambah)
        fabTambah.setOnClickListener {
            startActivity(Intent(this, Tambahan_cabang::class.java))
        }

        val dbRef = FirebaseDatabase.getInstance().getReference("Cabang")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cabangList.clear()
                for (data in snapshot.children) {
                    val cabang = data.getValue(ModelCabang::class.java)
                    if (cabang != null) {
                        cabangList.add(cabang)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
