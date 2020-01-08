package com.example.carassistantforcar;

import com.example.carassistantforcar.entity.Car;

public class AuthCar {

    private static Car car;

    public static void setCar(Car car) {
        AuthCar.car = car;
    }

    public static Car getCar() {
        return AuthCar.car;
    }
}
