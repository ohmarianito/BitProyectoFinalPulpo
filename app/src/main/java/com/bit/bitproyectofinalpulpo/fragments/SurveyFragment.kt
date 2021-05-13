package com.bit.bitproyectofinalpulpo.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bit.bitproyectofinalpulpo.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_survey.*
import kotlinx.android.synthetic.main.survey_complete.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class SurveyFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    var preguntas : HashMap<Int, String>
            = HashMap<Int, String> ()
    var coinsEnJuego = 0
    var coinsDelUsuario = Long.MIN_VALUE

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_survey, container, false)

        view.findViewById<TextView>(R.id.textViewRespVacia).visibility = View.INVISIBLE

        val encuestaId = requireArguments().getString("encuestaId")
        val encuestaTitulo = requireArguments().getString("encuestaTitulo")
        //val cantMonedas: String? =
          //  encuestaTitulo?.substringAfterLast(delimiter = "(", missingDelimiterValue = "Extension Not found")
            //    ?.replace(")","")

        //println("other message CANTIDAD MONEDAS " + cantMonedas)
        println("other message LLEGA A PREGUNTAS ENCUESTAS " + encuestaId + " " + encuestaTitulo)
        view.findViewById<TextView>(R.id.textViewPruebaID).text = encuestaTitulo

        // obtengo mail
        val userEmail = requireArguments().getString("email").toString()
        //getCoinsFromFirebase(userEmail)


        // al cargar el fragment va a buscar la info
        // se dejo aca porque si se mueve la mierda esa no actualiza
        db.collection("usuarios").document(userEmail).get().addOnSuccessListener {
            coinsDelUsuario = it.get("monedas") as Long
        }

        if (encuestaId != null) {
            obtenerPreguntas(view, encuestaId)
            siguienteResp(view, encuestaId, userEmail)
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun siguienteResp(view: View, encuestaId: String, email: String){
        val btnEnviarEncuesta  = view.findViewById<View>(R.id.buttonEnviarRespuesta) as Button
        btnEnviarEncuesta.setOnClickListener(){

            val radioButtonID: Int = radioGroupRespUno.checkedRadioButtonId
            if (radioButtonID > 0) {
                view.findViewById<TextView>(R.id.textViewRespVacia).visibility = View.INVISIBLE

                val radioButton: View = radioGroupRespUno.findViewById(radioButtonID)
                //aca obtengo el indice QUE VA DEL 0 al 3 SON 4 POSICIONES

                val respuestaRadio: Int = radioGroupRespUno.indexOfChild(radioButton)

                val selectedRadioButton = view.findViewById<RadioButton>(radioButtonID)

                val textoDelRadio: String = selectedRadioButton.text.toString()

                //println("other message RADIOBUTTON idx " + idx)
                //println("other message RADIOBUTTON textoDelRadio " + textoDelRadio)

                //Consulto el numero de pregunta que voy a tener q IR a buscar de la encuesta
                val nroPregunta = view.findViewById<TextView>(R.id.textViewNroPregunta).text.toString()

                guardarRespuesta(encuestaId, respuestaRadio, email, nroPregunta)

                //controlo ultima pregunta y muestro finalizar o siguiente pregunta
                // el numero despues del == es siempre +1  la cantidad de preguntas
                // es decir que si pongo == 11 son 10 preguntas
                if (nroPregunta.toInt() == preguntas.size ){
                    updateMonedas(email)
                    val miDialogView  = LayoutInflater.from(this.context).inflate(R.layout.survey_complete, null)
                    val mBuilder = AlertDialog.Builder(this.context).setView(miDialogView).setTitle("¡FELICITACIONES!")
                    miDialogView.findViewById<TextView>(R.id.textViewModal).text = "Completaste la encuesta y generaste " + coinsEnJuego + " coins!"
                    val mAlertDialog  = mBuilder.show()
                    miDialogView.buttonModal.setOnClickListener(){
                        mAlertDialog.dismiss()
                        cerrarEncuesta(email)
                    }
                    miDialogView.buttonShare.setOnClickListener(){
                        val url = "https://api.whatsapp.com/send?phone=598111111&text=Prueba Pool of Polls y GANA!!!!"
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        startActivity(i)
                    }
                }else{
                    setPregunta(view, encuestaId, nroPregunta.toInt() +1)
                }
            }else{
                view.findViewById<TextView>(R.id.textViewRespVacia).visibility = View.VISIBLE
            }
        }
    }
    private fun updateMonedas(email: String){
        var coinsFinal = coinsDelUsuario.toInt() + coinsEnJuego
        db.collection("usuarios").document(email).set(
            hashMapOf(
                "monedas" to coinsFinal
            )
        )
    }
    private fun getCoinsFromFirebase(userEmail: String){
        // al cargar el fragment va a buscar la info
        println("other message coinsDelUsuario MAIL - " + userEmail)
        db.collection("usuarios").document(userEmail).get().addOnSuccessListener {
            //coinsDelUsuario = (it.get("monedas") as Long?).toString()
        }
        println("other message coinsDelUsuario coinsDelUsuario INICIOOOOO - " + coinsDelUsuario)
    }
    private fun obtenerPreguntas(view: View, encuestaId: String){

        db.collection("encuestas").document(encuestaId).get().addOnSuccessListener{
            coinsEnJuego = it.get("encuestaMonedas").toString().toInt()
        }

        db.collection("encuestas").document(encuestaId)
            .collection("encuestaPreguntas").get().addOnSuccessListener{document ->
                if (document != null) {
                    //println("other message TRAJO Preguntas ")
                    var i = 0
                    while ( i < document.documents.size){
                        //tamaño de preguntas
                        val idPregu : Int = document.documents.get(i).data?.get("encuestaPreguntaId").toString().toInt()
                        val pregu = document.documents.get(i).data?.get("encuestaPreguntaDescripcion").toString()
                        preguntas.put(idPregu, pregu)
                        i++
                    }
                    setPregunta(view, encuestaId, 1)
                } else {
                    println("other message ERROR AL TRAER PREGUNTA")
                }
            }
    }
    private fun setPregunta(view: View, encuestaId: String, proximaPregu: Int){
        view.findViewById<RadioButton>(R.id.radioButtonUno).isChecked = false
        view.findViewById<RadioButton>(R.id.radioButtonDos).isChecked = false
        view.findViewById<RadioButton>(R.id.radioButtonTres).isChecked = false
        view.findViewById<RadioButton>(R.id.radioButtonCuatro).isChecked = false
        radioGroupRespUno.clearCheck()

        db.collection("encuestas").document(encuestaId)
            .collection("encuestaPreguntas").document(proximaPregu.toString())
                .collection("encuestaPreguntaOpciones").get().addOnSuccessListener{ document ->
                if (document != null) {
                    println("other message PREGUNTA NUMEROOO " + proximaPregu)
                    //voy a buscar la pregunta a firebase y seteo los campos con el numero de pregunta
                    view.findViewById<TextView>(R.id.textViewPregUno).text = preguntas.get(proximaPregu)
                    view.findViewById<RadioButton>(R.id.radioButtonUno).text = document.documents.get(0).data?.get("encuestaPreguntaOpcionDsc").toString()
                    view.findViewById<RadioButton>(R.id.radioButtonDos).text = document.documents.get(1).data?.get("encuestaPreguntaOpcionDsc").toString()
                    view.findViewById<RadioButton>(R.id.radioButtonTres).text = document.documents.get(2).data?.get("encuestaPreguntaOpcionDsc").toString()
                    view.findViewById<RadioButton>(R.id.radioButtonCuatro).text = document.documents.get(3).data?.get("encuestaPreguntaOpcionDsc").toString()
                } else {
                    println("other message ERROR AL TRAER ReSPUESTAS")
                }
        }
        //println("other message LLEGAA ACAAC")
        view.findViewById<TextView>(R.id.textViewNroPregunta).text = proximaPregu.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun guardarRespuesta(encuestaId: String, respuesta: Int, email: String, nroPregu: String){
        //println("other message GUARDO LA RESPUESTA EN FIREBASE " + encuestaId + " -- " + respuesta)
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
        val formatted = now.format(formatter)
        println("other message RESPUESTAAAAAAA " + respuesta)
        val documentID = "$email-($nroPregu)-$formatted"
        db.collection("respuestas").document(documentID).set(
            hashMapOf(
                "encuestaId" to encuestaId,
                "encuestaPreguntaId" to nroPregu,
                "encuestaPreguntaOpcionId" to respuesta+1,
                "usuarioId" to email)
        )
    }

    private fun cerrarEncuesta(email: String){
        val args = Bundle()
        args.putString("email", email)
        println("other message LLEGA AL CERRARRRRR")
        val fragment = HomeFragment()
        fragment.arguments = args
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}