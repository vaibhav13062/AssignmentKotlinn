package com.techstrum.myapplication.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.techstrum.myapplication.ui.home.HomeActivity
import com.techstrum.myapplication.R
import com.techstrum.myapplication.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    var email_login: EditText?=null
    var password_login:EditText?=null
    var button_login: Button?=null
    var notRegistered_login: TextView?=null
    var forgotPassword_login:TextView?=null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email_login= findViewById<EditText>(R.id.username_login)
        password_login= findViewById<EditText>(R.id.password_login)
        button_login= findViewById<Button>(R.id.button_login)
        forgotPassword_login= findViewById<TextView>(R.id.already_register)
        notRegistered_login= findViewById<TextView>(R.id.notRegistered_login)
        auth = Firebase.auth
        val user = auth.currentUser

        if (user!=null){
            val intent = Intent(applicationContext,
                HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        button_login!!.setOnClickListener { login()}
        notRegistered_login!!.setOnClickListener {
            val intent = Intent(applicationContext,
                RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
    fun login(){
        val email:String=email_login!!.text.toString()
        val password:String=password_login!!.text.toString()
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_login!!.error = "enter a valid email address"
            return
        }
        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            password_login!!.error = "between 4 and 10 alphanumeric characters"
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(applicationContext,
                        HomeActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                    // ...
                }

                // ...
            }

    }
}