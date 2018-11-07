package org.rooms.ar.soulstorm.dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rooms.ar.soulstorm.R;

import de.blox.graphview.GraphView;

public class BuildingTreeFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_building_tree, container);
        GraphView view = root.findViewById(R.id.graph);

        return root;
    }
}
