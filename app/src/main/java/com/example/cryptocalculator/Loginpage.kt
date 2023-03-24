package com.example.cryptocalculator

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.cryptocalculator.databinding.ActivityLoginpageBinding
import com.example.cryptocalculator.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint("CheckResult")

class Loginpage : AppCompatActivity() {
    private lateinit var binding: ActivityLoginpageBinding
    private lateinit var auth: FirebaseAuth
    @SuppressLint("SuspiciousIndentation", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginpageBinding.inflate(layoutInflater)
        setContentView(binding.root)



//Auth


        auth = FirebaseAuth.getInstance()




        val emailStream = RxTextView.textChanges(binding.enterEmail)
            .skipInitialValue()
            .map {
                    email ->
               email.isEmpty()
            }
        emailStream.subscribe {
            showTextMinimalAlert(it, "Email")
        }

        val passwordStream = RxTextView.textChanges(binding.enterPass)
            .skipInitialValue()
            .map {
                    password ->
                password.isEmpty()
            }
        passwordStream.subscribe {
            showTextMinimalAlert(it, "Password")
        }

        val invalidFieldStream = io.reactivex.Observable.combineLatest(
            emailStream,
            passwordStream,
            {  emailInvalid: Boolean, passwordInvalid: Boolean  ->
                 !emailInvalid && !passwordInvalid
            })
        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.btn2.isEnabled = true
                binding.btn2.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.white)
            } else {
                binding.btn2.isEnabled = false
                binding.btn2.backgroundTintList =
                    ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }
        binding.btn2.setOnClickListener {
            val email = binding.enterEmail.text.toString().trim()
            val password =binding.enterPass.text.toString().trim()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful){
                     val verification = auth.currentUser?.isEmailVerified
                        if (verification == true){
                               val user = auth.currentUser

                            updateUI(user)
                            }else
                                startActivity(Intent(this,Homeactivity::class.java))
                    } else {
                        Toast.makeText(baseContext, "Login Failed. please recheck your email and password", Toast.LENGTH_SHORT).show()

                    }
                }

        }


        binding.btn3.setOnClickListener {
            startActivity(Intent(this,Signupactivity::class.java))
        }

binding.forgotPw.setOnClickListener {
    startActivity(Intent(this, ForgotPasswordActivity::class.java))
}

       binding.forgotEm.setOnClickListener {
           startActivity(
               Intent(this, Forgot_emailact::class.java)
           )
       }

        }


private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
    if (text == "email")
        binding.enterEmail.error = if (isNotValid) "$text Cannot be empty" else null
    else if (text == "Password")
        binding.enterPass.error = if (isNotValid) "$text Cannot be empty" else null


}

private fun updateUI(user: FirebaseUser?) {
    val intent = Intent(this,Homeactivity::class.java)
    startActivity(intent)

}
}