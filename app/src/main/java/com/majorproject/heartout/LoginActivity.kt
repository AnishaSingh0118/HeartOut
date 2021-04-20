package com.majorproject.heartout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        auth = FirebaseAuth.getInstance()


        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            signUpUser()

        }

    }

    private fun signUpUser() {
            val usernameInput = findViewById<EditText>(R.id.usernameInput)
            val passwordInput = findViewById<EditText>(R.id.passwordInput)
            if (usernameInput.text.toString().isEmpty()){
                usernameInput.error = "Please enter email"
                usernameInput.requestFocus()
                return
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(usernameInput.text.toString()).matches()) {
                usernameInput.error = "Please enter valid email"
                usernameInput.requestFocus()
                return
            }
            if (passwordInput.text.toString().isEmpty()){
                passwordInput.error= "please enter password"
                passwordInput.requestFocus()
                return
            }

            auth.createUserWithEmailAndPassword(usernameInput.text.toString(), passwordInput.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                            val user = auth.currentUser

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()

                        }
                    }
               }

          }
}