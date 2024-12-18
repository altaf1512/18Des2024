package com.arin.titik_suara.Util.Interface;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arin.titik_suara.R;

public class NewPengaduanActivity extends AppCompatActivity {

    private EditText etTitle, etDescription;
    private Button btnSubmitPengaduan;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tambah_pengaduan_baru);

        // Menghubungkan view dengan variabel
        etTitle = findViewById(R.id.et_pengaduan_title);
        etDescription = findViewById(R.id.et_pengaduan_description);
        btnSubmitPengaduan = findViewById(R.id.btn_submit_pengaduan);

        // Action ketika tombol submit ditekan
        btnSubmitPengaduan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();

                if (!title.isEmpty() && !description.isEmpty()) {
                    // Di sini, Anda dapat menambahkan logika untuk menyimpan data ke database
                    Toast.makeText(NewPengaduanActivity.this, "Pengaduan berhasil dikirim!", Toast.LENGTH_SHORT).show();

                    // Kembali ke dashboard setelah pengaduan dikirim
                    finish();
                } else {
                    Toast.makeText(NewPengaduanActivity.this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}