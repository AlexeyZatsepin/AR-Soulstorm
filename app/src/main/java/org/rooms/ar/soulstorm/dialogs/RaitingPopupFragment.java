package org.rooms.ar.soulstorm.dialogs;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.rooms.ar.soulstorm.R;
import org.rooms.ar.soulstorm.model.DatabaseManager;
import org.rooms.ar.soulstorm.model.MyResources;
import org.rooms.ar.soulstorm.model.User;

import java.util.List;

public class RaitingPopupFragment extends DialogFragment {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private List<Pair<User,MyResources>> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_raiting, container);
        recyclerView = root.findViewById(R.id.recyclerView);
        progressBar = root.findViewById(R.id.progress);
        update();
        return root;
    }

    private void update() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().post(() -> {
            try {
                data = DatabaseManager.getInstance().getUserData();
                recyclerView.setAdapter(new RaitingAdapter());
                progressBar.setVisibility(View.GONE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    class RaitingAdapter extends RecyclerView.Adapter<PersonViewHolder>{

        @NonNull
        @Override
        public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_person_card, parent, false);
            return new PersonViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
            Pair<User,MyResources> item = data.get(position);
            holder.titleView.setText(item.first.getDisplayName());
            holder.imageView.setImageURI(item.first.getUri());
            holder.energyView.setText(String.valueOf(item.second.getEnergy()));
            holder.battleView.setText(String.valueOf(item.second.getForce()));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleView, energyView, battleView;

        public PersonViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.avatar_image);
            titleView = itemView.findViewById(R.id.title_text);
            energyView = itemView.findViewById(R.id.energy_text);
            battleView = itemView.findViewById(R.id.battle_text);
        }
    }
}
