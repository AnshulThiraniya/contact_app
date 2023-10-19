package com.example.recyclerviewapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity()
    , Itemclicklistner, PopupMenu.OnMenuItemClickListener {

    lateinit var myRecyclerview: RecyclerView
    lateinit var addButtonDialog: FloatingActionButton
    lateinit var adapter: RecyclerModelClass
    lateinit var myDbAdapter:MyDataBase
    lateinit var adfName:EditText
    lateinit var admName:EditText
    lateinit var adlName:EditText
    lateinit var admNumber:EditText
    lateinit var saveButton: Button

    var dataList=ArrayList<DataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addButtonDialog = findViewById(R.id.addBtnDialog)

        addButtonDialog.setOnClickListener{
            AddContact()
        }
        myRecyclerview = findViewById(R.id.recycler_view_id)
        myRecyclerview.layoutManager = LinearLayoutManager(this)

        myDbAdapter=MyDataBase(this)
        dataList=myDbAdapter.fetchData()

        adapter = RecyclerModelClass(this,dataList,this)
        myRecyclerview.adapter = adapter



    }

    override fun onResume() {
        super.onResume()
        setDataToList()
    }

    private fun AddContact() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_add_update)
        val width = resources.displayMetrics.widthPixels * 0.9
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog.window?.setLayout(width.toInt(), height)

        adfName = dialog.findViewById(R.id.et_Fname)
        admName = dialog.findViewById(R.id.et_Mname)
        adlName = dialog.findViewById(R.id.et_Lname)
        admNumber=dialog.findViewById(R.id.et_Mnumber)
        saveButton=dialog.findViewById(R.id.action_btn)

        saveButton.setOnClickListener {

           myDbAdapter.createData(adfName.editableText.toString(),admName.editableText.toString(),
               adlName.editableText.toString(),admNumber.editableText.toString())

            dialog.dismiss()
            setDataToList()

        }
        
        dialog.show()
    }


    private fun setDataToList(){
        myDbAdapter=MyDataBase(this)
        val temp=myDbAdapter.fetchData()
        adapter=RecyclerModelClass(this,temp,this)
        myRecyclerview.adapter=adapter
    }

    lateinit var temp:String

    override fun oncallbuttonclick(number: String,id:Int) {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            val callIntent = Intent(Intent.ACTION_CALL,Uri.parse("tel:$number"))
            startActivity(callIntent)

            } else {
                    temp=number
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.CALL_PHONE),100)

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val callIntent = Intent(Intent.ACTION_CALL,Uri.parse("tel:$temp"))
                startActivity(callIntent)

            } else {
                Toast.makeText(this@MainActivity,"Access Denied", Toast.LENGTH_LONG).show()

            }
        }
    }


    @SuppressLint("SuspiciousIndentation")
    override fun MyPopupMenu(view:View) {
      val mypopupmenu=PopupMenu(this,view)
        mypopupmenu.menuInflater.inflate(R.menu.popup_menu,mypopupmenu.menu)
        mypopupmenu.show()

        mypopupmenu.setOnMenuItemClickListener(this)

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
      when(item?.itemId){
          R.id.pm_delete->{
              myDbAdapter.deleteSingleData(item.itemId)
          }
          R.id.pm_update->{
              val dialog = Dialog(this)
              dialog.setContentView(R.layout.activity_add_update)
              val width = resources.displayMetrics.widthPixels * 0.9
              val height = ViewGroup.LayoutParams.WRAP_CONTENT
              dialog.window?.setLayout(width.toInt(), height)
               dialog.show()
          }
      }
       return false
    }


}
