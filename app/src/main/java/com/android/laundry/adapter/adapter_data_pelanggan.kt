package com.android.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.modeldata.ModelPelanggan

class AdapterDataPelanggan(private val listPelanggan: ArrayList<ModelPelanggan>) :
    RecyclerView.Adapter<AdapterDataPelanggan.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_pelanggan, parent, false) // Updated to match the new layout
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPelanggan[position]
        holder.tvNama.text = item.nama
        holder.tvAlamat.text = item.alamat
        holder.tvNoHP.text = item.noHP
        holder.tvCabang.text = item.cabang
    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }

    // ViewHolder to hold references to the views
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvAlamat)
        val tvNoHP: TextView = itemView.findViewById(R.id.tvNoHp)
        val tvCabang: TextView = itemView.findViewById(R.id.tvCabang) // Use correct ID here
    }
}

