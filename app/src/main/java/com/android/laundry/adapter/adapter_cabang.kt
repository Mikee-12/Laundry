package com.android.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.modeldata.ModelCabang

class adapter_cabang(private val cabangList: ArrayList<ModelCabang>) :
    RecyclerView.Adapter<adapter_cabang.CabangViewHolder>() {

    inner class CabangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaCabang: TextView = itemView.findViewById(R.id.tvNamaCabang)
        val tvManager: TextView = itemView.findViewById(R.id.tvManager)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvAlamat)
        val tvNoHp: TextView = itemView.findViewById(R.id.tvNoHp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CabangViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_cabang, parent, false)
        return CabangViewHolder(view)
    }

    override fun onBindViewHolder(holder: CabangViewHolder, position: Int) {
        val cabang = cabangList[position]
        holder.tvNamaCabang.text = cabang.namaCabang
        holder.tvManager.text = cabang.manager
        holder.tvAlamat.text = cabang.alamat
        holder.tvNoHp.text = cabang.noHp
    }

    override fun getItemCount(): Int {
        return cabangList.size
    }
}
