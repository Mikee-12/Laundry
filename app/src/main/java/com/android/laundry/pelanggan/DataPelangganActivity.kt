package com.android.laundry.pelanggan

import android.content.Intent
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.android.laundry.R
import com.android.laundry.modeldata.ModelPelanggan
import com.android.laundry.adapter.adapter_data_pelanggan

class DataPelangganActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pelanggan")
    private lateinit var rvDataPelangganActivity: RecyclerView
    private lateinit var fabTambahanPelangganActivity: FloatingActionButton
    private lateinit var pelangganList: ArrayList<ModelPelanggan>
    private lateinit var pelangganKeyList: ArrayList<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pelanggan)

        initViews()
        setupRecyclerView()
        loadData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_pelanggan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        rvDataPelangganActivity = findViewById(R.id.rvDataPelanggan)
        fabTambahanPelangganActivity = findViewById(R.id.fabData_Pelanggan_Tambah)
        pelangganList = ArrayList()
        pelangganKeyList = ArrayList()

        fabTambahanPelangganActivity.setOnClickListener {
            startActivity(Intent(this, TambahanPelangganActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        rvDataPelangganActivity.layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = false
            stackFromEnd = false
        }
        rvDataPelangganActivity.setHasFixedSize(true)
    }

    private fun loadData() {
        val query = myRef.limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pelangganList.clear()
                pelangganKeyList.clear()

                snapshot.children.forEach { dataSnapshot ->
                    try {
                        val pelanggan = dataSnapshot.getValue(ModelPelanggan::class.java)
                        pelanggan?.let {
                            pelangganList.add(it)
                            pelangganKeyList.add(dataSnapshot.key ?: "")
                        }
                    } catch (e: Exception) {
                        println("❌ Error parsing data: ${e.message}")
                    }
                }

                if (rvDataPelangganActivity.adapter == null) {
                    rvDataPelangganActivity.adapter = adapter_data_pelanggan(pelangganList, pelangganKeyList)
                } else {
                    rvDataPelangganActivity.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataPelangganActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}