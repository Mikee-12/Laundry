package com.android.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.modeldata.ModelPelanggan

class adapter_transaksi_pelanggan(
    private val listFull: ArrayList<ModelPelanggan>,
    private val onItemClick: (ModelPelanggan) -> Unit
) : RecyclerView.Adapter<adapter_transaksi_pelanggan.ViewHolder>(), Filterable {

    private var listFiltered = ArrayList<ModelPelanggan>()

    init {
        listFiltered = ArrayList(listFull) // Tampilkan semua data awalnya
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNama)
        val tvAlamat: TextView = view.findViewById(R.id.tvAlamat)
        val tvNoHp: TextView = view.findViewById(R.id.tvNoHp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_pelanggan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelanggan = listFiltered[position]
        holder.tvNama.text = pelanggan.nama
        holder.tvAlamat.text = pelanggan.alamat
        holder.tvNoHp.text = pelanggan.noHP

        holder.itemView.setOnClickListener {
            onItemClick(pelanggan)
        }
    }

    override fun getItemCount(): Int = listFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterText = constraint?.toString()?.lowercase()?.trim() ?: ""
                val filteredList = if (filterText.isEmpty()) {
                    listFull
                } else {
                    listFull.filter {
                        it.nama.lowercase().contains(filterText) || it.noHP.contains(filterText)
                    }
                }
                val result = FilterResults()
                result.values = filteredList
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listFiltered = ArrayList(results?.values as List<ModelPelanggan>)
                notifyDataSetChanged()
            }
        }
    }

    // Fungsi untuk update data listFull dan listFiltered sekaligus dari luar adapter
    fun updateData(newList: List<ModelPelanggan>) {
        listFull.clear()
        listFull.addAll(newList)

        listFiltered.clear()
        listFiltered.addAll(newList)

        notifyDataSetChanged()
    }
}
