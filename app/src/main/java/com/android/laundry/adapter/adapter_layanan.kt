package com.android.laundry.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import android.widget.Toast
import com.android.laundry.modeldata.ModelLayanan
import com.android.laundry.layanan.Edit_layanan

class adapter_layanan(private val layananList: ArrayList<ModelLayanan>) :
    RecyclerView.Adapter<adapter_layanan.LayananViewHolder>() {

    class LayananViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaLayanan: TextView = itemView.findViewById(R.id.tvNamaLayanan)
        val tvHarga: TextView = itemView.findViewById(R.id.tvHarga)
        val tvCabang: TextView = itemView.findViewById(R.id.tvCabang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayananViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_layanan, parent, false)
        return LayananViewHolder(view)
    }

    override fun onBindViewHolder(holder: LayananViewHolder, position: Int) {
        val layanan = layananList[position]
        holder.tvNamaLayanan.text = layanan.nama ?: "Tidak ada nama"
        holder.tvHarga.text = "Rp ${layanan.harga ?: 0}"
        holder.tvCabang.text = layanan.cabang ?: "Tidak ada cabang"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, Edit_layanan::class.java)

            layanan.id?.let { id ->
                intent.putExtra("layananId", id)
                intent.putExtra("nama", layanan.nama)         // Perbaikan di sini
                intent.putExtra("harga", layanan.harga?.toString())
                intent.putExtra("cabang", layanan.cabang)
                context.startActivity(intent)
            } ?: run {
                Toast.makeText(context, "ID layanan tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return layananList.size
    }
}
