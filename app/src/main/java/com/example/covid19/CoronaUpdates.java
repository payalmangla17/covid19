package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class CoronaUpdates extends Fragment {
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

    }
}
