package com.android.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.Tambahan.ModelTambahan
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class adapter_tambahan_transaksi(
    private val tambahanList: ArrayList<ModelTambahan>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<adapter_tambahan_transaksi.TambahanViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: ModelTambahan)
    }

    // Setup DecimalFormat dengan format Indonesia
    private val decimalFormat: DecimalFormat by lazy {
        val symbols = DecimalFormatSymbols(Locale("in", "ID")).apply {
            currencySymbol = "Rp "
            groupingSeparator = '.'
            monetaryDecimalSeparator = ','
        }
        DecimalFormat("Rp #,###.00", symbols)
    }

    class TambahanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTambahan: CardView = itemView.findViewById(R.id.cardTambahan)
        val tvTambahan: TextView = itemView.findViewById(R.id.tvTambahan)
        val tvHarga: TextView = itemView.findViewById(R.id.tvHarga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TambahanViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_tambahan, parent, false)
        return TambahanViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TambahanViewHolder, position: Int) {
        val currentItem = tambahanList[position]

        holder.tvTambahan.text = currentItem.nama
        val hargaFormatted = decimalFormat.format(currentItem.harga ?: 0)
        holder.tvHarga.text = hargaFormatted

        holder.cardTambahan.setOnClickListener {
            onItemClickListener.onItemClick(currentItem)
        }

        holder.cardTambahan.isClickable = true
        holder.cardTambahan.isFocusable = true
    }

    override fun getItemCount(): Int = tambahanList.size

    fun updateData(newList: List<ModelTambahan>) {
        tambahanList.clear()
        tambahanList.addAll(newList)
        notifyDataSetChanged()
    }
}
