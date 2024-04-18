import android.widget.Button

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gadgetsfuture.Cart_fragment
import com.example.gadgetsfuture.R
import com.example.gadgetsfuture.config.config
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale

class adapterCarrito(var context: Context?, var listaCarrito: JSONArray) :
    RecyclerView.Adapter<adapterCarrito.MyHolder>() {

    inner class MyHolder(Item: View) : RecyclerView.ViewHolder(Item) {
        lateinit var lblNombre: TextView
        lateinit var imgProducto: ImageView
        lateinit var txtCantidad: TextView
        lateinit var lblPrecio: TextView
        lateinit var lblSubtotal: TextView
        lateinit var btnEliminarCarrito: ImageButton
        lateinit var btnSumar: Button
        lateinit var btnRestar: Button

        init {
            lblNombre = itemView.findViewById(R.id.lblNombreCart)
            imgProducto = itemView.findViewById(R.id.imgProductoCart)
            txtCantidad = itemView.findViewById(R.id.txtCantidadCart)
            lblPrecio = itemView.findViewById(R.id.lblPrecioCart)
            lblSubtotal = itemView.findViewById(R.id.lblSubtotalCart)
            btnEliminarCarrito = itemView.findViewById(R.id.btnEliminarCart)
            btnSumar = itemView.findViewById(R.id.btnMas)
            btnRestar = itemView.findViewById(R.id.btnMenos)
        }
    }

    interface TotalCalculadoListener {
        fun onTotalCalculado(total: Double)
    }

    private var totalCalculadoListener: TotalCalculadoListener? = null
    fun setTotalListener(listener: TotalCalculadoListener) {
        totalCalculadoListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_carro, parent, false)
        return MyHolder(itemView)
    }

    var onclick:((JSONObject)->Unit)?=null
    var onclickSuma:((JSONObject)->Unit)?=null
    var onclickResta:((JSONObject)->Unit)?=null
    var onclickEliminar:((JSONObject)->Unit)?=null
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val carrito = listaCarrito.getJSONObject(position)

        var idCarrito = carrito.getInt("id")
        var nombre = carrito.getString("producto")
        var cantidad = carrito.getInt("cantidad")
        val precio = carrito.getDouble("precio")
        var imagen = config.urlBase + carrito.getString("imagen")

        var subtotal = cantidad * precio
        var total = 0.0
        total += subtotal
        totalCalculadoListener?.onTotalCalculado(total)


        if (nombre.length >= 40) {
            nombre = nombre.substring(0, 39) + "..."
        }

        val formato = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        formato.maximumFractionDigits = 0
        val formatoPrecio = formato.format(precio)
        val formatoSubtotal = formato.format(subtotal)

        holder.lblNombre.text = nombre
        holder.txtCantidad.text = cantidad.toString()
        holder.lblPrecio.text = "$formatoPrecio"
        holder.lblSubtotal.text = "$formatoSubtotal"
        Glide.with(holder.itemView.context).load(imagen).into(holder.imgProducto)

        holder.btnSumar.setOnClickListener {
            sumarCantidad(holder.txtCantidad)
        }

        holder.btnRestar.setOnClickListener {
            restarCantidad(holder.txtCantidad)
        }

        holder.btnEliminarCarrito.setOnClickListener {
            //val carrito = listaCarrito.getJSONObject(position)
            //val idCarrito = carrito.getInt("id")
            onclickEliminar?.invoke(carrito)
            eliminarItemDelCarrito(idCarrito)
        }
    }

    private fun sumarCantidad(textView: TextView) {
        var cantidad = textView.text.toString().toIntOrNull() ?: 0
        cantidad++
        if (cantidad <= 99) {
            textView.text = cantidad.toString()
        }
    }

    private fun restarCantidad(textView: TextView) {
        var cantidad = textView.text.toString().toIntOrNull() ?: 0
        if (cantidad > 1) {
            cantidad--
            textView.text = cantidad.toString()
        }
    }

    private fun eliminarItemDelCarrito(id: Int) {
        GlobalScope.launch {
            try {
                (context as? Cart_fragment)?.eliminarCarrito(id)
            } catch (error: Exception) {
                Toast.makeText(context, "Error en la petición: $error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    interface OnCartItemDeleteListener {
        fun onCartItemDelete(productId: Int)
    }


    override fun getItemCount(): Int {
        return listaCarrito.length()
    }
}
