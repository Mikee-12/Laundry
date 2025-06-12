// File: adapter_pembayaran.kt (Fixed)
package com.android.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.Tambahan.ModelTambahan

class adapter_pembayaran(
    private var listTambahan: List<ModelTambahan>
) : RecyclerView.Adapter<adapter_pembayaran.TambahanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TambahanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_tambahan, parent, false)
        return TambahanViewHolder(view)
    }

    override fun onBindViewHolder(holder: TambahanViewHolder, position: Int) {
        val tambahan = listTambahan[position]
        holder.bind(tambahan)
    }

    override fun getItemCount(): Int = listTambahan.size

    fun updateData(newList: List<ModelTambahan>) {
        listTambahan = newList
        notifyDataSetChanged()
    }

    class TambahanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNamaTambahan: TextView = itemView.findViewById(R.id.tvTambahan)
        private val tvHargaTambahan: TextView = itemView.findViewById(R.id.tvHarga)

        fun bind(tambahan: ModelTambahan) {
            tvNamaTambahan.text = tambahan.nama
            tvHargaTambahan.text = "Rp ${String.format("%,d", tambahan.harga)}"
        }
    }
}