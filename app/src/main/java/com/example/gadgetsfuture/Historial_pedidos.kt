package com.example.gadgetsfuture

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gadgetsfuture.adapter.adapterHistorialPedidos
import com.example.gadgetsfuture.adapter.adapterHome
import com.example.gadgetsfuture.config.config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException

class Historial_pedidos : AppCompatActivity() {

    lateinit var recycler: RecyclerView
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_pedidos)

        context = applicationContext
        recycler = findViewById(R.id.RVHistorialPedidos)

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }


        GlobalScope.launch {
            try {
                HistorialPedidos()
            } catch (error: Exception) {
                Toast.makeText(
                    applicationContext,
                    "Error en el servidor, por favor conectate a internet",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    suspend fun HistorialPedidos() {
        var url = config.urlPedido + "v1/historial_pedidos/"
        var queue = Volley.newRequestQueue(applicationContext)
        var request = object : JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val historial = response.getJSONArray("historial")
                cargarHistorial(historial)
            } catch (e: JSONException) {
                e.printStackTrace()
                Toast.makeText(
                    applicationContext,
                    "Error al procesar la respuesta del servidor",
                    Toast.LENGTH_LONG
                ).show()
            }
        }, { error ->
            Toast.makeText(
                applicationContext, "Error en la solicitud: {$error}", Toast.LENGTH_LONG
            ).show()
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                if (config.token.isNotEmpty()) {
                    headers["Authorization"] = "Bearer ${config.token}"
                }
                return headers
            }
        }
        queue.add(request)
    }

    fun cargarHistorial(listaPedidos: JSONArray) {
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        var adapter = adapterHistorialPedidos(applicationContext, listaPedidos)
        //recycler.adapter = adapter
        adapter.onclick = { historial ->
            try {
                val productoId = historial.getInt("id")
                val bundle = Bundle().apply {
                    putInt("id_productoH", productoId)
                }

                /** Corregir */
                val transaction = supportFragmentManager.beginTransaction()
                var fragmento = detalle_producto()
                fragmento.arguments = bundle
                transaction.replace(R.id.container, fragmento)
                transaction.addToBackStack(null)
                transaction.commit()
            } catch (e: JSONException) {
                Toast.makeText(applicationContext, "Error $e", Toast.LENGTH_SHORT).show()
            }
        }
        recycler.adapter = adapter
    }

}