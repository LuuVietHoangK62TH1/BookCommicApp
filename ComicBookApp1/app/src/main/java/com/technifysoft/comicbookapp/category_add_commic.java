package com.technifysoft.comicbookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class category_add_commic extends AppCompatActivity {

    private ImageView imageView;
    private Button btnAddCategory;
    private EditText editText;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add_commic);

        imageView = findViewById(R.id.img_back);
        btnAddCategory = findViewById(R.id.btn_add_category);

        editText = findViewById(R.id.edt_category);

        firebaseAuth = FirebaseAuth.getInstance();

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate();
            }
        });
        back();
    }

    String category = "";
    private void Validate() {
        category = editText.getText().toString().trim();
//        TextUtils used to check null , it return boolen
        if(TextUtils.isEmpty(category)){
            Toast.makeText(category_add_commic.this,"Không được để trống!",Toast.LENGTH_LONG).show();
        }else{
            AddCategory();
        }
    }

    private void AddCategory() {
//        before put data u need to get data
        long timeStamp = System.currentTimeMillis();

//      set some inf, Using hasmap with key<String> and type value is Object
        HashMap<String , Object> hashMap= new HashMap<>();

        hashMap.put("id", ""+timeStamp);
        hashMap.put("category", ""+category);
//        UID = user id...
        hashMap.put("udi", firebaseAuth.getUid()+"");

//                  start Add(setting) from Database root>Categories>categoryID>cgrINF
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");

        ref.child(""+timeStamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(category_add_commic.this,"Thêm Category thành công!!",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(category_add_commic.this,"Thêm Category thất bại!!",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void back() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}