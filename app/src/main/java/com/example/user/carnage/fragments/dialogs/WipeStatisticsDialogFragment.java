package com.example.user.carnage.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.user.carnage.MainActivity;
import com.example.user.carnage.R;

public class WipeStatisticsDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_wipe_stats_title);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.dialog_wipe_stats_message) + (MainActivity.getStatsSum() - 8)
                + getString(R.string.dialog_wipe_stats_comfirmation));

        builder.setPositiveButton(
                R.string.common_no,
                (dialogInterface, i) ->
                        Toast.makeText(getContext(), "Statistics have not changed.", Toast.LENGTH_SHORT).show());

        builder.setNegativeButton(
                R.string.common_yes,
                (dialogInterface, i) -> {
                    MainActivity.wipeNeuralNetStatistics(MainActivity.currentProfile);
                    Toast.makeText(getContext(), "Statistics are wiped.", Toast.LENGTH_SHORT).show();
                });


        return builder.create();
    }
}
