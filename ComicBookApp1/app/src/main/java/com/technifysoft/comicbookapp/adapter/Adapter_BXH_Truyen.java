package com.technifysoft.comicbookapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technifysoft.comicbookapp.DetailBooks;
import com.technifysoft.comicbookapp.R;
import com.technifysoft.comicbookapp.adapter.Adapter_TopTruyen;
import com.technifysoft.comicbookapp.model.Pdf;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Adapter_BXH_Truyen extends RecyclerView.Adapter<Adapter_BXH_Truyen.HolderBXH> {

    private Context context;
    private ArrayList<Pdf> pdfArrayList;

    public Adapter_BXH_Truyen(Context context, ArrayList<Pdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
    }

    @NonNull
    @Override
    public HolderBXH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bxh_comic, parent, false);
        return new HolderBXH(itemView);
    }


    private final long MaxSize = 5000000;
    @Override
    public void onBindViewHolder(@NonNull HolderBXH holder, int position) {
        Pdf model = pdfArrayList.get(position);

        String url = model.getUrl();

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        ref.getBytes(MaxSize)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        holder.myPdfView_BXH.fromBytes(bytes)
                                .pages(0)
                                .enableSwipe(false)
                                .spacing(0)
                                .load();
                    }
                });

        String title = model.getTitle();
        holder.txtTitle.setText(title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailBooks.class);
                intent.putExtra("BookID",model.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    public static class HolderBXH extends RecyclerView.ViewHolder{
        private final PDFView myPdfView_BXH;
        private TextView txtTitle;
        public HolderBXH(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTiTle_BXK);
            myPdfView_BXH = itemView.findViewById(R.id.myPdfView_BXH);
        }
    }
}
