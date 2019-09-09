package com.evolum.pruebaarquitecturasw2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.core.StitchAppClient
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential

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

        Stitch.initializeDefaultAppClient(
            resources.getString(R.string.my_app_id)
        )

        val stitchAppClient = Stitch.getDefaultAppClient()

        stitchAppClient.auth.loginWithCredential(AnonymousCredential())
            .addOnSuccessListener{

            }



        connectFirebaseDB()
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
