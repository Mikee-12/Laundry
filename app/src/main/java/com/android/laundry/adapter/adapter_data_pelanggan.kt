package com.android.laundry.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.modeldata.ModelPelanggan
import com.android.laundry.pelanggan.TambahanPelangganActivity

class AdapterDataPelanggan(private val pelangganList: ArrayList<ModelPelanggan>) :
    RecyclerView.Adapter<AdapterDataPelanggan.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvAlamat)
        val tvNoHp: TextView = itemView.findViewById(R.id.tvNoHp)
        val btnHubungi: Button = itemView.findViewById(R.id.btnHubungi)
        val btnLihat: Button = itemView.findViewById(R.id.btnLihat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_pelanggan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelanggan = pelangganList[position]

        holder.tvNama.text = pelanggan.nama
        holder.tvAlamat.text = pelanggan.alamat
        holder.tvNoHp.text = pelanggan.noHP

        val context = holder.itemView.context

        // Fungsi btnLihat
        holder.btnLihat.setOnClickListener {
            val intent = Intent(context, TambahanPelangganActivity::class.java).apply {
                putExtra("Judul", "Detail Pelanggan")
                putExtra("Nama", pelanggan.nama)
                putExtra("Alamat", pelanggan.alamat)
                putExtra("noHPPelanggan", pelanggan.noHP)
            }
            context.startActivity(intent)
        }

        // Fungsi btnHubungi (buka dialer)
        holder.btnHubungi.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${pelanggan.noHP}")
            }
            context.startActivity(dialIntent)
        }
    }

    override fun getItemCount(): Int = pelangganList.size
}
