package org.rooms.ar.soulstorm.model;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;

public class EnergyMinerWorker extends Worker {

    static final String TAG = "EnergyMinerWorker";

    @NonNull
    @Override
    public Worker.Result doWork() {
        Log.d(TAG, "EnergyMinerWorker: start");

        MyResources current = SignInState.getInstance().getResources().getValue();
        current.setEnergy(current.increase());
        SignInState.getInstance().getResources().postValue(current);

        Log.d(TAG, "EnergyMinerWorker: end");

        return Worker.Result.SUCCESS;
    }
}
