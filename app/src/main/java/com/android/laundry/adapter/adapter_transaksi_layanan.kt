package com.android.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.modeldata.ModelLayanan

class adapter_transaksi_layanan(
    private val layananList: ArrayList<ModelLayanan>,
    private val onItemClick: (ModelLayanan) -> Unit
) : RecyclerView.Adapter<adapter_transaksi_layanan.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaLayanan: TextView = view.findViewById(R.id.tvNamaLayanan)
        val tvHarga: TextView = view.findViewById(R.id.tvHarga)
        val tvCabang: TextView = view.findViewById(R.id.tvCabang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_layanan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = layananList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val layanan = layananList[position]
        holder.tvNamaLayanan.text = layanan.nama ?: "Tidak ada nama"
        holder.tvHarga.text = "Rp ${layanan.harga ?: 0}"
        holder.tvCabang.text = layanan.cabang ?: "Tidak ada cabang"

        holder.itemView.setOnClickListener {
            onItemClick(layanan)
        }
    }

    fun updateData(newList: List<ModelLayanan>) {
        layananList.clear()
        layananList.addAll(newList)
        notifyDataSetChanged()
    }
}
