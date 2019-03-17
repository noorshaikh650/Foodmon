package com.example.fooduck;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Adpters.RecyclerviewAdapter;
import com.example.model.Food;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Restaurant_Finder extends FragmentActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    private DatabaseReference mref;
    private List<Food> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant__finder);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mref = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();


    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {



        mref.child("Donor Info").child("Restaurant").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (final DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {

                    String lal = dataSnapshot1.child("lal").getValue().toString();
                    String lon = dataSnapshot1.child("lon").getValue().toString();
                    double lala = Double.valueOf(lal);
                    double longi = Double.valueOf(lon);

                    //Toast.makeText(Restaurant_Finder.this,dataSnapshot1.child("lal").getValue().toString(),Toast.LENGTH_LONG).show();


                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lala,longi))
                            .title(dataSnapshot1.getKey())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));


                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            list.clear();

                            Toast.makeText(Restaurant_Finder.this, dataSnapshot1.child("uid").getValue().toString(), Toast.LENGTH_LONG).show();

                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Restaurant_Finder.this);
                            LayoutInflater inflater = Restaurant_Finder.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.menudialog, null);
                            dialogBuilder.setView(dialogView);


                            final RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerview);
                            Button address = dialogView.findViewById(R.id.address);
                            final Button call = dialogView.findViewById(R.id.call);
                            LinearLayoutManager lm =new LinearLayoutManager(Restaurant_Finder.this);
                            recyclerView.setLayoutManager(lm);


                            DatabaseReference aref = FirebaseDatabase.getInstance().getReference();

                            aref.child("Food").child("Restaurante").child(dataSnapshot1.child("uid").getValue().toString()).addChildEventListener(new ChildEventListener() {

                                @Override
                                public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {

                                    //list.clear();
                                        final Food food = dataSnapshot.getValue(Food.class);
                                        //Toast.makeText(Restaurant_Finder.this, food.getFoodname(), Toast.LENGTH_LONG).show();
                                        list.add(food);
                                        RecyclerviewAdapter adapter = new RecyclerviewAdapter(list);
                                        recyclerView.setAdapter(adapter);
                                        call.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", dataSnapshot1.child("phone_no").getValue().toString(), null));
                                                startActivity(intent);

                                            }
                                        });
                                        adapter.notifyDataSetChanged();
                                    }


                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                            final AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();



































                            Toast.makeText(Restaurant_Finder.this,marker.getTitle(),Toast.LENGTH_LONG).show();

                            return false;
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


     //   googleMap.addMarker(new MarkerOptions()
     //           .position(new LatLng(37.4233438, -122.0728817))
     //           .title("LinkedIn")
     //           .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//
     //   googleMap.addMarker(new MarkerOptions()
     //           .position(new LatLng(37.4629101,-122.2449094))
     //           .title("Facebook")
     //           .snippet("Facebook HQ: Menlo Park"));
//
     //   googleMap.addMarker(new MarkerOptions()
     //           .position(new LatLng(37.3092293, -122.1136845))
     //           .title("Apple"));
//
//
//
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(18.969132, 72.823598), 10));
    }
}
