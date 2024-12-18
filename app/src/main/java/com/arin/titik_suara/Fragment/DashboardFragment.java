package com.arin.titik_suara.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.arin.titik_suara.CobaActivity;
import com.arin.titik_suara.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Inisialisasi FloatingActionButton
        FloatingActionButton fabTambahPengaduan = view.findViewById(R.id.fab_add);

        // Set onClickListener untuk tombol tambah pengaduan
        fabTambahPengaduan.setOnClickListener(v -> {
            // Ganti fragment ke TambahPengaduanFragment
            Fragment tambahPengaduanFragment = new CobaFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, tambahPengaduanFragment); // R.id.fragment_container adalah layout tempat fragment ditampilkan di MainActivity
            transaction.addToBackStack(null); // Agar bisa kembali ke fragment sebelumnya
            transaction.commit();
        });

        return view;
    }
}
