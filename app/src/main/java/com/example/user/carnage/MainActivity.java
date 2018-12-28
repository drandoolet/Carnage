package com.example.user.carnage;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity
        implements SkillsFragment.OnSelectedButtonListener, ProfileChooseFragment.OnProfileSelectedListener {
    public static PlayCharacter player, enemy;

    public static final String TAG = "CARNAGE";
    public static Context mContext;
    public static final String APP_PREFERENCES = "gameOverSettings";
    public static final String APP_PREFERENCES_WINNER = "winner";
    public static final String APP_PREFERENCES_WINNER_BOOLEAN = "winner boolean";
    public static final String APP_PREFERENCES_ROUNDS = "rounds";
    public static final String APP_PREFERENCES_DAMAGE_INFLICTED = "damage inflicted";
    public static final String APP_PREFERENCES_HITS = "hits";
    public static final String APP_PREFERENCES_CRITICALS = "criticals";
    public static final String APP_PREFERENCES_BLOCK_BREAKS = "block breaks";
    public static final String APP_PREFERENCES_BLOCKS = "blocks";
    public static final String APP_PREFERENCES_DODGES = "dodges";
    public static final String APP_PREFERENCES_TRACK_STATS = "track statistics";

    public static final String NEURAL_NET = "self-learner";
    public static final String NEURAL_NET_ATK_SUCCESSFUL_HEAD = "successful strike to head";
    public static final String NEURAL_NET_ATK_SUCCESSFUL_BODY = "successful strike to body";
    public static final String NEURAL_NET_ATK_SUCCESSFUL_WAIST = "successful strike to waist";
    public static final String NEURAL_NET_ATK_SUCCESSFUL_LEGS = "successful strike to legs";

    public static final String NEURAL_NET_DEF_ATTACKED_HEAD = "player attacked head";
    public static final String NEURAL_NET_DEF_ATTACKED_BODY = "player attacked body";
    public static final String NEURAL_NET_DEF_ATTACKED_WAIST = "player attacked waist";
    public static final String NEURAL_NET_DEF_ATTACKED_LEGS = "player attacked legs";

    public static final String RPG_STATS = "RPG stats";
    public static final String RPG_PROFILE_1 = "player profile 1";
    public static final String RPG_PROFILE_2 = "player profile 2";
    public static final String RPG_PROFILE_IMAGE = "player profile image";
    public static final String RPG_STATS_STR = "strength";
    public static final String RPG_STATS_STA = "stamina";
    public static final String RPG_STATS_AGI = "agility";
    public static final String RPG_STATS_LUCK = "luck";
    public static final String RPG_STATS_INT = "intelligence";
    public static final String RPG_STATS_LEVEL = "level";
    public static final String RPG_STATS_CURRENT_EXP = "current exp";
    public static final String RPG_STATS_AVAILABLE_STAT_POINTS = "available stat points";

    public static final long ANIMATION_WINDOW_CHANGE_DURATION = 2000;

    public static boolean trackStatistics;
    public static String currentProfile = RPG_PROFILE_1;
    public static SkillsAnimations currentSkill;

    public static Drawable player_image;

    private static ImageView changeImageView, changeImageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        trackStatistics = getPrefs().getBoolean(APP_PREFERENCES_TRACK_STATS, true);

        if (getStatsSum() == 0) {
            addStatisticsToNeuralNet(new int[]{1, 1, 1, 1, 1, 1, 1, 1}, currentProfile);
        }
        if (getRPGStatsSum(currentProfile) == 0) {
            updatePlayerStatsSharedPreferences(new int[]{1, 1, 1, 1, 1, 1}, currentProfile);
        }
        setProfileImage(currentProfile, "player_img/alina_lupit.png");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        changeImageView = findViewById(R.id.changeImageView);
        changeImageView2 = findViewById(R.id.changeImageView2);
        AssetManager assets = getAssets();
        InputStream stream = null;
        try {
            stream = assets.open("change1.png");
            Drawable img = Drawable.createFromStream(stream, "change 1");
            changeImageView.setImageDrawable(img);

            stream = assets.open("change2.png");
            img = Drawable.createFromStream(stream, "change 2");
            changeImageView2.setImageDrawable(img);
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "error loading change images : "+e);
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
                Log.e(MainActivity.TAG, "error closing stream - "+e);
            }
        }

        ProfileChooseFragment firstFragment = new ProfileChooseFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, firstFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_track_stats);
        item.setChecked(getPrefs().getBoolean(APP_PREFERENCES_TRACK_STATS, true));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_new_game: newGame(getSupportFragmentManager()); return true;
            case R.id.menu_reset_player: updatePlayerStatsSharedPreferences(new int[]{10,10,10,10,10,1,0,0}, RPG_PROFILE_1); return true;
            case R.id.menu_reset_stats:
                WipeStatisticsDialogFragment fragment = new WipeStatisticsDialogFragment();
                fragment.show(getSupportFragmentManager(), MainActivity.TAG);
                return true;
            case R.id.menu_track_stats: trackStats(item); return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            event.startTracking();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking() && !event.isCanceled()) {
            createExitDialog(getSupportFragmentManager());
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public static void createExitDialog(FragmentManager fragmentManager) {
        ExitDialog dialog = new ExitDialog();
        dialog.show(fragmentManager, MainActivity.TAG);
    }

    public static void newGame(FragmentManager fragmentManager) {
        if (player != null) player.refresh();
        if (enemy != null) enemy.refresh();
        ProfileChooseFragment fragment = new ProfileChooseFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void levelUp(FragmentManager fragmentManager) {
        LevelUpFragment fragment = new LevelUpFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void trackStats(MenuItem item) {
        if (item.isChecked()) {
            trackStatistics = false;
            item.setChecked(false);
        } else if (!item.isChecked()) {
            trackStatistics = true;
            item.setChecked(true);
        }
        setTrackStatistics(trackStatistics);
        Toast.makeText(getApplicationContext(), "Tracking statistics is set to "+trackStatistics, Toast.LENGTH_SHORT).show();
    }

    public static SharedPreferences getPrefs() {
        return mContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static String getSharedWinner() {
        return getPrefs().getString(APP_PREFERENCES_WINNER, "nobody");
    }
    public static boolean getSharedBooleanWinner() {
        return getPrefs().getBoolean(APP_PREFERENCES_WINNER_BOOLEAN, false);
    }
    public static void setSharedWinner(String winner) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(APP_PREFERENCES_WINNER, winner);
        editor.commit();
    }

    public static void setSharedRounds(int rounds) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putInt(APP_PREFERENCES_ROUNDS, rounds);
        editor.commit();
    }
    public static int getSharedRounds() {
        return getPrefs().getInt(APP_PREFERENCES_ROUNDS, 0);
    }
    public static int getSharedDamage() {
        return getPrefs().getInt(APP_PREFERENCES_DAMAGE_INFLICTED, 0);
    }

    public static void setGameOverSharedPref(String winner, int rounds, int hits, int criticals,
                                             int blockBreaks, int blocks, int dodges) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(APP_PREFERENCES_WINNER, winner);
        editor.putInt(APP_PREFERENCES_ROUNDS, rounds);
        editor.putInt(APP_PREFERENCES_HITS, hits);
        editor.putInt(APP_PREFERENCES_CRITICALS, criticals);
        editor.putInt(APP_PREFERENCES_BLOCK_BREAKS, blockBreaks);
        editor.putInt(APP_PREFERENCES_BLOCKS, blocks);
        editor.putInt(APP_PREFERENCES_DODGES, dodges);
        editor.commit();
    }

    public static void setGameOverSharedPref(String winner, boolean isWinner, int rounds, int damage) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(APP_PREFERENCES_WINNER, winner);
        editor.putInt(APP_PREFERENCES_ROUNDS, rounds);
        editor.putInt(APP_PREFERENCES_DAMAGE_INFLICTED, damage);
        editor.putBoolean(APP_PREFERENCES_WINNER_BOOLEAN, isWinner);
        editor.commit();
    }

    public static int getSharedHits() {
        return getPrefs().getInt(APP_PREFERENCES_HITS, 0);
    }
    public static int getSharedCriticals() {
        return getPrefs().getInt(APP_PREFERENCES_CRITICALS, 0);
    }
    public static int getSharedBlockBreaks() {
        return getPrefs().getInt(APP_PREFERENCES_BLOCK_BREAKS, 0);
    }
    public static int getSharedBlocks() {
        return getPrefs().getInt(APP_PREFERENCES_BLOCKS, 0);
    }
    public static int getSharedDodges() {
        return getPrefs().getInt(APP_PREFERENCES_DODGES, 0);
    }



    public static void addStatisticsToNeuralNet(int[] stats, String profile) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(profile, Context.MODE_PRIVATE).edit();
        editor.putInt(NEURAL_NET_ATK_SUCCESSFUL_HEAD, getPrefs().getInt(NEURAL_NET_ATK_SUCCESSFUL_HEAD, 1)+stats[0]);
        editor.putInt(NEURAL_NET_ATK_SUCCESSFUL_BODY, getPrefs().getInt(NEURAL_NET_ATK_SUCCESSFUL_BODY, 1)+stats[1]);
        editor.putInt(NEURAL_NET_ATK_SUCCESSFUL_WAIST, getPrefs().getInt(NEURAL_NET_ATK_SUCCESSFUL_WAIST, 1)+stats[2]);
        editor.putInt(NEURAL_NET_ATK_SUCCESSFUL_LEGS, getPrefs().getInt(NEURAL_NET_ATK_SUCCESSFUL_LEGS, 1)+stats[3]);

        editor.putInt(NEURAL_NET_DEF_ATTACKED_HEAD, getPrefs().getInt(NEURAL_NET_DEF_ATTACKED_HEAD, 1)+stats[4]);
        editor.putInt(NEURAL_NET_DEF_ATTACKED_BODY, getPrefs().getInt(NEURAL_NET_DEF_ATTACKED_BODY, 1)+stats[5]);
        editor.putInt(NEURAL_NET_DEF_ATTACKED_WAIST, getPrefs().getInt(NEURAL_NET_DEF_ATTACKED_WAIST, 1)+stats[6]);
        editor.putInt(NEURAL_NET_DEF_ATTACKED_LEGS, getPrefs().getInt(NEURAL_NET_DEF_ATTACKED_LEGS, 1)+stats[7]);

        editor.commit();
    }
    public static int[] getNeuralNetStatistics(String profile) {
        int[] stats = new int[8];
        SharedPreferences sp = mContext.getSharedPreferences(profile, Context.MODE_PRIVATE);
        stats[0] = sp.getInt(NEURAL_NET_ATK_SUCCESSFUL_HEAD,1);
        stats[1] = sp.getInt(NEURAL_NET_ATK_SUCCESSFUL_BODY,1);
        stats[2] = sp.getInt(NEURAL_NET_ATK_SUCCESSFUL_WAIST,1);
        stats[3] = sp.getInt(NEURAL_NET_ATK_SUCCESSFUL_LEGS,1);

        stats[4] = sp.getInt(NEURAL_NET_DEF_ATTACKED_HEAD,1);
        stats[5] = sp.getInt(NEURAL_NET_DEF_ATTACKED_BODY,1);
        stats[6] = sp.getInt(NEURAL_NET_DEF_ATTACKED_WAIST,1);
        stats[7] = sp.getInt(NEURAL_NET_DEF_ATTACKED_LEGS,1);

        return stats;
    }
    public static void wipeNeuralNetStatistics(String profile) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(profile, Context.MODE_PRIVATE).edit();
        editor.putInt(NEURAL_NET_ATK_SUCCESSFUL_HEAD, 1);
        editor.putInt(NEURAL_NET_ATK_SUCCESSFUL_BODY, 1);
        editor.putInt(NEURAL_NET_ATK_SUCCESSFUL_WAIST, 1);
        editor.putInt(NEURAL_NET_ATK_SUCCESSFUL_LEGS, 1);

        editor.putInt(NEURAL_NET_DEF_ATTACKED_HEAD, 1);
        editor.putInt(NEURAL_NET_DEF_ATTACKED_BODY, 1);
        editor.putInt(NEURAL_NET_DEF_ATTACKED_WAIST, 1);
        editor.putInt(NEURAL_NET_DEF_ATTACKED_LEGS, 1);

        editor.commit();


    }

    public static int getStatsSum() {
        int[] stats = getNeuralNetStatistics(currentProfile);
        int sum = 0;
        for (int i=0; i<stats.length; i++) sum += stats[i];
        return sum;
    }

    private void setTrackStatistics(boolean track) { // TODO : on start, "track stats" is always true
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putBoolean(APP_PREFERENCES_TRACK_STATS, track);
        editor.commit();
    }

    private static SharedPreferences getRPGPrefs() {
        return mContext.getSharedPreferences(RPG_STATS, Context.MODE_PRIVATE);
    }

    public static int[] getInitialStats(String profile) {
        SharedPreferences preferences = mContext.getSharedPreferences(profile, Context.MODE_PRIVATE);
        int stats[] = new int[8];
        stats[0] = preferences.getInt(RPG_STATS_STR, 1);
        stats[1] = preferences.getInt(RPG_STATS_STA, 1);
        stats[2] = preferences.getInt(RPG_STATS_AGI, 1);
        stats[3] = preferences.getInt(RPG_STATS_LUCK, 1);
        stats[4] = preferences.getInt(RPG_STATS_INT, 1);
        stats[5] = preferences.getInt(RPG_STATS_LEVEL, 1);
        stats[6] = preferences.getInt(RPG_STATS_CURRENT_EXP, 1);
        stats[7] = preferences.getInt(RPG_STATS_AVAILABLE_STAT_POINTS, 1);
        return stats;
    }

    public static void updatePlayerStatsSharedPreferences(int[] stats, String profile) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(profile, Context.MODE_PRIVATE).edit();
        editor.putInt(RPG_STATS_STR, stats[0]);
        editor.putInt(RPG_STATS_STA, stats[1]);
        editor.putInt(RPG_STATS_AGI, stats[2]);
        editor.putInt(RPG_STATS_LUCK, stats[3]);
        editor.putInt(RPG_STATS_INT, stats[4]);
        editor.putInt(RPG_STATS_LEVEL, stats[5]);
        editor.putInt(RPG_STATS_CURRENT_EXP, stats[6]);
        editor.putInt(RPG_STATS_AVAILABLE_STAT_POINTS, stats[7]);
        editor.commit();
    }

    public static void updatePlayerLevel(String profile, int level, int exp, int statPoints) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(profile, Context.MODE_PRIVATE).edit();
        editor.putInt(RPG_STATS_LEVEL, level);
        editor.putInt(RPG_STATS_CURRENT_EXP, exp);
        editor.putInt(RPG_STATS_AVAILABLE_STAT_POINTS, statPoints);
        editor.commit();
    }

    private int getRPGStatsSum(String profile) {
        int sum = 0;
        int[] stats = getInitialStats(profile);
        for (int i=0; i<stats.length; i++) sum += stats[i];
        return sum;
    }

    @Override
    public void onButtonSelected(Skill skill) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RPGBattleFragment fragment = (RPGBattleFragment) fragmentManager.findFragmentByTag("MAIN BATTLE FRAGMENT");

        SkillsAnimator animator = new SkillsAnimator(skill, fragment.skillEffect_img,
                fragment.enemy_img, (skill.isEffectOnPlayer? fragment.player_points : fragment.enemy_points), fragment);

        try (InputStream stream = getAssets().open(animator.imageFile)) {
            Drawable img = Drawable.createFromStream(stream, "skill image");
            fragment.skillEffect_img.setImageDrawable(img);
        } catch (IOException e) {
            Log.e(TAG, "error in onButtonSelected loading skill img: "+e);
        }
        animator.start();
    }

    public static String getProfileImage(String profile) {
        return mContext.getSharedPreferences(profile, Context.MODE_PRIVATE).getString(RPG_PROFILE_IMAGE, "carnage_label.png");

    }
    public static void setProfileImage(String profile, String image) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(profile, Context.MODE_PRIVATE).edit();
        editor.putString(RPG_PROFILE_IMAGE, image);
        editor.apply();
    }

    @Override
    public void profileSelected(String profile) {
        currentProfile = profile;
        player = (profile != null? new PlayCharacter(currentProfile, getString(R.string.player_1_name)) : null);
        enemy = (profile != null? new PlayCharacter(player, getString(R.string.player_2_name)) : null);
        //RPGBattleFragment fragment = new RPGBattleFragment();
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.replace(R.id.container, fragment, "MAIN BATTLE FRAGMENT");
        //transaction.addToBackStack(null);
        //transaction.commit();
    }

    public static void animateChangeWindow() {
        AnimatorSet set = new AnimatorSet();
        changeImageView.setVisibility(View.VISIBLE);
        changeImageView2.setVisibility(View.VISIBLE);
        AnimatorSet state1 = new AnimatorSet();
        AnimatorSet state2 = new AnimatorSet();
        state1.setDuration(0).playTogether(
                ObjectAnimator.ofFloat(changeImageView, View.TRANSLATION_X, changeImageView.getWidth()*(-1)),
                ObjectAnimator.ofFloat(changeImageView2, View.TRANSLATION_X, changeImageView2.getWidth()*(-2))
        );
        state2.setDuration(ANIMATION_WINDOW_CHANGE_DURATION).playTogether(
                ObjectAnimator.ofFloat(changeImageView, View.TRANSLATION_X, changeImageView.getWidth()*3),
                ObjectAnimator.ofFloat(changeImageView2, View.TRANSLATION_X, changeImageView2.getWidth()*2)
        );
        set.playSequentially(
                state1,
                //ObjectAnimator.ofFloat(changeImageView, View.TRANSLATION_X, changeImageView.getWidth()).setDuration(duration),
                state2
                //ObjectAnimator.ofFloat(changeImageView2, View.TRANSLATION_X, changeImageView2.getWidth()*2).setDuration(duration)
        );
        set.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeImageView.setVisibility(View.INVISIBLE);
                changeImageView2.setVisibility(View.INVISIBLE);
            }
        }, ANIMATION_WINDOW_CHANGE_DURATION);
    }

    public static long getChangeAnimationDuration() {
        return ANIMATION_WINDOW_CHANGE_DURATION/2;
    }
}
