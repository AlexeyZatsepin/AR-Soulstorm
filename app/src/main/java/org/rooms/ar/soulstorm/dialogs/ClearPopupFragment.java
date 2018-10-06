package org.rooms.ar.soulstorm.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import org.rooms.ar.soulstorm.R;

public class ClearPopupFragment extends DialogFragment {

    private DialogInterface.OnClickListener listener;
    private @StringRes int message;

    public void setListener(DialogInterface.OnClickListener listener, @StringRes int message) {
        this.listener = listener;
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext())
                .setTitle(R.string.clear)
                .setPositiveButton(R.string.ok, listener)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setMessage(message);
        return adb.create();
    }
}
