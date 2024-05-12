package com.example.furdoruhawebshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText username;
    EditText email;
    EditText password;
    EditText passwordCon;
    EditText phone;

    private void toShop(){
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordCon = findViewById(R.id.passwordCon);
        phone = findViewById(R.id.phone);
    }

    public void registration(View view) {
        String usernameStr = username.getText().toString();
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        String passwordConStr = passwordCon.getText().toString();
        String phoneStr = phone.getText().toString();

        if (usernameStr.isEmpty() || emailStr.isEmpty() || passwordStr.isEmpty() || passwordConStr.isEmpty() || phoneStr.isEmpty()){
            Toast.makeText(RegisterActivity.this,"Kérem töltse ki az összes mezőt",Toast.LENGTH_LONG).show();
            return;
        }

        if (!passwordStr.equals(passwordConStr)){
            Log.e("RegisterActivity","Nem megfelelő jelszó");
            Toast.makeText(RegisterActivity.this,"Nem egyeznek meg a jelszavak",Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    toShop();
                }else {
                    Toast.makeText(RegisterActivity.this,"Már létezik ilyen felhasználó\nvagy helytelen az e-mail cím",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cancel(View view) {
        finish();
    }
}