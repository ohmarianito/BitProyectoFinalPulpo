package com.bit.bitproyectofinalpulpo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bit.bitproyectofinalpulpo.fragments.HomeFragment
import com.bit.bitproyectofinalpulpo.fragments.ProfileFragment
import com.bit.bitproyectofinalpulpo.fragments.StoreFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_store.*

//private lateinit var adapter:StoreAdapter

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        //Navegacion fragments
        val homeFragment = HomeFragment()
        val storeFragment = StoreFragment()
        val profileFragment = ProfileFragment()

        //obtengo mail del activity y lo paso al fragment de profile
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val args = Bundle()
        //println("other message " + email)
        args.putString("email", email)
        profileFragment.arguments = args
        storeFragment.arguments = args


        makeFragment(homeFragment)


        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> makeFragment(homeFragment)
                R.id.store -> makeFragment(storeFragment)
                R.id.profile -> makeFragment(profileFragment)
            }
            true
        }



        start()
    }

    private fun makeFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer , fragment)
            commit()
        }

    private fun start(){
        title = "A pool of polls"
    }

}