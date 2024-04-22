package com.example.gadgetsfuture

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gadgetsfuture.config.config
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import okhttp3.RequestBody.Companion.asRequestBody



class pago_Fragment : Fragment(R.layout.fragment_pago_) {

    private var pedido_id: Int = 0

    private lateinit var btnSelectImage: Button
    private lateinit var btnPagar: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var imgComprobante: ImageView

    private val SELECT_IMAGE_REQUEST_CODE = 1001
    private var selectedImageFile: File? = null

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pedido_id = it.getInt("pedido_id")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnPagar = view.findViewById(R.id.btnPagar)
        btnSelectImage = view.findViewById(R.id.btnSelectImage)
        radioGroup = view.findViewById(R.id.rdGrupo)
        imgComprobante = view.findViewById(R.id.imgComprobante)

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE)
        }

        btnPagar.setOnClickListener {
            GlobalScope.launch {
                realizarPago()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val inputStream = requireContext().contentResolver.openInputStream(data.data!!)
            val tempFile = createTempFile("temp_image", null, requireContext().cacheDir)
            selectedImageFile = tempFile.apply {
                inputStream?.use { input ->
                    outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
            inputStream?.close()
            imgComprobante.setImageBitmap(BitmapFactory.decodeFile(selectedImageFile?.path))
        }
    }



    suspend fun realizarPago() {
        val radioButtonId = radioGroup.checkedRadioButtonId
        val radioButton = view?.findViewById<RadioButton>(radioButtonId)
        val metodoPago = radioButton?.text.toString()
        if (metodoPago.isEmpty() || selectedImageFile == null) {
            requireActivity().runOnUiThread {
                Toast.makeText(
                    requireContext(),
                    "Seleccione un método de pago e imagen",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return
        }

        selectedImageFile?.let { imageBytes ->
            try {
                val url = config.urlPedido + "v1/pago/$pedido_id/"
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "comprobante",
                        "comprobante.jpg",
                        selectedImageFile!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )
                    .addFormDataPart("metodo_pago", metodoPago)
                    .build()


                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer ${config.token}")
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Toast.makeText(requireContext(),"$responseBody",Toast.LENGTH_LONG).show()
                    val transaction=requireFragmentManager().beginTransaction()
                    var fragmento=Home_fragment()
                    transaction.replace(R.id.container, fragmento)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    /*val intent = Intent(requireContext(), Home_fragment::class.java)
                    startActivity(intent)
                    activity.finish()*/
                } else {
                    // Manejar error de la solicitud
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            context,
                            "Error en la solicitud: ${response.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                // Manejar excepción
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        context,
                        "Error al realizar el pago: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }



}


/*
private fun convertBitmapToBase64(bitmapBytes: ByteArray, format: Bitmap.CompressFormat): String {
    val bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.size)
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(format, 100, byteArrayOutputStream)
    val bytes = byteArrayOutputStream.toByteArray()
    return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
        val inputStream = requireContext().contentResolver.openInputStream(data.data!!)
        selectedImageBytes = inputStream?.readBytes()
        inputStream?.close()
        imgComprobante.setImageBitmap(BitmapFactory.decodeByteArray(selectedImageBytes, 0, selectedImageBytes?.size ?: 0))
    }
}
 */