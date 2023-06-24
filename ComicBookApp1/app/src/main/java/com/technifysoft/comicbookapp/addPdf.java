package com.technifysoft.comicbookapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.technifysoft.comicbookapp.model.Category;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class addPdf extends AppCompatActivity {

    private static final int PDF_PICK_CODE = 1000;
    private ImageView imgBack,imgPickPdf;
    private Button buttonAddFilePdf;
    private EditText edtPickCategory,edtBookTitle,edtBookDecription;

    private FirebaseAuth firebaseAuth;
    private ArrayList<String> categoryIDArrayList;

    //    Title dùng để lấy ra category trên Categories trong firebase
    private ArrayList<String> categoryTitleArrayList;

    private Uri pdfUri=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pdf);

        imgBack = findViewById(R.id.img_back);
        imgPickPdf = findViewById(R.id.pick_Pdf);
        edtPickCategory = findViewById(R.id.edt_pick_category);
        buttonAddFilePdf = findViewById(R.id.btn_add_PDF);

        edtBookDecription = findViewById(R.id.edt_bookDecription);
        edtBookTitle = findViewById(R.id.edt_bookTitle);

//before load data u need to getinstance, dùng để thực thi cái firebaseAuth ni
        firebaseAuth = FirebaseAuth.getInstance();

        BackToDashBoard();
        PickPdf();
        LoadCategory();
        PickCategory();

        buttonAddFilePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate();
            }
        });

    }

    private String title = "", decription = "";
    private void Validate() {
//        kiểm tra rỗng

        title = edtBookTitle.getText().toString().trim();
        decription = edtBookDecription.getText().toString().trim();
//        category = edtPickCategory.getText().toString().trim();

        if(TextUtils.isEmpty(title)){
            Toast.makeText(addPdf.this,"Không được để trống nhan đề!",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(decription)){
            Toast.makeText(addPdf.this,"Không được để trống mô tả truyện!",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(SelectedTiTleCategory)){
            Toast.makeText(addPdf.this,"Không được để trống loại truyện !",Toast.LENGTH_SHORT).show();
        }else if(pdfUri==null){
            Toast.makeText(addPdf.this,"Chưa chọn file truyện!",Toast.LENGTH_SHORT).show();
        }else{
            UploadToFireBase();
        }
    }

    //    upload file truyện lên storage firebase , lấy đường link và tạo hàm thêm một nút con Book vào firebase realtime
//    với thể loại là bên category
    private long timeStamp = System.currentTimeMillis();

    private void UploadToFireBase() {
//        tạo path(đường) lưu trữ
        String filePathAndName = "Books/"+ timeStamp;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                        get url
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadPdfUrl = ""+uriTask.getResult();

//                        upload to firebase
                        UploadPdfInforToDb(uploadPdfUrl,timeStamp);
                    }
                });
    }

    private void UploadPdfInforToDb(String uploadPdfUrl, long timeStamp) {
//        long TimeStamp = System.currentTimeMillis();

        String udi = firebaseAuth.getUid();

//        set up data to upload
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid",""+udi);
        hashMap.put("id",""+timeStamp);
        hashMap.put("title",""+title);
        hashMap.put("category",""+SelectedTiTleCategory);
        hashMap.put("decreption",""+decription);
        hashMap.put("url",""+uploadPdfUrl);
        hashMap.put("viewCount",0);

//        sử dụng datareference để thao tác CRUD vào một nút con mới BOOKS
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(""+timeStamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(addPdf.this,"UpLoad thành công!",Toast.LENGTH_SHORT).show();
                        Log.d("CheckUploadBook","Upload thành công!!");
                    }
                });
    }


    //    Khai báo ra 2 biến để lưu id của loại sách với Tên của loại sách đó("title")
    String SelectedIdCategory="",SelectedTiTleCategory="";
    private void PickCategory() {
        edtPickCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //khai báo mọt mảng để lưu các phần tử của ArraylistCategory sang
                String[] categoryArray = new String[categoryTitleArrayList.size()];
                for(int i=0;i<categoryTitleArrayList.size();i++){
                    categoryArray[i] = categoryTitleArrayList.get(i);
                }

//              using dialog to showing our categories
                AlertDialog.Builder builder = new AlertDialog.Builder(addPdf.this);
                builder.setTitle("Chọn Thể Loại")
                        .setItems(categoryArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                          khai báo một biến có kiểu string để lưu giá trị mình click vô đó
                                SelectedTiTleCategory = categoryArray[which];
//                                SelectedIdCategory = categoryArray[which];

//                           set category vào text view đó
                                edtPickCategory.setText(SelectedTiTleCategory);
                            }
                        }).show();
            }
        });
    }

    //    dùng một mảng có kiểu là category và lưu data vào mảng đó
    private void LoadCategory() {
        categoryIDArrayList = new ArrayList<>();
        categoryTitleArrayList = new ArrayList<>();
//        wanna thao tác với dữ liệu thì cần phải khai báo ra database dùng để tham chiếu tới các nut của csdl JSON
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryIDArrayList.clear();
                categoryTitleArrayList.clear();

//                gán dữ liệu vừa đọc được từ firebase về và gán vô arraylist
                for (DataSnapshot ds:snapshot.getChildren()){
                    String categoryId = ""+ds.child("id").getValue();

                    String categoryTitle = ""+ds.child("category").getValue();


//                    add vào Arraylist
                    categoryIDArrayList.add(categoryId);
                    categoryTitleArrayList.add(categoryTitle);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        resultCode là yêu cầu đã được set trong phương thức setResult == OK
        if(resultCode==RESULT_OK){
            if(requestCode == PDF_PICK_CODE){
                Log.d("pick_PDF","Picked");

//                dùng để tham chiếu tới dữ liệu sẽ được thực hiện bởi hành động ACTION_GET_CONTENT và kiểu của dữ liệu đó
//                là PDF okiiii
                pdfUri = data.getData();
                Log.d("Uri","uri: "+ pdfUri);
            }
        }else{
            Toast.makeText(addPdf.this,"Thêm PDF thất bại!!!",Toast.LENGTH_SHORT).show();
        }
    }

    private void PickPdf() {
        imgPickPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//       Các thành phần trong intent gồm có name,action,data để xác định hdong và kiểu dữ liệu của intent
                Intent intent = new Intent();
//       và xác định data sẽ lấy là PDF
                intent.setType("application/pdf");
//       trong intent me xác định action là Action getContent, ActionGetcontent sẽ trả về một tham số với kiểu dũ liệu
//       mình đã định nghĩa trc đó là pdf
                intent.setAction(intent.ACTION_GET_CONTENT);

//        what is createChooser: dùng để hiển thị trình trọn
//                2 tham số trong sart activityforresult 1:Chứa thông tin đối tượng muốn chuyển tới , request code là 1 con số kiểu int
                startActivityForResult(Intent.createChooser(intent,"select your title"),PDF_PICK_CODE);
            }
        });
    }

    private void BackToDashBoard() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}