package com.mitya.photosapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mitya.photosapp.users.UsersFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceCurrentFragment(UsersFragment.newInstance())
    }

    fun replaceCurrentFragment(newFragment: Fragment, addToBackStack: Boolean = false) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, newFragment)
            .apply { if (addToBackStack) addToBackStack(null) }
            .commit()
    }
}
