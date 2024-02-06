package com.example.gateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.gateapp.databinding.ActivitySignUpBinding

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    private lateinit var  binding: com.example.gateapp.databinding.ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth= FirebaseAuth.getInstance()

        val adView = binding.adView // Retrieving AdView using its id

        MobileAds.initialize(
            this
        ) { }

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        binding.anchorSignIn.setOnClickListener {
            val intent= Intent(this,LogIn::class.java)
            startActivity(intent)
        }

        binding.button2.setOnClickListener {
            val email=binding.editText.text.toString()
            val pass=binding.password.text.toString()
            val rePass=binding.passwordReenter.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty() && rePass.isNotEmpty()){
                if(pass==rePass){
                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if(it.isSuccessful){
                            val intent= Intent(this,home::class.java)
                            startActivity(intent)

                        }else{
                            Log.e("ex","Firebase Exception",it.exception)
                            Toast.makeText(this,it.exception.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"Password is not matching", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Empty fields are present please recheck", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if(firebaseAuth.currentUser!=null){
            val intent= Intent(this,home::class.java)
            startActivity(intent)
        }
    }
}
