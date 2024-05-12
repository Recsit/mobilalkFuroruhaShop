package com.example.furdoruhawebshop;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class newSwimsuitActivity extends AppCompatActivity {

    EditText name;
    EditText price;
    EditText details;
    TextView imageText;
    int image;
    private CollectionReference items;

    private static final int GALERY_CODE = 371;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_swimsuit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        details = findViewById(R.id.details);
        imageText = findViewById(R.id.image);

        items = FirebaseFirestore.getInstance().collection("Swimsuits");
    }

    public void upload(View view) {
        Intent iGallary = new Intent(Intent.ACTION_PICK);
        iGallary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(iGallary,GALERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALERY_CODE){
            imageText.setText("Feltöltve");
            image = Integer.parseInt(data.getData().getLastPathSegment());
        }
    }

    public void add(View view) {

        items.add(new Swimsuit(name.getText().toString(),Integer.parseInt(price.getText().toString()),details.getText().toString(),image));
        finish();
    }

    public void cancel(View view) {
        finish();
    }
}