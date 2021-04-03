package com.bit.bitproyectofinalpulpo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_authentication.*

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        //GOOGLE ANALYTICS con Eventos personalizados
        //ver mas en la doc de firebase/analytics
        //analytics/events
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Se integró con firebase")
        analytics.logEvent("InitScreen", bundle)


        start()

    }

    //metodo que se llama en pantalla login
    //se puede mejorar codigo
    private fun start(){
        title = "Autenticación"


        //ACA SE MANEJA LA CREACION DE USUARIO
        signUpButton.setOnClickListener{
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.text.toString(),passwordEditText.text.toString())
                    .addOnCompleteListener{

                        if (it.isSuccessful){
                            showHome(it.result?.user?.email ?:"")
                        } else {
                            //ERROR
                            showMessage()
                        }

                    }
            }
        }

        //ACA SE MANEJA LA LOGIN DE USUARIO
        loginButton.setOnClickListener{
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){

                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.text.toString(),passwordEditText.text.toString())
                    .addOnCompleteListener{

                        if (it.isSuccessful){
                            showHome(it.result?.user?.email ?:"")
                        } else {
                            //ERROR
                            showMessage()
                        }

                    }
            }
        }

    }

    // redirige a la nueva activity y pasa parametro email o setear otros
    private fun showHome(email: String){
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(homeIntent)
    }

    //Muestra msj Error
    private fun showMessage(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error al autenticar")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}