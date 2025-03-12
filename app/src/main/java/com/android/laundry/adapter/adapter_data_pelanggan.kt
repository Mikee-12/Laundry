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

class AdapterDataPelanggan(private val pelangganList: ArrayList<ModelPelanggan>) :
    RecyclerView.Adapter<AdapterDataPelanggan.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvTerdaftar: TextView = itemView.findViewById(R.id.tvTerdaftar)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvAlamat)
        val tvNoHp: TextView = itemView.findViewById(R.id.tvNoHp)
        val btnHubungi: Button = itemView.findViewById(R.id.btnHubungi)
        val btnLihat: Button = itemView.findViewById(R.id.btnLihat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Meng-inflate layout cardview_pelanggan.xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_pelanggan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelanggan = pelangganList[position]

        // Tampilkan ID pelanggan dengan format "ID: <id>"
        holder.tvNama.text = pelanggan.nama

        // Konversi timestamp ke format tanggal: "dd MMM yyyy"
        val currentTimestamp = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        holder.tvTerdaftar.text = dateFormat.format(Date(currentTimestamp))

        holder.tvAlamat.text = pelanggan.alamat
        holder.tvNoHp.text = pelanggan.noHP

        // Aksi untuk tombol Hubungi dan Lihat (sesuaikan sesuai kebutuhan)
        holder.btnHubungi.setOnClickListener {
            // Contoh: mulai intent panggilan atau membuka aplikasi pesan
        }
        holder.btnLihat.setOnClickListener {
            // Contoh: tampilkan detail pelanggan
        }
    }

    override fun getItemCount(): Int = pelangganList.size
}
