package com.example.cryptocalculator

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.database.Observable
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.cryptocalculator.databinding.ActivityMainBinding
import com.example.cryptocalculator.databinding.ActivitySignupactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jakewharton.rxbinding2.widget.RxTextView
import java.util.stream.Stream

@SuppressLint("CheckResult")

class Signupactivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignupactivityBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


//Auth
       auth = FirebaseAuth.getInstance()


        val mEditText= findViewById<EditText>(R.id.enter_pass)

        mEditText.customSelectionActionModeCallback = object: ActionMode.Callback{
            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return false
            }

            override fun onDestroyActionMode(p0: ActionMode?) {

            }
        }


// fullname Validation

        val nameStream = RxTextView.textChanges(binding.enterFullname)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        nameStream.subscribe {
            showNameExistAlert(it)
        }

        val phoneStream = RxTextView.textChanges(binding.enterPhonenum)
            .skipInitialValue()
            .map {
                phonenumber ->
                phonenumber.length >10
            }
        phoneStream.subscribe {
           showPhonenumberAlert(it)
        }

        val emailStream = RxTextView.textChanges(binding.enterEmail1)
            .skipInitialValue()
            .map {
                email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailValidAlert(it)
        }
        val passwordStream = RxTextView.textChanges(binding.enterPass)
            .skipInitialValue()
            .map {
                password ->
                password.length <8
          }

        passwordStream.subscribe {
            showTextMinimalAlert(it, "Password")

        }
        val passwordConfirmStream = io.reactivex.Observable.merge(
            RxTextView.textChanges(binding.enterPass)
                .skipInitialValue()
                .map {
                    password ->
                    password.toString() != binding.enterRepass.text.toString()
                },
            RxTextView.textChanges(binding.enterRepass)
                .skipInitialValue()
                .map {
                    confirmpassword ->
                    confirmpassword.toString() !=binding.enterPass.text.toString()
                })
passwordConfirmStream.subscribe {
    showPasswordConfirmAlert(it)
}
        val invalidFieldStream = io.reactivex.Observable.combineLatest(
            nameStream,
            emailStream,
            passwordStream,
            passwordConfirmStream,
            phoneStream,

            {nameInvalid: Boolean, emailInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmInvalid: Boolean, phonenumberinvalid: Boolean ->
                !nameInvalid && !emailInvalid && !passwordInvalid && !passwordConfirmInvalid && !phonenumberinvalid
            })
        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.btn3.isEnabled = true
                binding.btn3.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.Beige)
            } else {
                binding.btn3.isEnabled = false
                binding.btn3.backgroundTintList =
                    ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }
//click
        binding.btn3.setOnClickListener {
           val email = binding.enterEmail1.text.toString().trim()
            val password = binding.enterPass.text.toString().trim()
            val dialogBinding = layoutInflater.inflate(R.layout.try_dialog, null)
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful){
                        val mydialog = Dialog(this)
                        mydialog.setContentView(dialogBinding)
                        mydialog.setCancelable(true)
                        mydialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        mydialog.show()

                        val nextbtn = dialogBinding.findViewById<Button>(R.id.btn8)
                        nextbtn.setOnClickListener {
                            startActivity(Intent(this,Loginpage::class.java))
                        }
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener {
                                Toast.makeText(this, "Check your email for Verification",Toast.LENGTH_SHORT).show()
                            }
                            ?.addOnFailureListener {
                                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "You already have an account",  Toast.LENGTH_SHORT).show()
                    }
                }


        }
    }

    private fun showNameExistAlert(isNotValid: Boolean){
        binding.enterFullname.error = if(isNotValid) "Name cannot be empty!" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
        if (text == "password")
            binding.enterPass.error = if (isNotValid) "$text Must be more than 8 characters" else null
    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        binding.enterEmail1.error = if(isNotValid) "Invalid Email!" else null
    }
private fun showPasswordConfirmAlert(isNotValid: Boolean){
    binding.enterRepass.error = if(isNotValid) "Invalid Password" else null
    }

    private fun showPhonenumberAlert(isNotValid: Boolean){
        binding.enterPhonenum.error = if (isNotValid) "invalid Phone number" else null
    }



    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this,Loginpage::class.java)
        startActivity(intent)
    }
}