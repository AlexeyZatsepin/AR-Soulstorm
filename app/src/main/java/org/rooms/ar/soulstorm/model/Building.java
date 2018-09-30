package org.rooms.ar.soulstorm.model;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.Toast;

import com.google.ar.sceneform.rendering.ModelRenderable;

import org.rooms.ar.soulstorm.R;

public enum Building {
    FIREBASE   (0, 0, 100, R.drawable.img_firebase, R.string.firebase, R.string.about, R.raw.firebasetau),
    DEFENSE    (0, 100, 100, R.drawable.img_defense, R.string.defense, R.string.about, R.raw.taudefense),
    BARRACK    (0, 50, 100, R.drawable.img_barrack, R.string.barrack, R.string.about, R.raw.taubarracks),
    STATION    (10, 0, 50, R.drawable.img_station, R.string.station, R.string.about, R.raw.voidillumitus),
    GENERATOR  (30, 0, 150, R.drawable.img_generator, R.string.generator, R.string.about, R.raw.plasmagenerator),
    MONKA      (0, 0, 200, R.drawable.img_monka, R.string.monka, R.string.about, R.raw.pcmonka),
    CENTRE     (10, 0, 70, R.drawable.img_centre, R.string.celouie, R.string.about, R.raw.celouie),
    KROOT      (10, 0, 90, R.drawable.img_kroot, R.string.kroot_facility, R.string.about, R.raw.krootfacility),
    KAIUNE     (10, 0, 60, R.drawable.img_kaiune, R.string.kaiune, R.string.about, R.raw.pckaiune),
    LANDING_PAD(10, 0, 300, R.drawable.img_pad, R.string.landing_pad, R.string.about, R.raw.taulandingpad),
    SHIP       (0, 0, 0, R.drawable.img_ship, R.string.ship, R.string.about, R.raw.scene);

    private float energyBoost;
    private float battleBoost;
    private int coast;

    @DrawableRes
    private int image;
    @StringRes
    private int title;
    @StringRes
    private int description;
    @RawRes
    private int resId;

    private ModelRenderable rendarable;

    Building(float energyBoost, float battleBoost, int coast, @DrawableRes int image, @StringRes int title, @StringRes int description, @RawRes int resId) {
        this.energyBoost = energyBoost;
        this.battleBoost = battleBoost;
        this.coast = coast;
        this.image = image;
        this.title = title;
        this.description = description;
        this.resId = resId;
    }

    public int getImage() {
        return image;
    }

    public int getTitle() {
        return title;
    }

    public int getDescription() {
        return description;
    }

    public ModelRenderable getRendarable() {
        return rendarable;
    }

    private void setRendarable(ModelRenderable rendarable) {
        this.rendarable = rendarable;
    }

    public float getEnergyBoost() {
        return energyBoost;
    }

    public float getBattleBoost() {
        return battleBoost;
    }

    public int getCoast() {
        return coast;
    }

    public void initModel(Context context) {
        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(context, resId)
                .build()
                .thenAccept(this::setRendarable)
                .exceptionally(
                        throwable -> {
                            Toast toast = Toast.makeText(context, "Unable to load rendarable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
    }
}
