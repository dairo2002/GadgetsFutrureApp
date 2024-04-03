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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.ActivityNavigator
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gadgetsfuture.config.config
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
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

    private suspend fun login():Boolean{
        var peticion:Boolean = false
        var inicioExitoso:Boolean = false
        val username = correoEditText.text.toString()
        val password = passwordEditText.text.toString()
        var url=config.urlCuenta+"v1/login/"
        //preparar los parametros
        var parametros= JSONObject().apply {
            put("correo_electronico", username)
            put("password", password)
        }
        //hace la petición

        //sí la api responde un json = JsonRequest
        //si responde un String= StringRequest
        val request=JsonObjectRequest (
            Request.Method.POST,
            url,
            parametros,
            {response->
                //cuando la respuesta es positiva
                val message = response.getString("message")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                inicioExitoso = true
                peticion = true
                saveCredentials(username, password) // Guardar las credenciales
            },
            {error->
                inicioExitoso = false
                peticion = true
            }

        )

        var queue= Volley.newRequestQueue(context)
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
