package com.evolum.retrofittest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getBooks()

    }

    /*Realizar petición de llamada. Creamos la instancia 'Retrofit' en donde le pasamos: la URL base,y el JSON converter
    ya que nosotros usamos Gson para parsear los datos */
    fun getBooks()
    {
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://cryptic-tor-28162.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Creamos el servicio para hacer las llamadas, y le pasamos la interfaz
        val bookService : BookService = retrofit.create<BookService>(BookService::class.java)

        bookService.getAllBooks().enqueue(object : Callback<List<Book>>
        {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>)
            {
              val books = response?.body()
                Log.i("BOOKS", Gson().toJson(books))
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable)
            {
                t?.printStackTrace()
            }
        })
    }
}
