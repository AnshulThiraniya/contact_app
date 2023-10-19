package com.example.recyclerviewapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerModelClass(context: Context,var dataList:ArrayList<DataModel>,var itemclicklistner:Itemclicklistner): RecyclerView.Adapter<RecyclerModelClass.myViewHolder>() {

    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("ResourceType")
        var myLayout=itemView.findViewById<LinearLayout>(R.id.list_row_contact)
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
       var item=LayoutInflater.from(parent.context).inflate(R.layout.mycontect_row,parent,false)
        return myViewHolder(item)
    }

    override fun getItemCount(): Int {
      return dataList.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
         val mylistItem=dataList[position]

        val fName=mylistItem.fname
        val mName=mylistItem.mname
        val lName=mylistItem.lname
        val number=mylistItem.mnumber

        holder.myLayout.findViewById<TextView>(R.id.list_user_name).text="$fName $mName $lName"
        holder.myLayout.findViewById<TextView>(R.id.list_user_number).text=number
        holder.myLayout.findViewById<ImageView>(R.id.imageview_id).setImageResource(R.drawable.housekeeper)

        val call=holder.myLayout.findViewById<ImageView>(R.id.list_ic_call)
        val rowid=holder.myLayout.id
        call.setOnClickListener{
            itemclicklistner.oncallbuttonclick(number,rowid)
        }

        val menuIcon=holder.myLayout.findViewById<ImageView>(R.id.list_ic_option)

        menuIcon.setOnClickListener {
            itemclicklistner.MyPopupMenu(menuIcon)
        }
    }
}
