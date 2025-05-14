package com.android.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.android.laundry.MainActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var etNoHp: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        database = FirebaseDatabase.getInstance().getReference("login")

        // Sesuaikan padding sesuai system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi view
        btnBack = findViewById(R.id.btnBack)
        etNoHp = findViewById(R.id.etNoHp)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        // Tombol kembali
        btnBack.setOnClickListener {
            finish()
        }

        // Aksi login
        btnLogin.setOnClickListener {
            val noHp = etNoHp.text.toString().trim()
            val passwordInput = etPassword.text.toString().trim()

            if (noHp.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(this, "Harap isi No HP dan Password", Toast.LENGTH_SHORT).show()
            } else {
                // Ambil data login dari Firebase berdasarkan No HP
                database.child(noHp).get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val passwordDb = snapshot.child("password").value.toString()
                        if (passwordDb == passwordInput) {
                            Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Password salah", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "No HP tidak terdaftar", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Gagal mengakses database: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}
