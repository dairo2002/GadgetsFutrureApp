package com.example.gadgetsfuture;

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.gadgetsfuture.adapter.adapterHome
import com.example.gadgetsfuture.config.config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    //Se define la variable que contiene el RecyclerView
    lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_home, container, false)
        var imageSlider: ImageSlider = view.findViewById(R.id.imageSlider)
        var slideModels : ArrayList<SlideModel> = ArrayList<SlideModel>()

        slideModels.add(SlideModel(R.drawable.banner_gadgets_future,ScaleTypes.FIT))
        slideModels.add(SlideModel(R.drawable.banner_gadgets_future2,ScaleTypes.FIT))
        slideModels.add(SlideModel(R.drawable.banner_gadgets_future3,ScaleTypes.FIT))
        imageSlider.setImageList(slideModels,ScaleTypes.FIT)

        // MUESTRA LA VISTA HOME CON LOS PRODUCTOS
        recycler=view.findViewById(R.id.RVHome)

        llamarPeticion()

        return view
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun llamarPeticion(){
        GlobalScope.launch(Dispatchers.Main) {
            try {
                peticionListaProductosH()
            }catch (error: Exception){
                Toast.makeText(activity, "Error en el servidor, por favor conectate a internet", Toast.LENGTH_LONG).show()
            }
        }
    }

    /*suspend fun peticionBusquedaDeProductos( palabraClave: String, onSuccess: (List<Producto>) -> Unit, onError: (String) -> Unit){
        var queue=Volley.newRequestQueue(context)
        val url = config.urlTienda+"v1/search_product/"
        val request = JsonArrayRequest(
            Request.Method.POST,
            url,
            null,
            {response ->
            

            },
            {error ->

            }


        )

    }*/


    suspend fun peticionListaProductosH(){
        var url=config.urlBase+"/api/list_product/v1/"
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
        recycler.layoutManager= LinearLayoutManager(activity)
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
        recycler.adapter=adapter
    }




}