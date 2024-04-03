package com.example.gadgetsfuture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Recuperar_Contrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contrasena)

    }
    fun  inicioS(view: View) {
        val intent = Intent(this, InicioSesion::class.java)
        startActivity(intent)
        this@Recuperar_Contrasena.overridePendingTransition(
            R.anim.animate_slide_in_left,
            R.anim.animate_slide_out_right
        )
    }
}