package com.android.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.modeldata.ModelLayanan

class adapter_transaksi_layanan(
    private val listFull: ArrayList<ModelLayanan>,
    private val onItemClick: (ModelLayanan) -> Unit
) : RecyclerView.Adapter<adapter_transaksi_layanan.ViewHolder>(), Filterable {

    private var listFiltered = ArrayList<ModelLayanan>()

    init {
        listFiltered = ArrayList(listFull) // awalnya semua data ditampilkan
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaLayanan: TextView = view.findViewById(R.id.tvNamaLayanan)
        val tvHargaLayanan: TextView = view.findViewById(R.id.tvHarga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_layanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val layanan = listFiltered[position]
        holder.tvNamaLayanan.text = layanan.nama
        holder.tvHargaLayanan.text = "Rp ${layanan.harga}"

        holder.itemView.setOnClickListener {
            onItemClick(layanan)
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
                        it.nama?.lowercase()?.contains(filterText) == true
                    }
                }
                val result = FilterResults()
                result.values = filteredList
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listFiltered = ArrayList(results?.values as List<ModelLayanan>)
                notifyDataSetChanged()
            }
        }
    }

    fun updateData(newList: List<ModelLayanan>) {
        listFull.clear()
        listFull.addAll(newList)

        listFiltered.clear()
        listFiltered.addAll(newList)

        notifyDataSetChanged()
    }
}
