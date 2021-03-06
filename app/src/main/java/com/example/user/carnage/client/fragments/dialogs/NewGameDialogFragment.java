package com.example.user.carnage.client.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.user.carnage.client.MainActivity;
import com.example.user.carnage.R;

public class NewGameDialogFragment extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_newgame_title);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.common_no, (dialogInterface, i) -> {});

        builder.setNegativeButton(
                R.string.common_yes,
                (dialogInterface, i) -> MainActivity.newGame(getFragmentManager()));


        return builder.create();
    }
}
