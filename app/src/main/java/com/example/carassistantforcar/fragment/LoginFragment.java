package com.example.carassistantforcar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.carassistantforcar.AuthCar;
import com.example.carassistantforcar.R;
import com.example.carassistantforcar.entity.Car;
import com.example.carassistantforcar.entity.CarInfo;
import com.example.carassistantforcar.service.CarApi;
import com.example.carassistantforcar.service.NetworkService;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {

    private EditText plateNumberEditText;
    private FragmentTransaction fragmentTransaction;
    private StateFragment stateFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button button = view.findViewById(R.id.button);
        plateNumberEditText = view.findViewById(R.id.editText);
        button.setOnClickListener(v -> sendLogInData());
        return view;
    }

    private void sendLogInData() {
        CarInfo car = new CarInfo();
        car.setPlateNumber(plateNumberEditText.getText().toString());
        NetworkService.getInstance()
                .getInterface(CarApi.class)
                .loginCar(car)
                .enqueue(new Callback<Car>() {
                    @Override
                    public void onResponse(@NonNull Call<Car> call, @NonNull Response<Car> response) {
                        if (response.body() != null) {
                            AuthCar.setCar(response.body());
                            stateFragment = new StateFragment();
                            fragmentTransaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                            fragmentTransaction.replace(R.id.fragTag, stateFragment).addToBackStack(null);
                            fragmentTransaction.commit();
                        } else {
                            Toast.makeText(getContext(), "Ошибка авторизации!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Car> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), "Ошибка авторизации!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
