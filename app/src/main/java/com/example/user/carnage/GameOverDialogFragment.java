package com.example.user.carnage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class GameOverDialogFragment extends DialogFragment {
    private String message, winner;
    private int rounds, damage, exp;
    private boolean isWinner;



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.gameover_dialog_title);
        builder.setCancelable(false);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                MainActivity.createExitDialog(getFragmentManager());
            }
        });

        winner = MainActivity.getSharedWinner();
        rounds = MainActivity.getSharedRounds();
        damage = MainActivity.getSharedDamage();
        isWinner = MainActivity.getSharedBooleanWinner();
        Levels levels = new Levels(MainActivity.player, rounds, damage, isWinner);
        exp = levels.getExpReceived();
        Toast.makeText(getContext(), "target exp: "+levels.getTargetExp(), Toast.LENGTH_SHORT).show();
/*
        message = getString(R.string.gameover_dialog_winner) + MainActivity.getSharedWinner() +'\n'+
                getString(R.string.gameover_dialog_hits) + MainActivity.getSharedHits() +'\n'+
                getString(R.string.gameover_dialog_criticals) + MainActivity.getSharedCriticals() +'\n'+
                getString(R.string.gameover_dialog_block_breaks) + MainActivity.getSharedBlockBreaks() +'\n'+
                getString(R.string.gameover_dialog_blocks) + MainActivity.getSharedBlocks() +'\n'+
                getString(R.string.gameover_dialog_dodges) + MainActivity.getSharedDodges();  */
        message = getString(R.string.dialog_fragment_gameover_message, winner, rounds, damage, exp);
        builder.setMessage(message);
/*
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
        */
        if (levels.isLevelUp) {
            builder.setPositiveButton("LEVEL UP!("+MainActivity.player.getLevel()+")", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.updatePlayerStatsSharedPreferences(MainActivity.player.getMainStats(), MainActivity.currentProfile);
                    MainActivity.levelUp(getFragmentManager());
                }
            });
        } else builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.updatePlayerStatsSharedPreferences(MainActivity.player.getMainStats(), MainActivity.currentProfile);
                MainActivity.newGame(getFragmentManager());
            }
        });

        return builder.create();
    }
}
