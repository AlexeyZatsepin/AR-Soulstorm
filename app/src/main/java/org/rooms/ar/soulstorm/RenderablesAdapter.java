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


public class RenderablesAdapter extends RecyclerView.Adapter<RenderablesAdapter.ModelViewHolder> {
    private List<Building> data = new ArrayList<>();
    private OnRenderableSelectListener listener;

    public interface OnRenderableSelectListener {
        void onSelect(ModelRenderable renderable);
    }

    public RenderablesAdapter(ARActivity activity) {
        this.listener = activity;
        for (Building item : Building.values()) {
            item.initModel(activity.getApplicationContext());
            data.add(item);
        }
    }

    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_card_layout, parent, false);
        return new ModelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {
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

    class ModelViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView descriptionView;
        TextView pin;
        TextView about;

        ModelViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.media_image);
            titleView = itemView.findViewById(R.id.primary_text);
            descriptionView = itemView.findViewById(R.id.sub_text);
            about = itemView.findViewById(R.id.action_button_2);
            pin = itemView.findViewById(R.id.action_button_1);
        }
    }
}
