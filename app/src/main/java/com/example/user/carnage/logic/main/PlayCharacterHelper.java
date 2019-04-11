package com.example.user.carnage.logic.main;

import java.security.SecureRandom;
import java.util.ArrayList;

import static com.example.user.carnage.MainActivity.player;
import static com.example.user.carnage.MainActivity.enemy;

public class PlayCharacterHelper {
    private SecureRandom random = new SecureRandom();
    private final double player_crit_dmg, enemy_crit_dmg;
    private final int dodgeRate, critRate;
    private final PlayCharacter playerChar, enemyChar;

    public PlayCharacterHelper(PlayCharacter player, PlayCharacter enemy) {
        playerChar = player;
        enemyChar = enemy;
        player_crit_dmg = player.getCriticalDmg();
        enemy_crit_dmg = enemy.getCriticalDmg();

        if (player.getDodgeRate() == enemy.getAntiDodgeRate()) {
            dodgeRate = 10;
        } else {
            int rate;
            rate = ((player.getDodgeRate() - enemy.getAntiDodgeRate()) / player.getDodgeRate());
            rate = rate *50 /2 +30;
            if (rate <10) rate = 10;
            dodgeRate = rate;
        }

        if (player.getCritical() == enemy.getAntiCritical()) {
            critRate = 10;
        } else {
            int rate;
            rate = ((player.getCritical() - enemy.getAntiCritical()) / player.getCritical());
            rate = rate *50 /2 +30;
            if (rate <10) rate = 10;
            critRate = rate;
        }
    }

    public void handle(PlayerChoice plc, PlayerChoice enc) {
        if (hasDodged()) { // player dodged
            System.out.println("RH player dodged!");
        } else {
            for (BodyPart.BodyPartNames attack : enc.getAttacked()) {
                if (hasBlocked(attack, enc.getAttacked())) {
                    System.out.println("RH ");
                }
            }
        }


        for (BodyPart.BodyPartNames attack : plc.getAttacked()) {
            if (enc.getDefended().contains(attack)) {

            }
        }
    }

    private boolean hasDodged() {
        return random.nextInt(100) < dodgeRate;
    }
    private boolean hasPerformedCritical() {
        return random.nextInt(100) < critRate;
    }
    private boolean hasBlocked(BodyPart.BodyPartNames attack,
                                       ArrayList<BodyPart.BodyPartNames> defended) {
        return defended.contains(attack);
    }
}
