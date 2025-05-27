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

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")
    lateinit var rvDataPelangganActivity: RecyclerView
    lateinit var fabTambahanPelangganActivity: FloatingActionButton
    lateinit var pelangganList: ArrayList<ModelPelanggan>
    lateinit var pelangganKeyList: ArrayList<String>  // Jangan lupa inisialisasi ini juga

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pelanggan)

        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = false
        layoutManager.stackFromEnd = false
        rvDataPelangganActivity.layoutManager = layoutManager
        rvDataPelangganActivity.setHasFixedSize(true)

        pelangganList = arrayListOf()
        pelangganKeyList = arrayListOf() // Inisialisasi list key juga
        getData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_pelanggan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fabTambahanPelangganActivity.setOnClickListener {
            val intent = Intent(this, TambahanPelangganActivity::class.java)
            startActivity(intent)
        }
    }

    private fun init() {
        rvDataPelangganActivity = findViewById(R.id.rvDataPelanggan)
        fabTambahanPelangganActivity = findViewById(R.id.fabData_Pelanggan_Tambah)
    }

    private fun getData() {
        val query = myRef.limitToLast(100) // Ambil maksimal 100 data terakhir
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pelangganList.clear()
                    pelangganKeyList.clear()
                    for (dataSnapshot in snapshot.children) {
                        if (dataSnapshot.value is Map<*, *>) {
                            try {
                                val pelanggan = dataSnapshot.getValue(ModelPelanggan::class.java)
                                if (pelanggan != null) {
                                    pelangganList.add(pelanggan)
                                    pelangganKeyList.add(dataSnapshot.key.toString())
                                }
                            } catch (e: Exception) {
                                println("❌ Parsing error di key: ${dataSnapshot.key}, value: ${dataSnapshot.value}")
                            }
                        } else {
                            println("⚠️ Data tidak valid di key: ${dataSnapshot.key}, value: ${dataSnapshot.value}")
                        }
                    }

                    if (rvDataPelangganActivity.adapter == null) {
                        val adapter = adapter_data_pelanggan(pelangganList, pelangganKeyList)
                        rvDataPelangganActivity.adapter = adapter
                    } else {
                        rvDataPelangganActivity.adapter?.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataPelangganActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

