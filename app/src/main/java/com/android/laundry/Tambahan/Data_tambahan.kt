package com.android.laundry.Tambahan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Data_tambahan : AppCompatActivity() {

    private lateinit var rvDataTambahan: RecyclerView
    private lateinit var tambahanAdapter: adapter_tambahan
    private val listTambahan = ArrayList<ModelTambahan>()

    // Untuk menangani hasil dari Edit_tambahan
    private val editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val id = result.data!!.getStringExtra("id")
            val nama = result.data!!.getStringExtra("nama")
            val harga = result.data!!.getStringExtra("harga")?.toIntOrNull()

            if (id != null && nama != null && harga != null) {
                val index = listTambahan.indexOfFirst { it.id == id }
                if (index != -1) {
                    listTambahan[index].nama = nama
                    listTambahan[index].harga = harga
                    tambahanAdapter.notifyItemChanged(index)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_data_tambahan)

        val mainView = findViewById<View>(R.id.data_tambahan)
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

        rvDataTambahan = findViewById(R.id.rvDataTambahan)
        rvDataTambahan.layoutManager = LinearLayoutManager(this)

        tambahanAdapter = adapter_tambahan(listTambahan) { tambahan ->
            // Buka halaman edit jika item diklik
            val intent = Intent(this, Edit_tambahan::class.java).apply {
                putExtra("tambahanId", tambahan.id)
                putExtra("nama", tambahan.nama)
                putExtra("harga", tambahan.harga.toString())
            }
            editLauncher.launch(intent)
        }

        rvDataTambahan.adapter = tambahanAdapter

        findViewById<FloatingActionButton>(R.id.fabData_Tambahan).setOnClickListener {
            startActivity(Intent(this, Tambah_tambahan::class.java))
        }

        val dbRef = FirebaseDatabase.getInstance().getReference("Tambahan")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listTambahan.clear()
                for (data in snapshot.children) {
                    val tambahan = data.getValue(ModelTambahan::class.java)
                    if (tambahan != null) listTambahan.add(tambahan)
                }
                tambahanAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Data_tambahan, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}