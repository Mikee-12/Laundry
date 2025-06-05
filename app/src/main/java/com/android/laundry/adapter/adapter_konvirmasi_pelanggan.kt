package com.android.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.laundry.R
import com.android.laundry.Tambahan.ModelTambahan
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class adapter_konvirmasi_pelanggan(
    private val listTambahan: List<ModelTambahan>,
    private val itemClickListener: ((ModelTambahan) -> Unit)? = null
) : RecyclerView.Adapter<adapter_konvirmasi_pelanggan.ViewHolder>() {

    private val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }

    private val decimalFormat = DecimalFormat("#,##0", symbols)
    private val decimalFormatWithDecimal = DecimalFormat("#,##0.00", symbols)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTambahan: TextView = view.findViewById(R.id.tvTambahan)
        val tvHarga: TextView = view.findViewById(R.id.tvHarga)

        init {
            view.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    itemClickListener?.invoke(listTambahan[pos])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_tambahan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tambahan = listTambahan[position]
        holder.tvTambahan.text = tambahan.nama
        holder.tvHarga.text = "Rp ${decimalFormat.format(tambahan.harga ?: 0)}"
    }

    override fun getItemCount(): Int = listTambahan.size
}
