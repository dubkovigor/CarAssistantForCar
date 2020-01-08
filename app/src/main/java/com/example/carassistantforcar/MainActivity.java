package com.example.carassistantforcar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.carassistantforcar.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;
    MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragTag, mainFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (FragmentManager.POP_BACK_STACK_INCLUSIVE == 1) {
            super.onBackPressed();
        } else
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
