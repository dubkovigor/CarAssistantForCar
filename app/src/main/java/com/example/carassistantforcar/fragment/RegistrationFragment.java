package com.example.carassistantforcar.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.example.carassistantforcar.entity.CarInfo;
import com.example.carassistantforcar.service.CarApi;
import com.example.carassistantforcar.service.NetworkService;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationFragment extends Fragment {

    private FragmentTransaction fragmentTransaction;
    private StateFragment stateFragment;
    private EditText nameEditText;
    private EditText plateNumberEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        nameEditText = view.findViewById(R.id.editText);
        plateNumberEditText = view.findViewById(R.id.editText2);
        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(v -> sendRegistrationData());

        return view;
    }

    private void sendRegistrationData() {
        final CarInfo regCar = new CarInfo();
        regCar.setName(nameEditText.getText().toString());
        regCar.setPlateNumber(plateNumberEditText.getText().toString());
        NetworkService.getInstance()
                .getInterface(CarApi.class)
                .registerCar(regCar)
                .enqueue(new Callback<CarInfo>() {
                    @Override
                    public void onResponse(@NonNull Call<CarInfo> call, @NonNull Response<CarInfo> response) {
                        if (response.body() != null) {
                            AuthCar.getCar().setCarInfo(response.body());
                            AuthCar.getCar().setPhoneNumbers(Collections.emptyList());

                            stateFragment = new StateFragment();

                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragTag, stateFragment).addToBackStack(null);
                            fragmentTransaction.commit();
                        } else {
                            Toast.makeText(getContext(), "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CarInfo> call, @NonNull Throwable t) {
                        Log.getStackTraceString(t);
                    }
                });
    }
}
