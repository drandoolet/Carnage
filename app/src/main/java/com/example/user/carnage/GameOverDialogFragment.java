package com.example.user.carnage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

public class GameOverDialogFragment extends DialogFragment {
    private String message;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.gameover_dialog_title);
        builder.setCancelable(false);

        message = getString(R.string.gameover_dialog_winner) + MainActivity.getSharedWinner() +'\n'+
                getString(R.string.gameover_dialog_hits) + MainActivity.getSharedHits() +'\n'+
                getString(R.string.gameover_dialog_criticals) + MainActivity.getSharedCriticals() +'\n'+
                getString(R.string.gameover_dialog_block_breaks) + MainActivity.getSharedBlockBreaks() +'\n'+
                getString(R.string.gameover_dialog_blocks) + MainActivity.getSharedBlocks() +'\n'+
                getString(R.string.gameover_dialog_dodges) + MainActivity.getSharedDodges();
        builder.setMessage(message);

        builder.setPositiveButton(R.string.gameover_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.newGame(getFragmentManager());
            }
        });

        builder.setNegativeButton(R.string.gameover_dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.createExitDialog(getFragmentManager());
            }
        });

        return builder.create();
    }
}
