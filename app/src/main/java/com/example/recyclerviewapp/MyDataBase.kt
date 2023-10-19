package com.example.recyclerviewapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast


class MyDataBase(context: Context) {
        private val mcontext: Context =context
        val DBNAME = "ContactDatabase"
        val TABLE_NAME = "ContactTable"
        val VERSION = 3
        val ID = "Id"
        val FNAME = "Fname"
        val MNAME="Mname"
        val LNAME = "Lname"
        val MOBILE_NO = "Mobile_no"


        val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME($ID INTEGER PRIMARY KEY AUTOINCREMENT,$FNAME TEXT,$MNAME TEXT,$LNAME TEXT,$MOBILE_NO TEXT) "


        val Sqlhelper=MyOpenHelperClass(context)
        val SqlDb=Sqlhelper.writableDatabase


        inner class MyOpenHelperClass(context: Context) : SQLiteOpenHelper(context, DBNAME, null, VERSION) {
            override fun onCreate(sqlLiteDb: SQLiteDatabase?) {
                sqlLiteDb?.execSQL(CREATE_TABLE)
            }

            override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
                p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
                onCreate(p0)
            }
        }

        fun  createData(fname:String,mname:String,lname:String,mobile:String){
            val values= ContentValues()

            values.put(FNAME,fname)
            values.put(MNAME,mname)
            values.put(LNAME,lname)
            values.put(MOBILE_NO,mobile)
            SqlDb.insert(TABLE_NAME,null,values)

        }

        @SuppressLint("Range")
        fun fetchData(): ArrayList<DataModel> {
            val dataList = ArrayList<DataModel>()
            val myCursor = SqlDb.rawQuery("SELECT * FROM $TABLE_NAME", null,null)

            if (myCursor.moveToFirst()) {
                do {
                    val id=myCursor.getInt(myCursor.getColumnIndex(ID))
                    val fname = myCursor.getString(myCursor.getColumnIndex(FNAME))
                    val mname = myCursor.getString(myCursor.getColumnIndex(MNAME))
                    val lname = myCursor.getString(myCursor.getColumnIndex(LNAME))
                    val mobileNo = myCursor.getString(myCursor.getColumnIndex(MOBILE_NO))

                    val data = DataModel(id,fname,mname, lname, mobileNo)
                    dataList.add(data)
                } while (myCursor.moveToNext())
            }

            return dataList
        }

        fun deleteSingleData(rowId:Int){
            val deletedRow=SqlDb.delete(TABLE_NAME,"$ID=$rowId",null)
            if(deletedRow>0){
                Toast.makeText(mcontext,"$deletedRow row delete", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(mcontext,"something went wrong", Toast.LENGTH_LONG).show()
            }
        }


        fun  updateData(id:Int,fname:String,mname:String,lname:String,mobile:String){

            val values= ContentValues()

            values.put(FNAME,fname)
            values.put(MNAME,mname)
            values.put(LNAME,lname)
            values.put(MOBILE_NO,mobile)
            SqlDb.update(TABLE_NAME,values,"$ID=$id",null)
            Toast.makeText(mcontext, "update data", Toast.LENGTH_SHORT).show()

        }


    }
