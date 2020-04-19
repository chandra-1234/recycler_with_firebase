package com.chandra.recycler_with_firebase

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_add.*
import kotlinx.android.synthetic.main.item_add.view.*
import java.io.IOException


class item_add :AppCompatActivity(){
    lateinit var db:DatabaseReference
    lateinit var ds:StorageReference
    private val PICK_IMAGE_REQUEST = 22
     var  filePath: Uri?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_add)


        db= FirebaseDatabase.getInstance().getReference("users").push()
        ds=FirebaseStorage.getInstance().reference
        imagebutton.setOnClickListener {
            setimage()
        }
        savebutton.setOnClickListener {
            saveimage()
        }
    }

    private fun saveimage() {
        var progressBar=ProgressDialog(this)

        var Title=findViewById<EditText>(R.id.Title)
        var title=Title.text.toString()
        if(!TextUtils.isEmpty(title)){
            progressBar.setTitle("Uploading..")
            progressBar.show()
            val ref= filePath!!.lastPathSegment?.let { ds.child("images").child(it) }
            ref?.putFile(filePath!!)
                ?.addOnSuccessListener{
                  ref.downloadUrl.addOnSuccessListener {
                      db.child("Title").setValue(title)
                      db.child("Image").setValue(it.toString())
                      progressBar.dismiss()
                  }
                }
                ?.addOnFailureListener {
                 Toast.makeText(this,"${it.printStackTrace()}",Toast.LENGTH_LONG).show()
                }


        }
        else{
            Toast.makeText(this,"some error",Toast.LENGTH_LONG).show()
        }

    }

    private fun setimage() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )

        // Defining Implicit Intent to mobile gallery

    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null
        ) { // Get the Uri of data
            filePath = data.data
            try { // Setting image on image view using Bitmap
                Picasso.get().load(filePath).fit().centerCrop().into(imagebutton)
            } catch (e: IOException) { // Log the exception
                e.printStackTrace()
            }
        }
    }


}