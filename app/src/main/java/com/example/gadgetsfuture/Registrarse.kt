package com.example.gadgetsfuture

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gadgetsfuture.config.config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class Registrarse : AppCompatActivity() {

    lateinit var context: Context
    private lateinit var txtNombre: EditText
    private lateinit var txtApellidos: EditText
    private lateinit var txtNum: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtContras: EditText
    private lateinit var txtConfirmContra: EditText
    private lateinit var nameError: TextView
    private lateinit var apellidosError: TextView
    private lateinit var numError: TextView
    private lateinit var emailError: TextView
    private lateinit var contraError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)
        context=applicationContext

        txtNombre = findViewById(R.id.txtNombre)
        txtApellidos = findViewById(R.id.txtApellidos)
        txtNum = findViewById(R.id.txtNum)
        txtEmail = findViewById(R.id.txtEmail)
        txtContras = findViewById(R.id.txtContras)
        txtConfirmContra = findViewById(R.id.txtConfirmContra)

        nameError = findViewById(R.id.nameError)
        apellidosError = findViewById(R.id.apellidosError)
        numError = findViewById(R.id.numError)
        emailError = findViewById(R.id.emailError)
        contraError = findViewById(R.id.contraError)

        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)
        btnRegistrarse.setOnClickListener {
            validarCampos()
        }
    }
    fun  inicioS(view: View) {
        val intent = Intent(this, InicioSesion::class.java)
        startActivity(intent)
        finish()
        this@Registrarse.overridePendingTransition(
            R.anim.animate_slide_in_left,
            R.anim.animate_slide_out_right
        )
    }

    private fun validarCampos() {
        val nombre = txtNombre.text.toString()
        val apellidos = txtApellidos.text.toString()
        val num = txtNum.text.toString()
        val email = txtEmail.text.toString()
        val contras = txtContras.text.toString()
        val confirmContra = txtConfirmContra.text.toString()

        var isValid = true

        if (nombre.isEmpty()) {
            nameError.visibility = View.VISIBLE
            nameError.text = "El nombre es requerido"
            isValid = false
        } else {
            nameError.visibility = View.VISIBLE
            nameError.text =""
        }

        if (apellidos.isEmpty()) {
            apellidosError.visibility = View.VISIBLE
            apellidosError.text = "Los apellidos son requeridos"
            isValid = false
        } else {
            apellidosError.visibility = View.VISIBLE
            apellidosError.text = ""
        }

        if (num.isEmpty()) {
            numError.visibility = View.VISIBLE
            numError.text = "El número es requerido"
            isValid = false
        } else {
            numError.visibility = View.VISIBLE
            numError.text =""
        }

        if (email.isEmpty()) {
            emailError.visibility = View.VISIBLE
            emailError.text = "El correo electrónico es requerido"
            isValid = false
        } else {
            emailError.visibility = View.VISIBLE
            emailError.text = ""
        }

        if (contras.isEmpty()) {
            contraError.visibility = View.VISIBLE
            contraError.text = "La contraseña es requerida"
            isValid = false
        } else {
            contraError.visibility = View.VISIBLE
            contraError.text = ""
        }

        if (confirmContra.isEmpty()) {
            contraError.visibility = View.VISIBLE
            contraError.text = "La confirmación de contraseña es requerida"
            isValid = false
        } else if (contras != confirmContra) {
            contraError.visibility = View.VISIBLE
            contraError.text = "Las contraseñas no coinciden"
            isValid = false
        } else {
            contraError.visibility = View.VISIBLE
            contraError.text = ""
        }

        if (isValid) {
            // Todos los campos son válidos, así que llamamos a la función para realizar el registro
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    peticionRegistrase()
                    // Corregir que no lo deje entrar, asta activar la cuenta
                    val intent = Intent(this@Registrarse, FrmPrincipal::class.java)
                    startActivity(intent)
                    finish()
                } catch (error: Exception) {
                    // Manejo de errores
                    Toast.makeText(this@Registrarse, "Error en el registro: $error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //Funciones
    suspend fun peticionRegistrase(){
        var url=config.urlCuenta+"v1/signup/"

        //Obtener los valores de los campos del formulario
        var nombre = txtNombre.text.toString()
        var apellido = txtApellidos.text.toString()
        var correo = txtEmail.text.toString()
        var telefono = txtNum.text.toString()
        var password = txtContras.text.toString()
        var confirmPwd = txtConfirmContra.text.toString()

        if (password != confirmPwd){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            // Salir de la funcion ya que las contrasenas no conciden
            return
        }

        // Parámetros con la asignación al campo.
        // Se crea un objeto JSONObject y luego aplica una serie de operaciones a ese objeto dentro del bloque de código del apply
        var parametros = JSONObject().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("correo_electronico", correo)
            put("telefono", telefono)
            put("password", password)
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            parametros,
            {response ->
                val message = response.getString("message")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            },
            {error ->
                Toast.makeText(this, "Error en la solicitud: $error", Toast.LENGTH_SHORT).show()
            }
        )
        var queue= Volley.newRequestQueue(context)
        queue.add(request)

    }



}