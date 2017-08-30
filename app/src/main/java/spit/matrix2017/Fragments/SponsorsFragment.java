/*
 * *
 *  * This file is part of Matrix2017
 *  * Created for the annual technical festival of Sardar Patel Institute of Technology
 *  *
 *  * The original contributors of the software include:
 *  * - Adnan Ansari (psyclone20)
 *  * - Tejas Bhitle (TejasBhitle)
 *  * - Mithil Gotarne (mithilgotarne)
 *  * - Rohit Nahata (rohitnahata)
 *  * - Akshay Shah (akshah1997)
 *  *
 *  * Matrix2017 is free software: you can redistribute it and/or modify
 *  * it under the terms of the MIT License as published by the Massachusetts Institute of Technology
*/

package spit.matrix2017.Fragments;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import spit.matrix2017.Activities.EventDetails;
import spit.matrix2017.HelperClasses.Event;
import spit.matrix2017.HelperClasses.EventListAdapter;
import spit.matrix2017.HelperClasses.MainSponsor;
import spit.matrix2017.HelperClasses.MainSponsorAdapter;
import spit.matrix2017.HelperClasses.RecyclerItemClickListener;
import spit.matrix2017.HelperClasses.Sponsor;
import spit.matrix2017.HelperClasses.SponsorAdapter;
import spit.matrix2017.HelperClasses.SponsorRecyclerAdapter;
import spit.matrix2017.R;

public class SponsorsFragment extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private  DatabaseReference mDatabaseReference,tDatabaseReference,coDatabaseReference,aDatabaseReference;
    private ValueEventListener mValueEventListener;

    private ArrayList<Sponsor> aSponsors, coSponsors;
    private ArrayList<MainSponsor> mSponsors;

    private RecyclerView mRecyclerView, aRecylerView, coRecycler;
    ImageView tImg;

    String onTitle = "";

    MainSponsorAdapter msa;
    SponsorAdapter cos,as;

    public SponsorsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sposors_2,container,false);

        tImg = (ImageView) view.findViewById(R.id.titleImage);

        String onTitle, onCo1, onCo2;

        mRecyclerView = (RecyclerView) view.findViewById(R.id.mainRecyler);
        aRecylerView = (RecyclerView) view.findViewById(R.id.associationRecycler);
        coRecycler = (RecyclerView) view.findViewById(R.id.CoRecycler);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        coRecycler.setHasFixedSize(true);
        coRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        coRecycler.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        aRecylerView.setHasFixedSize(true);
        aRecylerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        aRecylerView.setItemAnimator(new DefaultItemAnimator());

        mSponsors = new ArrayList<MainSponsor>();
        aSponsors = new ArrayList<Sponsor>();
        coSponsors = new ArrayList<Sponsor>();


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override

                    public void onItemClick(View view, int position) {
                        MainSponsor ms = mSponsors.get(position);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ms.getUrl()));
                        startActivity(browserIntent);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        aRecylerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override

                    public void onItemClick(View view, int position) {
                        Sponsor ms = aSponsors.get(position);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ms.getUrl()));
                        startActivity(browserIntent);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        coRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override

                    public void onItemClick(View view, int position) {
                        Sponsor ms = coSponsors.get(position);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ms.getUrl()));
                        startActivity(browserIntent);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );



        SponsorsFragment.FetchSponsors fs = new SponsorsFragment.FetchSponsors();
        fs.execute();

        return view;
    }

    public void goTitle(View v){
        if(onTitle.equals("")){
            Toast.makeText(getContext(),"Please Wait",Toast.LENGTH_SHORT);
            return;
        }

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(onTitle));
        startActivity(browserIntent);
    }

    public void updateUI(){
        msa = new MainSponsorAdapter(mSponsors,getContext());
        mRecyclerView.setAdapter(msa);
        mRecyclerView.scrollToPosition(0);
    }

    public void updateUI2(){

        as = new SponsorAdapter(aSponsors,getContext());
        aRecylerView.setAdapter(as);
        aRecylerView.scrollToPosition(0);
    }

    public void updateUI3(){
        cos = new SponsorAdapter(coSponsors,getContext());
        coRecycler.setAdapter(cos);
        coRecycler.scrollToPosition(0);
    }

    public class FetchSponsors extends AsyncTask<Void,Void,ArrayList<Event>> {

        @Override
        protected void onPreExecute() {
            //bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Event> doInBackground(Void... params) {

           tDatabaseReference = mFirebaseDatabase.getReference().child("Sponsors").child("Title");

            //mDatabaseReference.push().setValue(new Sponsor("https://s-media-cache-ak0.pinimg.com/originals/8e/f5/9d/8ef59dc3c90a3abd56c87a5901709132.jpg","Test"));
            tDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String url = (String) snapshot.child("picUrl").getValue();
                        Glide.with(SponsorsFragment.this).load(url).placeholder(R.drawable.download).crossFade().into(tImg);
                        onTitle = (String) snapshot.child("url").getValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            coDatabaseReference = mFirebaseDatabase.getReference().child("Sponsors").child("Co");
            //mDatabaseReference.push().setValue(new Sponsor("https://s-media-cache-ak0.pinimg.com/originals/8e/f5/9d/8ef59dc3c90a3abd56c87a5901709132.jpg","Test"));
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    coSponsors.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String posterUrl = (String) snapshot.child("picUrl").getValue();
                        String Url = (String) snapshot.child("url").getValue();

                        coSponsors.add(new Sponsor(posterUrl,Url));
                    }
                    updateUI3();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            coDatabaseReference.addValueEventListener(mValueEventListener);



            aDatabaseReference = mFirebaseDatabase.getReference().child("Sponsors").child("Association");
            //mDatabaseReference.push().setValue(new Sponsor("https://s-media-cache-ak0.pinimg.com/originals/8e/f5/9d/8ef59dc3c90a3abd56c87a5901709132.jpg","Test"));
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    aSponsors.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String posterUrl = (String) snapshot.child("picUrl").getValue();
                        String Url = (String) snapshot.child("url").getValue();

                        aSponsors.add(new Sponsor(posterUrl,Url));
                    }
                    updateUI2();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            aDatabaseReference.addValueEventListener(mValueEventListener);






            mDatabaseReference = mFirebaseDatabase.getReference().child("Sponsors").child("Main");
            //mDatabaseReference.push().setValue(new MainSponsor("https://s-media-cache-ak0.pinimg.com/originals/8e/f5/9d/8ef59dc3c90a3abd56c87a5901709132.jpg","Test","Beverage"));
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mSponsors.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String posterUrl = (String) snapshot.child("picUrl").getValue();
                        String Url = (String) snapshot.child("url").getValue();
                        String type = (String) snapshot.child("type").getValue();

                        mSponsors.add(new MainSponsor(posterUrl,Url,type));
                    }
                    updateUI();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mDatabaseReference.addValueEventListener(mValueEventListener);


            return null;
        }
    }
}
