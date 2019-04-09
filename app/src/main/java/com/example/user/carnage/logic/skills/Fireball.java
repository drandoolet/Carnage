package com.example.user.carnage.logic.skills;

import com.example.user.carnage.animation.SkillsAnimator;
import com.example.user.carnage.logic.main.PlayCharacter;

import static com.example.user.carnage.MainActivity.currentSkill;

public class Fireball extends Skill {
    public Fireball(PlayCharacter player, PlayCharacter enemy) {
        super(
                new Builder(player, enemy, SkillTypes.FIREBALL)
                        .setAddition(20.0)
                        .setMagnification(1.5)
                        .setMagnifiedStat(PlayCharacter.Stats.INTELLIGENCE)
                        .setAffectedSubstat(PlayCharacter.Substats.MAGICAL_DEFENCE)
                        .setDefBoundTaker(1)
                        .setAtkBoundTaker(1)
                        .setInfo("fireball info")
        );
    }
}