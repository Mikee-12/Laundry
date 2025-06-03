package com.android.laundry.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.modeldata.ModelPelanggan
import com.android.laundry.pelanggan.Edit_pelanggan
import com.google.firebase.database.FirebaseDatabase

class adapter_data_pelanggan(
    private val pelangganList: ArrayList<ModelPelanggan>,
    private val pelangganKeyList: ArrayList<String>
) : RecyclerView.Adapter<adapter_data_pelanggan.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvAlamat)
        val tvNoHp: TextView = itemView.findViewById(R.id.tvNoHp)
        val tvTerdaftar: TextView = itemView.findViewById(R.id.tvTerdaftar)
        val btnHubungi: Button = itemView.findViewById(R.id.btnHubungi)
        val btnLihat: Button = itemView.findViewById(R.id.btnLihat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_pelanggan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelanggan = pelangganList[position]
        val keyPelanggan = pelangganKeyList[position]
        val context = holder.itemView.context

        holder.tvNama.text = pelanggan.nama
        holder.tvAlamat.text = pelanggan.alamat
        holder.tvNoHp.text = pelanggan.noHP
        holder.tvTerdaftar.text = pelanggan.dateRegistered ?: "-"

        holder.itemView.setOnClickListener {
            navigateToEdit(context, keyPelanggan, pelanggan)
        }

        holder.btnLihat.setOnClickListener {
            showDetailDialog(context, pelanggan, keyPelanggan, position)
        }

        // Ubah tombol Hubungi supaya membuka WhatsApp chat
        holder.btnHubungi.setOnClickListener {
            val nomorWhatsapp = pelanggan.noHP?.replace("[^\\d+]".toRegex(), "") ?: ""
            val url = "https://wa.me/$nomorWhatsapp"

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            intent.setPackage("com.whatsapp")

            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "WhatsApp tidak terpasang di perangkat ini", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToEdit(context: android.content.Context, key: String, pelanggan: ModelPelanggan) {
        Intent(context, Edit_pelanggan::class.java).apply {
            putExtra("pelangganId", key)
            putExtra("Nama", pelanggan.nama)
            putExtra("Alamat", pelanggan.alamat)
            putExtra("noHPPelanggan", pelanggan.noHP)
            putExtra("dateRegistered", pelanggan.dateRegistered)
        }.also { context.startActivity(it) }
    }

    private fun showDetailDialog(
        context: android.content.Context,
        pelanggan: ModelPelanggan,
        key: String,
        position: Int
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialogmod_pelanggan, null)

        val tvNama = dialogView.findViewById<TextView>(R.id.tvNama)
        val tvAlamat = dialogView.findViewById<TextView>(R.id.tvAlamat)
        val tvNoHp = dialogView.findViewById<TextView>(R.id.tvNoHp)
        val tvTerdaftar = dialogView.findViewById<TextView>(R.id.tvTerdaftar)
        val btnEdit = dialogView.findViewById<Button>(R.id.btnEdit)
        val btnHapus = dialogView.findViewById<Button>(R.id.btnHapus)

        tvNama.text = pelanggan.nama
        tvAlamat.text = pelanggan.alamat
        tvNoHp.text = pelanggan.noHP
        tvTerdaftar.text = pelanggan.dateRegistered ?: "-"

        val dialog = androidx.appcompat.app.AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        btnEdit.setOnClickListener {
            dialog.dismiss()
            navigateToEdit(context, key, pelanggan)
        }

        // Hapus tanpa konfirmasi dialog
        btnHapus.setOnClickListener {
            val databaseRef = FirebaseDatabase.getInstance()
                .getReference("pelanggan")
                .child(key)

            databaseRef.removeValue()
                .addOnSuccessListener {
                    pelangganList.removeAt(position)
                    pelangganKeyList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, pelangganList.size)
                    Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
        }

        dialog.show()
    }

    override fun getItemCount(): Int = pelangganList.size
}
