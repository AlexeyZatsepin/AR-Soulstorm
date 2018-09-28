package org.rooms.ar.soulstorm;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.ar.sceneform.rendering.ModelRenderable;

import org.rooms.ar.soulstorm.model.Building;

import java.util.ArrayList;
import java.util.List;


public class RenderablesAdapter extends RecyclerView.Adapter<RenderablesAdapter.RenderableViewHolder> {
    private List<Building> data = new ArrayList<>();
    private OnRenderableSelectListener listener;

    public interface OnRenderableSelectListener {
        void onSelect(ModelRenderable renderable);
    }

    public RenderablesAdapter(ARActivity activity) {
        this.listener = activity;
        data.add(new Building(R.drawable.img_firebase, "Firebase", "About", R.raw.firebasetau, activity));
        data.add(new Building(R.drawable.img_defense, "Defense", "About", R.raw.taudefense, activity));
        data.add(new Building(R.drawable.img_barrack, "Barrack", "About", R.raw.taubarracks, activity));
        data.add(new Building(R.drawable.img_station, "Station", "About", R.raw.voidillumitus, activity));
        data.add(new Building(R.drawable.img_generator, "Generator", "About", R.raw.plasmagenerator, activity));
        data.add(new Building(R.drawable.img_monka, "Monka", "About", R.raw.pcmonka, activity));
        data.add(new Building(R.drawable.img_centre, "Celouie", "About", R.raw.celouie, activity));
        data.add(new Building(R.drawable.img_kroot, "Kroot Facility", "About", R.raw.krootfacility, activity));
        data.add(new Building(R.drawable.img_kaiune, "Kaiune PC", "About", R.raw.pckaiune, activity));
        data.add(new Building(R.drawable.img_pad, "Landing Pad", "About", R.raw.taulandingpad, activity));
        data.add(new Building(R.drawable.img_ship, "Ship", "About", R.raw.scene, activity));
    }

    @NonNull
    @Override
    public RenderableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_card_layout, parent, false);
        return new RenderableViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RenderableViewHolder holder, int position) {
        Building info = data.get(position);
        holder.imageView.setImageDrawable(holder.itemView.getContext().getDrawable(info.getImage()));
        holder.titleView.setText(info.getTitle());
        holder.descriptionView.setText(info.getDescription());
        holder.pin.setOnClickListener(v -> listener.onSelect(info.getRendarable()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RenderableViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView descriptionView;
        TextView pin;
        TextView about;

        RenderableViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.media_image);
            titleView = itemView.findViewById(R.id.primary_text);
            descriptionView = itemView.findViewById(R.id.sub_text);
            about = itemView.findViewById(R.id.action_button_2);
            pin = itemView.findViewById(R.id.action_button_1);
        }
    }
}
