package com.example.gadgetsfuture

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.gadgetsfuture.config.config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [detalle_producto.newInstance] factory method to
 * create an instance of this fragment.
 */
class detalle_producto : Fragment() {

    private var id_producto: Int = 0

    lateinit var lblNombre: TextView
    lateinit var lblPrecio: TextView
    lateinit var imgProducto: ImageView
    lateinit var lbldescripcion: TextView
    lateinit var btnAgregarCarrito: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //Obtener le id del producto
            id_producto = it.getInt("id_productoH")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detalle_producto, container, false)

        val btnBack: ImageButton = view.findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            // Cerrar el fragmento (puedes cambiar esto según lo que necesites hacer)
            requireActivity().supportFragmentManager.popBackStack()
        }

        lblNombre = view.findViewById(R.id.lblNombreDetalleProducto)
        lblPrecio = view.findViewById(R.id.lblPrecioDetalleProducto)
        imgProducto = view.findViewById(R.id.imgDetalleProducto)
        lbldescripcion = view.findViewById(R.id.lblDetalleDescripcion)
        btnAgregarCarrito = view.findViewById(R.id.btnDetalleAggCarrito)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                peticionDetalleUnProducto()
            } catch (error: Exception) {
                Toast.makeText(activity, "Error en la petición: {$error}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        btnAgregarCarrito.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    agregarCarrito(id_producto)
                    val transaction = requireFragmentManager().beginTransaction()
                    transaction.replace(R.id.container, Cart_fragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                } catch (error: Exception) {
                    Toast.makeText(activity, "Error en la petición: {$error}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = detalle_producto().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }


    suspend fun peticionDetalleUnProducto() {
        var queue = Volley.newRequestQueue(activity)
        var url = config.urlTienda + "v1/detail_product/$id_producto/"
        var request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val nombre = response.getString("nombre")
                val precio = response.getDouble("precio")
                val descripcion = response.getString("descripcion")
                val imgURL = config.urlBase + response.getString("imagen")

                val formato = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
                formato.maximumFractionDigits = 0
                val formatoMoneda = formato.format(precio)

                lblNombre.text = nombre
                lblPrecio.text = "$formatoMoneda"
                lbldescripcion.text = descripcion
                Glide.with(this).load(imgURL).into(imgProducto)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error ->
            Toast.makeText(activity, "Error en la solicitud: $error", Toast.LENGTH_SHORT).show()
            Log.e("PeticionDetalleProducto", "Error en la solicitud", error)
        })
        queue.add(request)
    }

    suspend fun agregarCarrito(id: Int) {
        var url = config.urlCarrito + "v1/agregar_carrito/"
        var queue = Volley.newRequestQueue(activity)
        val parametro = JSONObject().apply {
            put("id", id)
        }
        val request = object : JsonObjectRequest(Method.POST, url, parametro, { response ->
            Toast.makeText(activity, "Se agrego el producto", Toast.LENGTH_LONG).show()
        }, { error ->
            Toast.makeText(activity, "Error $error", Toast.LENGTH_LONG).show()
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

}