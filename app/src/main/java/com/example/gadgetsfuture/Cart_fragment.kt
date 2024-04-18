package com.example.gadgetsfuture;

import adapterCarrito
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gadgetsfuture.adapter.adapterCategoria
import com.example.gadgetsfuture.config.config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Cart_fragment : Fragment(), adapterCarrito.TotalCalculadoListener {

    lateinit var ID: TextView
    lateinit var btnMas: Button
    lateinit var btnMenos: Button
    lateinit var txtCantidad: EditText
    lateinit var lblTotal: TextView
    lateinit var btnActualizar: Button
    lateinit var btnRealizarPedido: Button
    lateinit var recyclerCarrito: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        recyclerCarrito = view.findViewById(R.id.RVCart)
        lblTotal = view.findViewById(R.id.lblTotalCarrito)
        btnActualizar = view.findViewById(R.id.btnActualizarCart)
        btnRealizarPedido = view.findViewById(R.id.btnRealizarPedido)

        btnRealizarPedido.setOnClickListener {
            val transaction = requireFragmentManager().beginTransaction()
            var fragmento = pedido_fragment()
            transaction.replace(R.id.container, fragmento)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        peticionMostarCarrito()

        // Inicializar la RecyclerView
        recyclerCarrito = view.findViewById(R.id.RVCart)
        recyclerCarrito.layoutManager = LinearLayoutManager(activity)

        // Inicializar el TextView para el total
        lblTotal = view.findViewById(R.id.lblTotalCarrito)

        // Crear un adaptador de carrito con una lista vacía
        val listaCarrito = JSONArray()
        val adapter = adapterCarrito(requireContext(), listaCarrito)

        // Establecer este fragmento como listener para el total calculado en el adaptador
        adapter.setTotalListener(this)

        // Asignar el adaptador a la RecyclerView
        recyclerCarrito.adapter = adapter

        return view
    }

    override fun onTotalCalculado(total: Double) {
        // Aquí recibes el total calculado del adaptador
        // Haz lo que necesites con el total en este fragmento
        lblTotal.text = total.toString()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment cart.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = Cart_fragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }


    fun peticionMostarCarrito() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                mostrarCarrito()
            } catch (error: Exception) {
                Toast.makeText(
                    activity,
                    "Error en el servidor, por favor conectate a internet",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    suspend fun mostrarCarrito() {
        var url = config.urlCarrito + "v2/mostrar_carrito/"
        var queue = Volley.newRequestQueue(activity)
        var request = object : JsonArrayRequest(Request.Method.GET, url, null, { response ->
            cargarListaCarrito(response)
        }, { error ->
            Toast.makeText(activity, "Error: $error", Toast.LENGTH_LONG).show()
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

    fun cargarListaCarrito(listaCarrito: JSONArray) {
        recyclerCarrito.layoutManager = LinearLayoutManager(activity)
        var adapter = adapterCarrito(activity, listaCarrito)
        adapter.onclickEliminar = { carrito ->
            var idCart = carrito.getInt("id_producto")

            GlobalScope.launch {
                try {
                    eliminarCarrito(idCart)
                } catch (error: Exception) {
                    Toast.makeText(
                        activity,
                        "Error en la petición: {$error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        recyclerCarrito.adapter = adapter

    }

    /** Corregir */
    suspend fun eliminarCarrito(id: Int) {
        var url = config.urlCarrito + "v1/eliminar_carrito/$id/"
        var queue = Volley.newRequestQueue(activity)

        var request = object : JsonObjectRequest(
            Method.DELETE,
            url,
            null,
            { response ->
                val message = response.getString("message")
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                peticionMostarCarrito()
            },
            { error ->
                var mensajeError = JSONObject(String(error.networkResponse.data))
                Toast.makeText(activity, "Error al eliminar: ${mensajeError}", Toast.LENGTH_LONG)
                    .show()
            }
        ) {
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


    suspend fun actualizarCarrito(id: Int, cantidad: Int) {
        var url = config.urlCarrito + "v1/actualizar_carrito/"
        var queue = Volley.newRequestQueue(context)
        val parametros = JSONObject().apply {
            put("id", id)
            put("cantidad", cantidad)
        }
        var request = object : JsonObjectRequest(Request.Method.PUT, url, parametros, { response ->
            Toast.makeText(activity, "Actualizado", Toast.LENGTH_LONG).show()
            cargarListaCarrito(response.getJSONArray("listaCarrito"))

            // Cargar el carrito
            //var lista = JSONArray()
            //mostrarCarrito()
            //cargarListaCarrito(lista)
        }, { error ->
            Toast.makeText(activity, "Error: $error", Toast.LENGTH_LONG).show()
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


    suspend fun realizarPedido() {

    }
}
