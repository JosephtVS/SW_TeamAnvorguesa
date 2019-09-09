package com.evolum.pruebaarquitecturasw2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.core.StitchAppClient
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential
import org.bson.Document
import java.util.*
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.OnCompleteListener
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.format.DateUtils
import java.lang.StringBuilder
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(), View.OnClickListener
{
    private lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference

    lateinit var editTextTest: EditText
    lateinit var butSubmitTest: Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTest =  findViewById(R.id.etePrueba)
        butSubmitTest = findViewById(R.id.butPrueba)
        butSubmitTest.setOnClickListener(this)

        //Conectando a Firebase
        connectFirebaseDB()

        //Inicializando la instancia, se llama al metodo y se pasa el ID de la app
        Stitch.initializeDefaultAppClient(
            resources.getString(R.string.my_app_id)
        )

        //Se llama al metodo para obtener una referencia al cliente
        val stitchAppClient = Stitch.getDefaultAppClient()

        stitchAppClient.auth.loginWithCredential(AnonymousCredential())
            .addOnSuccessListener{
                /*Obtener una instancia de la clase RemoteMongoClient, y así poder interactuar
                con nuestro cluster MongoDB Atlas. */
                    val mongoClient = stitchAppClient.getServiceClient(
                        RemoteMongoClient.factory,
                        "mongodb-atlas"
                    )

                //Obtenemos una referencia a la DB y a la coleccion.
                val myCollection = mongoClient.getDatabase("Test")
                    .getCollection("test_collection")

                /*Creando un documento BSON, agregamos el campo 'user_id' con el valor del ID de su auth,
                y una marca del tiempo*/
                val myDocument = Document()
                myDocument["time"] = Date().time
                myDocument["user_id"] = it.id

                //Insertamos el documento llamando al metodo 'insertOne'.
                /*Debido a que se ejecuta de forma asíncrona, se necesitará otro detector de éxito
                para verificar si la operación de inserción se realizó correctamente.*/
                val insertTask = myCollection.insertOne(myDocument)
                insertTask.addOnCompleteListener{ task ->
                    if (task.isSuccessful)
                    {
                        Log.d(
                            "app", String.format(
                                "Documento insertado exitosamente con id %s",
                                task.result!!.insertedId
                            )
                        )
                    }
                    else
                    {
                        Log.e("app", "No se pudo insertar el documento con: ", task.exception)
                    }
                }

                /*Al llamar al método find() del objeto RemoteMongoCollection, podemos crear una consulta
                para encontrar los últimos cinco documentos creados por el usuario.*/
                val query = myCollection.find()
                    .sort(Document("time", -1))
                    .limit(5)

                //Creamos una lista mutable de objetos ' Document'
                val result = mutableListOf<Document>()

                /*Para ejecutar realmente la consulta, llamamos al metodo into(), que espera una lista como argumento.
                Como su nombre lo indica, carga los resultados de la consulta, que no son más que objetos 'Document'. */
                query.into(result).addOnSuccessListener{

                    val output = StringBuilder("Accedió a la app: \n\n")

                    result.forEach{
                        output.append(
                            DateUtils.getRelativeDateTimeString(
                                this@MainActivity,
                                it["time"] as Long,
                                DateUtils.SECOND_IN_MILLIS,
                                DateUtils.WEEK_IN_MILLIS,
                                0
                            )
                        ).append("\n")
                    }
                    tviQueryMongo.text = output
                }
            }
    }

    fun connectFirebaseDB()
    {
        database = FirebaseDatabase.getInstance()
        myRef = database.reference.child("NodeTest")
    }

    fun submitTextTest()
    {
        val sendTest: String = editTextTest.text.toString()
        myRef.setValue(sendTest)

    }

    override fun onClick(v: View?)
    {
        submitTextTest()
        Toast.makeText(this, "Presionado", Toast.LENGTH_SHORT).show()
    }
}
