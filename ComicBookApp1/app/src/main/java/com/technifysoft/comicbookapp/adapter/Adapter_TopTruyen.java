package com.technifysoft.comicbookapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technifysoft.comicbookapp.DetailBooks;
import com.technifysoft.comicbookapp.R;
import com.technifysoft.comicbookapp.model.Pdf;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Adapter_TopTruyen extends RecyclerView.Adapter<Adapter_TopTruyen.HolderToptruyen> {

    private final Context context;
    private final ArrayList<Pdf> pdfArrayList;

    public Adapter_TopTruyen(Context context, ArrayList<Pdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
    }


    @NonNull
    @Override
    public HolderToptruyen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fragment_home_toptruyen, parent, false);
        return new HolderToptruyen(itemView);
    }

    public static final long Max_Byte_Pdf1 = 50000000;

    @Override
    public void onBindViewHolder(@NonNull HolderToptruyen holder, int position) {
        Pdf model = pdfArrayList.get(position);

        String url = model.getUrl();
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        ref.getBytes(Max_Byte_Pdf1)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        holder.myView.fromBytes(bytes)
                                .pages(0)
                                .swipeHorizontal(false)
                                .spacing(0)
                                .load();

                        holder.progressBarTT.setVisibility(View.INVISIBLE);
                    }
                });

        String titleBook = model.getTitle();
        holder.textView.setText(titleBook);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailBooks.class);
                intent.putExtra("BookID",model.getId());
                context.startActivity(intent);

                Toast.makeText(context, "@@@@@", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    public static class HolderToptruyen extends RecyclerView.ViewHolder{

        private final PDFView myView;
        private final TextView textView;
        private ProgressBar progressBarTT;

        public HolderToptruyen(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.booktitle_topTruyen);
            myView = itemView.findViewById(R.id.RCL_TopTruyen);
            progressBarTT = itemView.findViewById(R.id.Progressbar_TopTruyen);
        }
    }
}
