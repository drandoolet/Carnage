package com.example.user.carnage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Arrays;

import static com.example.user.carnage.MainActivity.enemy;
import static com.example.user.carnage.MainActivity.player;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String TAG = "Carnage MAF";
    //private PlayCharacter player, enemy;

    private TextView player_name, enemy_name, player_hp_view, enemy_hp_view,
            player_subtitle, enemy_subtitle, battle_textView;
    private RadioGroup atk_group, def_group;
    private CheckBox checkBox_def_head, checkBox_def_body, checkBox_def_waist, checkBox_def_legs;
    private Button attackButton;
    private ProgressBar player_HP_bar, enemy_HP_bar;
    private ImageView player_img, enemy_img;
    private SecureRandom random;

    private String selectedAtk, selectedDef;
    private int defCheckBoxCounter;
    private int roundCounter;
    private int maxHP_pl, maxHP_en;
    private boolean isAtkSelected;

    private PlayerChoice playerChoice, enemyChoice;

    private int hits, criticals, blockBreaks, blocks, dodges;

    private Button testAnimButton;

    public MainActivityFragment() {
    }

    private RadioGroup.OnCheckedChangeListener radioListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int radioId) {
            switch (radioId) {
                case R.id.radioAtkHead : selectedAtk = "HEAD"; break;
                case R.id.radioAtkBody : selectedAtk = "BODY"; break;
                case R.id.radioAtkWaist : selectedAtk = "WAIST"; break;
                case R.id.radioAtkLegs : selectedAtk = "LEGS"; break;
            }
            isAtkSelected = true;
        }
    };

    private CompoundButton.OnCheckedChangeListener checkBoxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int id = compoundButton.getId();
            String text = "";
            switch (id) {
                case R.id.checkBoxDefHead: text = "HEAD"; break;
                case R.id.checkBoxDefBody: text = "BODY"; break;
                case R.id.checkBoxDefWaist: text = "WAIST"; break;
                case R.id.checkBoxDefLegs: text = "LEGS"; break;
            }
            if (b) {
                if (defCheckBoxCounter == 2) {
                    compoundButton.setChecked(false);
                } else if (defCheckBoxCounter < 2 && defCheckBoxCounter >= 0) {
                    defCheckBoxCounter++;
                    selectedDef += text;
                } else {
                    Log.e(MainActivity.TAG, "ERROR in MAF onCheckedChanged true");
                }
            } else {
                defCheckBoxCounter--;
                selectedDef = selectedDef.replace(text, "");
            }
        }
    };


    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                if (defCheckBoxCounter != 2 || !isAtkSelected) {
                    throw new NullPointerException("thrown by if");
                } else {
                    roundCounter++;
                    addRound(roundCounter);
                    playerChoice = new PlayerChoice(selectedAtk, selectedDef);
                    enemyChoice = new PlayerChoice();

                    player.getChoices(playerChoice, enemyChoice);
                    enemy.getChoices(enemyChoice, playerChoice);

                    enemy.damageReceived(player);
                    addLogText(player, enemy);
                    enemy_HP_bar.setProgress(enemy.getHP()); // animate - true ?!
                    enemy_hp_view.setText(" [" + enemy.getHP() + "/" + maxHP_en + "]");

                    if (enemy.getHP() > 0) {
                        player.damageReceived(enemy);
                        addLogText(enemy, player);
                        player_hp_view.setText(" [" + player.getHP() + "/" + maxHP_pl + "]");
                        player_HP_bar.setProgress(player.getHP());

                    } else {
                        System.out.println("\n*** GAME OVER. YOU WIN ***");
                        setGameOver(player.getName());
                    }
                    if (player.getHP() <= 0) {
                        System.out.println("\n*** GAME OVER. YOU LOSE ***");
                        setGameOver(enemy.getName());
                    }

                    selectedAtk = "";
                    selectedDef = "";

                    player.clearBodyPartsSelection();
                    enemy.clearBodyPartsSelection();
                    atk_group.clearCheck();
                    CheckBox[] boxes = {checkBox_def_body, checkBox_def_head, checkBox_def_waist, checkBox_def_legs};
                    for (CheckBox box : boxes) box.setChecked(false);
                    atk_group.clearCheck();
                    isAtkSelected = false;
                }
            } catch (NullPointerException exc) {
                Log.e(MainActivity.TAG, "error in inClick: "+exc);
                Toast.makeText(getContext(), R.string.toast_choose_atk, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        random = new SecureRandom();

        defCheckBoxCounter = 0;
        roundCounter = 0;
        hits = 0;
        criticals = 0;
        blockBreaks = 0;
        blocks = 0;
        dodges = 0;
        selectedAtk = "";
        selectedDef = "";
        isAtkSelected = false;

        maxHP_pl = player.getHP();
        maxHP_en = enemy.getHP();

        player_name = view.findViewById(R.id.player1TextView);
        enemy_name = view.findViewById(R.id.player2TextView);
        battle_textView = view.findViewById(R.id.battleTextView);
        player_name.setText(R.string.player_1_name);
        enemy_name.setText(R.string.player_2_name);

        battle_textView.setText(getInfo(player, enemy));
        battle_textView.setMovementMethod(new ScrollingMovementMethod());
        player_hp_view = view.findViewById(R.id.player1HPTextView);
        enemy_hp_view = view.findViewById(R.id.player2HPTextView);
        player_subtitle = view.findViewById(R.id.player1Subtitile);
        enemy_subtitle = view.findViewById(R.id.player2Subtitle);

        handlePlayerClasses(player, enemy);

        enemy_hp_view.setText(" ["+enemy.getHP()+"/"+maxHP_en+"]");
        player_hp_view.setText(" ["+player.getHP()+"/"+maxHP_pl+"]");

        player_img = view.findViewById(R.id.player1ImageView);
        enemy_img = view.findViewById(R.id.player2ImageView);

        AssetManager assets = getActivity().getAssets();
        try (InputStream stream = assets.open("player_img/alina_lupit.png")) {
            Drawable playerImage = Drawable.createFromStream(stream, "alina_lupit.png");
            player_img.setImageDrawable(playerImage);
        } catch (IOException exc) {
            Log.e(TAG, "error loading player 1 img: "+exc);
        }
        try (InputStream stream = assets.open("player_img/spider_face.png")) {
            Drawable playerImage = Drawable.createFromStream(stream, "spider_face.png");
            enemy_img.setImageDrawable(playerImage);
        } catch (IOException exc) {
            Log.e(TAG, "error loading player 1 img: "+exc);
        }

        player_HP_bar = view.findViewById(R.id.player1ProgressBar);
        enemy_HP_bar = view.findViewById(R.id.player2ProgressBar);
        player_HP_bar.setMax(player.getHP());
        enemy_HP_bar.setMax(enemy.getHP());
        player_HP_bar.setProgress(player.getHP());
        enemy_HP_bar.setProgress(enemy.getHP());

        atk_group = view.findViewById(R.id.attackRadioGroup);
        def_group = view.findViewById(R.id.defenceRadioGroup);
        atk_group.setOnCheckedChangeListener(radioListener);
        def_group.setOnCheckedChangeListener(radioListener);

        checkBox_def_head = view.findViewById(R.id.checkBoxDefHead);
        checkBox_def_body = view.findViewById(R.id.checkBoxDefBody);
        checkBox_def_waist = view.findViewById(R.id.checkBoxDefWaist);
        checkBox_def_legs = view.findViewById(R.id.checkBoxDefLegs);
        CheckBox[] boxes = {checkBox_def_body, checkBox_def_head, checkBox_def_waist, checkBox_def_legs};
        for (CheckBox x : boxes) x.setOnCheckedChangeListener(checkBoxListener);

        attackButton = view.findViewById(R.id.buttonAttack);
        attackButton.setOnClickListener(buttonListener);

        testAnimButton = view.findViewById(R.id.animTestButton);
        testAnimButton.setOnClickListener(testAnimListener);

        return view;
    }

    private void addRound(int roundCounter) {
        String text = battle_textView.getText().toString() +'\n'+'\n'+'\n'
                +getString(R.string.battle_text_stars)
                +getString(R.string.battle_round) + roundCounter +" "
                +getString(R.string.battle_text_stars);
        battle_textView.setText(text);
    }



    private void setGameOver(String winner) { // TODO: check DialogFragment show() method
        MainActivity.setGameOverSharedPref(winner, roundCounter, hits, criticals, blockBreaks, blocks, dodges);
        if (MainActivity.trackStatistics) {
            System.out.println("*** Added to neural: ***\nsuccessful hits (4), received hits (4): "+Arrays.toString(enemy.getStatsForNeuralNet()));
            MainActivity.addStatisticsToNeuralNet(enemy.getStatsForNeuralNet());
        } else {
            System.out.println("Tracker is set to: "+MainActivity.trackStatistics+". Tracking not performed.");
        }


        GameOverDialogFragment dialogFragment = new GameOverDialogFragment();
        dialogFragment.show(getFragmentManager(), TAG);
    }

    public String getInfo(PlayCharacter player, PlayCharacter enemy) {
        String s1 = getResources().getString(R.string.battle_text_info_start_fight) +player.getName()+
                getResources().getString(R.string.battle_and) +enemy.getName() +'\n'+'\n';
        String s2 = getResources().getString(R.string.battle_text_info_chars_pl) +'\n'
                +getResources().getString(R.string.battle_text_info_hp) +player.getHP() +'\n'
                +getResources().getString(R.string.battle_text_info_power) +player.getStrPower() +'\n'
                +getResources().getString(R.string.battle_text_info_crit) +player.getCritical() +'\n'
                +getResources().getString(R.string.battle_text_info_crit_dmg) +player.getStrCritDmg() +'\n'+'\n';
        String s3 = getString(R.string.battle_text_info_chars_en) +'\n'
                +getString(R.string.battle_text_info_hp)+enemy.getHP() +'\n'
                +getString(R.string.battle_text_info_power) +enemy.getStrPower() +'\n'
                +getString(R.string.battle_text_info_crit)+enemy.getCritical() +'\n'
                +getString(R.string.battle_text_info_crit_dmg)+enemy.getStrCritDmg();
        String s = s1+s2+s3;
        return s;
    }

    private void addLogText(PlayCharacter character, PlayCharacter enemy) { // TODO: add placeholders to strings.xml
        String text = battle_textView.getText().toString() +'\n'+'\n'
                +character.getName()+ getString(R.string.battle_text_aims) +character.getTarget();
        String text2;
        System.out.println("roundStatus: "+character.getRoundStatus());
        switch (character.getRoundStatus()) {
            case "normal":
                text2 = getString(R.string.battle_text_normal) + character.getCurrentKick() + getString(R.string.battle_text_dmg_points);
                hits++;
                break;
            case "blocked":
                text2 = getString(R.string.battle_text_but) + enemy.getName() + getString(R.string.battle_text_blocked);
                blocks++;
                break;
            case "dodged":
                text2 = getString(R.string.battle_text_but) + enemy.getName() + getString(R.string.battle_text_dodged);
                dodges++;
                break;
            case "critical":
                text2 = getString(R.string.battle_text_critical) + enemy.getName() + getString(R.string.battle_text_receives)
                + character.getCurrentKick() + getString(R.string.battle_text_dmg_points);
                criticals++;
                break;
            case "block break":
                text2 = getString(R.string.battle_text_block_break) + enemy.getName() + getString(R.string.battle_text_receives)
                        + character.getCurrentKick() + getString(R.string.battle_text_dmg_points);
                blockBreaks++;
                break;
            default: text2 = '\n'+'\n'+ "  ERROR in round "+roundCounter;
        }

        battle_textView.setText(text+text2);
    }

    private void handlePlayerClasses(PlayCharacter character, PlayCharacter enemy) {
        String playerClass, enemyClass;
        switch (character.getPlayerClass()) {
            case "Сбалансированный" : playerClass = getString(R.string.player_type_balanced); break;
            case "Берсеркер" : playerClass = getString(R.string.player_type_berserker); break;
            case "Танк" : playerClass = getString(R.string.player_type_tank); break;
            case "RANDOM" : playerClass = getString(R.string.player_type_random); break;
            default: playerClass = "ERROR";
        }
        switch (enemy.getPlayerClass()) {
            case "Сбалансированный" : enemyClass = getString(R.string.player_type_balanced); break;
            case "Берсеркер" : enemyClass = getString(R.string.player_type_berserker); break;
            case "Танк" : enemyClass = getString(R.string.player_type_tank); break;
            case "RANDOM" : enemyClass = getString(R.string.player_type_random); break;
            default: enemyClass = "ERROR";
        }

        player_subtitle.setText(playerClass);
        enemy_subtitle.setText(enemyClass);
    }

    private static void testAnim(final ImageView imageView) {
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(testAnimSetBigger(imageView), testAnimSetSmaller(imageView));
        set.addListener(testListener);
        set.setDuration(4000);
        System.out.println("testAnim duration: "+set.getDuration());
        set.start();
        //imageView.animate().alphaBy(0).alpha(1).start();
    }

    private static AnimatorSet testAnimSetBigger(View view) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 1.3f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 1.5f)
        );
        set.setDuration(2000);
        System.out.println("animBigger duration: "+set.getDuration());
        return set;
    }

    private static AnimatorSet testAnimSetSmaller(View view) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(2000).playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, 1.3f, 1.0f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.5f, 1.0f)
        );
        System.out.println("smaller: "+set.getDuration());
        return set;
    }

    private View.OnClickListener testAnimListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            testAnim(player_img);
            //ObjectAnimator.ofFloat(player_img, View.SCALE_X, 1.0f, 1.3f).setDuration((long) 2000).start();
            //ObjectAnimator.ofFloat(player_img, View.SCALE_Y, 1.0f, 1.3f).setDuration( 2000).start();
        }
    };

    private static AnimatorListenerAdapter testListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
        }
    };
}
