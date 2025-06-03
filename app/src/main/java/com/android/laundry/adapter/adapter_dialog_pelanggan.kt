package com.android.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.modeldata.ModelPelanggan
import java.text.SimpleDateFormat
import java.util.*

class adapter_dialog_pelanggan(
    private val pelangganList: List<ModelPelanggan>,
    private val onEditClick: (ModelPelanggan) -> Unit,
    private val onDeleteClick: (ModelPelanggan) -> Unit
) : RecyclerView.Adapter<adapter_dialog_pelanggan.PelangganViewHolder>() {

    class PelangganViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvAlamat)
        val tvNoHp: TextView = itemView.findViewById(R.id.tvNoHp)
        val tvTerdaftar: TextView = itemView.findViewById(R.id.tvTerdaftar)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnHapus: Button = itemView.findViewById(R.id.btnHapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PelangganViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialogmod_pelanggan, parent, false) // Replace with your dialog layout
        return PelangganViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PelangganViewHolder, position: Int) {
        val currentItem = pelangganList[position]

        // Format the registration date if timestamp exists
        val formattedDate = if (currentItem.timestamp != null) {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            sdf.format(Date(currentItem.timestamp))
        } else {
            currentItem.dateRegistered
        }

        holder.tvNama.text = currentItem.nama
        holder.tvAlamat.text = currentItem.alamat
        holder.tvNoHp.text = currentItem.noHP
        holder.tvTerdaftar.text = formattedDate

        holder.btnEdit.setOnClickListener {
            onEditClick(currentItem)
        }

        holder.btnHapus.setOnClickListener {
            onDeleteClick(currentItem)
        }
    }

    override fun getItemCount() = pelangganList.size

    fun updateData(newList: List<ModelPelanggan>) {
        (pelangganList as ArrayList).clear()
        (pelangganList as ArrayList).addAll(newList)
        notifyDataSetChanged()
    }
}