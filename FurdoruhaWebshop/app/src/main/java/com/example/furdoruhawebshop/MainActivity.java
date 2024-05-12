package com.example.furdoruhawebshop;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText email;
    EditText password;

    private void toShop(){
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

    }

    public void login(View view) {
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();

        if ( emailStr.isEmpty() || passwordStr.isEmpty()){
            Toast.makeText(MainActivity.this,"Kérem töltse ki a mezőket",Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                email.setText("");
                password.setText("");
                toShop();
            }else {
                Toast.makeText(MainActivity.this,"Nem megfelelő e-mail cím vagy jelszó",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void register(View view) {
        email.setText("");
        password.setText("");
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public void guest(View view) {
        mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                toShop();
            }else {
                Toast.makeText(MainActivity.this,"Valami gond történt",Toast.LENGTH_LONG).show();
            }
        });
    }
}