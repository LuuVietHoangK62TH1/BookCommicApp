package com.technifysoft.comicbookapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technifysoft.comicbookapp.DashboardAdminActivity;
import com.technifysoft.comicbookapp.Pdf_List_Admin;
import com.technifysoft.comicbookapp.R;
import com.technifysoft.comicbookapp.model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//trong recycle RecycleView thì nó có một lớp RecyclerView.Adapter và nó luôn yêu cầu một iner class đó chính
//là ViewHolder.
public class Adapter_category extends RecyclerView.Adapter<Adapter_category.HolderCategory>{

    private final Context context;
    // định nghĩa một List dạng Category
    private ArrayList<Category> categoryArrayList;

    public Adapter_category(Context context, ArrayList<Category> categoryArrayList1) {
        this.context = context;
        this.categoryArrayList = categoryArrayList1;
    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new HolderCategory(itemView);
    }

    //   dùng để gắn data vào view
    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, int position) {
        Category model = categoryArrayList.get(position);

        String id = model.getId();
        String category = model.getCategory();
        String udi = model.getUdi();
        long timestamp = model.getTimestamp();

        holder.TvCategory.setText(category);

//      read data from FireBase using ADDVALUE_EVENLISTENER(bonus)
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
////                Log.d("tagGetValue", "Value is: " + map);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("failed","failed to read data");
//            }
//        });

//        start delete
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                khai baso một alertbuilder dùng để thông báo xác nhận xóa
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Bạn có muốn xóa không?")
                        .setPositiveButton("Xác Nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteCategory(model,holder);
                                Toast.makeText(context,"Đang xóa....",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Pdf_List_Admin.class);
//               gán giá trị của loại sách của bên Category vào trong Category
                intent.putExtra("categoryId",id);
                intent.putExtra("categoryTitle",category);
                context.startActivity(intent);
            }
        });
    }

    private void DeleteCategory(Category model, HolderCategory holder) {
//      to delete u need get id of that category
        String id = model.getId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Xóa thành công!",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    //đếm số List trong Data
    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

//    có tác dụng là chuyển đổi đối tượng được định nghĩa lên từng row của view
//    sự khác nhau giữa re và list là Re nó có thêm View Holder cái này dùng để tránh những lần gọi hàm findViewById thường xuyên
//    khi cuộn listView và sẽ làm cho nó smooth hơn

    public class HolderCategory extends RecyclerView.ViewHolder{
        protected TextView TvCategory;
        protected ImageView imgDelete;
        public HolderCategory(@NonNull View itemView) {
            super(itemView);

            TvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
        }
    }

}


