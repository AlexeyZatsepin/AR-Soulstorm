package org.rooms.ar.soulstorm;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.RawRes;
import android.view.Gravity;
import android.widget.Toast;

import com.google.ar.sceneform.rendering.ModelRenderable;

public class Building {
    @DrawableRes
    private int image;
    private String title;
    private String description;
    private ModelRenderable rendarable;

    public Building(int image, String title, String description, @RawRes int resId, Context context) {
        this.image = image;
        this.title = title;
        this.description = description;
        initModel(resId, context);
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ModelRenderable getRendarable() {
        return rendarable;
    }

    public void setRendarable(ModelRenderable rendarable) {
        this.rendarable = rendarable;
    }

    private void initModel(@RawRes int resId, Context context) {
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
