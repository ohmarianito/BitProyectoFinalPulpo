package com.bit.bitproyectofinalpulpo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bit.bitproyectofinalpulpo.fragments.HomeFragment
import com.bit.bitproyectofinalpulpo.fragments.ProfileFragment
import com.bit.bitproyectofinalpulpo.fragments.StoreFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Navegacion fragments
        val homeFragment = HomeFragment()
        val storeFragment = StoreFragment()
        val profileFragment = ProfileFragment()
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