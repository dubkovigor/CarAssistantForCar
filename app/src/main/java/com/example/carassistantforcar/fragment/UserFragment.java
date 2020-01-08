package com.example.carassistantforcar.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.carassistantforcar.AuthCar;
import com.example.carassistantforcar.R;
import com.example.carassistantforcar.entity.Car;
import com.example.carassistantforcar.entity.UserForCar;
import com.example.carassistantforcar.service.CarApi;
import com.example.carassistantforcar.service.NetworkService;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserFragment extends Fragment {

    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ListView userList = view.findViewById(R.id.userList);

        adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                android.R.layout.simple_list_item_1);
        userList.setAdapter(adapter);
        adapter.addAll(AuthCar.getCar().getPhoneNumbers());
        Button addUserButton = view.findViewById(R.id.button);
        addUserButton.setOnClickListener(v -> showAddUserDialog());
        userList.setOnItemClickListener((parent, view1, position, id) -> {
            String phoneNumber = (String) userList.getItemAtPosition(position);
            showDeleteUserDialog(phoneNumber);
        });

        return view;
    }

    private void showDeleteUserDialog(final String phoneNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Удалить пользователя номером телефона: " + phoneNumber + "?")
                .setPositiveButton("Да", (dialog, which) -> deleteUser(phoneNumber)).setCancelable(false)
                .setCancelable(false)
                .setNegativeButton("Закрыть", (dialog, which) ->
                        dialog.cancel());
        builder.create().show();
    }

    private void deleteUser(final String phoneNumber) {
        UserForCar userForCar = new UserForCar();
        userForCar.setPhoneNumbers(phoneNumber);
        NetworkService.getInstance()
                .getInterface(CarApi.class)
                .deleteUser(userForCar, AuthCar.getCar().getCarInfo().getId())
                .enqueue(new Callback<Car>() {
                    @Override
                    public void onResponse(@NonNull Call<Car> call, @NonNull Response<Car> response) {
                        if (response.body() != null) {
                            adapter.remove(phoneNumber);
                        } else {
                            Toast.makeText(getContext(), "Ошибка удаления", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Car> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void addUser(String phoneNumber) {
        UserForCar userForCar = new UserForCar();
        userForCar.setPhoneNumbers(phoneNumber);
        NetworkService.getInstance()
                .getInterface(CarApi.class)
                .addUser(userForCar, AuthCar.getCar().getCarInfo().getId())
                .enqueue(new Callback<Car>() {
                    @Override
                    public void onResponse(@NonNull Call<Car> call, @NonNull Response<Car> response) {
                        if (response.body() != null) {
                            AuthCar.setCar(response.body());
                            adapter.clear();
                            adapter.addAll(AuthCar.getCar().getPhoneNumbers());
                        } else {
                            Toast.makeText(getContext(), "Ошибка добавления!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Car> call, @NonNull Throwable t) {
                        Log.getStackTraceString(t);

                    }
                });
    }

    private void showAddUserDialog() {
        final EditText editText = new EditText(getContext());
        editText.setHint("Телефонный номер");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Введите телефонный номер, который вы хотите добавить")
                .setView(editText)
                .setPositiveButton("Добавить", (dialog, which) ->
                        addUser(editText.getText().toString())).setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }
}
