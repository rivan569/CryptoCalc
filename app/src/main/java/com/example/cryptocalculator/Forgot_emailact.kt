package com.example.cryptocalculator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.cryptocalculator.databinding.ActivityForgotEmailactBinding
import com.example.cryptocalculator.databinding.ActivityForgotPasswordBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint ("CheckResult")
class Forgot_emailact : AppCompatActivity() {


    private lateinit var binding: ActivityForgotEmailactBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotEmailactBinding.inflate(layoutInflater)
        setContentView(binding.root)
//auth
        auth = FirebaseAuth.getInstance()


val user = auth.currentUser



        binding.resEmail.setOnClickListener {


        val newemail = binding.enterEmail2.text.toString()
            val password =  binding.enterPass1.text.toString()




    if (newemail.isEmpty()) {
        binding.enterEmail2.error = "New email cannot be empty"
        binding.enterEmail2.requestFocus()
        return@setOnClickListener
    }

    if (password.isEmpty()){
        binding.password.error = "Password Cannot be empty"
        binding.password.requestFocus()
        return@setOnClickListener
    }
      if (!Patterns.EMAIL_ADDRESS.matcher(newemail).matches()) {
         binding.password.error = "Invalid Email"
          binding.password.requestFocus()
          return@setOnClickListener
      }
            if (!Patterns.EMAIL_ADDRESS.matcher(newemail).matches()) {
                binding.enterEmail2.error = "Invalid Email"
                binding.enterEmail2.requestFocus()
                return@setOnClickListener
            }



        user.let {
            val userCredential = EmailAuthProvider.getCredential(it?.email!!, password)

            it.reauthenticate(userCredential).addOnCompleteListener { Task ->
                when{
                    Task.isSuccessful -> {
                        user.let { it?.updateEmail(newemail)?.addOnCompleteListener {
                            if (it.isSuccessful){
                                val i = Intent(this, Loginpage::class.java)
                                Toast.makeText(
                                    this, "You change your Email Successfully!", Toast.LENGTH_SHORT
                                ).show()
                                startActivity(i)
                                finish()
                            }
                        }
                        }
                    }   Task.exception is FirebaseAuthInvalidCredentialsException -> {
                        binding.password.error = "wrong password"
                    binding.password.requestFocus()
                    }
                else -> {
                    Toast.makeText(this, "${Task.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                }
            }
        }
        }
        binding.tvBackLogin.setOnClickListener {
            startActivity(Intent(this, Loginpage::class.java))
        }
    }

}