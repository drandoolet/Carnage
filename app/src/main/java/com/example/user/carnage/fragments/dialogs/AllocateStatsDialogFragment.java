package com.example.user.carnage.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import com.example.user.carnage.MainActivity;
import com.example.user.carnage.R;
import com.example.user.carnage.fragments.RPGBattleFragment;

public class AllocateStatsDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(null);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.dialog_fragment_allocate_stats, MainActivity.player.getAvailableStatPoints()));

        builder.setPositiveButton(R.string.common_yes, (dialogInterface, i) -> {
            MainActivity.animateChangeWindow();
            MainActivity.levelUp(getFragmentManager()); /*
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.levelUp(getFragmentManager());
                }
            }, MainActivity.getChangeAnimationDuration()); */
        });

        builder.setNegativeButton(R.string.common_no, (dialogInterface, i) -> {
            MainActivity.animateChangeWindow();
            new Handler().postDelayed(() -> {
                RPGBattleFragment fragment = new RPGBattleFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment, "MAIN BATTLE FRAGMENT");
                transaction.addToBackStack(null);
                transaction.commit();
            }, MainActivity.getChangeAnimationDuration());
        });

        return builder.create();
    }
}
