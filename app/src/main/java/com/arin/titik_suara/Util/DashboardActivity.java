package com.arin.titik_suara.Util;

import android.content.Intent;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arin.titik_suara.Util.Interface.NewPengaduanActivity;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvPengaduanDiajukan, tvValidasiOsis, tvPengaduanDiproses, tvPengaduanDitolak, tvPengaduanSelesai;
    private View fabAdd;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.R.attr R = null;
        setContentView(R.layout);

        // Initialize TextViews
        tvPengaduanDiajukan = findViewById(R.id);
        tvValidasiOsis = findViewById(R.id);
        tvPengaduanDiproses = findViewById(R.id);
        tvPengaduanDitolak = findViewById(R.id);
        tvPengaduanSelesai = findViewById(R.id);

        // Set example data (this can be dynamic based on your data source)
        tvPengaduanDiajukan.setText("5");
        tvValidasiOsis.setText("3");
        tvPengaduanDiproses.setText("2");
        tvPengaduanDitolak.setText("1");
        tvPengaduanSelesai.setText("7");

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Membuka NewPengaduanActivity ketika tombol plus ditekan
                Intent intent = new Intent(DashboardActivity.this, NewPengaduanActivity.class);
                startActivity(intent);
            }

        });
    }
}
