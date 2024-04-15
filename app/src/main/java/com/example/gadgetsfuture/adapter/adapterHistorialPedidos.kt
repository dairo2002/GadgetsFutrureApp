package com.example.gadgetsfuture.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gadgetsfuture.Historial_pedidos
import com.example.gadgetsfuture.R
import com.example.gadgetsfuture.config.config
import org.json.JSONArray
import org.json.JSONObject

class adapterHistorialPedidos(var context: Context?, var listaHistorialPedidos:JSONArray):RecyclerView.Adapter<adapterHistorialPedidos.MyHolder>(){

    inner class MyHolder(ItemPedido : View):RecyclerView.ViewHolder(ItemPedido) {

        lateinit var nombre: TextView
        lateinit var imagen: ImageView
        lateinit var cantidad: TextView
        lateinit var fecha: TextView
        lateinit var estado: TextView
        lateinit var btnVolverComprar : Button

        init {
            nombre=ItemPedido.findViewById(R.id.lblNombreHistorial)
            imagen=ItemPedido.findViewById(R.id.imgProductoHistorial)
            cantidad=ItemPedido.findViewById(R.id.lblCantidadHistorial)
            fecha=ItemPedido.findViewById(R.id.lblFechaHistorial)
            estado=ItemPedido.findViewById(R.id.lblEstadoHistorial)
            btnVolverComprar=ItemPedido.findViewById(R.id.btnCarritoHistorial)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapterHistorialPedidos.MyHolder {
        var itemView= LayoutInflater.from(context).inflate(R.layout.item_pedidos,parent,false)
        return MyHolder(itemView)
    }

    var onclick:((JSONObject)->Unit)?=null
    override fun onBindViewHolder(holder: adapterHistorialPedidos.MyHolder, position: Int) {
        val historial=listaHistorialPedidos.getJSONObject(position)
        var nombre=historial.getString("nombre")
        var imagen=config.urlBase+historial.getString("imagen")
        var cantidad=historial.getInt("cantidad")
        var fecha=historial.getString("fecha")
        var estado=historial.getString("estado")

        holder.nombre.text=nombre
        holder.cantidad.text="$cantidad"
        holder.fecha.text=fecha
        holder.estado.text=estado
        Glide.with(holder.itemView.context).load(imagen).into(holder.imagen)

        holder.btnVolverComprar.setOnClickListener {
            onclick?.invoke(historial)
        }
    }

    override fun getItemCount(): Int {
        return listaHistorialPedidos.length()
    }


}

// cardProductoH nos lleva a detalle del producto
/*        holder.cardProductoH.setOnClickListener {
            onclick?.invoke(producto)
        }*/