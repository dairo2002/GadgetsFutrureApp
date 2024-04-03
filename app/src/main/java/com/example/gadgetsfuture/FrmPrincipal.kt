package com.example.gadgetsfuture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class FrmPrincipal : AppCompatActivity() {


    private  lateinit var bottomNavigationView: BottomNavigationView
    private  lateinit var cart: Cart_fragment
    private  lateinit var home: Home_fragment
    private  lateinit var store: store_fragment
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

        // Badge que muestra la cantidad de los productos
        val badge = bottomNavigationView.getOrCreateBadge(R.id.cart)
        badge.isVisible = true
        badge.number = 5

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

}