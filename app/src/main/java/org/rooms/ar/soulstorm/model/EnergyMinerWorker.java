package org.rooms.ar.soulstorm.model;

import android.util.Log;


public class EnergyMinerWorker implements Runnable {

    static final String TAG = "EnergyMinerWorker";

    @Override
    public void run() {
        Log.d(TAG, "EnergyMinerWorker: start");

        MyResources current = SignInState.getInstance().getResources().getValue();
        if (current!=null) SignInState.getInstance().getResources().postValue(current.increase());

        Log.d(TAG, "EnergyMinerWorker: end");
    }
}
