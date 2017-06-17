package com.designfreed.grasigas_app_diaria.service;

import com.designfreed.grasigas_app_diaria.model.Chofer;

public class AuthService {
    private static AuthService instance = null;

    private Chofer currentUser;

    private AuthService() {

    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }

        return instance;
    }

    public Chofer getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Chofer currentUser) {
        this.currentUser = currentUser;
    }

    public void logout() {
        this.currentUser = null;
    }
}
