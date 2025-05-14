package com.android.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.modeldata.ModelLayanan

class adapter_layanan(private val list: List<ModelLayanan>) :
    RecyclerView.Adapter<adapter_layanan.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaLayanan: TextView = itemView.findViewById(R.id.tvNamaLayanan)
        val harga: TextView = itemView.findViewById(R.id.tvHarga)
        val namaCabang: TextView = itemView.findViewById(R.id.tvNamaCabang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_layanan, parent, false) // Pastikan nama layout sesuai
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val layanan = list[position]
        holder.namaLayanan.text = layanan.namaLayanan
        holder.harga.text = layanan.harga
        holder.namaCabang.text = layanan.namaCabang
    }

    override fun getItemCount(): Int = list.size
}
