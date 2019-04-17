package com.example.user.carnage.fragments;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.carnage.animation.AnimateGame;
import com.example.user.carnage.MainActivity;
import com.example.user.carnage.R;
import com.example.user.carnage.animation.SkillsAnimator;
import com.example.user.carnage.fragments.dialogs.GameOverDialogFragment;
import com.example.user.carnage.logic.main.BodyPart;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.PlayCharacterHelper;
import com.example.user.carnage.logic.main.PlayerChoice;
import com.example.user.carnage.animation.AnimateGame.AnimationTypes;
import com.example.user.carnage.logic.skills.Skill;
import com.example.user.carnage.logic.main.PlayCharacter.RoundStatus;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.user.carnage.MainActivity.currentProfile;
import static com.example.user.carnage.MainActivity.enemy;
import static com.example.user.carnage.MainActivity.getSharedCriticals;
import static com.example.user.carnage.MainActivity.player;

public class RPGBattleFragment extends Fragment implements SkillsAnimator.MagicCallBack {
    public final String TAG = "Carnage MAF RPG";
    //private PlayCharacter player, enemy;
    private AnimateGame animateGame;

    private TextView player_name, enemy_name, player_hp_view, enemy_hp_view,
            player_sp_view, player_mp_view, battle_textView;
    private TextView player_max_hp_view, enemy_max_hp_view, player_max_sp_view, player_max_mp_view;
    public TextView player_points, enemy_points;
    private RadioGroup atk_group, def_group;
    private CheckBox checkBox_def_head, checkBox_def_body, checkBox_def_waist, checkBox_def_legs;
    private CheckBox checkBox_atk_head, checkBox_atk_body, checkBox_atk_waist, checkBox_atk_legs;
    private Button attackButton, skillsButton;
    private ProgressBar player_HP_bar, enemy_HP_bar, player_SP_bar, player_MP_bar;
    public ImageView player_img, enemy_img, skillEffect_img;
    private SecureRandom random;
    public LinearLayout skillsFragmentContainer;

    private String selectedAtk, selectedDef;
    private int defCheckBoxCounter, atkCheckBoxCounter;
    private int roundCounter;
    private int maxHP_pl, maxHP_en, maxSP_pl, maxMP_pl;
    private String maxHP_pl_string;
    private boolean isAtkSelected;

    private PlayerChoice playerChoice, enemyChoice;
    private ArrayList<BodyPart.BodyPartNames> playerAttacked = new ArrayList<>();
    private ArrayList<BodyPart.BodyPartNames> playerDefended = new ArrayList<>();

    private int hits, criticals, blockBreaks, blocks, dodges, totalDamage;

    private int defCounterBound, atkCounterBound;
    private long currentAnimationDuration = 0L;

    private PlayCharacterHelper playerHelper, enemyHelper;


    private Button testAnimButton;

    public RPGBattleFragment() {
    }


