package com.arin.titik_suara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import es.dmoral.toasty.Toasty;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText et_username, et_password;

    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        if (sharedPreferences.getBoolean("Login", false)){
            if (sharedPreferences.getBoolean("Login_User", false)){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }
    }

    public void btnLogin(View view) {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        // Misalnya ada validasi login sederhana
        if (username.equals("user") && password.equals("user123")) {
            // Simpan status login di SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("login", true);
            editor.apply();

            // Pindah ke MainActivity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            Toasty.error(this, "Username atau password salah", Toasty.LENGTH_SHORT).show();
        }
    }
}