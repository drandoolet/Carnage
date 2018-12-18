package com.example.user.carnage;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import java.util.Locale;

import static com.example.user.carnage.MenuChooseFragment.enemy;
import static com.example.user.carnage.MenuChooseFragment.player;

public class RPGBattleFragment extends Fragment {
    private final String TAG = "Carnage MAF RPG";
    //private PlayCharacter player, enemy;
    private AnimateGame animateGame;

    private TextView player_name, enemy_name, player_hp_view, enemy_hp_view,
            player_subtitle, enemy_subtitle, battle_textView;
    private TextView player_max_hp_view, enemy_max_hp_view;
    private TextView player_points, enemy_points;
    private RadioGroup atk_group, def_group;
    private CheckBox checkBox_def_head, checkBox_def_body, checkBox_def_waist, checkBox_def_legs;
    private CheckBox checkBox_atk_head, checkBox_atk_body, checkBox_atk_waist, checkBox_atk_legs;
    private Button attackButton, skillsButton;
    private ProgressBar player_HP_bar, enemy_HP_bar;
    private ImageView player_img, enemy_img;
    private SecureRandom random;

    private String selectedAtk, selectedDef;
    private int defCheckBoxCounter, atkCheckBoxCounter;
    private int roundCounter;
    private int maxHP_pl, maxHP_en;
    private String maxHP_pl_string;
    private boolean isAtkSelected;

    private PlayerChoice playerChoice, enemyChoice;

    private int hits, criticals, blockBreaks, blocks, dodges;

    private int defCounterBound, atkCounterBound;
    private long currentAnimationDuration = 0L;


    private Button testAnimButton;

    public RPGBattleFragment() {
    }


