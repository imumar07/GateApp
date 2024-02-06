package com.example.gateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gateapp.databinding.ActivityLoginBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth

class LogIn : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adView = binding.adView // Retrieving AdView using its id

        MobileAds.initialize(
            this
        ) { }

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        binding.button2.setOnClickListener {
            val email=binding.editText.text.toString()
            val pass=binding.password.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty()){

                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                    if(it.isSuccessful){
                        val intent= Intent(this,home::class.java)
                        startActivity(intent)

                    }else {

                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Empty fields are present please recheck", Toast.LENGTH_SHORT).show()
            }
        }
    }

}