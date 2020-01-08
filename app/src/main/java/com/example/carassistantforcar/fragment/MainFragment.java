package com.example.carassistantforcar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.carassistantforcar.AuthCar;
import com.example.carassistantforcar.R;
import com.example.carassistantforcar.entity.Car;


public class MainFragment extends Fragment {

    private FragmentTransaction fragmentTransaction;
    private LoginFragment loginFragment;
    private RegistrationFragment registrationFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        AuthCar.setCar(new Car());
        Button logInButton = view.findViewById(R.id.button);
        logInButton.setOnClickListener(v -> {
            loginFragment = new LoginFragment();
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragTag, loginFragment).addToBackStack(null);
            fragmentTransaction.commit();
        });
        Button registrationButton = view.findViewById(R.id.button2);
        registrationButton.setOnClickListener(v -> {
            registrationFragment = new RegistrationFragment();
            fragmentTransaction = getFragmentManager().beginTransaction().addToBackStack(null);
            fragmentTransaction.replace(R.id.fragTag, registrationFragment);
            fragmentTransaction.commit();
        });

        return view;
    }

}
