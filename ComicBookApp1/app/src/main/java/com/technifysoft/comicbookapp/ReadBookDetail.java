package com.technifysoft.comicbookapp;

import static com.technifysoft.comicbookapp.adapter.Adapter_Pdf_Admin.Max_Byte_Pdf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ReadBookDetail extends AppCompatActivity {

    private ImageView imgBack;
    private PDFView myViewPdf;
    private TextView textView;
    private String bookId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book_detail);

        Intent intent = getIntent();
        bookId = intent.getStringExtra("BookID");
        textView = findViewById(R.id.Count_page);
        myViewPdf = findViewById(R.id.myPdfViewReadBook);

        imgBack = findViewById(R.id.img_back_Readbook);
        back();
        LoadUrlPDF();
    }

    private void LoadUrlPDF() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String url = ""+snapshot.child("url").getValue();

                        LoadPdf(url);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private long Max_byte = 5000000;
    private void LoadPdf(String url) {

        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
//tải xuống dữ liệu
//       truyền vào một một kích thước tối đa cho phép ngăn tình trạng thiếu
        reference.getBytes(Max_Byte_Pdf)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        myViewPdf.fromBytes(bytes)
                                .swipeHorizontal(false)
                                .onPageChange(new OnPageChangeListener() {
                                    @Override
                                    public void onPageChanged(int page, int pageCount) {
                                        int currentPage = (page+1);
                                        textView.setText(currentPage+"/"+pageCount);
                                    }
                                }).load();
                    }
                });
    }

    private void back() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}