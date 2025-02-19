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
import com.android.laundry.adapter.AdapterDataPelanggan

class DataPelangganActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")
    lateinit var rvDataPelangganActivity: RecyclerView
    lateinit var fabTambahanPelangganActivity: FloatingActionButton
    lateinit var pelangganList: ArrayList<ModelPelanggan>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Ensure full screen view for the activity
        setContentView(R.layout.activity_data_pelanggan)

        init() // Initialize views

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = false // To show items from top to bottom
        layoutManager.stackFromEnd = false // To stack items normally (from the top)
        rvDataPelangganActivity.layoutManager = layoutManager
        rvDataPelangganActivity.setHasFixedSize(true)

        pelangganList = arrayListOf()
        getData()

        // Adjust system insets f or edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_pelanggan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // FAB click listener
        fabTambahanPelangganActivity.setOnClickListener {
            val intent = Intent(this, TambahanPelangganActivity::class.java)
            startActivity(intent)
        }
    }

    // Initialize views
    private fun init() {
        rvDataPelangganActivity = findViewById(R.id.rvDataPelanggan)
        fabTambahanPelangganActivity = findViewById(R.id.fabData_Pelanggan_Tambah)
    }

    // Get data from Firebase and set it to the RecyclerView
    private fun getData() {
        val query = myRef.orderByChild("idPelanggan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pelangganList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pelanggan = dataSnapshot.getValue(ModelPelanggan::class.java)
                        if (pelanggan != null) {
                            pelangganList.add(pelanggan)
                        }
                    }

                    // Only set the adapter once, and update data without re-creating it
                    if (rvDataPelangganActivity.adapter == null) {
                        val adapter = AdapterDataPelanggan(pelangganList)
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
