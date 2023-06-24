package com.technifysoft.comicbookapp.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.adapters.ViewPagerAdapter;
import com.technifysoft.comicbookapp.MyApplication;
import com.technifysoft.comicbookapp.R;
import com.technifysoft.comicbookapp.model.Pdf;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Adapter_Pdf_User extends RecyclerView.Adapter<Adapter_Pdf_User.HolderPdfUser> {

    private Context context;
    ArrayList<Pdf> pdfArrayList;

    public Adapter_Pdf_User(Context context, ArrayList<Pdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
    }

    @NonNull
    @Override
    public Adapter_Pdf_User.HolderPdfUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_pdf_user,parent,false);
        return new HolderPdfUser(view);
    }
    public static final long Max_Byte_Pdf = 50000000;

    //    dùng để gán giá trị vào các view tương ứng
    @Override
    public void onBindViewHolder(@NonNull Adapter_Pdf_User.HolderPdfUser holder, int position) {
        Pdf model = pdfArrayList.get(position);

        String title = model.getTitle();
        String decreption = model.getDecreption();
        long timeStamp = model.getTimeStamp();
        String formatDate = MyApplication.forMatTimeStamp(timeStamp);


        holder.TvTitleUser.setText(title);
        holder.TvDecreptionUser.setText(decreption);
        holder.TvDateUser.setText(formatDate);

//        load anh

        String url = model.getUrl();
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        ref.getBytes(Max_Byte_Pdf)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        holder.pdfView.fromBytes(bytes)
                                .pages(0)
                                .swipeHorizontal(false)
                                .load();
                        holder.progress_circularUser.setVisibility(View.INVISIBLE);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }


    public static class HolderPdfUser extends RecyclerView.ViewHolder{
        private PDFView pdfView;
        private TextView TvTitleUser,TvDecreptionUser,TvDateUser;
        private ProgressBar progress_circularUser;

        public HolderPdfUser(@NonNull View itemView) {
            super(itemView);

            pdfView = itemView.findViewById(R.id.pdfViewUser);
            TvTitleUser = itemView.findViewById(R.id.TvTitleUser);
            TvDecreptionUser = itemView.findViewById(R.id.TvDecreptionUser);
            TvDateUser = itemView.findViewById(R.id.TvDateUser);
            progress_circularUser = itemView.findViewById(R.id.progress_circularUser);
        }
    }

}
