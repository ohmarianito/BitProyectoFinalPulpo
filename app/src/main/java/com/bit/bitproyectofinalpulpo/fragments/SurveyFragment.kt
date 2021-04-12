package com.bit.bitproyectofinalpulpo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bit.bitproyectofinalpulpo.R
import kotlinx.android.synthetic.main.fragment_survey.*


class SurveyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_survey, container, false)

        view.findViewById<TextView>(R.id.textViewRespVacia).visibility = View.INVISIBLE

        val encuestaId = requireArguments().getString("encuestaId")
        //println("other message " + encuestaId)
        view.findViewById<TextView>(R.id.textViewPruebaID).text = "id de la encuesta: " + encuestaId

        if (encuestaId != null) {
            setPregunta(view, encuestaId, 1)
            siguienteResp(view, encuestaId)
        }

        return view
    }

    private fun siguienteResp(view: View, encuestaId: String){
        val btnEnviarEncuesta  = view.findViewById<View>(R.id.buttonEnviarRespuesta) as Button
        btnEnviarEncuesta.setOnClickListener(){

            val radioButtonID: Int = radioGroupRespUno.checkedRadioButtonId
            if (radioButtonID > 0) {
                view.findViewById<TextView>(R.id.textViewRespVacia).visibility = View.INVISIBLE

                val radioButton: View = radioGroupRespUno.findViewById(radioButtonID)
                //aca obtengo el indice QUE VA DEL 0 al 3 SON 4 POSICIONES

                val idx: Int = radioGroupRespUno.indexOfChild(radioButton)

                val selectedRadioButton = view.findViewById<RadioButton>(radioButtonID)

                val textoDelRadio: String = selectedRadioButton.text.toString()

                //println("other message RADIOBUTTON idx " + idx)
                //println("other message RADIOBUTTON textoDelRadio " + textoDelRadio)

                //Consulto el numero de pregunta que voy a tener q IR a buscar de la encuesta
                val nroPregunta = view.findViewById<TextView>(R.id.textViewNroPregunta).text.toString()

                guardarRespuesta(encuestaId, idx)

                //controlo ultima pregunta y muestro finalizar o siguiente pregunta
                // el numero despues del == es siempre +1  la cantidad de preguntas
                // es decir que si pongo == 11 son 10 preguntas
                if ((nroPregunta.toInt() +1) == 3){
                    val fragment = HomeFragment()
                    val fragmentManager = activity!!.supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }else{
                    setPregunta(view, encuestaId, nroPregunta.toInt() +1)
                }
            }else{
                view.findViewById<TextView>(R.id.textViewRespVacia).visibility = View.VISIBLE
            }
        }
    }
    private fun setPregunta(view: View, encuestaId: String, proximaPregu: Int){
        println("other message PREGUNTA NUMEROOO " + proximaPregu)
        //voy a buscar la pregunta a firebase y seteo los campos con el numero de pregunta
        view.findViewById<TextView>(R.id.textViewPregUno).text = "esto viene de firebase con la pregunta: " + proximaPregu
        view.findViewById<RadioButton>(R.id.radioButtonUno).text = "PRUEBA" + proximaPregu
        view.findViewById<RadioButton>(R.id.radioButtonDos).text = "PRUEBA" + proximaPregu
        view.findViewById<RadioButton>(R.id.radioButtonTres).text = "PRUEBA" + proximaPregu
        view.findViewById<RadioButton>(R.id.radioButtonCuatro).text = "PRUEBA" + proximaPregu
        view.findViewById<RadioButton>(R.id.radioButtonCuatro).text = "PRUEBA" + proximaPregu
        view.findViewById<TextView>(R.id.textViewNroPregunta).text = proximaPregu.toString()
    }
    private fun guardarRespuesta(encuestaId: String, respuesta: Int){
        println("other message GUARDO LA RESPUESTA EN FIREBASE " + encuestaId + " -- " + respuesta)
    }

}