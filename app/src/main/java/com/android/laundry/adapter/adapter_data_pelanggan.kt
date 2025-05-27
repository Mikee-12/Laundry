    package com.android.laundry.adapter

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
    import com.android.laundry.pelanggan.edit_pelanggan

    class AdapterDataPelanggan(
        private val pelangganList: ArrayList<ModelPelanggan>,
        private val pelangganKeyList: ArrayList<String>  // Tambahan list key/ID
    ) : RecyclerView.Adapter<AdapterDataPelanggan.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvNama      : TextView = itemView.findViewById(R.id.tvNama)
            val tvAlamat    : TextView = itemView.findViewById(R.id.tvAlamat)
            val tvNoHp      : TextView = itemView.findViewById(R.id.tvNoHp)
            val tvTerdaftar : TextView = itemView.findViewById(R.id.tvTerdaftar)
            val btnHubungi  : Button   = itemView.findViewById(R.id.btnHubungi)
            val btnLihat    : Button   = itemView.findViewById(R.id.btnLihat)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_pelanggan, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val pelanggan = pelangganList[position]
            val keyPelanggan = pelangganKeyList[position]
            val context = holder.itemView.context

            // Bind data
            holder.tvNama.text = pelanggan.nama
            holder.tvAlamat.text = pelanggan.alamat
            holder.tvNoHp.text = pelanggan.noHP
            holder.tvTerdaftar.text = pelanggan.dateRegistered ?: "-"

            // Klik cardview (itemView) bawa ke EditPelangganActivity
            holder.itemView.setOnClickListener {
                Intent(context, edit_pelanggan::class.java).apply {
                    putExtra("pelangganId", keyPelanggan)
                    putExtra("Nama", pelanggan.nama)
                    putExtra("Alamat", pelanggan.alamat)
                    putExtra("noHPPelanggan", pelanggan.noHP)
                    putExtra("dateRegistered", pelanggan.dateRegistered)
                }.also { context.startActivity(it) }
            }

            // Tombol lihat detail tetap ke TambahanPelangganActivity
            holder.btnLihat.setOnClickListener {
                Intent(context, TambahanPelangganActivity::class.java).apply {
                    putExtra("Judul", "Detail Pelanggan")
                    putExtra("Nama", pelanggan.nama)
                    putExtra("Alamat", pelanggan.alamat)
                    putExtra("noHPPelanggan", pelanggan.noHP)
                    putExtra("dateRegistered", pelanggan.dateRegistered)
                    putExtra("pelangganId", keyPelanggan)
                }.also { context.startActivity(it) }
            }

            // Tombol hubungi
            holder.btnHubungi.setOnClickListener {
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:${pelanggan.noHP}"))
                    .also(context::startActivity)
            }
        }

        override fun getItemCount(): Int = pelangganList.size
    }
