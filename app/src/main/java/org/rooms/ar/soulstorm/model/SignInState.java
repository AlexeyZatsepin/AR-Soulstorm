package org.rooms.ar.soulstorm.model;


import com.google.firebase.auth.FirebaseUser;

public class SignInState {
    private static SignInState INSTANCE = new SignInState();
    private FirebaseUser user;
    private Resourses resourses;

    private SignInState(){
        resourses = new Resourses(0,0);
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

    public Resourses getResourses() {
        return resourses;
    }

    public void setResourses(Resourses resourses) {
        this.resourses = resourses;
    }
}
