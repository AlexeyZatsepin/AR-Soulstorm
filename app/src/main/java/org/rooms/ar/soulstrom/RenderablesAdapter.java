package org.rooms.ar.soulstrom;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.sceneform.rendering.ModelRenderable;

import org.rooms.ar.soulstorm.R;

import java.util.ArrayList;
import java.util.List;


public class RenderablesAdapter extends RecyclerView.Adapter<RenderablesAdapter.RenderableViewHolder> {
    private List<RenderableInfo> data = new ArrayList<>();
    private OnRenderableSelectListener listener;

    public interface OnRenderableSelectListener {
        void onSelect(ModelRenderable renderable);
    }

    public RenderablesAdapter(ARActivity activity) {
        this.listener = activity;
        data.add(new RenderableInfo(R.drawable.firebase, "Firebase", "About", R.raw.firebasetau, activity));
        data.add(new RenderableInfo(R.drawable.defense, "Defense", "About", R.raw.taudefense, activity));
        data.add(new RenderableInfo(R.drawable.barrack, "Barrack", "About", R.raw.taubarracks, activity));
        data.add(new RenderableInfo(R.drawable.station, "Station", "About", R.raw.voidillumitus, activity));
        data.add(new RenderableInfo(R.drawable.generator, "Generator", "About", R.raw.plasmagenerator, activity));
    }

    @NonNull
    @Override
    public RenderableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RenderableViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RenderableViewHolder holder, int position) {
        RenderableInfo info = data.get(position);
        holder.imageView.setImageDrawable(holder.itemView.getContext().getDrawable(info.image));
        holder.titleView.setText(info.title);
        holder.descriptionView.setText(info.description);
        holder.about.setText("About");
        holder.pin.setText("Pin");
        holder.pin.setOnClickListener(v -> listener.onSelect(info.rendarable));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RenderableViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView descriptionView;
        Button pin;
        Button about;

        public RenderableViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.media_image);
            titleView = itemView.findViewById(R.id.primary_text);
            descriptionView = itemView.findViewById(R.id.sub_text);
            about = itemView.findViewById(R.id.action_button_2);
            pin = itemView.findViewById(R.id.action_button_1);
        }
    }

    public class RenderableInfo {
        @DrawableRes
        int image;
        String title;
        String description;
        ModelRenderable rendarable;

        private RenderableInfo(int image, String title, String description, @RawRes int resId, Context context) {
            this.image = image;
            this.title = title;
            this.description = description;
            initModel(resId, context);
        }

        private void initModel(@RawRes int resId, Context context) {
            // When you build a Renderable, Sceneform loads its resources in the background while returning
            // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
            ModelRenderable.builder()
                    .setSource(context, resId)
                    .build()
                    .thenAccept(r -> this.rendarable = r)
                    .exceptionally(
                            throwable -> {
                                Toast toast = Toast.makeText(context, "Unable to load rendarable", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return null;
                            });
        }
    }
}
