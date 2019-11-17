package com.example.user.carnage.common.logic.main;

import com.example.user.carnage.client.MainActivity;

public class Levels {
    private int targetExp;
    private int expReceived;
    private int rounds;
    private int damage;
    private PlayCharacter character;
    private boolean isWinner;
    public boolean isLevelUp = false;

    public Levels(PlayCharacter playCharacter, int rounds, int damageInflicted, boolean isPlayerWinner) {
        character = playCharacter;
        this.rounds = rounds;
        damage = damageInflicted;
        isWinner = isPlayerWinner;
        handle();
    }

    private void handle() {
        expReceived = (int) ((((double) rounds * 5.0)/100.0 + 1.00) * character.getLevel()) * damage /5;
        expReceived /= (isWinner ? 1: 5);
        setTargetExp();
        character.setExp(character.getCurrentExp() + expReceived);
        if (character.getCurrentExp() >= targetExp) {
            isLevelUp = true;
            do {
                character.levelUp();
                setTargetExp();
            } while (character.getCurrentExp() >= targetExp);
            MainActivity.updatePlayerLevel(MainActivity.RPG_PROFILE_1, character.getLevel(),
                    character.getCurrentExp(), character.getAvailableStatPoints());
        }
    }

    private void setTargetExp() {
        int magnification = 50;
        targetExp = ((character.getLevel()-1)*(character.getLevel()-1) + character.getLevel()*character.getLevel())*magnification;
        System.out.println("level: "+character.getLevel()+". Target EXP: "+targetExp);
    }

    public int getTargetExp() { return targetExp; }

    public int getExpReceived() {
        return expReceived;
    }

    public String getStringExpReceivedCalc() {
        String s = "EXP = ((rounds "+rounds+" * 5) / 100 + 1) * level "+character.getLevel()+" * damage "+damage+" /5 = "+expReceived
                +'\n'+"target EXP: "+targetExp;
        return s;
    }
}
