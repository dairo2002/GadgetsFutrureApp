package com.example.gadgetsfuture.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gadgetsfuture.R
import com.example.gadgetsfuture.config.config
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale

class adapterCarrito (var context: Context?, var  listaCarrito:JSONArray)
    :RecyclerView.Adapter<adapterCarrito.MyHolder>() {

    inner class MyHolder(Item: View):RecyclerView.ViewHolder(Item){
        lateinit var lblNombre:TextView
        lateinit var lblPrecio:TextView
        lateinit var lblSubtotal:TextView
        lateinit var imgProducto: ImageView
        lateinit var txtCantidad:TextView
        lateinit var btnEliminarCarrito: Button

        init {
            lblNombre=itemView.findViewById(R.id.lblNombreCart)
            lblPrecio=itemView.findViewById(R.id.lblPrecioCart)
            lblSubtotal=itemView.findViewById(R.id.lblSubtotalCart)
            txtCantidad=itemView.findViewById(R.id.txtCantidadCart)
            imgProducto=itemView.findViewById(R.id.imgProductoCart)
            //btnEliminarCarrito=itemView.findViewById(R.id.btnEliminarCart)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapterCarrito.MyHolder {
        var itemView=LayoutInflater.from(context).inflate(R.layout.item_carro,parent,false)
        return MyHolder(itemView)
    }

    // Variable que almacena la funcion onclick
    var onclick:((JSONObject)->Unit)?=null

    override fun onBindViewHolder(holder: adapterCarrito.MyHolder, position: Int) {
        val producto = listaCarrito.getJSONObject(position)

        var nombre=producto.getString("producto")
        var imagen= config.urlBase+producto.getString("imagen")
        var cantidad=producto.getInt("cantidad")
        val precio=producto.getDouble("precio")
        //val subtotal=producto.getDouble("subtotal")


        if (nombre.length >= 40){
            nombre = nombre.substring(0, 39) + "..."
        }

        val formato = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        formato.maximumFractionDigits = 0
        val formatoPrecio = formato.format(precio)
        //val formatoSubtotal = formato.format(subtotal)


        // holder.lbl.text = "$formatoSubtotal"
        holder.lblNombre.text = nombre
        holder.lblPrecio.text = "$formatoPrecio"
        holder.txtCantidad.text = "$cantidad"
        Glide.with(holder.itemView.context).load(imagen).into(holder.imgProducto)

        /*holder.btnEliminarCarrito.setOnClickListener {
            onclick?.invoke(producto)
        }*/


    }

    override fun getItemCount(): Int {
        return  listaCarrito.length()
    }
}
