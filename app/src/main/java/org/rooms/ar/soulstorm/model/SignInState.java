package org.rooms.ar.soulstorm.model;


import android.arch.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

public class SignInState {
    private static SignInState INSTANCE = new SignInState();
    private FirebaseUser user;
    private MutableLiveData<Resources> resourses;

    private SignInState(){
        resourses = new MutableLiveData<>();
    }

    public static SignInState getInstance() {
        return INSTANCE;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public MutableLiveData<Resources> getResourses() {
        return resourses;
    }

    public void setResourses(Resources resources) {
        this.resourses.postValue(resources);
    }
}
