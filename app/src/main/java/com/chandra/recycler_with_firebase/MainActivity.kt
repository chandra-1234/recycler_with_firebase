package com.chandra.recycler_with_firebase

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cardview_layout.*


class MainActivity : AppCompatActivity() {
    lateinit var databaseReference: DatabaseReference
    var datalist=ArrayList<Model>()
    lateinit var options: FirebaseRecyclerOptions<Model>
   lateinit var adapter: FirebaseRecyclerAdapter<Model, View_Holder>
    var rv: RecyclerView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        val options: FirebaseRecyclerOptions<Model> = FirebaseRecyclerOptions.Builder<Model>()
            .setQuery(databaseReference, Model::class.java)
            .build()
    adapter=object :FirebaseRecyclerAdapter<Model,View_Holder>(options){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.cardview_layout,parent,false)
            return View_Holder(view)

        }

        override fun onBindViewHolder(holder: View_Holder, position: Int, model: Model) {
            holder.title.setText(model.title)
            Picasso.get().load(model.uri).fit().centerCrop().into(displayimage)

        }



    }
        recycler_view.layoutManager=LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
        recycler_view.adapter=adapter

    }

    class View_Holder(itemview: View): RecyclerView.ViewHolder(itemview) {
        var title=itemView.findViewById<TextView>(R.id.displaytitle)
        var img=itemview.findViewById<ImageView>(R.id.displayimage)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onStart() {
        super.onStart()

            adapter.startListening()

    }

    override fun onStop() {
        super.onStop()

            adapter.stopListening()

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.title) {
            val intent = Intent(this, item_add::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }


}
