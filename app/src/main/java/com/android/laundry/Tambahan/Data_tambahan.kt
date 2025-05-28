package com.android.laundry.Tambahan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Data_tambahan : AppCompatActivity() {

    private lateinit var rvDataTambahan: RecyclerView
    private lateinit var tambahanAdapter: adapter_tambahan
    private val listTambahan = ArrayList<ModelTambahan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_tambahan)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_tambahan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvDataTambahan = findViewById(R.id.rvDataTambahan)
        rvDataTambahan.layoutManager = LinearLayoutManager(this)

        tambahanAdapter = adapter_tambahan(listTambahan)
        rvDataTambahan.adapter = tambahanAdapter

        val fab: FloatingActionButton = findViewById(R.id.fabData_Tambahan)
        fab.setOnClickListener {
            startActivity(Intent(this, Tambah_tambahan::class.java))
        }

        // 🔽 Tambahkan bagian ini untuk mengambil data dari Firebase
        val dbRef = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("Tambahan")
        dbRef.get().addOnSuccessListener { snapshot ->
            listTambahan.clear() // bersihkan data sebelumnya
            for (data in snapshot.children) {
                val tambahan = data.getValue(ModelTambahan::class.java)
                if (tambahan != null) {
                    listTambahan.add(tambahan)
                }
            }
            tambahanAdapter.notifyDataSetChanged()
        }.addOnFailureListener {
            android.widget.Toast.makeText(this, "Gagal mengambil data", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}
