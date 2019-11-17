package com.example.user.carnage.client.fragments.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import com.example.user.carnage.client.MainActivity;
import com.example.user.carnage.R;
import com.example.user.carnage.client.fragments.ProfileChooseFragment;

public class ExitDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_exit_title);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.common_no, (dialogInterface, i) -> {});

        builder.setNegativeButton(R.string.common_yes, (dialogInterface, i) -> {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);

            goToMainMenu();
        });


        return builder.create();
    }

    private void goToMainMenu() {
        if (MainActivity.player != null) MainActivity.player.refresh();
        if (MainActivity.enemy != null) MainActivity.enemy.refresh();
        ProfileChooseFragment fragment = new ProfileChooseFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
