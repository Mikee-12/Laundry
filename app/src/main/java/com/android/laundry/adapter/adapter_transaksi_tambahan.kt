package com.android.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.Tambahan.ModelTambahan

class adapter_transaksi_tambahan(
    private val tambahanList: ArrayList<ModelTambahan>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<adapter_transaksi_tambahan.TambahanTransaksiViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: ModelTambahan)
        fun onDeleteClick(item: ModelTambahan, position: Int)
    }

    class TambahanTransaksiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTambahanTransaksi: CardView = itemView.findViewById(R.id.cardTambahanTransaksi)
        val tvTambahan: TextView = itemView.findViewById(R.id.tvTambahan)
        val tvHarga: TextView = itemView.findViewById(R.id.tvHarga)
        val btnDelete: ImageButton = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TambahanTransaksiViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_tambahan_transaksi, parent, false)
        return TambahanTransaksiViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TambahanTransaksiViewHolder, position: Int) {
        val currentItem = tambahanList[position]

        // Set data ke views
        holder.tvTambahan.text = currentItem.nama
        holder.tvHarga.text = "Rp ${String.format("%,d", currentItem.harga ?: 0)}"

        // Handle card click
        holder.cardTambahanTransaksi.setOnClickListener {
            onItemClickListener.onItemClick(currentItem)
        }

        // Handle delete button click
        holder.btnDelete.setOnClickListener {
            onItemClickListener.onDeleteClick(currentItem, position)
        }
    }

    override fun getItemCount(): Int {
        return tambahanList.size
    }

    // Method untuk menghapus item
    fun removeItem(position: Int) {
        if (position >= 0 && position < tambahanList.size) {
            tambahanList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, tambahanList.size)
        }
    }

    // Method untuk mendapatkan total harga semua tambahan
    fun getTotalHarga(): Int {
        return tambahanList.sumOf { it.harga ?: 0 }
    }
}