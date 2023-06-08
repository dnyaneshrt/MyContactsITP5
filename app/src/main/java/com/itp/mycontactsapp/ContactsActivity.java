package com.itp.mycontactsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ContactsActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        listView=findViewById(R.id.lview);

        //1. get the permission status.
       int status= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
      //if we have persmission, then only access contacts
       if(status==PackageManager.PERMISSION_GRANTED)
       {
           getContacts(); //this method will read contacts from SQLiteDB
       }else {
           ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1615);
       }

    }

    private void getContacts() {

        //step 1: get the object of ContentResolver class
        ContentResolver resolver=getContentResolver();
        Cursor cursor=null;
        //step 2:read the data from db using query() method.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
           cursor=  resolver.query(ContactsContract.CommonDataKinds.Contactables.CONTENT_URI,null,null,null);
        }
       String[] from=new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER};
        int[] to=new int[]{R.id.tv_name,R.id.tv_number};

     SimpleCursorAdapter simpleCursorAdapter= new  SimpleCursorAdapter(this,R.layout.mydesign,cursor,from,to);
     listView.setAdapter(simpleCursorAdapter);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            getContacts();
        }else {
            Toast.makeText(this, "User is not allowed to access contacts", Toast.LENGTH_SHORT).show();
        }
    }
}