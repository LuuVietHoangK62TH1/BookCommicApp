package com.technifysoft.comicbookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Update_Books extends AppCompatActivity {

    private ImageView imageViewBack;
    private TextInputEditText edtCategory_update,edt_bookDecription,edt_bookTitle;
    private String Bookid;
    private Button btnUpdate;
    ProgressDialog progressDialog;

    ArrayList<String> ArrayListBookTitle;
    ArrayList<String> ArraylistDeceptonBook;

    ArrayList<String> CategoryTitleArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_books);

        progressDialog = new ProgressDialog(Update_Books.this);


        imageViewBack = findViewById(R.id.img_back);
        edtCategory_update = findViewById(R.id.edtCategory_update);
        edt_bookDecription = findViewById(R.id.edt_bookDecription_update);
        edt_bookTitle = findViewById(R.id.edt_bookTitle_Update);
        btnUpdate = findViewById(R.id.btnUpdateBook);

        Bookid = getIntent().getStringExtra("BookID");

        LoadCategory();
        LoadData();

        BackToDashBoard();
        ShowCategory();
        ValidateBook();
    }

    String titleBook = "";
    String deceptrionBook = "";
    private void ValidateBook() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleBook = edt_bookTitle.getText().toString().trim();
                deceptrionBook = edt_bookDecription.getText().toString().trim();

                if(TextUtils.isEmpty(titleBook)){
                    Toast.makeText(Update_Books.this,"Không được để trống tên sách ! ",Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(deceptrionBook)){
                    Toast.makeText(Update_Books.this,"Không được để trống mô tả sách ! ",Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(SelectedCategoryTitle)){
                    Toast.makeText(Update_Books.this,"Không được để trống loại sách ! ",Toast.LENGTH_LONG).show();
                }else{
                    UpdateBooks();
                }
            }
        });

    }

    //    update thông qua bookId từ bên adapter Pdf Admin
    private void UpdateBooks() {
//        progressDialog.setTitle("Cập nhật!");
        progressDialog.setMessage("Đang Cập Nhật...!");
        progressDialog.show();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("title", ""+titleBook);
        hashMap.put("decreption", ""+deceptrionBook);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(Bookid)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Update_Books.this,"Update Thành Công!!",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
    }
//    lấy dữ liệu từ fire base về lưu vào một mảng
//    gán giá trị cần lấy từ mảng vào trong các biến
//    update lên fire base

    private void LoadData(){
//        ArrayListBookTitle = new ArrayList<>();
//        ArraylistDeceptonBook = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(Bookid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String TitleBook = ""+snapshot.child("title").getValue();
                        String BookDecreption = ""+snapshot.child("decreption").getValue();

                        edt_bookTitle.setText(TitleBook);
                        edt_bookDecription.setText(BookDecreption);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private String SelectedCategoryTitle = "";
    private void ShowCategory() {
        edtCategory_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] ArrayCategoryTitle = new String[CategoryTitleArrayList.size()];

                for(int i=0;i<CategoryTitleArrayList.size();i++){
                    ArrayCategoryTitle[i] = CategoryTitleArrayList.get(i);
                }
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Update_Books.this);
                alertDialog.setTitle("Lựa chọn loại sách")
                        .setItems(ArrayCategoryTitle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SelectedCategoryTitle = ArrayCategoryTitle[which];

                                edtCategory_update.setText(SelectedCategoryTitle);
                            }
                        }).show();
            }
        });
    }

    private void LoadCategory(){
        CategoryTitleArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CategoryTitleArrayList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    String categoryTitle = ""+ds.child("category").getValue();

                    CategoryTitleArrayList.add(categoryTitle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void BackToDashBoard() {
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}