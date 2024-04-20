package com.example.gadgetsfuture

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gadgetsfuture.config.config
import com.example.gadgetsfuture.model.Departamentos
import com.example.gadgetsfuture.model.Municipios
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class pedido_fragment : Fragment() {

    lateinit var txtNombre: EditText
    lateinit var txtApellido: EditText
    lateinit var txtTelefono: EditText
    lateinit var txtCorreo: EditText
    lateinit var txtDireccion: EditText
    lateinit var txtDireccionLocal: EditText
    lateinit var lblDireccionLocal: TextView
    lateinit var sprDepartamento: Spinner
    lateinit var sprMunicipio: Spinner
    lateinit var txtCodigoPostal: EditText
    lateinit var btnContinuarPago: Button


    lateinit var btnBack: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pedido_fragment, container, false)

        txtCorreo = view.findViewById(R.id.txtCorreoPedido)
        txtNombre = view.findViewById(R.id.txtNombrePedido)
        txtApellido = view.findViewById(R.id.txtApellidoPedido)
        txtTelefono = view.findViewById(R.id.txtTelefonoPedidos)
        txtDireccion = view.findViewById(R.id.txtDireccionPedido)
        txtDireccionLocal = view.findViewById(R.id.txtDireccionLocal)
        lblDireccionLocal = view.findViewById(R.id.lblDireccionLocal)
        sprDepartamento = view.findViewById(R.id.sprDepartamentoPedido)
        sprMunicipio = view.findViewById(R.id.sprMunicipioPedido)
        txtCodigoPostal = view.findViewById(R.id.txtCodigoPostalPedido)
        btnContinuarPago = view.findViewById(R.id.btnContinuarPago)
        btnBack = view.findViewById(R.id.btnBack)

        // Configuración del límite de longitud para el campo de teléfono
        txtTelefono.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))

        // Configuración del texto con subrayado para el texto de dirección local
        val texto = "Agregar apartamento, local (Opcional)"
        val spannableString = SpannableString(texto)
        spannableString.setSpan(UnderlineSpan(), 0, texto.length, 0)
        lblDireccionLocal.text = spannableString

        // Manejo del evento de clic en el texto de dirección local para mostrar/ocultar el campo
        lblDireccionLocal.setOnClickListener {
            txtDireccionLocal.visibility = if (txtDireccionLocal.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        // Cargar los departamentos al spinner al iniciar el fragmento
        cargarDepartamentos(requireActivity(), sprDepartamento)

        // Configurar el listener para el Spinner de departamentos
        /*sprDepartamento.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Obtener el departamento seleccionado del spinner
                val departamentoSeleccionado = parent?.getItemAtPosition(position) as Departamentos
                // Obtener el código del departamento seleccionado
                val codigoDepartamentoSeleccionado = departamentoSeleccionado.codigo
                // Cargar los municipios del departamento seleccionado en el Spinner de municipios
                cargarMunicipiosPorDepartamento(requireActivity(), codigoDepartamentoSeleccionado, sprMunicipio)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No es necesario realizar ninguna acción si no se selecciona nada en el spinner
            }
        }*/

        /*
         val spinnerDepartamentos = view.findViewById<Spinner>(R.id.spinner)
        val spinnerCiudades = view.findViewById<Spinner>(R.id.spinnerciudad)

        val departamentos = arrayOf("Seleccionar Departamentos", "Huila", "Caldas", "Antioquia")
        val ciudadesDepartamento1 = arrayOf("Seleccionar", "Neiva", "Rivera", "Yaguará")
        val ciudadesDepartamento2 = arrayOf("Seleccionar", "Manizales", "La dorada", "Villa María")
        val ciudadesDepartamento3 = arrayOf("Seleccionar", "Medellin", "Caucasia", "Apartado")

        val largeTextSize = 60f // Tamaño de texto más grande

        val adapterDepartamentos = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, departamentos)
        adapterDepartamentos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDepartamentos.adapter = adapterDepartamentos

        spinnerDepartamentos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val departamentoSeleccionado = departamentos[position]

                val ciudadesArray = when (departamentoSeleccionado) {
                    "Huila" -> ciudadesDepartamento1
                    "Caldas" -> ciudadesDepartamento2
                    "Antioquia" -> ciudadesDepartamento3
                    else -> emptyArray()
                }

                val adapterCiudades = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ciudadesArray)
                adapterCiudades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCiudades.adapter = adapterCiudades
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No es necesario hacer nada en este caso
            }
        }
         */

        btnContinuarPago.setOnClickListener {
            GlobalScope.launch {
                try {
                    realizarPedido()
                } catch (error: Exception) {
                    Toast.makeText(context, "Error en el pedido: $error", Toast.LENGTH_SHORT).show()
                }
            }
        }



        btnBack.setOnClickListener {
            /*val intent = Intent(this@pedido_fragment, Cart_fragment::class.java)
            startActivity(intent)
            finish()*/
        }

        return view
    }

    // Función para cargar los departamentos en el spinner
    fun cargarDepartamentos(context: Context, spinner: Spinner) {
        GlobalScope.launch {
            try {
                // Realizar la solicitud para obtener la lista de departamentos
                ListaDepartamento(context) { departamentos ->
                    // Crear un adaptador con la lista de departamentos y establecerlo en el Spinner
                    val adapterDepartamentos = ArrayAdapter(
                        context,
                        android.R.layout.simple_spinner_item,
                        departamentos.map { it.nombre }
                    )
                    adapterDepartamentos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapterDepartamentos
                }
            } catch (error: Exception) {
                Toast.makeText(context, "Error al cargar los departamentos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para cargar los municipios en el spinner según el departamento seleccionado
    fun cargarMunicipiosPorDepartamento(context: Context, codigoDepartamento: String, spinner: Spinner) {
        GlobalScope.launch {
            try {
                // Realizar la solicitud para obtener la lista de municipios del departamento seleccionado
                obtenerMunicipiosPorDepartamento(context, codigoDepartamento) { municipios ->
                    // Crear un adaptador con la lista de municipios y establecerlo en el Spinner
                    val adapterMunicipios = ArrayAdapter(
                        context,
                        android.R.layout.simple_spinner_item,
                        municipios.map { it.nombre }
                    )
                    adapterMunicipios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapterMunicipios
                }
            } catch (error: Exception) {
                Toast.makeText(context, "Error al cargar los municipios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para obtener la lista de departamentos
    suspend fun ListaDepartamento(context: Context, callback: (List<Departamentos>) -> Unit) {
        val url = config.urlPedido + "v1/departamento/"
        val queue = Volley.newRequestQueue(context)
        val request = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            try {
                val departamentos = mutableListOf<Departamentos>()
                for (i in 0 until response.length()) {
                    val departamento = response.getJSONObject(i)
                    val depa = Departamentos(
                        departamento.getString("nombre"), departamento.getString("codigo")
                    )
                    departamentos.add(depa)
                }
                callback(departamentos)
            } catch (e: Exception) {
                Log.e("ListaDepartamentos", "Error al procesar la respuesta JSON: $e")
            }
        }, { error ->
            Toast.makeText(context, "Error en la solicitud: $error", Toast.LENGTH_LONG).show()
        })
        queue.add(request)
    }

    // Función para obtener la lista de municipios según el departamento seleccionado
     suspend fun obtenerMunicipiosPorDepartamento(
        context: Context, codigoDepartamento: String, callback: (List<Municipios>) -> Unit
    ) {
        val url = config.urlPedido + "v1/municipio/?codigo_departamento=$codigoDepartamento"
        val queue = Volley.newRequestQueue(context)
        val request = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            try {
                val municipios = mutableListOf<Municipios>()
                for (i in 0 until response.length()) {
                    val municipio = response.getJSONObject(i)
                    val muni = Municipios(
                        municipio.getString("nombre"),
                        municipio.getString("codigo"),
                        municipio.getString("codigo_departamento"),
                    )
                    municipios.add(muni)
                }
                callback(municipios)
            } catch (e: Exception) {
                Log.e("obtenerMunicipios", "Error al procesar la respuesta JSON: $e")
            }
        }, { error ->
            Toast.makeText(context, "Error en la solicitud: $error", Toast.LENGTH_LONG).show()
        })
        queue.add(request)
    }

     suspend fun realizarPedido() {
         val url = config.urlPedido + "v1/realizar_pedido/"
         val queue = Volley.newRequestQueue(context)
         val parametro = JSONObject()

         parametro.put("nombre", txtNombre.text)
         parametro.put("apellido", txtApellido.text)
         parametro.put("correo_electronico", txtCorreo.text)
         parametro.put("telefono", txtTelefono.text)
         parametro.put("direccion", txtDireccion.text)
         parametro.put("direccion_local", txtDireccionLocal.text)
         parametro.put("departamento", sprDepartamento.selectedItem?.toString() ?: "")
         parametro.put("municipio", sprMunicipio.selectedItem?.toString() ?: "")
         parametro.put("codigo_postal", txtCodigoPostal.text)

         val request = object : JsonObjectRequest(
             Request.Method.POST,
             url,
             parametro,
             { response ->
                 // Maneja la respuesta de la API aquí
                 val pedidoId = response.getInt("pedido_id")
                 var url = config.urlBase + "/pedido/pago/$pedidoId/"
                 val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                 startActivity(intent)
             },
             { error ->
                 Toast.makeText(
                     context, "Error en la solicitud: $error", Toast.LENGTH_SHORT
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

}
