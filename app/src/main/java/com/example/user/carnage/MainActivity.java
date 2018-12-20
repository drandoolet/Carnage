package com.example.user.carnage;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SkillsFragment.OnSelectedButtonListener {
    public static final String TAG = "CARNAGE";
    public static Context mContext;
    public static final String APP_PREFERENCES = "gameOverSettings";
    public static final String APP_PREFERENCES_WINNER = "winner";
    public static final String APP_PREFERENCES_ROUNDS = "rounds";
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

    public static boolean trackStatistics;

    public static Drawable player_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        if (getStatsSum() == 0) {
            addStatisticsToNeuralNet(new int[]{1, 1, 1, 1, 1, 1, 1, 1});
        }
        if (getRPGStatsSum() == 0) {
            updatePlayerStatsSharedPreferences(new int[]{1, 1, 1, 1, 1}, RPG_PROFILE_1);
        }
        setProfileImage(RPG_PROFILE_1, "player_img/alina_lupit.png");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MenuChooseFragment firstFragment = new MenuChooseFragment();
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
        MenuChooseFragment.player.refresh();
        MenuChooseFragment.enemy.refresh();
        MenuChooseFragment fragment = new MenuChooseFragment();
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



    public static void addStatisticsToNeuralNet(int[] stats) {
        SharedPreferences.Editor editor = getPrefs().edit();
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
    public static int[] getNeuralNetStatistics() {
        int[] stats = new int[8];
        stats[0] = getPrefs().getInt(NEURAL_NET_ATK_SUCCESSFUL_HEAD,1);
        stats[1] = getPrefs().getInt(NEURAL_NET_ATK_SUCCESSFUL_BODY,1);
        stats[2] = getPrefs().getInt(NEURAL_NET_ATK_SUCCESSFUL_WAIST,1);
        stats[3] = getPrefs().getInt(NEURAL_NET_ATK_SUCCESSFUL_LEGS,1);

        stats[4] = getPrefs().getInt(NEURAL_NET_DEF_ATTACKED_HEAD,1);
        stats[5] = getPrefs().getInt(NEURAL_NET_DEF_ATTACKED_BODY,1);
        stats[6] = getPrefs().getInt(NEURAL_NET_DEF_ATTACKED_WAIST,1);
        stats[7] = getPrefs().getInt(NEURAL_NET_DEF_ATTACKED_LEGS,1);

        return stats;
    }
    public static void wipeNeuralNetStatistics() {
        SharedPreferences.Editor editor = getPrefs().edit();
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
        int[] stats = getNeuralNetStatistics();
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

    public static int[] getInitialStats() {
        int stats[] = new int[7];
        stats[0] = getRPGPrefs().getInt(RPG_STATS_STR, 1);
        stats[1] = getRPGPrefs().getInt(RPG_STATS_STA, 1);
        stats[2] = getRPGPrefs().getInt(RPG_STATS_AGI, 1);
        stats[3] = getRPGPrefs().getInt(RPG_STATS_LUCK, 1);
        stats[4] = getRPGPrefs().getInt(RPG_STATS_INT, 1);
        stats[5] = getRPGPrefs().getInt(RPG_STATS_LEVEL, 1);
        stats[6] = getRPGPrefs().getInt(RPG_STATS_CURRENT_EXP, 1);
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
        editor.commit();
    }

    private int getRPGStatsSum() {
        int sum = 0;
        int[] stats = getInitialStats();
        for (int i=0; i<stats.length; i++) sum += stats[i];
        return sum;
    }

    @Override
    public void onButtonSelected(int buttonIndex) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RPGBattleFragment fragment = (RPGBattleFragment) fragmentManager.findFragmentByTag("MAIN BATTLE FRAGMENT");
        fragment.setAllEnabled(true);
        fragment.animateSkillsFragmentAppearance(false);
    }

    public static String getProfileImage(String profile) {
        return mContext.getSharedPreferences(profile, Context.MODE_PRIVATE).getString(RPG_PROFILE_IMAGE, "carnage_label.png");

    }
    public static void setProfileImage(String profile, String image) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(profile, Context.MODE_PRIVATE).edit();
        editor.putString(RPG_PROFILE_IMAGE, image);
        editor.apply();
    }
}
