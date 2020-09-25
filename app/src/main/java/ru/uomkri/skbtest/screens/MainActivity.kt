package ru.uomkri.skbtest.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ru.uomkri.skbtest.R
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var userPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpNavigation()

        val extras = intent.extras
        val username = extras!!.getString("username")
        val uid = extras.getString("uid")

        userPrefs = this.getSharedPreferences("userData", Context.MODE_PRIVATE)
        userPrefs.edit()
            .putString("username", username)
            .putString("uid", uid)
            .apply()

        val usernameView = findViewById<TextView>(R.id.username)
        val signOutButton = findViewById<AppCompatButton>(R.id.button_logout)

        usernameView.text = "Logged in as: $username"
        signOutButton.setOnClickListener {
            signOut()
        }
    }

    private fun setUpNavigation() {
        val navBar = findViewById<BottomNavigationView>(R.id.bottomNavBar)
        val hostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(navBar, hostFragment.navController)
    }

    private fun signOut() {
        firebaseAuth.signOut()
        userPrefs.edit()
            .clear()
            .apply()

        val intent = Intent(applicationContext, AuthActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}