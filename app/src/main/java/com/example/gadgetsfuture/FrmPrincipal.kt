package com.example.gadgetsfuture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gadgetsfuture.config.config
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class FrmPrincipal : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var cart: Cart_fragment
    private lateinit var home: Home_fragment
    private lateinit var store: store_fragment
    private lateinit var user: UserFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frm_principal)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Instancias
        cart = Cart_fragment()
        home = Home_fragment()
        store = store_fragment()
        user = UserFragment()

        supportFragmentManager.beginTransaction().replace(R.id.container, home).commit()

        GlobalScope.launch(Dispatchers.Main) {
            try {
                contador_productos()

            } catch (error: Exception) {
                Toast.makeText(
                    applicationContext,
                    "Error en el servidor",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, home).commit()
                    true
                }

                R.id.cart -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, cart).commit()
                    true
                }

                R.id.store -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, store).commit()
                    true
                }
                R.id.user -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, user).commit()
                    true
                }

                // Agrega más casos para otras opciones del menú si es necesario
                else -> false
            }
        }
    }

    suspend fun contador_productos() {
        val url = config.urlCarrito + "v1/contar_productos/"
        val queue = Volley.newRequestQueue(applicationContext)

        val request = object : JsonObjectRequest(
            Method.GET,
            url,
            null,
            { response ->
                try {
                    val contador = response.getInt("contador")
                    //Badge que muestra la cantidad de los productos
                    val badge = bottomNavigationView.getOrCreateBadge(R.id.cart)
                    badge.isVisible = true
                    badge.number = contador
                } catch (e: NumberFormatException) {
                    // Handle specific NumberFormatException if the response is not an integer
                    Toast.makeText(applicationContext, "Error: Invalid response format", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Toast.makeText(applicationContext, "Error: $error", Toast.LENGTH_LONG).show()
            }
        ) {
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