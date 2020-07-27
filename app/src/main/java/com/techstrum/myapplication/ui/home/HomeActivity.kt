package com.techstrum.myapplication.ui.home

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.techstrum.myapplication.R
import com.techstrum.myapplication.adapter.MyListAdapter
import java.util.*


class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var floatingButton: FloatingActionButton? = null
    val storage = Firebase.storage
    var list = arrayListOf<String>()
    var storageRef = storage.reference
    var postid: String=UUID.randomUUID().toString()
    var imagesRef: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = Firebase.auth
        val user = auth.currentUser
        list.clear()
        imagesRef=storageRef.child("Images").child(user!!.uid).child("$postid.jpg")
        floatingButton = findViewById<FloatingActionButton>(R.id.floatingActionButton4)
        //listView=findViewById(R.id.listView)
        var listView:ListView?=findViewById(R.id.listView)
        floatingButton!!.setOnClickListener {
            if (checkSelfPermission(READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(READ_EXTERNAL_STORAGE);
                //show popup to request runtime permission
                requestPermissions(permissions,
                    PERMISSION_CODE
                );
            } else {
                //permission already granted
                pickImageFromGallery();
            }
        }
        // ...
        val database: DatabaseReference = Firebase.database.getReference("Posts")
        database.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    Log.i("DATABASE",postSnapshot.getValue().toString())
                    list.add(postSnapshot.getValue().toString())
                }
                val myListAdapter= MyListAdapter(
                    this@HomeActivity,
                    user.email,
                    list
                )
                listView?.adapter= myListAdapter
            }
        })
        //storageRef.child(user.uid).listAll().addOnSuccessListener { listResult ->
            //listResult.prefixes.forEach { prefix ->
                // All the prefixes under listRef.
                //og.i("URL", prefix.downloadUrl.toString())
                // You may call listAll() recursively on them.
            //}

            //listResult.items.forEach { item ->
                // All the items under listRef.
              //  list.add(item.downloadUrl.toString())
              //  Log.i("URL", item.downloadUrl.toString())
            //}
            //Log.i("URL", list[1])
      //  }
          //  .addOnFailureListener {
              // Uh-oh, an error occurred!
          //  }
       
       

    }


    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,
            IMAGE_PICK_CODE
        )
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;

        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            //image_view.setImageURI(data?.data)
            val uploadTask = imagesRef?.putFile(data?.data!!)
            val urlTask = uploadTask?.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imagesRef?.downloadUrl
            }?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    Log.i("URL",downloadUri.toString())
                    val database = Firebase.database
                    val myRef = database.getReference("Posts").child(postid)
                    myRef.setValue(downloadUri.toString())
                } else {
                    // Handle failures
                    // ...
                }
            }

            uploadTask?.addOnSuccessListener {

                val intent = Intent(applicationContext,
                    HomeActivity::class.java)
                startActivity(intent)
                finish()
                //Sucess
            }
        }
    }
}

