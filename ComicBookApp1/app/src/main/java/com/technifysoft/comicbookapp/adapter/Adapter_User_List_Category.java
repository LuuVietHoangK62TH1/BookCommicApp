package com.technifysoft.comicbookapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technifysoft.comicbookapp.Pdf_List_Admin;
import com.technifysoft.comicbookapp.Pdf_List_User;
import com.technifysoft.comicbookapp.R;
import com.technifysoft.comicbookapp.model.Category;
import com.technifysoft.comicbookapp.model.Pdf;

import java.util.ArrayList;

public class Adapter_User_List_Category extends RecyclerView.Adapter<Adapter_User_List_Category.HolderAdapterUserListCategory> {

    private ArrayList<Category> categoryArrayList;
    private Context context;

    public Adapter_User_List_Category(ArrayList<Category> categoryArrayList, Context context) {
        this.categoryArrayList = categoryArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_User_List_Category.HolderAdapterUserListCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_category_user,parent,false);
        return new HolderAdapterUserListCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_User_List_Category.HolderAdapterUserListCategory holder, int position) {
        Category model = categoryArrayList.get(position);

        String title = model.getCategory();
        holder.textView.setText(title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Pdf_List_User.class);
                intent.putExtra("categoryTitle",model.getCategory());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size() ;
    }

    public static class HolderAdapterUserListCategory extends RecyclerView.ViewHolder{

        private TextView textView;
        public HolderAdapterUserListCategory(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvCategory_list_category_user);

        }
    }
}
