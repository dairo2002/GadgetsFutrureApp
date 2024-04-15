package com.example.gadgetsfuture

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.ActivityNavigator
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gadgetsfuture.config.config
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class InicioSesion : AppCompatActivity() {
    lateinit var correoEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginButton: Button
    lateinit var passwordError: TextView
    lateinit var context:Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
        setContentView(R.layout.activity_inicio_sesion)
        context=applicationContext

        MostrarContrasena()
        correoEditText = findViewById(R.id.txtCorreo)
        passwordEditText = findViewById(R.id.txtContra)
        loginButton = findViewById(R.id.btnIniciarS)
        passwordError = findViewById(R.id.passwordError)

        // Verificar si las credenciales están guardadas
        if (checkCredentials()) {
            // Si las credenciales están guardadas, iniciar la actividad principal
            val intent = Intent(this, FrmPrincipal::class.java)
            startActivity(intent)
            finish()
        }

        // Forzar el modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        delegate.applyDayNight()
    }

    fun olvideContra(view: View) {
        val olvideContra = Intent(this, Recuperar_Contrasena::class.java)
        startActivity(olvideContra)
        this@InicioSesion.overridePendingTransition(
            R.anim.animate_slide_left_enter,
            R.anim.animate_slide_left_exit
        )
    }

    fun registrarse(view: View) {
        val regis = Intent(this, Registrarse::class.java)
        startActivity(regis)
        finish()
        this@InicioSesion.overridePendingTransition(
            R.anim.animate_slide_left_enter,
            R.anim.animate_slide_left_exit
        )
        
    }

    fun inicioSesion(view: View) {
        GlobalScope.launch {
        if (login()) {
            // Inicio de sesión exitoso
            val intent = Intent(context, FrmPrincipal::class.java)
            startActivity(intent)
            finish()
        } else {
            passwordError.visibility = View.VISIBLE
            passwordError.text = "Correo o contraseña incorrecta"
        }
        }
    }

    //private suspend fun login():Boolean{
    private suspend fun login():Boolean{
        var peticion:Boolean = false
        var inicioExitoso:Boolean = false
        val username = correoEditText.text.toString()
        val password = passwordEditText.text.toString()
        //var queue= Volley.newRequestQueue(context)
        var queue= Volley.newRequestQueue(applicationContext)
        var url=config.urlCuenta+"v1/login/"
        //preparar los parametros
        var parametros= JSONObject().apply {
            put("correo_electronico", username)
            put("password", password)
        }
        //responde un: json = JsonRequest , String= StringRequest
        val request= object : JsonObjectRequest (
            Request.Method.POST,
            url,
            parametros,
            {response->
                val message = response.getString("message")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                inicioExitoso = true
                peticion = true

                // Obtener el token de la respuesta y guardarlo
                val token = response.getString("token")
                config.token = token
                //saveCredentials(username, password) // Guardar las credenciales
            },
            {error->
                inicioExitoso = false
                peticion = true
            }
        ){
            override fun getHeaders(): MutableMap<String, String>{
                val headers = HashMap<String, String>()
                // Agregar el token JWT si está disponible
                if (config.token.isNotEmpty()){
                    headers["Authorization"] = "Bearer ${config.token}"
                }
                return headers
            }
        }
        queue.add(request)

        while (peticion == false){
            delay(500)
        }
        return inicioExitoso
    }

    // Función para guardar las credenciales
    private fun saveCredentials(username: String, password: String) {
        config.SharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = config.SharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }

    // Función para verificar si las credenciales están guardadas
    private fun checkCredentials(): Boolean {
        config.SharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val username = config.SharedPreferences.getString("username", null)
        val password = config.SharedPreferences.getString("password", null)
        return !username.isNullOrEmpty() && !password.isNullOrEmpty()
    }

    private fun MostrarContrasena(){
        val checkBoxMostrarContrasena: CheckBox = findViewById(R.id.checkBoxMostrarContrasena)
        val password: EditText = findViewById(R.id.txtContra)

        checkBoxMostrarContrasena.setOnCheckedChangeListener() {buttonView, isChecked ->
            if (isChecked) {
                password.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                password.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }
}


/*
lass MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    lateinit var registro : Button
    lateinit var txtUsername: TextInputEditText
    lateinit var txtPassword: TextInputEditText
    lateinit var registraraqui: TextView
    lateinit var  recuperarcntra : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btningreso.setOnClickListener { }
        setContentView(R.layout.activity_main)
        txtUsername=findViewById<TextInputEditText>(R.id.texemail)
        txtPassword=findViewById<TextInputEditText>(R.id.texpassword)
        registraraqui=findViewById<TextInputEditText>(R.id.linkregistrar)
        recuperarcntra=findViewById<TextView>(R.id.linkcontrasena)
        recuperarcntra.setOnClickListener {
            var intent =
                Intent(this, olvidar_contrasena::class.java)
            startActivity(intent)
        }
        registraraqui.setOnClickListener {
            var intent =
                Intent(this, registrar::class.java)
            startActivity(intent)
        }

        registro=findViewById(R.id.btningreso)
        registro.setOnClickListener{

            peticionIngresar()

        }

    }

    //aca funcion
    fun peticionIngresar() {
        // validar el formulario
        if (validarFormulario()) {
            // hacer la petición
            // Aquí deberías realizar la lógica de la petición
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    ingresarCliente()
                } catch (error: Exception) {
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("error en l apeticon")
                        .setMessage("error")
                        .setPositiveButton("Aceptar") { dialog, which ->
                        }
                    val dialog = builder.create()
                    dialog.show()
                } finally {
                }
            }
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("inicio de sesion incompleto.")
                .setMessage("Los campos de correo y contraseña con obligatorios")
                .setPositiveButton("Aceptar") { dialog, which ->
                }
            val dialog = builder.create()
            dialog.show()
        }
    }
    fun validarFormulario(): Boolean {
        var valido = true
        valido = validarCampo(txtUsername, "Usuario", 30)
                && validarCampo(txtPassword, "Contraseña", 30)
        return valido
    }
    fun validarCampo(editText: EditText, nombreCampo: String, longitudMaxima: Int = -1): Boolean {
        val valor = editText.text.toString().trim()
        if (valor.isEmpty()) {
            editText.error = "$nombreCampo es requerido"
            return false
        }
        if (longitudMaxima != -1 && valor.length > longitudMaxima) {
            editText.error = "$nombreCampo no puede tener más de $longitudMaxima caracteres"
            return false
        }
        return true
    }

    suspend fun ingresarCliente() {
        val url = config().login + "/login"
        val queue = Volley.newRequestQueue(applicationContext)
        // Se preparan los parámetros
        val parametros = JSONObject()
        parametros.put("username", txtUsername.text)
        parametros.put("password", txtPassword.text)
        val stringRequest = object : JsonObjectRequest(
            Method.POST,
            url,
            parametros,
            { response ->
                // Manejar la respuesta del servidor aquí
                try {
                    val token = response.getString("token") // Suponiendo que el token se devuelva en la respuesta como "token"
                    // Guardar el token en tu clase config
                    config.token = token
                    var intent =
                        Intent(this, ingresar::class.java)
                    startActivity(intent)
                    // Aquí puedes realizar cualquier acción adicional que necesites con el token
                } catch (e: JSONException) {
                    Toast.makeText(
                        applicationContext,
                        "Error al obtener el token",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            { error ->
                val builder = AlertDialog.Builder(this)
                // Configura el título y el mensaje del diálogo
                builder.setTitle("Usuario o contraseña incorrecta.")
                    .setMessage("Digite correctamente el correo y la contraseña. ")
                    // Agrega botones positivos y su comportamiento
                    .setPositiveButton("Aceptar") { dialog, which ->
                        // Aquí puedes poner el código que quieras ejecutar cuando se haga clic en "Cancelar"
                    }
                val dialog = builder.create()
                // Muestra el diálogo
                dialog.show()
            }
        ) {
            // Se agregan los encabezados de la solicitud, incluyendo el token JWT si está disponible
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                // Agregar el token JWT si está disponible
                if (config.token.isNotEmpty()) {
                    headers["Authorization"] = "Bearer ${config.token}"
                }
                return headers
            }
        }

        queue.add(stringRequest)




}
 */