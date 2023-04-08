package br.com.buscacep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edtCEP = findViewById<EditText>(R.id.edtCep)
        val edtRua = findViewById<EditText>(R.id.edtRua)
        val edtBairro = findViewById<EditText>(R.id.edtBairro)

        edtCEP.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                val cep = s.toString()
                if(cep.length == 8){
                    buscarCEP(cep)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })
    }

    fun buscarCEP(cep: String){

        Thread(Runnable {

        val url = URL("https://viacep.com.br/ws/$cep/json/")

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = connection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))

            val response = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                response.append(line)
            }
            bufferedReader.close()

            val enderecoJson = JSONObject(response.toString())
            val rua = enderecoJson.getString("logradouro")
            val bairro = enderecoJson.getString("bairro")

            val edtRua: EditText = findViewById(R.id.edtRua)
            val edtBairro: EditText = findViewById(R.id.edtBairro)
            edtRua.setText(rua)
            edtBairro.setText(bairro)


        } else {
            Toast.makeText(applicationContext, "CEP inválido", Toast.LENGTH_LONG).show()
        }

        connection.disconnect()

        }).start()

    }

}