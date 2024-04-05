package com.example.gadgetsfuture.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gadgetsfuture.R
import com.example.gadgetsfuture.config.config
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale

class adapterCategDeProduct(var context: Context?, var listaCategDeProduct:JSONArray)
    :RecyclerView.Adapter<adapterCategDeProduct.MyHolder>(){

    inner class MyHolder(ItemCategProduts : View):RecyclerView.ViewHolder(ItemCategProduts) {

        lateinit var lblnombre:TextView
        lateinit var lblprecio:TextView
        lateinit var lblprecioDescunto:TextView
        lateinit var lblporcentajeDescunto:TextView
        lateinit var imgProducto: ImageView
        lateinit var btnCarrito: Button
        lateinit var cardProductoC: CardView

        init {
            lblnombre=itemView.findViewById(R.id.lblNombreC)
            lblprecio=itemView.findViewById(R.id.lblPrecioC)
            lblprecioDescunto=itemView.findViewById(R.id.lblDescuentoPrecioC)
            lblporcentajeDescunto=itemView.findViewById(R.id.lblPorcenDescuentoC)
            imgProducto=itemView.findViewById(R.id.imgProductoC)
            btnCarrito=itemView.findViewById(R.id.btnCarritoC)
            cardProductoC=itemView.findViewById(R.id.cardProductoC)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapterCategDeProduct.MyHolder {
        var itemView= LayoutInflater.from(context).inflate(R.layout.item_categ_produts,parent,false)
        return MyHolder(itemView)
    }

    var onclick:((JSONObject)->Unit)?=null
    override fun onBindViewHolder(holder: adapterCategDeProduct.MyHolder, position: Int) {
        val categoriaDeProduct = listaCategDeProduct.getJSONObject(position)
        var nombre=categoriaDeProduct.getString("nombre")
        if (nombre.length >= 40){
            nombre = nombre.substring(0, 39) + "..."
        }
        val precio=categoriaDeProduct.getDouble("precio")
        var url = config.urlBase
        val imgProductoUrl= url+categoriaDeProduct.getString("imagen")
        //val precioDescunto=producto.getDouble()
        //val porcentajeDescuento=producto.getDouble()

        val formato = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        formato.maximumFractionDigits = 0
        val formatoMoneda = formato.format(precio)

        holder.lblnombre.text = nombre
        holder.lblprecio.text = "$formatoMoneda"
        Glide.with(holder.itemView.context).load(imgProductoUrl).into(holder.imgProducto)

        // cardProductoH nos lleva a detalle del producto
        holder.cardProductoC.setOnClickListener {
            onclick?.invoke(categoriaDeProduct)
        }
    }

    override fun getItemCount(): Int {
        return listaCategDeProduct.length()
    }


}