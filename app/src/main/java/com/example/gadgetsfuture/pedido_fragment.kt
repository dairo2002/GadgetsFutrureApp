package com.example.gadgetsfuture

import android.os.Bundle
import android.text.InputFilter
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [pedido_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    //@SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
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

        txtTelefono.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))

        val texto = "Agregar apartamento, local (Opcional)"
        val spannableString = SpannableString(texto)
        spannableString.setSpan(UnderlineSpan(), 0, texto.length, 0)
        lblDireccionLocal.text = spannableString

        lblDireccionLocal.setOnClickListener {
            if (txtDireccionLocal.visibility == View.VISIBLE) {
                lblDireccionLocal.visibility = View.GONE
            } else {
                lblDireccionLocal.visibility = View.VISIBLE
            }
        }


        val departamentos = arrayOf("Seleccionar Departamentos", "Huila", "Caldas", "Antioquia")
        val ciudadesDepartamento1 = arrayOf("Seleccionar", "Neiva", "Rivera", "Yaguará")
        val ciudadesDepartamento2 = arrayOf("Seleccionar", "Manizales", "La dorada", "Villa María")
        val ciudadesDepartamento3 = arrayOf("Seleccionar", "Medellin", "Caucasia", "Apartado")

        val largeTextSize = 60f // Tamaño de texto más grande

        val adapterDepartamentos = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, departamentos)
        adapterDepartamentos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sprDepartamento.adapter = adapterDepartamentos

        sprDepartamento.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                sprMunicipio.adapter = adapterCiudades
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        btnContinuarPago.setOnClickListener {

            var valido = true
            // Verificar si algún campo está vacío
            if (txtNombre.text.toString().isEmpty()) {
                txtNombre.error = "El nombre es requerido"
                valido = false
            }
            if (txtApellido.text.toString().isEmpty()) {
                txtApellido.error = "El apellido es requerido"
                valido = false
            }
            if (txtCorreo.text.toString().isEmpty()) {
                txtCorreo.error = "El email es requerido"
                valido = false
            }
            if (txtDireccion.text.toString().isEmpty()) {
                txtDireccion.error = "El dirección es requerido"
                valido = false
            }
            if (txtCodigoPostal.text.toString().isEmpty()) {
                txtCodigoPostal.error = "El codigo postal es requerido"
                valido = false
            }
            if (txtTelefono.text.toString().isEmpty()) {
                txtTelefono.error = "El teléfono es requerido"
                valido = false
            }
            // Verificar si el campo de correo electrónico está vacío o no cumple con el formato requerido
            val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-z]+"
            val email = txtCorreo.text.toString()
            if (email.isEmpty() || !email.matches(emailPattern.toRegex())) {
                txtCorreo.error = "Ingrese un correo electrónico válido"
                valido = false
            }





            // Si todos los campos son válidos, iniciar la siguiente actividad
            if (valido) {
                /*val transaction = requireFragmentManager().beginTransaction()
                var fragment=pedidos2Fragment<Button>()
                transaction.replace(R.id.container, fragment).commit()*/
            }


            // Configuración del ImageButton para redireccionar a otra vista


        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment pedido_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            pedido_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}