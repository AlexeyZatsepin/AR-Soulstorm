package org.rooms.ar.soulstorm.dialogs;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.rooms.ar.soulstorm.R;
import org.rooms.ar.soulstorm.model.Building;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public final class PopupWindows {

    public static void openInfoPopup(View view, Building item) {
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
