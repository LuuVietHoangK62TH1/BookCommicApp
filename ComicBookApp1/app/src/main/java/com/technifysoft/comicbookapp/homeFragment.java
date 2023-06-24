package com.technifysoft.comicbookapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.technifysoft.comicbookapp.adapter.Adapter_BXH_Truyen;
import com.technifysoft.comicbookapp.adapter.Adapter_TopTruyen;
import com.technifysoft.comicbookapp.model.Pdf;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ImageSlider imageSlider;
    int[] images = {
            R.drawable.naruto,
            R.drawable.songoku,
            R.drawable.anhsongoku,
            R.drawable.sgk,
            R.drawable.doraemon,
            R.drawable.afkmobi,
    };

    private RecyclerView recyclerView,RCL_BangXepHang;
    private ArrayList<Pdf> pdfArrayList;
    private ArrayList<Pdf> pdfArrayList1;
    private Context context;
    private ProgressBar progressBarTopTRuyen,progressbar_BXH;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        start
        progressBarTopTRuyen = view.findViewById(R.id.progressbar_TruyenHot);
        progressbar_BXH = view.findViewById(R.id.progressbar_BXH);
        recyclerView = view.findViewById(R.id.RCL_TopComic);
        RCL_BangXepHang = view.findViewById(R.id.RCL_BangXepHang);



        imageSlider = view.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.naruto, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.songoku, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.anhsongoku, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.sgk, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.doraemon, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.afkmobi, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);


//        start get data Top Comic
        String BookTitle = "Fantasy";

        pdfArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.orderByChild("category")
                .equalTo(BookTitle)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren()){
                            Pdf model = ds.getValue(Pdf.class);

                            pdfArrayList.add(model);
                        }
//                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2,GridLayoutManager.HORIZONTAL,true));
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2,GridLayoutManager.HORIZONTAL,false));
                        Adapter_TopTruyen adapter_topTruyen = new Adapter_TopTruyen(getActivity(),pdfArrayList);
                        progressBarTopTRuyen.setVisibility(View.INVISIBLE);
                        recyclerView.setAdapter(adapter_topTruyen);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//        start get data BXH
        String Booktitle2 = "Drama";
        pdfArrayList1 = new ArrayList<>();

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Books");
        ref1.orderByChild("category")
                .equalTo(Booktitle2)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren()){
                            Pdf model = ds.getValue(Pdf.class);

                            pdfArrayList1.add(model);
                        }
                        RCL_BangXepHang.setLayoutManager(new GridLayoutManager(getActivity(),2,GridLayoutManager.HORIZONTAL,false));
                        Adapter_BXH_Truyen adapter_bxh_truyen = new Adapter_BXH_Truyen(getActivity(),pdfArrayList1);
                        progressbar_BXH.setVisibility(View.INVISIBLE);
                        RCL_BangXepHang.setAdapter(adapter_bxh_truyen);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return view;
    }
}