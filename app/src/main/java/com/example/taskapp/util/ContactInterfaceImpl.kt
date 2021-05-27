package com.example.taskapp.util

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import com.example.taskapp.datamodel.ContactModel
import java.lang.Exception

class ContactInterfaceImpl constructor(val context: Context):ContactInterface {
    override suspend fun getContactsAsync():List<ContactModel>{
        val nameList: ArrayList<ContactModel> = ArrayList()
        val cr: ContentResolver = context.contentResolver
        val cur: Cursor? = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null)
        if (cur?.count!! > 0) {
            while (cur.moveToNext()) {
                val id: String = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts._ID))
                var name="Hi"
                try {
                    name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                }catch (e: Exception)
                {

                }

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur: Cursor = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id), null)!!
                    var phoneNo:String?=null
                    var email:String?=null
                    while (pCur.moveToNext()) {
                        phoneNo = pCur.getString(pCur.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER))
                       email= pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                    }
                    nameList.add(ContactModel(id, name, phoneNo,email))
                    pCur.close()
                }
            }
        }
        cur.close()
        nameList.sortWith { o1, o2 ->
            o1.name.compareTo(o2.name)
        }
        return nameList
    }
}