package com.bit.bitproyectofinalpulpo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import com.bit.bitproyectofinalpulpo.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val btnEncuesta  = view.findViewById<View>(R.id.buttonEncuesta) as Button
        // obtengo mail
        val userEmail = requireArguments().getString("email")

        btnEncuesta.setOnClickListener(){
            val args = Bundle()
            val id = "1"
            val fragment = SurveyFragment()
            args.putString("encuestaId", id)
            args.putString("email", userEmail)
            fragment.arguments = args
            val fragmentManager = activity!!.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return view
    }
}