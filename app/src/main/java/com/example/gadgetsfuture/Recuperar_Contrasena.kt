package com.example.gadgetsfuture

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gadgetsfuture.config.config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class Recuperar_Contrasena : AppCompatActivity() {

    lateinit var context: Context
    private lateinit var  txtRecuperarPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contrasena)
        context=applicationContext

        txtRecuperarPassword = findViewById(R.id.txtRecuperarPassword)

        val btnRecuperarPwd = findViewById<Button>(R.id.btnRecuperarPwd)
            btnRecuperarPwd.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        peticionRecuperarPwd()
                        /*
                        val intent = Intent(this@Recuperar_Contrasena, FrmPrincipal::class.java)
                        startActivity(intent)
                        finish()*/
                    } catch (error: Exception) {
                        Toast.makeText(this@Recuperar_Contrasena, "Error en el registro: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    } 

    fun  inicioS(view: View) {
        val intent = Intent(this, InicioSesion::class.java)
        startActivity(intent)
        this@Recuperar_Contrasena.overridePendingTransition(
            R.anim.animate_slide_in_left,
            R.anim.animate_slide_out_right
        )
    }

    suspend fun peticionRecuperarPwd(){
        var  url=config.urlCuenta+"v1/recover_password/"

        val correo = txtRecuperarPassword.text.toString()

        var parametro = JSONObject().apply {
            put("correo_electronico", correo)
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            parametro,
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