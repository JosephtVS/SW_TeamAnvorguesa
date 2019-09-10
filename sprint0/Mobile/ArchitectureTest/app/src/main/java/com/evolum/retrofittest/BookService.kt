package com.evolum.retrofittest

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BookService
{
    //Primera llamada, le pasamos la URL
    @GET("books/")
    fun getAllBooks(): Call<List<Book>>

    /*Segunda llamada, le pasamos el URL pero esta vez con un parámetro 'id', para que nos devuelva
    solo ese objeto. Para que reconozca el parámetro se tiene que usar la anotación''Path¿ antes de la variable*/
    @GET("books/{id}")
    fun getBookById(@Path("id") id: Int): Call<Book>

    /*Como parámetro le pasamos la variable del objeto que queremos enviar, es decir Book, para que
    nos reconozca Book como un objeto que se enviará se tiene que usa la anotación 'Body' */
    @POST("books/{id}")
    fun editBookById(@Path("id") id: Int, @Body book: Book?): Call<Book>
}