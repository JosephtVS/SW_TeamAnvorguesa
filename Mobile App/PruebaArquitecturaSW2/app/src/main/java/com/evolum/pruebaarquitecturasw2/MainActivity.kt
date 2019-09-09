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

        Stitch.initializeDefaultAppClient(
            resources.getString(R.string.my_app_id)
        )

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
                val myCollection = mongoClient.getDatabase("test")
                    .getCollection("test_collection")

                //Creando un documento y agregar el campo 'user_id' con el valor del ID de su auth.
                val myDocument = Document()
                myDocument["time"] = Date().time
                myDocument["user_id"] = it.id

                val insertTask = myCollection.insertOne(myDocument)
                insertTask.addOnCompleteListener(OnCompleteListener<RemoteInsertOneResult> { task ->
                    if (task.isSuccessful) {
                        Log.d(
                            "app", String.format(
                                "successfully inserted item with id %s",
                                task.result!!.insertedId
                            )
                        )
                    } else {
                        Log.e("app", "failed to insert document with: ", task.exception)
                    }
                })
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

    /*fun connectMongoDB()
    {
        //Inicializando la instancia, se llama al metodo y se pasa el ID de la app
        Stitch.initializeAppClient(
            resources.getString(R.string.my_app_id)
        )

        //Se llama al metodo para obtener una referencia al cliente
        stitchAppClient = Stitch.getDefaultAppClient()
    }

    fun loginUser()
    {
        stitchAppClient.auth.loginWithCredential(AnonymousCredential())
            .addOnSuccessListener{

            }
    }*/

    override fun onClick(v: View?)
    {
        submitTextTest()
        Toast.makeText(this, "Presionado", Toast.LENGTH_SHORT).show()
    }
}