    private CompoundButton.OnCheckedChangeListener atkCheckBoxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int id = compoundButton.getId();
            String text = "";
            switch (id) {
                case R.id.checkBoxAtkHead:
                    text = "HEAD";
                    break;
                case R.id.checkBoxAtkBody:
                    text = "BODY";
                    break;
                case R.id.checkBoxAtkWaist:
                    text = "WAIST";
                    break;
                case R.id.checkBoxAtkLegs:
                    text = "LEGS";
                    break;
            }
            if (b) {
                if (atkCheckBoxCounter == atkCounterBound) {
                    compoundButton.setChecked(false);
                } else if (atkCheckBoxCounter < atkCounterBound && defCheckBoxCounter >= 0) {
                    atkCheckBoxCounter++;
                    selectedAtk += text;
                } else {
                    Log.e(MainActivity.TAG, "ERROR in MAF onCheckedChanged true");
                }
            } else {
                atkCheckBoxCounter--;
                selectedAtk = selectedAtk.replace(text, "");
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener defCheckBoxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int id = compoundButton.getId();
            String text = "";
            switch (id) {
                case R.id.checkBoxDefHead:
                    text = "HEAD";
                    break;
                case R.id.checkBoxDefBody:
                    text = "BODY";
                    break;
                case R.id.checkBoxDefWaist:
                    text = "WAIST";
                    break;
                case R.id.checkBoxDefLegs:
                    text = "LEGS";
                    break;
            }
            if (b) {
                if (defCheckBoxCounter == defCounterBound) {
                    compoundButton.setChecked(false);
                } else if (defCheckBoxCounter < defCounterBound && defCheckBoxCounter >= 0) {
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


    private View.OnClickListener attackButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                if (defCheckBoxCounter != defCounterBound || atkCheckBoxCounter != atkCounterBound) {
                    throw new NullPointerException("thrown by if");
                } else {
                    setButtonsEnabled(false);
                    roundCounter++;
                    addRound(roundCounter);
                    playerChoice = new PlayerChoice(selectedAtk, selectedDef);
                    enemyChoice = new PlayerChoice();

                    player.getChoices(playerChoice, enemyChoice);
                    enemy.getChoices(enemyChoice, playerChoice);

                    enemy.damageReceived(player);
                    addLogText(player, enemy);
                    enemy_HP_bar.setProgress(enemy.getHP()); // animate - true ?!
                    //enemy_hp_view.setText(enemy.getHP());

                    if (enemy.getHP() > 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                player.damageReceived(enemy);
                                addLogText(enemy, player);
                                player_hp_view.setText(new Integer(player.getHP()).toString());
                                player_HP_bar.setProgress(player.getHP());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        setButtonsEnabled(true);
                                    }
                                }, currentAnimationDuration);
                            }
                        }, currentAnimationDuration);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("\n*** GAME OVER. YOU WIN ***");
                                setGameOver(player.getName());
                            }
                        }, currentAnimationDuration);
                    }
                    if (player.getHP() <= 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("\n*** GAME OVER. YOU LOSE ***");
                                setGameOver(enemy.getName());
                            }
                        }, currentAnimationDuration);
                    }

                    selectedAtk = "";
                    selectedDef = "";

                    player.clearBodyPartsSelection();
                    enemy.clearBodyPartsSelection();
                    CheckBox[] atkboxes = {checkBox_atk_head, checkBox_atk_body, checkBox_atk_waist, checkBox_atk_legs};
                    for (CheckBox box : atkboxes) box.setChecked(false);
                    CheckBox[] boxes = {checkBox_def_body, checkBox_def_head, checkBox_def_waist, checkBox_def_legs};
                    for (CheckBox box : boxes) box.setChecked(false);
                    atkCheckBoxCounter = 0;
                    defCheckBoxCounter = 0;
                    currentAnimationDuration = 0L;
                }
            } catch (NullPointerException exc) {
                Log.e(MainActivity.TAG, "error in inClick: " + exc);
                Toast.makeText(getContext(), R.string.toast_choose_atk, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_rpg, container, false);

        animateGame = new AnimateGame();
        random = new SecureRandom();

        defCheckBoxCounter = 0;
        atkCheckBoxCounter = 0;
        roundCounter = 0;
        hits = 0;
        criticals = 0;
        blockBreaks = 0;
        blocks = 0;
        dodges = 0;
        selectedAtk = "";
        selectedDef = "";
        isAtkSelected = false;
        defCounterBound = 2;
        atkCounterBound = 1;

        maxHP_pl = player.getHP();
        maxHP_pl_string = new Integer(maxHP_pl).toString();
        maxHP_en = enemy.getHP();

        player_name = view.findViewById(R.id.player1TextView);
        enemy_name = view.findViewById(R.id.player2TextView);
        battle_textView = view.findViewById(R.id.battleTextView);
        player_name.setText(R.string.player_1_name);
        enemy_name.setText(R.string.player_2_name);

        battle_textView.setText(getInfo(player, enemy));
        battle_textView.setMovementMethod(new ScrollingMovementMethod());
        player_hp_view = view.findViewById(R.id.player1HPTextView);
        player_max_hp_view = view.findViewById(R.id.player1MaxHPTextView);
        player_max_hp_view.setText(maxHP_pl_string);
        player_points = view.findViewById(R.id.player1PointsTextView);
        enemy_points = view.findViewById(R.id.player2PointsTextView);
        //enemy_hp_view = view.findViewById(R.id.player2HPTextView);
        //player_subtitle = view.findViewById(R.id.player1Subtitile);
        //enemy_subtitle = view.findViewById(R.id.player2Subtitle);

        //handlePlayerClasses(player, enemy);

        //enemy_hp_view.setText(maxHP_en);
        player_hp_view.setText(maxHP_pl_string);

        player_img = view.findViewById(R.id.player1ImageView);
        enemy_img = view.findViewById(R.id.player2ImageView);

        AssetManager assets = getActivity().getAssets();
        try (InputStream stream = assets.open("player_img/alina_lupit.png")) {
            Drawable playerImage = Drawable.createFromStream(stream, "alina_lupit.png");
            player_img.setImageDrawable(playerImage);
        } catch (IOException exc) {
            Log.e(TAG, "error loading player 1 img: " + exc);
        }
        try (InputStream stream = assets.open("player_img/spider_face.png")) {
            Drawable playerImage = Drawable.createFromStream(stream, "spider_face.png");
            enemy_img.setImageDrawable(playerImage);
        } catch (IOException exc) {
            Log.e(TAG, "error loading player 1 img: " + exc);
        }

        player_HP_bar = view.findViewById(R.id.player1ProgressBar);
        enemy_HP_bar = view.findViewById(R.id.player2ProgressBar);
        player_HP_bar.setMax(maxHP_pl);
        enemy_HP_bar.setMax(maxHP_en);
        player_HP_bar.setProgress(maxHP_pl);
        enemy_HP_bar.setProgress(maxHP_en);

        checkBox_atk_head = view.findViewById(R.id.checkBoxAtkHead);
        checkBox_atk_body = view.findViewById(R.id.checkBoxAtkBody);
        checkBox_atk_waist = view.findViewById(R.id.checkBoxAtkWaist);
        checkBox_atk_legs = view.findViewById(R.id.checkBoxAtkLegs);
        CheckBox[] atkboxes = {checkBox_atk_head, checkBox_atk_body, checkBox_atk_waist, checkBox_atk_legs};
        for (CheckBox box : atkboxes) box.setOnCheckedChangeListener(atkCheckBoxListener);

        checkBox_def_head = view.findViewById(R.id.checkBoxDefHead);
        checkBox_def_body = view.findViewById(R.id.checkBoxDefBody);
        checkBox_def_waist = view.findViewById(R.id.checkBoxDefWaist);
        checkBox_def_legs = view.findViewById(R.id.checkBoxDefLegs);
        CheckBox[] boxes = {checkBox_def_body, checkBox_def_head, checkBox_def_waist, checkBox_def_legs};
        for (CheckBox x : boxes) x.setOnCheckedChangeListener(defCheckBoxListener);

        attackButton = view.findViewById(R.id.buttonAttack);
        attackButton.setOnClickListener(attackButtonListener);

        skillsButton = view.findViewById(R.id.buttonSkills);
        //skillsButton.setOnClickListener(skillsListener); // TODO: create skills fragment
        skillsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateGame.animateDamagePoints(player_points, true);
                animateGame.animateDamagePoints(enemy_points, false);
            }
        });

        return view;
    }

    private void addRound(int roundCounter) {
        battle_textView.append(getString(R.string.battle_round, roundCounter));
    }


    private void setGameOver(String winner) { // TODO: check DialogFragment show() method
        MainActivity.setGameOverSharedPref(winner, roundCounter, hits, criticals, blockBreaks, blocks, dodges);
        if (MainActivity.trackStatistics) {
            System.out.println("*** Added to neural: ***\nsuccessful hits (4), received hits (4): " + Arrays.toString(enemy.getStatsForNeuralNet()));
            MainActivity.addStatisticsToNeuralNet(enemy.getStatsForNeuralNet());
        } else {
            System.out.println("Tracker is set to: " + MainActivity.trackStatistics + ". Tracking not performed.");
        }


        GameOverDialogFragment dialogFragment = new GameOverDialogFragment();
        dialogFragment.show(getFragmentManager(), TAG);
    }

    public String getInfo(PlayCharacter pl, PlayCharacter en) {
        int[] stats = pl.getStats();
        String s1 = getString(R.string.battle_text_rpg_info,
                pl.getName(),
                stats[0], stats[1], stats[2], stats[3], stats[4], stats[5], stats[6], stats[7], stats[8], stats[9], stats[10],
                String.format(Locale.ENGLISH ,"%.2f", pl.getCriticalDmg()));
        stats = en.getStats();
        String s2 = getString(R.string.battle_text_rpg_info, en.getName(), stats[0], stats[1], stats[2], stats[3], stats[4],
                stats[5], stats[6], stats[7], stats[8], stats[9], stats[10], String.format(Locale.ENGLISH, "%.2f", en.getCriticalDmg()));
        String s = s1 + s2;
        return s;
    }

    private void addLogText(final PlayCharacter character, PlayCharacter enemy) { // TODO: add placeholders to strings.xml
        String text;
        final ImageView imgToAnimate, playerImage;
        final boolean isPlayer;
        final TextView pointsTextView;
        if (character == player) {
            imgToAnimate = enemy_img;
            playerImage = player_img;
            isPlayer = true;
            pointsTextView = enemy_points;
        }
        else {
            imgToAnimate = player_img;
            playerImage = enemy_img;
            isPlayer = false;
            pointsTextView = player_points;
        }
        switch (character.getRoundStatus()) {
            case "normal":
                text = getString(R.string.battle_text_normal, character.getName(), character.getTarget(), character.getCurrentKick());
                hits++;
                animateGame.animateAttack(playerImage, imgToAnimate, isPlayer);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateGame.animateHit(imgToAnimate, isPlayer);
                        pointsTextView.setText(String.format(Locale.ENGLISH, Integer.toString(-character.getCurrentKick())));
                        animateGame.animateDamagePoints(pointsTextView, isPlayer);
                    }
                }, AnimationTypes.ANIMATION_BATTLE_ATTACK.getDuration());
                currentAnimationDuration = AnimationTypes.ANIMATION_BATTLE_HIT.getDuration();
                break;
            case "blocked":
                text = getString(R.string.battle_text_blocked, character.getName(), character.getTarget(), enemy.getName());
                blocks++;
                animateGame.animateAttack(playerImage, imgToAnimate, isPlayer);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateGame.animateBlock(imgToAnimate, isPlayer);
                        pointsTextView.setText(getString(R.string.battle_points_block));
                        animateGame.animateDamagePoints(pointsTextView, isPlayer);
                    }
                }, AnimationTypes.ANIMATION_BATTLE_ATTACK.getDuration());
                currentAnimationDuration = AnimationTypes.ANIMATION_BATTLE_BLOCK.getDuration();
                break;
            case "dodged":
                text = getString(R.string.battle_text_dodged, character.getName(), character.getTarget(), enemy.getName());
                dodges++;
                animateGame.animateAttack(playerImage, imgToAnimate, isPlayer);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateGame.animateDodge(imgToAnimate, isPlayer);
                    }
                }, AnimationTypes.ANIMATION_BATTLE_ATTACK.getDuration());
                currentAnimationDuration = AnimationTypes.ANIMATION_BATTLE_DODGE.getDuration();
                break;
            case "critical":
                text = getString(R.string.battle_text_critical, character.getName(), character.getTarget(), enemy.getName(), character.getCurrentKick());
                criticals++;
                animateGame.animateAttack(playerImage, imgToAnimate, isPlayer);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateGame.animateCriticalHit(imgToAnimate, isPlayer);
                        pointsTextView.setText(getString(R.string.battle_points_critical, -character.getCurrentKick()));
                        animateGame.animateDamagePoints(pointsTextView, isPlayer);
                    }
                }, AnimationTypes.ANIMATION_BATTLE_ATTACK.getDuration());
                currentAnimationDuration = AnimationTypes.ANIMATION_BATTLE_CRITICAL.getDuration();
                break;
            case "block break":
                text = getString(R.string.battle_text_block_break, character.getName(), character.getTarget(), enemy.getName(), character.getCurrentKick());
                blockBreaks++;
                animateGame.animateAttack(playerImage, imgToAnimate, isPlayer);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateGame.animateBlockBreak(imgToAnimate, isPlayer);
                        pointsTextView.setText(getString(R.string.battle_points_block_break, -character.getCurrentKick()));
                        animateGame.animateDamagePoints(pointsTextView, isPlayer);
                    }
                }, AnimationTypes.ANIMATION_BATTLE_ATTACK.getDuration());
                currentAnimationDuration = AnimationTypes.ANIMATION_BATTLE_BLOCK_BREAK.getDuration();
                break;
            default:
                text = '\n' + '\n' + "  ERROR in round " + roundCounter;
        }

        battle_textView.append(text);
    }

    private void handlePlayerClasses(PlayCharacter character, PlayCharacter enemy) {
        String playerClass, enemyClass;
        switch (character.getPlayerClass()) {
            case "Сбалансированный":
                playerClass = getString(R.string.player_type_balanced);
                break;
            case "Берсеркер":
                playerClass = getString(R.string.player_type_berserker);
                break;
            case "Танк":
                playerClass = getString(R.string.player_type_tank);
                break;
            case "RANDOM":
                playerClass = getString(R.string.player_type_random);
                break;
            default:
                playerClass = "ERROR";
        }
        switch (enemy.getPlayerClass()) {
            case "Сбалансированный":
                enemyClass = getString(R.string.player_type_balanced);
                break;
            case "Берсеркер":
                enemyClass = getString(R.string.player_type_berserker);
                break;
            case "Танк":
                enemyClass = getString(R.string.player_type_tank);
                break;
            case "RANDOM":
                enemyClass = getString(R.string.player_type_random);
                break;
            default:
                enemyClass = "ERROR";
        }

        //player_subtitle.setText(playerClass);
        //enemy_subtitle.setText(enemyClass);
    }

    private void setButtonsEnabled(boolean set) {
        View[] views = {checkBox_def_head, checkBox_def_body, checkBox_def_waist, checkBox_def_legs,
                checkBox_atk_head, checkBox_atk_body, checkBox_atk_waist, checkBox_atk_legs,
                attackButton, skillsButton};
        for (View view : views) view.setEnabled(set);
    }

}

