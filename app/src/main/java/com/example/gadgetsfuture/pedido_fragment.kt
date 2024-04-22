package com.example.gadgetsfuture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.text.SpannableString
import android.text.style.UnderlineSpan
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

    private val departamentosMap = HashMap<String, String>()

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


        txtTelefono.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))

        // Configuración del texto con subrayado para el texto de dirección local
        val texto = "Agregar apartamento, local (Opcional)"
        val spannableString = SpannableString(texto)
        spannableString.setSpan(UnderlineSpan(), 0, texto.length, 0)
        lblDireccionLocal.text = spannableString

        // Manejo del evento de clic en el texto de dirección local para mostrar/ocultar el campo
        lblDireccionLocal.setOnClickListener {
            txtDireccionLocal.visibility =
                if (txtDireccionLocal.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        // Cargar los departamentos al spinner al iniciar el fragmento
        //cargarDepartamentos(requireActivity(), sprDepartamento)

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

        GlobalScope.launch {
            try {
                obtenerDepartamentos()
            } catch (error: Exception) {
                Toast.makeText(context, "Error en la solicitud: $error", Toast.LENGTH_SHORT).show()
            }

        }


        btnContinuarPago.setOnClickListener {
            GlobalScope.launch {
                try {
                    realizarPedido()
                } catch (error: Exception) {
                    Toast.makeText(context, "Error en el pedido: $error", Toast.LENGTH_SHORT).show()
                }
            }
        }


        /*btnBack.setOnClickListener {
            val intent = Intent(this@pedido_fragment, Cart_fragment::class.java)
            startActivity(intent)
            finish()
        }*/



        return view
    }

    suspend fun obtenerDepartamentos() {
        val url = config.urlPedido + "v1/departamento/"
        //val departamentos = ArrayList<String>()

        val queue = Volley.newRequestQueue(context)
        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                for (i in 0 until response.length()) {
                    val departamento = response.getJSONObject(i)
                    val nombreD = departamento.getString("nombre")
                    val codigoD = departamento.getString("codigo")
                    //departamentos.add(nombreD)
                    departamentosMap[nombreD] = codigoD
                }
                cargarDepartamentosSpinner(departamentosMap.keys.toList())
            },
            { error ->
                Toast.makeText(context, "Error en la solicitud: $error", Toast.LENGTH_LONG).show()
            })
        queue.add(request)
    }

    fun cargarDepartamentosSpinner(departamentos: List<String>) {
        val adapterDepartamentos = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, departamentos)
        adapterDepartamentos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sprDepartamento.adapter = adapterDepartamentos

        sprDepartamento.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val departamentoSeleccionado = parent?.getItemAtPosition(position) as String
                val codigoDepartamento = departamentosMap[departamentoSeleccionado]
                if (codigoDepartamento != null) {
                    GlobalScope.launch {
                        try {
                            obtenerMunicipiosPorDepartamento(codigoDepartamento)
                        } catch (error: Exception) {
                            // Manejar el caso cuando no se encuentra el código del departamento
                            Toast.makeText(
                                context,
                                "Error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    // Manejar el caso cuando no se encuentra el código del departamento
                    Toast.makeText(
                        context,
                        "Código de departamento no encontrado",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Método requerido pero no se necesita implementar en este caso
            }
        }
    }

    suspend fun obtenerMunicipiosPorDepartamento(codDepartamento: String) {
        val url = config.urlPedido + "v2/municipio/$codDepartamento"
        val municipios = ArrayList<String>()
        val queue = Volley.newRequestQueue(context)
        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                for (i in 0 until response.length()) {
                    val municipio = response.getJSONObject(i)
                    val nombreM = municipio.getString("nombre")
                    municipios.add(nombreM)
                }
                cargarMunicipiosSpinner(municipios)
            },
            { error ->
                Toast.makeText(context, "Error en la solicitud: $error", Toast.LENGTH_LONG).show()
            })
        queue.add(request)
    }

    fun cargarMunicipiosSpinner(municipios: List<String>) {
        val adapterMunicipios = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, municipios)
        adapterMunicipios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sprMunicipio.adapter = adapterMunicipios
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
                 val pedidoId = response.getInt("pedido_id")
                 // pasar el id al cambio de fragmento
                 /*var url = config.urlBase + "/pedido/pago/$pedidoId/"
                 val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                 startActivity(intent)*/

                 val bundle=Bundle().apply {
                     putInt("pedido_id", pedidoId)
                 }
                 val transaction=requireFragmentManager().beginTransaction()
                 var fragmento=pago_Fragment()
                 fragmento.arguments=bundle
                 transaction.replace(R.id.container, fragmento)
                 transaction.addToBackStack(null)
                 transaction.commit()
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
