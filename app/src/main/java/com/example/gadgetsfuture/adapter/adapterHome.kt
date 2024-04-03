package com.example.gadgetsfuture.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gadgetsfuture.R
import com.example.gadgetsfuture.config.config
import org.json.JSONArray
import org.json.JSONObject

class adapterHome (var context: Context?, var  listaProductoH:JSONArray)
    :RecyclerView.Adapter<adapterHome.MyHolder>() {

    inner class MyHolder(Item: View):RecyclerView.ViewHolder(Item){
        lateinit var lblnombre:TextView
        lateinit var lblprecio:TextView
        lateinit var lblprecioDescunto:TextView
        lateinit var lblporcentajeDescunto:TextView
        lateinit var imgProducto: ImageView
        lateinit var btnCarrito: Button
        lateinit var cardProductoH: CardView

        init {
            lblnombre=itemView.findViewById(R.id.lblNombreH)
            lblprecio=itemView.findViewById(R.id.lblPrecioH)
            lblprecioDescunto=itemView.findViewById(R.id.lblDescuentoPrecioH)
            lblporcentajeDescunto=itemView.findViewById(R.id.lblPorcenDescuentoH)
            imgProducto=itemView.findViewById(R.id.imgProductoH)
            btnCarrito=itemView.findViewById(R.id.btnCarritoH)
            cardProductoH=itemView.findViewById(R.id.cardProductoH)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapterHome.MyHolder {
        var itemView=LayoutInflater.from(context).inflate(R.layout.item_home,parent,false)
        return MyHolder(itemView)
    }

    // Variable que almacena la funcion onclick
    var onclick:((JSONObject)->Unit)?=null

    override fun onBindViewHolder(holder: adapterHome.MyHolder, position: Int) {
        val producto = listaProductoH.getJSONObject(position)
        var nombre=producto.getString("nombre")
        if (nombre.length >= 40){
            nombre = nombre.substring(0, 39) + "..."
        }
        val precio=producto.getDouble("precio")
        var url = config.urlBase
        val imgProductoUrl= url+producto.getString("imagen")
        //val precioDescunto=producto.getDouble()
        //val porcentajeDescuento=producto.getDouble()

        holder.lblnombre.text = nombre
        holder.lblprecio.text = "$precio"
        Glide.with(holder.itemView.context).load(imgProductoUrl).into(holder.imgProducto)

        // cardProductoH nos lleva a detalle del producto
        holder.cardProductoH.setOnClickListener {
            onclick?.invoke(producto)
        }


    }

    override fun getItemCount(): Int {
        return  listaProductoH.length()
    }
}

/*package com.example.gadgetsfuture.adapter

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

class adapterHome (var context: Context?, var  listaProductoH:JSONArray)
    :RecyclerView.Adapter<adapterHome.MyHolder>() {

    inner class MyHolder(Item: View):RecyclerView.ViewHolder(Item){
        lateinit var lblnombre:TextView
        lateinit var lblprecio:TextView
        lateinit var lblprecioDescunto:TextView
        lateinit var lblporcentajeDescunto:TextView
        lateinit var imgProducto: ImageView
        lateinit var btnCarrito: Button
        lateinit var cardProductoH: CardView

        init {
            lblnombre=itemView.findViewById(R.id.lblNombreH)
            lblprecio=itemView.findViewById(R.id.lblPrecioH)
            lblprecioDescunto=itemView.findViewById(R.id.lblDescuentoPrecioH)
            lblporcentajeDescunto=itemView.findViewById(R.id.lblPorcenDescuentoH)
            imgProducto=itemView.findViewById(R.id.imgProductoH)
            btnCarrito=itemView.findViewById(R.id.btnCarritoH)
            cardProductoH=itemView.findViewById(R.id.cardProductoH)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapterHome.MyHolder {
        var itemView=LayoutInflater.from(context).inflate(R.layout.item_home,parent,false)
        return MyHolder(itemView)
    }

    // Variable que almacena la funcion onclick
    var onclick:((JSONObject)->Unit)?=null

    override fun onBindViewHolder(holder: adapterHome.MyHolder, position: Int) {
        val producto = listaProductoH.getJSONObject(position)
        val nombre=producto.getString("nombre")
        val precio=producto.getDouble("precio")
        var url = config.urlBase
        val imgProductoUrl= url+producto.getString("imagen")
        //val precioDescunto=producto.getDouble()
        //val porcentajeDescunto=producto.getDouble()

        holder.lblnombre.text = nombre
        holder.lblprecio.text = "$precio"
        Glide.with(holder.itemView.context).load(imgProductoUrl).into(holder.imgProducto)

        // Imagen button nos lleva a detalle del producto
        holder.cardProductoH.setOnClickListener {
            onclick?.invoke(producto)
        }


    }

    override fun getItemCount(): Int {
        return  listaProductoH.length()
    }
}*/