package com.techstrum.myapplication.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.techstrum.myapplication.ui.home.HomeActivity
import com.techstrum.myapplication.R
import com.techstrum.myapplication.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    var email_register:EditText?=null
    var username_register:EditText?=null
    var password_register:EditText?=null
    var passwordRe_register:EditText?=null
    var button_register:Button?=null
    var already_register:TextView?=null

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        email_register= findViewById<EditText>(R.id.email_register)
        username_register= findViewById<EditText>(R.id.username_register)
        password_register= findViewById<EditText>(R.id.password_register)
        passwordRe_register= findViewById<EditText>(R.id.passwordRe_register)
        button_register= findViewById<Button>(R.id.button_register)
        already_register= findViewById<TextView>(R.id.already_register)

        auth = Firebase.auth

        already_register!!.setOnClickListener {
            val intent = Intent(applicationContext,
                LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        button_register!!.setOnClickListener { login() }

    }

    fun login(){
        val email:String=email_register!!.text.toString()
        val password:String=password_register!!.text.toString()
        val rePassword:String=passwordRe_register!!.text.toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_register!!.error = "enter a valid email address"
            return
        }
        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            password_register!!.error = "between 4 and 10 alphanumeric characters"
            return
        }
        if (!password.equals(rePassword)){
            passwordRe_register!!.error="Password Do Not Match "
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("STATUS", "createUserWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(applicationContext,
                        HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Status", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

                // ...
            }


    }
}