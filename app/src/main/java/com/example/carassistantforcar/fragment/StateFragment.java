package com.example.carassistantforcar.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.carassistantforcar.AuthCar;
import com.example.carassistantforcar.R;
import com.example.carassistantforcar.entity.CarInfo;
import com.example.carassistantforcar.service.CarApi;
import com.example.carassistantforcar.service.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StateFragment extends Fragment {


    private ImageView image;
    private FragmentTransaction fragmentTransaction;
    private UserFragment userFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_state, container, false);

        startUpdate();
        Button usersListButton = view.findViewById(R.id.button);
        image = view.findViewById(R.id.imageView);
        usersListButton.setOnClickListener(v -> {
            userFragment = new UserFragment();
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragTag, userFragment).addToBackStack(null);
            fragmentTransaction.commit();
        });
        return view;
    }

    private void startUpdate() {
        boolean isRunning = true;
        new Thread(() -> {
            try {
                while (isRunning) {
                    NetworkService.getInstance()
                            .getInterface(CarApi.class)
                            .checkState(AuthCar.getCar().getCarInfo().getId())
                            .enqueue(new Callback<CarInfo>() {
                                @Override
                                public void onResponse(@NonNull Call<CarInfo> call, @NonNull Response<CarInfo> response) {
                                    if (response.body() != null) {
                                        AuthCar.getCar().setCarInfo(response.body());
                                        changeImage();
                                    } else {
                                        Toast.makeText(getContext(), "Ошибка обновления статуса!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<CarInfo> call, @NonNull Throwable t) {
                                    Log.getStackTraceString(t);
                                }
                            });
                    Thread.sleep(500);
                }
            } catch (Exception t) {
                Log.getStackTraceString(t);
            }
        }).start();
    }

    private void changeImage() {
        if (AuthCar.getCar().getCarInfo().getDoorState().equals("unlocked")) {
            image.setImageResource(R.drawable.unlock);
        } else {
            image.setImageResource(R.drawable.lock);
        }
    }

}
