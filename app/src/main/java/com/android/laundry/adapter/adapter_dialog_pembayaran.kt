package com.android.laundry.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.android.laundry.R

class adapter_dialog_pembayaran (
    private val context: Context,
    private val onPaymentSelected: (String) -> Unit
    ) {

        fun showPaymentDialog() {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)

            // Inflate layout dialog
            val view = LayoutInflater.from(context).inflate(R.layout.dialogmod_konvirmasi, null)
            dialog.setContentView(view)

            // Initialize views
            val tvPilihPembayaran = view.findViewById<TextView>(R.id.tvPilihPembayaran)
            val cardCash = view.findViewById<CardView>(R.id.cash)
            val cardPayLater = view.findViewById<CardView>(R.id.paylater)
            val cardGoPay = view.findViewById<CardView>(R.id.GoPay)
            val btnBatal = view.findViewById<Button>(R.id.btnBatal)

            // Set click listeners untuk setiap pilihan pembayaran
            cardCash.setOnClickListener {
                onPaymentSelected("cash")
                dialog.dismiss()
            }

            cardPayLater.setOnClickListener {
                onPaymentSelected("paylater")
                dialog.dismiss()
            }

            cardGoPay.setOnClickListener {
                onPaymentSelected("gopay")
                dialog.dismiss()
            }

            // Set click listener untuk tombol batal
            btnBatal.setOnClickListener {
                dialog.dismiss()
            }

            // Konfigurasi dialog
            dialog.window?.let { window ->
                val layoutParams = window.attributes
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                window.attributes = layoutParams
                window.setBackgroundDrawableResource(android.R.color.transparent)
            }

            dialog.show()
        }
}