package com.arin.titik_suara.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arin.titik_suara.R;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CobaFragment extends Fragment {

    private Spinner spinnerKategori;
    private EditText etDeskripsi;
    private ImageView imagePreview;
    private Button btnPilihGambar, btnKirim;
    private Bitmap bitmap;
    private String[] kategoriList = {"Fasilitas", "Peralatan", "Kebersihan", "Keamanan", "Kenakalan Siswa", "Lainnya"};
    private final String URL_POST_PENGADUAN = "http://192.168.1.3/api/post_pengaduan.php"; // Update to your actual URL

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coba_pengaduan, container, false);

        // Initialize Views
        spinnerKategori = view.findViewById(R.id.spinnerKategori);
        etDeskripsi = view.findViewById(R.id.etDeskripsi);
        imagePreview = view.findViewById(R.id.imagePreview);
        btnPilihGambar = view.findViewById(R.id.btnPilihGambar);
        btnKirim = view.findViewById(R.id.btnKirim);

        // Setup Spinner for categories
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, kategoriList);
        spinnerKategori.setAdapter(adapter);

        // Select Image Button Click
        btnPilihGambar.setOnClickListener(v -> openGallery());

        // Submit Data Button Click
        btnKirim.setOnClickListener(v -> {
            if (validateInput()) {
                kirimPengaduan();
            }
        });

        return view;
    }

    // Open the gallery to pick an image
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imagePreview.setImageBitmap(bitmap); // Display the image preview
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Validate if the required fields are filled
    private boolean validateInput() {
        if (etDeskripsi.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Deskripsi tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Send the complaint data
    private void kirimPengaduan() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Mengirim...");
        progressDialog.show();

        // Volley Request for POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST_PENGADUAN,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(getContext(), "Pengaduan berhasil dikirim!", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed(); // Go back to previous fragment
                        } else {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Log.e("VolleyError", error.toString());
                    Toast.makeText(getContext(), "Gagal mengirim pengaduan!", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("deskripsi", etDeskripsi.getText().toString().trim());
                params.put("kategori", spinnerKategori.getSelectedItem().toString());

                // Send the encoded image if selected
                if (bitmap != null) {
                    params.put("gambar", encodeImage(bitmap)); // Send encoded image
                    Log.d("EncodedImage", encodeImage(bitmap));  // Debugging the Base64 image string
                }

                return params;
            }
        };

        // Add request to Volley queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    // Convert Bitmap to Base64 String
    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos); // Compress the image at 80% quality
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT); // Return Base64 encoded string
    }
}
