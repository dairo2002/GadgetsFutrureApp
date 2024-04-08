package com.example.gadgetsfuture;

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.gadgetsfuture.adapter.adapterCategDeProduct
import com.example.gadgetsfuture.adapter.adapterCategoria
import com.example.gadgetsfuture.adapter.adapterHome
import com.example.gadgetsfuture.config.config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [store_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class store_fragment : Fragment() {

    private var id_categoria: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            //id_categoria=it.getInt("id_categoria")

        }
    }

    lateinit var recyclerProducto: RecyclerView
    lateinit var recyclerCategoria: RecyclerView
    lateinit var recyclerCategoriaDeProductos: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_store_fragment, container, false)

        /*
        val buttonIds = listOf(
            R.id.btnPortatiles,
            R.id.btnMouses,
            R.id.btnTeclados,
            R.id.btnMonitores,
            R.id.btnComputadores,
            R.id.btnConsolas,
            R.id.btnSillas
        )*/

        /*for (buttonId in buttonIds) {
            val btnCategoria: Button = view.findViewById(buttonId)

            btnCategoria.setOnClickListener {
                // Cambiar el estado seleccionado del botón
                btnCategoria.isSelected = !btnCategoria.isSelected

                // Cambiar el fondo y el color del texto según el estado seleccionado
                if (btnCategoria.isSelected) {
                    btnCategoria.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                    btnCategoria.setTextColor(resources.getColor(R.color.white))
                    cambiarColorIcono(btnCategoria, R.color.black)
                } else {
                    btnCategoria.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
                    btnCategoria.setTextColor(resources.getColor(R.color.black))
                    cambiarColorIcono(btnCategoria, R.color.black)
                }
            }
        }*/


        recyclerProducto=view.findViewById(R.id.RVStoreProductos)
        llamarPeticionProductos()

        recyclerCategoria=view.findViewById(R.id.RVCategorias)
        llamarPeticionCategoria()

        recyclerCategoriaDeProductos=view.findViewById(R.id.RVCategoriaDeProductos)

        return view
    }

    private fun cambiarColorIcono(button: Button, colorId: Int) {
        val icono = button.compoundDrawablesRelative[0] // Obtiene el icono del botón
        icono?.setColorFilter(ContextCompat.getColor(requireContext(), colorId), PorterDuff.Mode.SRC_IN) // Cambia el color del icono
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment store_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            store_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun llamarPeticionProductos(){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                listaProductos()
            }catch (error: Exception){
                Toast.makeText(activity, "Error en el servidor, por favor conectate a internet", Toast.LENGTH_LONG).show()
            }
        }
    }


    fun llamarPeticionCategoria(){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                listaCategoria()
            }catch (error: Exception){
                Toast.makeText(activity, "Error en el servidor, por favor conectate a internet", Toast.LENGTH_LONG).show()
            }
        }
    }

    /** Productos de una categoria */

    suspend fun categoriaDeProductos(idCategoria: Int){
        // http://192.168.1.10:8000/tienda/api/v1/store/1/
        var url= config.urlTienda+"v1/store/$idCategoria/"
        var queue= Volley.newRequestQueue(activity)
        var request= JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            {response->
                cargarLista(response)
            },
            {error->
                Toast.makeText(activity, "Error en la solicitud: {$error}", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(request)

    }



    /* Corregir para que la dos apis no se ejcuten a al misma vez
    fun cargarListaCategproduct(listaCategDeProduct: JSONArray){
        recyclerCategoriaDeProductos.layoutManager=LinearLayoutManager(activity)
        var adapter= adapterCategDeProduct(activity, listaCategDeProduct)
        adapter.onclick= {
            val bundle=Bundle()
            bundle.putInt("id_productoH",it.getInt("id"))
            val transaction=requireFragmentManager().beginTransaction()
            //var fragmento=detalle_producto()
            //fragmento.arguments=bundle
            var fragmento=store_fragment().apply {
                arguments=bundle
            }
            transaction.replace(R.id.container, fragmento).commit()
            transaction.addToBackStack(null)
        }
        recyclerCategoriaDeProductos.adapter=adapter
    }
*/

    /** Categoria */
    suspend fun listaCategoria(){
        var queue= Volley.newRequestQueue(activity)
        var url= config.urlTienda+"v1/lista_categorias/"
        var request= JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            {response->
                try {
                    cargarListaCategoria(response)
                } catch (e: JSONException){
                    e.printStackTrace()
                }
            },
            {error->
                Toast.makeText(activity, "Error en la solicitud: {$error}", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(request)
    }




    fun cargarListaCategoria(listaCategoria: JSONArray){
        recyclerCategoria.layoutManager= LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        var adapter= adapterCategoria(activity, listaCategoria)
        adapter.onclick= {categoria ->
            val idCategoria = categoria.getInt("id")
            /*al bundle=Bundle().apply {
                putInt("id_categoria", idCategoria)
            }
            //bundle.putInt("id_categoria",it.getInt("id"))
            val transaction=requireFragmentManager().beginTransaction()
            var fragmento=store_fragment().apply {
                arguments=bundle
            }
            //fragmento.arguments=bundle
            transaction.replace(R.id.container, fragmento).commit()
            transaction.addToBackStack(null)*/

            GlobalScope.launch {
                try {
                    categoriaDeProductos(idCategoria)
                }catch (error: Exception){
                    Toast.makeText(activity, "Error en el servidor, por favor conectate a internet", Toast.LENGTH_LONG).show()
                }
            }

        }
        recyclerCategoria.adapter=adapter
    }


    /** Productos */

    suspend fun listaProductos(){
        var url= config.urlBase+"/api/list_product/v1/"
        var queue= Volley.newRequestQueue(activity)
        var request= JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            {response->
                cargarLista(response)
            },
            {error->
                Toast.makeText(activity, "Error en la solicitud: {$error}", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(request)
    }


    fun cargarLista(listaProductos: JSONArray){
        recyclerProducto.layoutManager= LinearLayoutManager(activity)
        var adapter= adapterHome(activity, listaProductos)
        // Cambio de fragmento desde otro
        adapter.onclick= {
            val bundle=Bundle()
            bundle.putInt("id_productoH",it.getInt("id"))
            val transaction=requireFragmentManager().beginTransaction()
            var fragmento=detalle_producto()
            fragmento.arguments=bundle
            transaction.replace(R.id.container, fragmento).commit()
            transaction.addToBackStack(null)
        }
        recyclerProducto.adapter=adapter
    }

}