package com.example.user.carnage;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class AllocateStatsDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(null);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.dialog_fragment_allocate_stats, MainActivity.player.getAvailableStatPoints()));

        builder.setPositiveButton(R.string.common_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.animateChangeWindow();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.levelUp(getFragmentManager());
                    }
                }, MainActivity.getChangeAnimationDuration());
            }
        });

        builder.setNegativeButton(R.string.common_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.animateChangeWindow();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RPGBattleFragment fragment = new RPGBattleFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, fragment, "MAIN BATTLE FRAGMENT");
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }, MainActivity.getChangeAnimationDuration());
            }
        });

        return builder.create();
    }
}
