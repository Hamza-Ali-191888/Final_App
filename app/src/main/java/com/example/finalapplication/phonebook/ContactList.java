package com.example.finalapplication.phonebook;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalapplication.R;

import java.util.ArrayList;

public class ContactList extends AppCompatActivity {
    private static final int REQUEST_CALL =1;
    RecyclerView recyclerView;
    ArrayList<ContactModel> arrayList=new ArrayList<ContactModel>();
    MainAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phonebook);

        recyclerView=findViewById(R.id.recycler_view);
        //checking permission
        takePermission1();
    }




    public void takePermission1(){
        if(isPermissionGranted() && isPhonePermissionGranted()){
            getContactList();
        }
        else{
            takePermission();
        }
    }

    private boolean isPhonePermissionGranted()
    {
        if(ContextCompat.checkSelfPermission(ContactList.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            return  false;

        }
        else
        {
            return true;
        }
    }
    //Taking Permissions FOR ANDROID 11 and Above
    private boolean isPermissionGranted(){
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }
        else{
            int readEnternalStoragePermissions = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS);
            return readEnternalStoragePermissions == PackageManager.PERMISSION_GRANTED;
        }
    }


    private void takePermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE}, 101);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0){
            if(requestCode==101){
                boolean readExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(readExternalStorage){
                    Toast.makeText(this,"Read Permission Is Granted",Toast.LENGTH_SHORT).show();
                    getContactList();
                }
                else {
                    takePermission();
                }
            }

        }

    }
    private void getContactList() {

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        ContactModel model=new ContactModel();

                        //setting name and number

                        model.setName(name);

                        model.setNumber(phoneNo);

                        //adding in array list
                        arrayList.add(model);

                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }

        //setting layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //initalizing adapter
        adapter=new MainAdapter(this,arrayList);
        //set Adapter
        recyclerView.setAdapter(adapter);
    }
}

