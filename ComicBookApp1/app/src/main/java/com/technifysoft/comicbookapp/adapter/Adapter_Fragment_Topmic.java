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
import com.technifysoft.comicbookapp.model.Pdf;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Adapter_Fragment_Topmic extends RecyclerView.Adapter<Adapter_Fragment_Topmic.HolderAdapterFgTopComic> {

    private Context context;
    private ArrayList<Pdf> pdfArrayList;

    public Adapter_Fragment_Topmic(Context context, ArrayList<Pdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
    }

    @NonNull
    @Override
    public Adapter_Fragment_Topmic.HolderAdapterFgTopComic onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fragment_topcomic, parent, false);
        return new HolderAdapterFgTopComic(itemView);
    }

    public static final long LoadPdfSize = 50000000;

    @Override
    public void onBindViewHolder(@NonNull Adapter_Fragment_Topmic.HolderAdapterFgTopComic holder, int position) {
        Pdf model = pdfArrayList.get(position);

        String url = model.getUrl();
        String title = model.getTitle();

        holder.txtView.setText(title);
        StorageReference firebaseStorage = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        firebaseStorage.getBytes(LoadPdfSize)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        holder.pdfView.fromBytes(bytes)
                                .pages(0)
                                .swipeHorizontal(false)
                                .spacing(0)
                                .enableSwipe(false)
                                .load();
                    }
                });

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


    public class HolderAdapterFgTopComic extends RecyclerView.ViewHolder{

        private final PDFView pdfView;
        private TextView txtView;
        public HolderAdapterFgTopComic(@NonNull View itemView) {
            super(itemView);

            pdfView = itemView.findViewById(R.id.PdfView_fragment_top_comic);
            txtView = itemView.findViewById(R.id.txtTitle_fragment_top_comic);
        }
    }

}
