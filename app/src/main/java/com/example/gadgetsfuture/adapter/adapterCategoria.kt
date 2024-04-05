package com.example.gadgetsfuture.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gadgetsfuture.R
import org.json.JSONArray
import org.json.JSONObject

class adapterCategoria(var context: Context?, var listaCategoria:JSONArray):RecyclerView.Adapter<adapterCategoria.MyHolder>(){

    inner class MyHolder(ItemCategoria : View):RecyclerView.ViewHolder(ItemCategoria) {

        lateinit var btnCategorias: TextView

        init {
            btnCategorias=itemView.findViewById(R.id.btnCategorias)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapterCategoria.MyHolder {
        var itemView= LayoutInflater.from(context).inflate(R.layout.item_categoria,parent,false)
        return MyHolder(itemView)
    }

    var onclick:((JSONObject)->Unit)?=null
    override fun onBindViewHolder(holder: adapterCategoria.MyHolder, position: Int) {
        val listCategoria = listaCategoria.getJSONObject(position)
        var nombre = listCategoria.getString("nombre")


        holder.btnCategorias.text=nombre

        // Acción para que nos lleve a los productos de una categoria
        holder.btnCategorias.setOnClickListener {
            onclick?.invoke(listCategoria)
        }
    }

    override fun getItemCount(): Int {
        return listaCategoria.length()
    }


}