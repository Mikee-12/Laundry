package com.android.laundry.pegawai

import android.content.Intent
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
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
import com.android.laundry.modeldata.ModelPegawai
import com.android.laundry.adapter.adapter_data_pegawai

class Data_pegawai : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pegawai")
    private lateinit var rvDataPegawaiActivity: RecyclerView
    private lateinit var fabTambahanPegawaiActivity: FloatingActionButton
    private lateinit var pegawailist: ArrayList<ModelPegawai>
    private lateinit var pegawaiKeyList: ArrayList<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_data_pegawai)

        val mainView = findViewById<View>(R.id.data_pegawai)
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

        initViews()
        setupRecyclerView()
        loadData()

    }

    private fun initViews() {
        rvDataPegawaiActivity = findViewById(R.id.rvDataPegawai)
        fabTambahanPegawaiActivity = findViewById(R.id.fabData_Pegawai_Tambah)
        pegawailist = ArrayList()
        pegawaiKeyList = ArrayList()

        fabTambahanPegawaiActivity.setOnClickListener {
            startActivity(Intent(this, Tambahan_pegawai::class.java))
        }
    }

    private fun setupRecyclerView() {
        rvDataPegawaiActivity.layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = false
            stackFromEnd = false
        }
        rvDataPegawaiActivity.setHasFixedSize(true)
    }

    private fun loadData() {
        val query = myRef.limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pegawailist.clear()
                pegawaiKeyList.clear()

                snapshot.children.forEach { dataSnapshot ->
                    try {
                        val pegawai = dataSnapshot.getValue(ModelPegawai::class.java)
                        pegawai?.let {
                            pegawailist.add(it)
                            pegawaiKeyList.add(dataSnapshot.key ?: "")
                        }
                    } catch (e: Exception) {
                        println("❌ Error parsing data: ${e.message}")
                    }
                }

                if (rvDataPegawaiActivity.adapter == null) {
                    rvDataPegawaiActivity.adapter = adapter_data_pegawai(pegawailist, pegawaiKeyList)
                } else {
                    rvDataPegawaiActivity.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Data_pegawai, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}