package com.arin.titik_suara.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.arin.titik_suara.LoginActivity;
import com.arin.titik_suara.R;
import com.arin.titik_suara.Util.Database;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfilFragment extends Fragment {
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    private Database dbHelper;

    TextView btnLogout, profileName, profileID, profileJabatan, profileAddress, profilePhone;
    ImageView profileIcon, cameraIcon;
    Button btnChangePassword;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        dbHelper = new Database(getContext());
        sharedPreferences = getActivity().getSharedPreferences("user_prefs", Activity.MODE_PRIVATE);
        initializeViews(view);
        setupClickListeners();
        loadProfileData();

        return view;
    }

    private void initializeViews(View view) {
        profileIcon = view.findViewById(R.id.profile_icon);
        profileName = view.findViewById(R.id.profile_name);
        profileID = view.findViewById(R.id.profile_id);
        profileJabatan = view.findViewById(R.id.profile_jabatan);
        profileAddress = view.findViewById(R.id.profile_address);
        profilePhone = view.findViewById(R.id.profile_phone);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnLogout = view.findViewById(R.id.btnLogout);
        cameraIcon = view.findViewById(R.id.camera_icon);

        profileIcon.setClipToOutline(true);
        profileIcon.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });
    }

    private void loadProfileData() {
        String id = sharedPreferences.getString("id_user", "");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("user", null, "id_user=?", new String[]{id}, null, null, null);

        if (cursor.moveToFirst()) {
            String nama = cursor.getString(cursor.getColumnIndexOrThrow("nama"));
            String jabatan = cursor.getString(cursor.getColumnIndexOrThrow("jabatan"));
            String alamat = cursor.getString(cursor.getColumnIndexOrThrow("alamat"));
            String noTelp = cursor.getString(cursor.getColumnIndexOrThrow("no_telepon"));
            String fotoProfil = cursor.getString(cursor.getColumnIndexOrThrow("foto_profil"));

            profileName.setText(nama);
            profileID.setText("ID: " + id);
            profileJabatan.setText("Jabatan: " + jabatan);
            profileAddress.setText("Alamat: " + alamat);
            profilePhone.setText("No Telp: " + noTelp);

            if (!TextUtils.isEmpty(fotoProfil)) {
                Glide.with(requireContext())
                        .load(Uri.parse(fotoProfil))
                        .circleCrop()
                        .placeholder(R.drawable.baseline_person_24)
                        .error(R.drawable.baseline_person_24)
                        .into(profileIcon);
            }
        }
        cursor.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            if (data != null) {
                imageUri = data.getData();
                String id = sharedPreferences.getString("id_user", "");

                String currentNama = profileName.getText().toString();
                String currentJabatan = profileJabatan.getText().toString();
                String currentAlamat = profileAddress.getText().toString();
                String currentNoTelp = profilePhone.getText().toString();
                String fotoUri = imageUri != null ? imageUri.toString() : null;

                dbHelper.updateUser(id, currentNama, currentJabatan, currentAlamat, currentNoTelp, fotoUri);

                Glide.with(requireContext())
                        .load(imageUri)
                        .circleCrop()
                        .into(profileIcon);
            }
        }
    }

    private void showChangePasswordDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        builder.setView(view);
        builder.setPositiveButton("Ubah", (dialog, which) -> {
            // Logika pengubahan password
        });
        builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void setupClickListeners() {
        cameraIcon.setOnClickListener(v -> openGallery());
        btnLogout.setOnClickListener(v -> showLogoutConfirmationDialog()); // Ganti di sini
        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());
    }

    private void showLogoutConfirmationDialog() {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya", (dialog, which) -> logout())
                .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void logout() {
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
}
