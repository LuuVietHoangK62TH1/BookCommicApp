package com.technifysoft.comicbookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.technifysoft.comicbookapp.model.Pdf;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class DetailBooks extends AppCompatActivity {
    private String bookId;

    private TextView tvTitleBook,tvCategoryBook,sizeBook,ViewBooks,tvDecreptionBook;
    private Button btnReadBook;
    private ImageView imgback_to_listComic;

    private ProgressBar progressBar;
    private PDFView myPdfView;


    //    get book thông qua id vừa lấy được từ ac Adapter Pdf Admin
//    load data về và gán vào từng view con tương ứng
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_books);

        Intent intent = getIntent();
        bookId = intent.getStringExtra("BookID");


        tvTitleBook = findViewById(R.id.tvTitleBook);
        ViewBooks = findViewById(R.id.ViewBooks);
        sizeBook = findViewById(R.id.sizeBook);
        tvCategoryBook = findViewById(R.id.tvCategoryBook);
        tvDecreptionBook = findViewById(R.id.tvDecreptionBook);
        imgback_to_listComic = findViewById(R.id.imgback_to_listComic);
        myPdfView = findViewById(R.id.myPdfView);
        btnReadBook = findViewById(R.id.btn_readBookDetail);

//        incrementBook();
        LoadBookDetails();
        readBook();
        back();
    }

    private void readBook() {
        btnReadBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailBooks.this,ReadBookDetail.class);
                intent.putExtra("BookID", bookId);
                startActivity(intent);
            }
        });
    }

    private void incrementBook(String bookId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String viewCount = ""+snapshot.child("viewCount").getValue();

                        if(viewCount.equals("")|| viewCount.equals("null")){
                            viewCount="0";
                        }
                        long newViewCount = Long.parseLong(viewCount)+1;
                        HashMap<String ,Object> hashMap = new HashMap<>();

                        hashMap.put("viewCount", newViewCount);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
                        reference.child(bookId)
                                .updateChildren(hashMap);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void back() {
        imgback_to_listComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static final long Max_Byte_Pdf2 = 50000000;


    private void LoadBookDetails() {
        incrementBook(bookId);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String title = ""+snapshot.child("title").getValue();
                        String category = ""+snapshot.child("category").getValue();
                        String decreption = ""+snapshot.child("decreption").getValue();
                        String viewCount = ""+snapshot.child("viewCount").getValue();
                        String url = ""+snapshot.child("url").getValue();

//                        setdataaa
                        tvTitleBook.setText(title);
                        tvCategoryBook.setText(category);
                        tvDecreptionBook.setText(decreption);
                        ViewBooks.setText(viewCount);

//                      get img for mypdfView
                        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                        ref.getBytes(Max_Byte_Pdf2)
                                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        myPdfView.fromBytes(bytes)
                                                .pages(0)
                                                .spacing(0)
                                                .swipeHorizontal(false)
                                                .enableSwipe(false)
                                                .load();
                                    }
                                });

//                        getsize for sizeBook
                        ref.getMetadata()
                                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                    @Override
                                    public void onSuccess(StorageMetadata storageMetadata) {
                                        double bytes = storageMetadata.getSizeBytes();

                                        double kb = bytes/1024;
                                        double mb = kb/1024;

                                        if(mb>=1){
//                                      .2f là định dạng float. sẽ lấy 2 số sau dấu phẩy.
                                            sizeBook.setText(String.format("%.2f",mb)+"MB");
                                        }else if(kb>=1){
                                            sizeBook.setText(String.format("%.2f",kb)+"KB");
                                        }else{
                                            sizeBook.setText(String.format("%.2f",bytes)+"BYTES");
                                        }
                                    }
                                });

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
//                        fail
                    }
                });
    }

}