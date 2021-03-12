package com.bit.bitproyectofinalpulpo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bit.bitproyectofinalpulpo.R
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    //private fun start(email: String){
      //  title = "Mi perfil"
        //emailtextView.text = email

        //logoutButton.setOnClickListener{
          //  FirebaseAuth.getInstance().signOut()
           // onBackPressed()
        //}
    //}
}