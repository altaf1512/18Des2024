package com.arin.titik_suara.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.arin.titik_suara.R;

public class PengaduanFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_pengaduan, container, false);
    }
}
