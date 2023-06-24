package com.technifysoft.comicbookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.technifysoft.comicbookapp.adapter.Adapter_Pdf_Admin;
import com.technifysoft.comicbookapp.model.Pdf;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Pdf_List_Admin extends AppCompatActivity {

    private ArrayList<Pdf> pdfArrayList;
    private RecyclerView recyclerView;
    private ImageView imageView;

    private String categoryId,categoryTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list_admin);
        recyclerView = findViewById(R.id.Recycle_listPdf);
        imageView = findViewById(R.id.img_back);

//        get intent
        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");
        categoryTitle = intent.getStringExtra("categoryTitle");

        LoadPdfList();
        back();
    }

    private void back() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void LoadPdfList() {
        pdfArrayList = new ArrayList<>();

//   Lấy dữ liệu cụ thể là tên category bên firebase gán vào Categorytitle và so sánh với giá trị category bên Books và đổ ra màn hình
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.orderByChild("category")
                .equalTo(categoryTitle)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren()){
                            Pdf model = ds.getValue(Pdf.class);

                            pdfArrayList.add(model);
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Pdf_List_Admin.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                        khởi tạo và đổ vào adapter
                        Adapter_Pdf_Admin adapter_pdf_admin = new Adapter_Pdf_Admin(Pdf_List_Admin.this,pdfArrayList);
                        recyclerView.setAdapter(adapter_pdf_admin);
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("CheckFailPdfList","Failedd.....");
                    }
                });
    }
}