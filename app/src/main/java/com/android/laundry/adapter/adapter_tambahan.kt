package com.android.laundry.Tambahan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R

class adapter_tambahan(private val listTambahan: List<ModelTambahan>) :
    RecyclerView.Adapter<adapter_tambahan.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTambahan: TextView = itemView.findViewById(R.id.tvTambahan)
        val tvHarga: TextView = itemView.findViewById(R.id.tvHarga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_tambahan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tambahan = listTambahan[position]
        holder.tvTambahan.text = tambahan.nama
        holder.tvHarga.text = "Rp ${tambahan.harga}"
    }

    override fun getItemCount(): Int = listTambahan.size
}
