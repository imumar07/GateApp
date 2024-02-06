package com.example.gateapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ValueEventListener
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.gateapp.databinding.ActivityHomeBinding
import com.example.gateapp.databinding.ActivityLoginBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var firebaseReference: DatabaseReference
    private lateinit var firebaseAuthUser: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adView = binding.adView // Retrieving AdView using its id

        MobileAds.initialize(
            this
        ) { }

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        firebaseAuthUser=FirebaseAuth.getInstance()
        firebaseReference=FirebaseDatabase.getInstance().reference
        firebaseAuthUser.currentUser?.let { Log.e("asasf", it.uid) }

        binding.button2.setOnClickListener {
            val name=binding.name.text.toString()
            val number=binding.number.text.toString()

            val uid = firebaseAuthUser.currentUser?.uid
            if (uid != null) {
                // Example: Saving a user's name to the "users" node with their UID as the key
                saveData(uid, name,number)
                Toast.makeText(this,"Visitor added",Toast.LENGTH_SHORT).show()
                getAllUserData(uid)
                binding.scrollView.visibility = View.VISIBLE
            } else {
                Log.e("UserID", "User not authenticated")
            }

        }
    }
    private fun saveData(uid:String,name:String,number:String){
        val userReference=firebaseReference.child("users").child(uid).push()

        val userData = HashMap<String, Any>()
        userData["name"] = name
        userData["number"]=number

        // Add more fields as needed
        userReference.setValue(userData)
            .addOnSuccessListener {
                Log.d("SaveData", "Data added successfully")
            }
            .addOnFailureListener {
                Log.e("SaveData", "Failed to add data: ${it.message}")
            }

        // Check if data already exists for the user
//        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Data already exists, update it
//                    userReference.updateChildren(userData)
//                        .addOnSuccessListener {
//                            Log.d("SaveData", "Data updated successfully")
//                        }
//                        .addOnFailureListener {
//                            Log.e("SaveData", "Failed to update data: ${it.message}")
//                        }
//                } else {
//                    // Data doesn't exist, create a new entry
//                    userReference.setValue(userData)
//                        .addOnSuccessListener {
//                            Log.d("SaveData", "Data saved successfully")
//                        }
//                        .addOnFailureListener {
//                            Log.e("SaveData", "Failed to save data: ${it.message}")
//                        }
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.e("SaveData", "Error checking data existence: ${databaseError.message}")
//            }
//        })
    }
    private fun getAllUserData(uid: String) {
        val userReference=firebaseReference.child("users").child(uid)

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataContainer = binding.dataContainer
                    for (childSnapshot in dataSnapshot.children) {
                        // Extract data from each child snapshot
                        val userName = childSnapshot.child("name").getValue(String::class.java)
                        val userNumber = childSnapshot.child("number").getValue(String::class.java)
                        // Extract more fields as needed
                        val textView = TextView(binding.root.context)
                        "Name: $userName, Number: $userNumber".also { textView.text = it }
                        textView.textSize = 18f

                        // Add TextView to the container
                        dataContainer.addView(textView)
                        Log.d("UserData", "Name: $userName")
                        // Log more fields or handle the data as needed
                    }
                } else {
                    Log.d("UserData", "No data found for UID: $uid")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("UserData", "Error retrieving data: ${databaseError.message}")
            }
        })
    }

}