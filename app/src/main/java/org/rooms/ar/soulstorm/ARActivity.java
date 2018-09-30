package org.rooms.ar.soulstorm;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
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
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import org.rooms.ar.soulstorm.model.SignInState;

public class ARActivity extends AppCompatActivity implements RenderablesAdapter.OnRenderableSelectListener {
    private static final String TAG = ARActivity.class.getSimpleName();

    private ArFragment arFragment;
    private RenderablesAdapter adapter;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView energyLevelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ux);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new RenderablesAdapter(this);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        SignInState.getInstance().getResourses().observe(this,
                resourses -> energyLevelTextView.setText(String.valueOf(resourses.getEnergy())));
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
                            ll.findViewById(R.id.start).setOnClickListener(v->Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_LONG).show());
                            ll.findViewById(R.id.exit).setOnClickListener(v->finish());
                            node.setRenderable(renderable);
                        })
                .exceptionally(
                        (throwable) -> {
                            throw new AssertionError("Could not load plane card view.", throwable);
                        });
    }

    @Override
    public void onSelect(ModelRenderable renderable) {
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (renderable == null) {
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
                    node.setRenderable(renderable);
//                    node.select();

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

  
}
