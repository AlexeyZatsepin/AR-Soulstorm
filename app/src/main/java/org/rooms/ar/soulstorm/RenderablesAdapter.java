package org.rooms.ar.soulstorm;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.rooms.ar.soulstorm.dialogs.PopupWindows;
import org.rooms.ar.soulstorm.model.Building;
import org.rooms.ar.soulstorm.model.MyResources;
import org.rooms.ar.soulstorm.model.SignInState;

import java.util.ArrayList;
import java.util.List;


public class RenderablesAdapter extends RecyclerView.Adapter<RenderablesAdapter.ModelViewHolder> {
    private List<Building> data = new ArrayList<>();
    private OnRenderableSelectListener listener;
    private LifecycleOwner lifecycleObserver;
    
    public interface OnRenderableSelectListener {
        void onSelect(Building info);
    }

    public RenderablesAdapter(ARActivity activity) {
        this.listener = activity;
        lifecycleObserver = activity;
        for (Building item : Building.values()) {
            item.initModel(activity.getApplicationContext());
            data.add(item);
        }
    }

    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_building_card, parent, false);
        return new ModelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {
        Building info = data.get(position);
        MutableLiveData<MyResources> resoursesLiveData = SignInState.getInstance().getResources();
        holder.imageView.setImageDrawable(holder.itemView.getContext().getDrawable(info.getImage()));
        holder.titleView.setText(info.getTitle());
        holder.descriptionView.setText(info.getDescription());
        holder.coast.setText(String.valueOf(info.getCoast()));
        holder.pin.setOnClickListener(v -> {
            listener.onSelect(info);
            MyResources resources = resoursesLiveData.getValue();
            resources.setEnergy(resources.getEnergy()-info.getCoast());
            resoursesLiveData.postValue(resources);
        });
        resoursesLiveData.observe(lifecycleObserver, resources -> {
            boolean condition = resources.getEnergy()>=info.getCoast();
            holder.pin.setEnabled(condition);
            Resources res = holder.itemView.getResources();
            holder.coast.setTextColor(condition? res.getColor(android.R.color.white) : res.getColor(android.R.color.holo_red_dark));
            holder.itemView.setAlpha(condition? 1 : 0.5f);
        });
        holder.about.setOnClickListener(v-> PopupWindows.openInfoPopup(holder.itemView, info));
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
        TextView coast;

        ModelViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.media_image);
            titleView = itemView.findViewById(R.id.primary_text);
            descriptionView = itemView.findViewById(R.id.sub_text);
            about = itemView.findViewById(R.id.action_button_2);
            pin = itemView.findViewById(R.id.action_button_1);
            coast = itemView.findViewById(R.id.energy_cost);
        }
    }
}
