package com.example.furdoruhawebshop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth Mauth;

    private RecyclerView rv;
    private ArrayList<Swimsuit> swimsiuts;
    private SwimsuitAdapter adapter;
    private int gridNumber = 1;

    private FirebaseFirestore firestore;
    private CollectionReference items;

    private int configOrientation;

    Spinner sortPrice;
    Spinner limitItem;


    int limit = 999;
    int price = 999999999;

    private void toAdd(){
        Intent intent = new Intent(this, newSwimsuitActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mauth = FirebaseAuth.getInstance();
        user = Mauth.getCurrentUser();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configOrientation = this.getResources().getConfiguration().orientation;

        switch (configOrientation){
            case Configuration.ORIENTATION_LANDSCAPE:{
                gridNumber = 2;
                break;
            }
            case Configuration.ORIENTATION_PORTRAIT:
            {
                gridNumber = 1;
                break;
            }
        }

        rv = findViewById(R.id.recycleView);
        rv.setLayoutManager(new GridLayoutManager(this, gridNumber));
        swimsiuts = new ArrayList<>();

        adapter = new SwimsuitAdapter(this,swimsiuts);
        rv.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        items = firestore.collection("Swimsuits");

        sortPrice = findViewById(R.id.sortPrice);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_price, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortPrice.setAdapter(adapter);

        sortPrice.setSelection(0);

        limitItem = findViewById(R.id.limitItem);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.limit_item, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        limitItem.setAdapter(adapter1);

        limitItem.setSelection(0);

    }

    public void query(){

        swimsiuts.clear();

        items.whereLessThanOrEqualTo("price",price).limit(limit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                Swimsuit swimsuit = document.toObject(Swimsuit.class);
                swimsuit.setId(document.getId());
                swimsiuts.add(swimsuit);
            }

            if (swimsiuts.isEmpty()){
                initialize();
                query();
            }

            adapter.notifyDataSetChanged();
        });
    }

    private void initialize() {
        String[] name = getResources().getStringArray(R.array.list_name);
        String[] details = getResources().getStringArray(R.array.list_details);
        int[] price = getResources().getIntArray(R.array.list_price);
        TypedArray image = getResources().obtainTypedArray(R.array.list_image);

        for (int i = 0; i < name.length; i++){
            items.add(new Swimsuit(
                    name[i],
                    price[i],
                    details[i],
                    image.getResourceId(i,0)
            ));
        }

        image.recycle();
    }

    public void filter(View view) {
        swimsiuts.clear();

        if (limitItem.getSelectedItem().toString().equals("Nincs")){
            limit = 999;
        }else {
            limit = Integer.parseInt(limitItem.getSelectedItem().toString());
        }

        if (sortPrice.getSelectedItem().toString().equals("Nincs")){
            price = 999999999;
        }else {
            price = Integer.parseInt(sortPrice.getSelectedItem().toString().replace(" Ft","").replace(" ",""));
        }

        items.whereLessThanOrEqualTo("price",price).limit(limit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for ( QueryDocumentSnapshot document :queryDocumentSnapshots){
                Swimsuit item = document.toObject(Swimsuit.class);
                item.setId(document.getId());
                swimsiuts.add(item);
            }

            if (swimsiuts.size() == 0  && price!=999999999 && limit != 999 ){
                initialize();
                query();
            }

            adapter.notifyDataSetChanged();
        });
    }

    public void makeNotification(){
        String channel_ID = "SwimsuitShop_channem";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),channel_ID);

        builder.setSmallIcon(R.drawable.swimsuit)
        .setContentTitle("FürdőruhaShop")
        .setContentText("Egy termék törölve lett!")
        .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager manager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notiChannel = manager.getNotificationChannel(channel_ID);
            if (notiChannel == null){
                int importance = manager.IMPORTANCE_HIGH;
                notiChannel = new NotificationChannel(channel_ID,"SwimsuitShopNoti",importance);
                notiChannel.setLightColor(Color.RED);
                notiChannel.enableVibration(true);
                manager.createNotificationChannel(notiChannel);
            }
        }

        manager.notify(0,builder.build());
    }

    public void delete(Swimsuit swimsuit){
        if (!user.isAnonymous()){
            DocumentReference ref = items.document(swimsuit._getId());
            ref.delete().addOnSuccessListener(success -> {});
            makeNotification();

            query();
        }else {
            Toast.makeText(this,"Vendégként nem engedett a törlés!",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            return true;
        } else if(id == R.id.add) {
            toAdd();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() { // Új fűrdőruha felvétele után egyböl frissül
        super.onResume();
        query();
    }
}