package com.android.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.Tambahan.ModelTambahan

class adapter_tambahan_transaksi(
    private val tambahanList: ArrayList<ModelTambahan>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<adapter_tambahan_transaksi.TambahanViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: ModelTambahan)
    }

    class TambahanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTambahan: CardView = itemView.findViewById(R.id.cardTambahan)
        val tvTambahan: TextView = itemView.findViewById(R.id.tvTambahan)
        val tvHarga: TextView = itemView.findViewById(R.id.tvHarga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TambahanViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_tambahan, parent, false)
        return TambahanViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TambahanViewHolder, position: Int) {
        val currentItem = tambahanList[position]

        // Set data ke views
        holder.tvTambahan.text = currentItem.nama
        holder.tvHarga.text = "Rp ${String.format("%,d", currentItem.harga ?: 0)}"

        // Handle item click
        holder.cardTambahan.setOnClickListener {
            onItemClickListener.onItemClick(currentItem)
        }

        // Optional: Add ripple effect atau visual feedback saat diklik
        holder.cardTambahan.isClickable = true
        holder.cardTambahan.isFocusable = true
    }

    override fun getItemCount(): Int {
        return tambahanList.size
    }

    // Method untuk update data (jika diperlukan)
    fun updateData(newList: List<ModelTambahan>) {
        tambahanList.clear()
        tambahanList.addAll(newList)
        notifyDataSetChanged()
    }
}