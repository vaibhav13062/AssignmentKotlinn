package com.techstrum.myapplication.adapter

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.techstrum.myapplication.R

class MyListAdapter(private val context: Activity, private val email: String?, private val imgUrl: ArrayList<String>)
    : ArrayAdapter<String>(context,R.layout.custom_list, imgUrl) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val emailText = rowView.findViewById(R.id.emailView) as TextView
        val imageView2 = rowView.findViewById(R.id.imageView2) as ImageView
        Log.i("URL", imgUrl[position])
        emailText.text = email
        Log.i("Adapter",imgUrl[2])
        Picasso.get().load(imgUrl[position]).into(imageView2)
        return rowView
    }
}