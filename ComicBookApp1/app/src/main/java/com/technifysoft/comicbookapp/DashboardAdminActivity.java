package com.technifysoft.comicbookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.technifysoft.comicbookapp.adapter.Adapter_category;
import com.technifysoft.comicbookapp.model.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardAdminActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    private FirebaseAuth firebaseAuth;
    private Button btnAdd;
    FloatingActionButton floatingActionButton;

    RecyclerView recyclerView;

    //    khai báo ra arraylist có kiểu là Category dùng để lưu trữ giữ liệu
    private ArrayList<Category> categoryArrayList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        textView = findViewById(R.id.DB_tvEmail);
        imageView = findViewById(R.id.BD_ImgLogOut);
        btnAdd = findViewById(R.id.btn_DashboardAD);
        floatingActionButton = findViewById(R.id.add_pdf);
        recyclerView = findViewById(R.id.Rc_dashboard_ad);

        ADD();
        Logout();
        addPdf();
//        getTextForAD();
        LoadCategory();
    }

    private void LoadCategory() {
        categoryArrayList1 = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            //            DataSnapshot no se contains data from firebase , and data that you receive as a dataSnapshot
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                clear list
                categoryArrayList1.clear();

                for(DataSnapshot ds: snapshot.getChildren()){
                    Category category = ds.getValue(Category.class);

                    categoryArrayList1.add(category);
                }
//                setup for category

//                note: Recycleview request a Layoutmanager ex as Linearlayout and Gridlayout
//                recyclerView.setLayoutManager(new GridLayoutManager(DashboardAdminActivity.this, 2));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DashboardAdminActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                Adapter_category adapter_category = new Adapter_category(DashboardAdminActivity.this,categoryArrayList1);
                recyclerView.setAdapter(adapter_category);
//                lett settt layoutmanagerrrr nha
                recyclerView.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DashboardAdminActivity.this,"Không thành công",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void ADD() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, com.technifysoft.comicbookapp.category_add_commic.class));
            }
        });
    }

    private void Logout() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(DashboardAdminActivity.this,loginFragment.class));
                finish();
            }
        });
    }

    private void getTextForAD() {
        Intent intent = getIntent();
        String nameAD = intent.getStringExtra("emailAdmin");
        textView.setText(nameAD);
    }

    private void addPdf(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, com.technifysoft.comicbookapp.addPdf.class));
            }
        });
    }

}