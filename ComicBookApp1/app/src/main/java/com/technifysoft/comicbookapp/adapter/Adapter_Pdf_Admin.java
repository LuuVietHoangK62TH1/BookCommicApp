package com.technifysoft.comicbookapp.adapter;

//import static com.technifysoft.comicbookapp.SetByte.Max_Byte_Pdf;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technifysoft.comicbookapp.DetailBooks;
import com.technifysoft.comicbookapp.MyApplication;
import com.technifysoft.comicbookapp.R;
import com.technifysoft.comicbookapp.Update_Books;
import com.technifysoft.comicbookapp.model.Pdf;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter_Pdf_Admin extends RecyclerView.Adapter<Adapter_Pdf_Admin.HolderPdfAdmin>{

    private final Context context;
    ArrayList<Pdf> pdfArrayList ;

    private ProgressDialog progressDialog;

    //    hàm tạo
    public Adapter_Pdf_Admin(Context context, ArrayList<Pdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Đợi xíuu!");

    }

    //phương thức này dùng để tạo view mới cho RecycleView
    @NonNull
    @Override
    public HolderPdfAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pdf_admin, parent, false);
        return new HolderPdfAdmin(itemView);
    }

    //    Phương thức này dùng để gắn data và view.
    @Override
    public void onBindViewHolder(@NonNull HolderPdfAdmin holder, int position) {
//        sử dụng mảng này để đổ dữ liệu vào mảng , nghĩa là ở bên Acivity chính ta sẽ Load data về và truyền vào mảng(giống cái tham số ở hàm tạo) rồi gán
//        vào mảng và đổ ra
        Pdf model = pdfArrayList.get(position);

        String title = model.getTitle();
        String decreption = model.getDecreption();
        long timeStamp = model.getTimeStamp();

        String formatDate = MyApplication.forMatTimeStamp(timeStamp);

//        set data vô view
        holder.TvTile.setText(title);
        holder.TvDecreption.setText(decreption);
        holder.TvDate.setText(formatDate);

        LoadPdfSize(model,holder);
        LoadPdfFromUrl(model,holder);
//        LoadDataFromFireBase(model,holder);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreOptionDialog(model,holder);
            }
        });

//        go to details Books
//        put book để biết khi nào mình cần dùng tới book đó thông qua id book
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailBooks.class);
                intent.putExtra("BookID",model.getId());
                context.startActivity(intent);
            }
        });

    }
    // 2 tham số có ý nghĩa là model đại diện cho đối tượng dữ liệu đã được lấy từ firebase, còn holder là dd cho view(holder này hạn chế số lần findViewById)
    private void moreOptionDialog(Pdf model, HolderPdfAdmin holder) {
        String[] options = {"Edit","Delete"};

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Chọn phần chỉnh sửa")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            //edittt
                            Intent intent = new Intent(context, Update_Books.class);
                            intent.putExtra("BookID",model.getId());
                            context.startActivity(intent);
                        }else if(which == 1 ){
                            DeleteComicBook(model,holder);
                        }
                    }
                }).show();
    }

    //    delete commic
    private void DeleteComicBook(Pdf model, HolderPdfAdmin holder) {
        String bookTitle = model.getTitle();
        String bookId = model.getId();

        progressDialog.setMessage("Đang xóa..."+bookTitle);
        progressDialog.show();

//        Muốn xóa sử dụng hàm delete
//        xóa từ storage
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Xóa Thành Công!",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Xóa thất bại",Toast.LENGTH_LONG).show();
                    }
                });

//        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl);
//        storageReference.delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(context,"Xoas thanhf cong!",Toast.LENGTH_LONG).show();
////                             xóa tiếp dữ liệu bên Books
////                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
////                        reference.child(bookId)
////                                .removeValue()
////                                .addOnSuccessListener(new OnSuccessListener<Void>() {
////                                    @Override
////                                    public void onSuccess(Void unused) {
////                                        progressDialog.dismiss();
////                                        Toast.makeText(context,"Xóa Thành Công!!",Toast.LENGTH_SHORT).show();
////                                    }
////                                });
//                    }
//                });
    }

    //    hàm này dùng để set pdfView
    public static final long Max_Byte_Pdf = 50000000;
    private void LoadPdfFromUrl(Pdf model, HolderPdfAdmin holder) {
        String pdfUrl = model.getUrl();
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getBytes(Max_Byte_Pdf)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        holder.pdfView.fromBytes(bytes)
                                .pages(0)
                                .spacing(0)
                                .swipeHorizontal(false)
                                .enableSwipe(false)
                                .load();
                        holder.progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    //    hàm này để setsize
    private void LoadPdfSize(Pdf model, HolderPdfAdmin holder) {
        String pdfUrl = model.getUrl();

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getMetadata()
                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        double bytes = storageMetadata.getSizeBytes();

                        double kb = bytes/1024;
                        double mb = kb/1024;

                        if(mb>=1){
//                            .2f là định dạng float. sẽ lấy 2 số lẻ sau dấu phẩy.
                            holder.TvSize.setText(String.format("%.2f",mb)+"MB");
                        }else if(kb>=1){
                            holder.TvSize.setText(String.format("%.2f",kb)+"KB");
                        }else{
                            holder.TvSize.setText(String.format("%.2f",bytes)+"BYTES");
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

//    mục đích  là gắn tái sử dụng view

    public static class HolderPdfAdmin extends RecyclerView.ViewHolder{

        private PDFView pdfView;
        private ProgressBar progressBar;
        private TextView TvTile,TvDecreption,TvSize,TvDate,sub_text;
        private ImageView imageView;

        public HolderPdfAdmin(@NonNull View itemView) {
            super(itemView);
            pdfView = itemView.findViewById(R.id.pdfView);
            TvTile = itemView.findViewById(R.id.TvTitle);
            TvDecreption =itemView.findViewById(R.id.TvDecreption);
            TvSize = itemView.findViewById(R.id.TvSize);
            TvDate = itemView.findViewById(R.id.TvDate);
            sub_text = itemView.findViewById(R.id.sub_text);
            progressBar = itemView.findViewById(R.id.progress_circular);
            imageView = itemView.findViewById(R.id.more_option);
        }
    }
}