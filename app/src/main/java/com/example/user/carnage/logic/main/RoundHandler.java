package com.example.user.carnage.logic.main;

import java.security.SecureRandom;

import static com.example.user.carnage.MainActivity.player;
import static com.example.user.carnage.MainActivity.enemy;

public class RoundHandler {
    private SecureRandom random = new SecureRandom();
    private PlayerChoice playerChoice, enemyChoice;
    private final int player_dodge, enemy_dodge;
    private final int player_a_dodge, enemy_a_dodge;
    private final int player_crit, enemy_crit;
    private final int player_a_crit, enemy_a_crit;
    private final double player_crit_dmg, enemy_crit_dmg;

    public RoundHandler(PlayCharacter player, PlayCharacter enemy) {
        player_dodge = player.getDodgeRate();
        player_a_dodge = player.getAntiDodgeRate();
        enemy_dodge = enemy.getDodgeRate();
        enemy_a_dodge = enemy.getAntiDodgeRate();
        player_crit = player.getCritical();
        player_a_crit = player.getAntiCritical();
        enemy_crit = enemy.getCritical();
        enemy_a_crit = enemy.getAntiCritical();
        player_crit_dmg = player.getCriticalDmg();
        enemy_crit_dmg = enemy.getCriticalDmg();
    }

    public void handle(PlayerChoice plc, PlayerChoice enc) {
        if (hasDodged()) { // player dodged
            System.out.println("RH player dodged!");
        } else {

        }


        for (BodyPart.BodyPartNames attack : plc.getAttacked()) {
            if (enc.getDefended().contains(attack)) {

            }
        }
    }

    public boolean hasDodged() {
        int rate;
        if (player_dodge == enemy_a_dodge) {
            rate = 10;
        } else {
            rate = ((player_dodge - enemy_a_dodge) / player_dodge);
            rate = rate *50 /2 +30;
            if (rate <10) rate = 10;
        }
        return random.nextInt(100) < rate;
    }
}
