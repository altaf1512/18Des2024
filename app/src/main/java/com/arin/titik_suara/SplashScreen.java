package com.arin.titik_suara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("login", false);

            Intent intent;
            if (isLoggedIn) {
                // Jika sudah login, arahkan ke MainActivity
                intent = new Intent(SplashScreen.this, MainActivity.class);
            } else {
                // Jika belum login, arahkan ke LoginActivity
                intent = new Intent(SplashScreen.this, LoginActivity.class);
            }

            startActivity(intent);
            finish(); // Tutup SplashScreen agar tidak bisa di-back
        }, 2000); // Durasi splash screen, bisa diatur sesuai kebutuhan
    }
}
