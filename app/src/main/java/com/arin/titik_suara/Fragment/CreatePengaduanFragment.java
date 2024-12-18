package com.arin.titik_suara.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arin.titik_suara.Network.ApiClient;
import com.arin.titik_suara.Network.ApiService;
import com.arin.titik_suara.R;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePengaduanFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etIdPengaduan, etDeskripsiPengaduan;
    private Spinner spinnerKategoriPengaduan;
    private ImageView imgPrev1;
    private Uri selectedImageUri;
    private static final String API_URL = "http://192.168.1.5/api/post_pengaduan.php"; // Update your API URL

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_pengaduan, container, false);

        etIdPengaduan = view.findViewById(R.id.etIdPengaduan);
        spinnerKategoriPengaduan = view.findViewById(R.id.spinnerKategoriPengaduan);
        etDeskripsiPengaduan = view.findViewById(R.id.etDeskripsiPengaduan);
        imgPrev1 = view.findViewById(R.id.imgPrev1);

        // Generate ID Pengaduan
        generateIdPengaduan();

        // Setup spinner with categories
        setupSpinner();

        // Pick Image Button click
        view.findViewById(R.id.btnImgPicker1).setOnClickListener(v -> pickImage());

        // Submit complaint
        view.findViewById(R.id.btnSubmitPengaduan).setOnClickListener(v -> {
            if (validateInput()) {
                submitPengaduan();
            }
        });

        return view;
    }

    private void generateIdPengaduan() {
        long timestamp = System.currentTimeMillis();
        etIdPengaduan.setText("PGD-" + timestamp);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.jenis_kerusakan_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategoriPengaduan.setAdapter(adapter);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imgPrev1.setVisibility(View.VISIBLE);
            imgPrev1.setImageURI(selectedImageUri);
        }
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(etIdPengaduan.getText().toString())) {
            etIdPengaduan.setError("ID Pengaduan tidak boleh kosong");
            return false;
        }
        if (spinnerKategoriPengaduan.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Pilih kategori pengaduan", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etDeskripsiPengaduan.getText().toString())) {
            etDeskripsiPengaduan.setError("Deskripsi pengaduan tidak boleh kosong");
            return false;
        }
        return true;
    }

    private void submitPengaduan() {
        String idPengaduan = etIdPengaduan.getText().toString();
        String kategoriPengaduan = spinnerKategoriPengaduan.getSelectedItem().toString();
        String deskripsiPengaduan = etDeskripsiPengaduan.getText().toString();

        if (selectedImageUri == null) {
            Toast.makeText(getContext(), "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare data for the API request
        File file = new File(getPathFromUri(selectedImageUri));
        RequestBody deskripsi = RequestBody.create(MultipartBody.FORM, deskripsiPengaduan);
        RequestBody kategori = RequestBody.create(MultipartBody.FORM, kategoriPengaduan);

        // Create a RequestBody for the image
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part gambar = MultipartBody.Part.createFormData("gambar", file.getName(), fileBody);

        // Use Retrofit to make the API request
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        apiService.submitPengaduan(deskripsi, kategori, gambar).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Pengaduan berhasil dikirim", Toast.LENGTH_SHORT).show();
                    resetForm();
                } else {
                    Toast.makeText(getContext(), "Gagal mengirim pengaduan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetForm() {
        generateIdPengaduan();
        spinnerKategoriPengaduan.setSelection(0);
        etDeskripsiPengaduan.setText("");
        imgPrev1.setVisibility(View.GONE);
        selectedImageUri = null;
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
}
