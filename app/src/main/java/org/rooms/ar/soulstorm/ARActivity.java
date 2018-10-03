package org.rooms.ar.soulstorm;

import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.PlaneRenderer;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import org.rooms.ar.soulstorm.model.Building;
import org.rooms.ar.soulstorm.model.DatabaseManager;
import org.rooms.ar.soulstorm.model.MyResources;
import org.rooms.ar.soulstorm.model.SignInState;

import java.util.concurrent.CompletableFuture;

public class ARActivity extends AppCompatActivity implements RenderablesAdapter.OnRenderableSelectListener {
    private static final String TAG = ARActivity.class.getSimpleName();

    private ArFragment arFragment;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView energyLevelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ux);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RenderablesAdapter adapter = new RenderablesAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        energyLevelTextView = findViewById(R.id.energy_level);
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        arFragment.getArSceneView().getScene().addOnUpdateListener(new Scene.OnUpdateListener() {
            @Override
            public void onUpdate(FrameTime frameTime) {
                initMenu();
                arFragment.getArSceneView().getScene().removeOnUpdateListener(this);
            }
        });

        Texture.Sampler sampler =
                Texture.Sampler.builder()
                        .setMagFilter(Texture.Sampler.MagFilter.LINEAR)
                        .setMinFilter(Texture.Sampler.MinFilter.LINEAR)
                        .setWrapMode(Texture.Sampler.WrapMode.REPEAT)
                        .build();

        CompletableFuture<Texture> trigrid = Texture.builder()
                .setSource(this, R.drawable.texture_grass)
                .setSampler(sampler).build();

        PlaneRenderer planeRenderer = arFragment.getArSceneView().getPlaneRenderer();
        planeRenderer.getMaterial().thenAcceptBoth(trigrid, (material, texture) -> {
            material.setTexture(PlaneRenderer.MATERIAL_TEXTURE, texture);
            material.setFloat2(PlaneRenderer.MATERIAL_UV_SCALE, 100.0f, 100.0f);
        });

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    //TODO tutorial
        });

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MutableLiveData<MyResources> liveData = SignInState.getInstance().getResources();
        liveData.observe(this, resources -> {
            DatabaseManager.getInstance().saveResources(resources);
            energyLevelTextView.setText(String.valueOf(resources.getEnergy()));
        });
    }

    private void initMenu() {
        Frame frame = arFragment.getArSceneView().getArFrame();
        Pose pose = frame.getCamera().getPose().compose(Pose.makeTranslation(0.00f, -0.4f, -1f));
        Node node = new Node();
        node.setParent(arFragment.getArSceneView().getScene());
        float[] v3 = pose.getTranslation();
        node.setLocalPosition(new Vector3(v3[0], v3[1], v3[2]));
        ViewRenderable.builder()
                .setView(getApplicationContext(), R.layout.ar_main_menu)
                .build()
                .thenAccept(
                        (renderable) -> {
                            LinearLayout ll = (LinearLayout) renderable.getView();
                            ll.findViewById(R.id.start).setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_LONG).show());
                            ll.findViewById(R.id.exit).setOnClickListener(v -> finish());
                            node.setRenderable(renderable);
                        })
                .exceptionally(
                        (throwable) -> {
                            throw new AssertionError("Could not load plane card view.", throwable);
                        });
    }

    @Override
    public void onSelect(Building item) {
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (item.getRendarable() == null) {
                        return;
                    }
                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor. (arFragment.getTransformationSystem())
                    Node node = new Node();
                    node.setParent(anchorNode);
                    node.setLocalScale(new Vector3(0.3f, 0.3f, 0.3f));
                    node.setRenderable(item.getRendarable());
//                    node.select();

                    MyResources res = SignInState.getInstance().getResources().getValue();
                    if (res != null) res.addBuilding(item);

                    Node infoCard = new Node();
                    infoCard.setParent(node);
                    infoCard.setEnabled(false);
                    infoCard.setLocalPosition(new Vector3(0.0f, 1f, 0.0f));
                    infoCard.setLocalScale(new Vector3(2f, 2f, 2f));

                    ViewRenderable.builder()
                            .setView(getApplicationContext(), R.layout.ar_info_card_layout)
                            .build()
                            .thenAccept(
                                    (r) -> {
                                        infoCard.setRenderable(r);
                                        View view = r.getView();
                                    })
                            .exceptionally(
                                    (throwable) -> {
                                        throw new AssertionError("Could not load info card view.", throwable);
                                    });

                    node.setOnTapListener((hitTestResult, motionEvent1) -> infoCard.setEnabled(true));
                    infoCard.setOnTapListener((hitTestResult, motionEvent12) -> infoCard.setEnabled(!infoCard.isEnabled()));
                });
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public static void openPopup(View view, Building item) {
        LayoutInflater inflater = (LayoutInflater)
                view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.component_popup_window, null);

        int width = (int) (300 * view.getContext().getResources().getDisplayMetrics().density);
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        ImageView imageView = popupView.findViewById(R.id.media_image);
        imageView.setImageDrawable(view.getContext().getDrawable(item.getImage()));
        TextView titleView = popupView.findViewById(R.id.primary_text);
        titleView.setText(item.getTitle());
        TextView descriptionView = popupView.findViewById(R.id.sub_text);
        descriptionView.setText(item.getDescription());
        popupView.findViewById(R.id.close).setOnClickListener(v->popupWindow.dismiss());
    }


}
