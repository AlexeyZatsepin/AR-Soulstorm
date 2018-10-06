package org.rooms.ar.soulstorm.model;


import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

public class SignInState {
    private static SignInState INSTANCE = new SignInState();
    private FirebaseUser user;
    private MutableLiveData<MyResources> resources;

    private SignInState(){
        resources = new MutableLiveData<>();
    }

    public static SignInState getInstance() {
        return INSTANCE;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
        DatabaseManager.getInstance().requestResources();
    }

    public MutableLiveData<MyResources> getResources() {
        return resources;
    }

    public void setResources(MyResources resources) {
        this.resources.postValue(resources);
    }
}
