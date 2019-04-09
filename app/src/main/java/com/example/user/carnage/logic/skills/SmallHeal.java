package com.example.user.carnage.logic.skills;

import com.example.user.carnage.animation.SkillsAnimator;
import com.example.user.carnage.logic.main.PlayCharacter;

import static com.example.user.carnage.MainActivity.currentSkill;

public class SmallHeal extends Skill {
    public SmallHeal(PlayCharacter player) {
        super(
                new Builder(player, null, SkillTypes.HEAL_SMALL)
                        .setAddition(10.0)
                        .setMagnification(1.0)
                        .setMagnifiedStat(PlayCharacter.Stats.INTELLIGENCE)
                        .setAffectedSubstat(null)
                        .setDefBoundTaker(1)
                        .setAtkBoundTaker(0)
                        .setIsEffectOnPlayer(true)
                        .setInfo("small heal info")
        );
    }
}