    private CompoundButton.OnCheckedChangeListener atkCheckBoxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int id = compoundButton.getId();
            String text = "";
            BodyPart.BodyPartNames bodyPart = null;
            switch (id) {
                case R.id.checkBoxAtkHead:
                    text = "HEAD";
                    bodyPart = BodyPart.BodyPartNames.HEAD;
                    break;
                case R.id.checkBoxAtkBody:
                    text = "BODY";
                    bodyPart = BodyPart.BodyPartNames.BODY;
                    break;
                case R.id.checkBoxAtkWaist:
                    text = "WAIST";
                    bodyPart = BodyPart.BodyPartNames.WAIST;
                    break;
                case R.id.checkBoxAtkLegs:
                    text = "LEGS";
                    bodyPart = BodyPart.BodyPartNames.LEGS;
                    break;
            }
            if (b) {
                if (atkCheckBoxCounter == atkCounterBound) {
                    compoundButton.setChecked(false);
                } else if (atkCheckBoxCounter < atkCounterBound && defCheckBoxCounter >= 0) {
                    atkCheckBoxCounter++;
                    selectedAtk += text;
                    try {
                        playerAttacked.add(bodyPart);
                    } catch (NullPointerException e) {
                        Log.e(TAG, "my favourite NPE, in atk checkbox listener add");
                    }
                } else {
                    Log.e(MainActivity.TAG, "ERROR in MAF onCheckedChanged true");
                }
            } else {
                atkCheckBoxCounter--;
                selectedAtk = selectedAtk.replace(text, "");
                try {
                    playerAttacked.remove(bodyPart);
                } catch (NullPointerException e) {
                    Log.e(TAG, "my favourite NPE, in atk checkbox listener remove");
                }
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener defCheckBoxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int id = compoundButton.getId();
            String text = "";
            BodyPart.BodyPartNames bodyPart = null;
            switch (id) {
                case R.id.checkBoxDefHead:
                    text = "HEAD";
                    bodyPart = BodyPart.BodyPartNames.HEAD;
                    break;
                case R.id.checkBoxDefBody:
                    text = "BODY";
                    bodyPart = BodyPart.BodyPartNames.BODY;
                    break;
                case R.id.checkBoxDefWaist:
                    text = "WAIST";
                    bodyPart = BodyPart.BodyPartNames.WAIST;
                    break;
                case R.id.checkBoxDefLegs:
                    text = "LEGS";
                    bodyPart = BodyPart.BodyPartNames.LEGS;
                    break;
            }
            if (b) {
                if (defCheckBoxCounter == defCounterBound) {
                    compoundButton.setChecked(false);
                } else if (defCheckBoxCounter < defCounterBound && defCheckBoxCounter >= 0) {
                    defCheckBoxCounter++;
                    selectedDef += text;
                    try {
                        playerDefended.add(bodyPart);
                    } catch (NullPointerException e) {
                        Log.e(TAG, "my favourite NPE, in atk checkbox listener add");
                    }
                } else {
                    Log.e(MainActivity.TAG, "ERROR in MAF onCheckedChanged true");
                }
            } else {
                defCheckBoxCounter--;
                selectedDef = selectedDef.replace(text, "");
                try {
                    playerDefended.remove(bodyPart);
                } catch (NullPointerException e) {
                    Log.e(TAG, "my favourite NPE, in atk checkbox listener remove");
                }
            }
        }
    };

    private View.OnClickListener attackButtonListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (defCheckBoxCounter != defCounterBound || atkCheckBoxCounter != atkCounterBound) {
                //throw new NullPointerException("thrown by if");
                Toast.makeText(getContext(), R.string.toast_choose_atk, Toast.LENGTH_SHORT).show(); // TODO add placeh for 2/2 atk
            } else {
                setButtonsEnabled(false);
                addRound(++roundCounter); // TODO check
                final PlayerChoice plCh = new PlayerChoice(playerAttacked, playerDefended);
                final PlayerChoice enCh = new PlayerChoice(1);
                ArrayList<PlayCharacterHelper.Result> enemyResult = enemyHelper.handle(enCh, plCh);

                int count = 0;
                for (PlayCharacterHelper.Result result : enemyResult) {
                    count++;
                    System.out.println("starting Result#"+count);
                    totalDamage += result.getAttack();
                    addLogText2(enemy, player, result);
                    enemy_HP_bar.setProgress(enemy.getHP());
                    enemy_hp_view.setText(Integer.toString(enemy.getHP()));
                }
                if (enemy.getHP() > 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<PlayCharacterHelper.Result> playerResult
                                    = playerHelper.handle(plCh, enCh);

                            for (PlayCharacterHelper.Result result : playerResult) {
                                addLogText2(player, enemy, result);
                                player_hp_view.setText(new Integer(player.getHP()).toString());
                                player_HP_bar.setProgress(player.getHP());

                                if (player.getHP() <= 0) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            System.out.println("\n*** GAME OVER. YOU LOSE ***");
                                            setGameOver(enemy.getName(), false);
                                        }
                                    }, currentAnimationDuration);
                                    break;
                                }
                            }
                            if (player.getHP() > 0) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        setButtonsEnabled(true);
                                        setArgsReadyForNextRound();
                                    }
                                }, currentAnimationDuration);
                            }

                        }
                    }, currentAnimationDuration);
                }
            }
        }
    };

    private View.OnClickListener attackButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //try {
                if (defCheckBoxCounter != defCounterBound || atkCheckBoxCounter != atkCounterBound) {
                    //throw new NullPointerException("thrown by if");
                    Toast.makeText(getContext(), R.string.toast_choose_atk, Toast.LENGTH_SHORT).show();
                } else {
                    setButtonsEnabled(false);
                    roundCounter++;
                    addRound(roundCounter);
                    playerChoice = new PlayerChoice(selectedAtk, selectedDef);
                    enemyChoice = new PlayerChoice();

                    player.getChoices(playerChoice, enemyChoice);
                    enemy.getChoices(enemyChoice, playerChoice);

                    enemy.damageReceived(player);
                    totalDamage += player.getCurrentKick();
                    addLogText(player, enemy);
                    enemy_HP_bar.setProgress(enemy.getHP()); // animate - true ?!
                    enemy_hp_view.setText(Integer.toString(enemy.getHP()));

                    if (enemy.getHP() > 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                player.damageReceived(enemy);
                                addLogText(enemy, player);
                                player_hp_view.setText(new Integer(player.getHP()).toString());
                                player_HP_bar.setProgress(player.getHP());

                                if (player.getHP() <= 0) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            System.out.println("\n*** GAME OVER. YOU LOSE ***");
                                            setGameOver(enemy.getName(), false);
                                        }
                                    }, currentAnimationDuration);
                                } else {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            setButtonsEnabled(true);
                                            setArgsReadyForNextRound();
                                        }
                                    }, currentAnimationDuration);
                                }
                            }
                        }, currentAnimationDuration);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("\n*** GAME OVER. YOU WIN ***");
                                setGameOver(player.getName(), true);
                            }
                        }, currentAnimationDuration);
                    }
                    if (player.getHP() <= 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("\n*** GAME OVER. YOU LOSE ***");
                                setGameOver(enemy.getName(), false);
                            }
                        }, currentAnimationDuration);
                    }

                }
            //} catch (NullPointerException exc) {
            //    Log.e(MainActivity.TAG, "error in inClick: " + exc);
            //    Toast.makeText(getContext(), R.string.toast_choose_atk, Toast.LENGTH_SHORT).show();
            //}
        }
    };

    private void setArgsReadyForNextRound() {
        selectedAtk = "";
        selectedDef = "";
        playerAttacked.clear();
        playerDefended.clear();

        player.clearBodyPartsSelection();
        enemy.clearBodyPartsSelection();
        CheckBox[] atkboxes = {checkBox_atk_head, checkBox_atk_body, checkBox_atk_waist, checkBox_atk_legs};
        for (CheckBox box : atkboxes) box.setChecked(false);
        CheckBox[] boxes = {checkBox_def_body, checkBox_def_head, checkBox_def_waist, checkBox_def_legs};
        for (CheckBox box : boxes) box.setChecked(false);
        atkCheckBoxCounter = 0;
        defCheckBoxCounter = 0;
        atkCounterBound = 1;
        defCounterBound = 2;
        currentAnimationDuration = 0L;
    }

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
        totalDamage = 0;

        maxHP_pl = player.getHP();
        maxHP_pl_string = new Integer(maxHP_pl).toString();
        maxHP_en = enemy.getHP();
        maxSP_pl = player.getSP();
        maxMP_pl = player.getMP();

        player_name = view.findViewById(R.id.player1TextView);
        enemy_name = view.findViewById(R.id.player2TextView);
        battle_textView = view.findViewById(R.id.battleTextView);
        player_name.setText(R.string.player_1_name);
        enemy_name.setText(R.string.player_2_name);

        battle_textView.setText(getInfo(player, enemy));
        //battle_textView.setText(player.getInfo());
        battle_textView.setMovementMethod(new ScrollingMovementMethod());
        player_hp_view = view.findViewById(R.id.player1HPTextView);
        player_sp_view = view.findViewById(R.id.player1SPTextView);
        player_mp_view = view.findViewById(R.id.player1MPTextView);
        player_max_hp_view = view.findViewById(R.id.player1MaxHPTextView);
        player_max_hp_view.setText(maxHP_pl_string);
        player_max_sp_view = view.findViewById(R.id.player1MaxSPTextView);
        player_max_sp_view.setText(Integer.toString(maxSP_pl));
        player_max_mp_view = view.findViewById(R.id.player1MaxMPTextView);
        player_max_mp_view.setText(Integer.toString(maxMP_pl));
        player_points = view.findViewById(R.id.player1PointsTextView);
        enemy_points = view.findViewById(R.id.player2PointsTextView);
        enemy_hp_view = view.findViewById(R.id.player2HPTextView);
        //player_subtitle = view.findViewById(R.id.player1Subtitile);
        //enemy_subtitle = view.findViewById(R.id.player2Subtitle);

        //handlePlayerClasses(player, enemy);

        enemy_hp_view.setText(Integer.toString(maxHP_en));
        player_hp_view.setText(maxHP_pl_string);
        player_sp_view.setText(Integer.toString(maxSP_pl));
        player_mp_view.setText(Integer.toString(maxMP_pl));

        player_img = view.findViewById(R.id.player1ImageView);
        enemy_img = view.findViewById(R.id.player2ImageView);
        skillEffect_img = view.findViewById(R.id.skillEffectImageView);

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
        player_SP_bar = view.findViewById(R.id.player1ProgressBarSP);
        player_MP_bar = view.findViewById(R.id.player1ProgressBarMP);
        player_SP_bar.setMax(maxSP_pl);
        player_MP_bar.setMax(maxMP_pl);
        player_HP_bar.setMax(maxHP_pl);
        enemy_HP_bar.setMax(maxHP_en);
        player_HP_bar.setProgress(maxHP_pl);
        enemy_HP_bar.setProgress(maxHP_en);
        player_SP_bar.setProgress(maxSP_pl);
        player_MP_bar.setProgress(maxMP_pl);

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
        attackButton.setOnClickListener(attackButtonListener2);

        skillsButton = view.findViewById(R.id.buttonSkills);
        //skillsButton.setOnClickListener(skillsListener); // TODO: create skills fragment
        skillsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSkillsFragment();
            }
        });

        skillsFragmentContainer = view.findViewById(R.id.skillsFragmentContainer);

        playerHelper = new PlayCharacterHelper(player, enemy);
        enemyHelper = new PlayCharacterHelper(enemy, player);

        return view;
    }

    private void addRound(int roundCounter) {
        battle_textView.append(getString(R.string.battle_round, roundCounter));
    }


    private void setGameOver(String winner, boolean isWinner) {
        MainActivity.setGameOverSharedPref(winner, isWinner, roundCounter, totalDamage);
        if (MainActivity.trackStatistics) {
            //Toast.makeText(getContext(), "*** Added to neural: ***\nsuccessful hits (4), received hits (4): "
            //        + Arrays.toString(enemy.getStatsForNeuralNet()), Toast.LENGTH_SHORT).show();
            MainActivity.addStatisticsToNeuralNet(enemy.getStatsForNeuralNet(), currentProfile);
        } else {
            Toast.makeText(getContext(), "Tracker is set to: " + MainActivity.trackStatistics + ". Tracking not performed.",
                    Toast.LENGTH_SHORT).show();
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
        return s1 + s2;
    }

    // character receives damage
    // enemy attacks
    // results are hits taken by character
    private void addLogText2(final PlayCharacter character, PlayCharacter enemy, final PlayCharacterHelper.Result result) {
        String text = "";
        final ImageView imgToAnimate, playerImage;
        final boolean isPlayer;
        final TextView pointsTextView;
        if (character == player) {
            imgToAnimate = player_img;
            playerImage = enemy_img;
            isPlayer = false;
            pointsTextView = player_points;
        }
        else {
            imgToAnimate = enemy_img;
            playerImage = player_img;
            isPlayer = true;
            pointsTextView = enemy_points;
        }

        //for (final PlayCharacterHelper.Result result : results) {
            switch (result.getRoundStatus()) {
                case NORMAL:
                    text = getString(R.string.battle_text_normal, enemy.getName(), result.getBodyPart(), result.getAttack());
                    hits++;
                    animateGame.animateAttack(playerImage, imgToAnimate, isPlayer);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animateGame.animateHit(imgToAnimate, isPlayer);
                            pointsTextView.setText(String.format(Locale.ENGLISH, Integer.toString(-result.getAttack())));
                            animateGame.animateDamagePoints(pointsTextView, isPlayer);
                        }
                    }, AnimationTypes.ANIMATION_BATTLE_ATTACK.getDuration());
                    currentAnimationDuration = AnimationTypes.ANIMATION_BATTLE_HIT.getDuration();
                    break;
                case BLOCK:
                    text = getString(R.string.battle_text_blocked, enemy.getName(), result.getBodyPart(), character.getName());
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
                case DODGE:
                    text = getString(R.string.battle_text_dodged, enemy.getName(), result.getBodyPart(), character.getName());
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
                case CRITICAL:
                    text = getString(R.string.battle_text_critical, enemy.getName(), result.getBodyPart(),
                            character.getName(), result.getAttack());
                    criticals++;
                    animateGame.animateAttack(playerImage, imgToAnimate, isPlayer);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animateGame.animateCriticalHit(imgToAnimate, isPlayer);
                            pointsTextView.setText(getString(R.string.battle_points_critical, -result.getAttack()));
                            animateGame.animateDamagePoints(pointsTextView, isPlayer);
                        }
                    }, AnimationTypes.ANIMATION_BATTLE_ATTACK.getDuration());
                    currentAnimationDuration = AnimationTypes.ANIMATION_BATTLE_CRITICAL.getDuration();
                    break;
                case BLOCK_BREAK:
                    text = getString(R.string.battle_text_block_break, enemy.getName(), result.getBodyPart(),
                            character.getName(), result.getAttack());
                    blockBreaks++;
                    animateGame.animateAttack(playerImage, imgToAnimate, isPlayer);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animateGame.animateBlockBreak(imgToAnimate, isPlayer);
                            pointsTextView.setText(getString(R.string.battle_points_block_break, -result.getAttack()));
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
    //}

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
            case NORMAL:
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
            case BLOCK:
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
            case DODGE:
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
            case CRITICAL:
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
            case BLOCK_BREAK:
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

    public void setButtonsEnabled(boolean set) {
        View[] views = {checkBox_def_head, checkBox_def_body, checkBox_def_waist, checkBox_def_legs,
                checkBox_atk_head, checkBox_atk_body, checkBox_atk_waist, checkBox_atk_legs,
                attackButton, skillsButton};
        for (View view : views) view.setEnabled(set);
    }

    public void setAllEnabled(boolean set) {
        View[] views = {checkBox_def_head, checkBox_def_body, checkBox_def_waist, checkBox_def_legs,
                checkBox_atk_head, checkBox_atk_body, checkBox_atk_waist, checkBox_atk_legs,
                attackButton, skillsButton, battle_textView};
        for (View view : views) view.setEnabled(set);
    }

    private void uncheckButtons() {
        CheckBox[] defButtons = {checkBox_def_head, checkBox_def_body, checkBox_def_waist, checkBox_def_legs};
        CheckBox[] atkButtons = {checkBox_atk_head, checkBox_atk_body, checkBox_atk_waist, checkBox_atk_legs};

        for (CheckBox defButton : defButtons) {
            if (defButton.isChecked()) {
                defButton.setChecked(false);
            }
        }
        for (CheckBox atkButton : atkButtons) {
            if (atkButton.isChecked()) {
                atkButton.setChecked(false);
            }
        }

        defCheckBoxCounter = 0;
        atkCheckBoxCounter = 0;

        playerAttacked.clear();
        playerDefended.clear();

        System.out.println("checkbox counters after uncheck: "+defCheckBoxCounter+", "+atkCheckBoxCounter);
        System.out.println("chb bounds: "+defCounterBound+", "+atkCounterBound);
    }


    public void showSkillsFragment() {
        skillsFragmentContainer.setVisibility(View.VISIBLE);
        animateSkillsFragmentAppearance(true);
        SkillsFragment fragment = new SkillsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.skillsFragmentContainer, fragment);
        transaction.commit();
        uncheckButtons();
        setAllEnabled(false);
    }


    public void animateSkillsFragmentAppearance(boolean b) {
        animateGame.animateSkillsFragmentAppearance(skillsFragmentContainer, b);
    }


    @Override
    public void magicUsed(final Skill skill, long animDurationToPoints) {
        // this method is used when it is needed to show dmg points
        if (defCounterBound - skill.getBoundTakers()[0] >= 0 &&
                atkCounterBound - skill.getBoundTakers()[1] >= 0) {
            final TextView points;
            if (skill.isEffectOnPlayer()) {
                points = player_points;
                points.setText(Integer.toString(skill.getEffect()));
                battle_textView.append(getString(R.string.magic_heal, player.getName(), skill.getName(), skill.getEffect()));
                player.receiveMagic(skill);
                player_HP_bar.setProgress(player.getHP());
                player_hp_view.setText(Integer.toString(player.getHP()));
            } else {
                points = enemy_points;
                points.setText(Integer.toString(skill.getEffect()));
                battle_textView.append(getString(R.string.magic_attack, player.getName(), skill.getName(),
                        enemy.getName(), Math.abs(skill.getEffect())));
                enemy.receiveMagic(skill);
                enemy_HP_bar.setProgress(enemy.getHP());
                enemy_hp_view.setText(Integer.toString(enemy.getHP()));
                totalDamage -= skill.getEffect(); // getEffect() returns negative int if damage and positive if heal
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    animateGame.animateDamagePoints(points, skill.isEffectOnPlayer());
                    if (!skill.isEffectOnPlayer()) animateGame.animateHit(enemy_img, true);
                }
            }, animDurationToPoints);

            defCounterBound -= skill.getBoundTakers()[0];
            atkCounterBound -= skill.getBoundTakers()[1];

            if (enemy.getHP() <= 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("\n*** GAME OVER. YOU WIN ***");
                        setGameOver(player.getName(), true);
                    }
                }, currentAnimationDuration);
            }
        } else Toast.makeText(getContext(), "You cannot use this skill now", Toast.LENGTH_SHORT).show();
    }
}

