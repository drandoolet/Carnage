package com.example.user.carnage.logic.skills;

import com.example.user.carnage.animation.SkillsAnimator;
import com.example.user.carnage.logic.main.PlayCharacter;

import static com.example.user.carnage.MainActivity.currentSkill;

public class Fireball extends Skill {
    //public static final String icon = "skill/Fireball.png";

    public Fireball(PlayCharacter player, PlayCharacter enemy) {
        super(
                new Builder(player, enemy, SkillTypes.FIREBALL)
                        .setAddition(5.0)
                        .setMagnification(3.5)
                        .setMagnifiedStat(PlayCharacter.Stats.INTELLIGENCE)
                        .setAffectedSubstat(PlayCharacter.Substats.MAGICAL_DEFENCE)
                        .setDefBoundTaker(1)
                        .setAtkBoundTaker(0)
                        .setInfo("fireball info")
                        .setManaCost(player.getMPPercent(10))
                        .setIcon("skill/Fireball.png")
        );
    }
}