import android.widget.Button
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gadgetsfuture.R
import com.example.gadgetsfuture.config.config
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale

class adapterCarrito(var context: Context?, var listaCarrito: JSONArray) :
    RecyclerView.Adapter<adapterCarrito.MyHolder>() {

    interface TotalListener {
        fun onTotalUpdated(total: Double)
    }

    var totalListener: TotalListener? = null
    var totalValue: Double = 0.0

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

        if (nombre.length >= 30) {
            nombre = nombre.substring(0, 29) + "..."
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
            totalValue = calcularTotal()
            totalListener?.onTotalUpdated(totalValue)
        }

        holder.btnRestar.setOnClickListener {
            restarCantidad(holder.txtCantidad)
            totalValue = calcularTotal()
            totalListener?.onTotalUpdated(totalValue)
        }

        holder.btnEliminarCarrito.setOnClickListener {
            onclickEliminar?.invoke(carrito)
            totalValue = calcularTotal()
            totalListener?.onTotalUpdated(totalValue)
        }
    }

    private fun calcularTotal(): Double {
        var total = 0.0
        for (i in 0 until listaCarrito.length()) {
            val carrito = listaCarrito.getJSONObject(i)
            val cantidad = carrito.getInt("cantidad")
            val precio = carrito.getDouble("precio")
            total += cantidad * precio
        }
        return total
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


    override fun getItemCount(): Int {
        return listaCarrito.length()
    }

}
