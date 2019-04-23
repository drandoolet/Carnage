package com.example.user.carnage.logic.skills;

import com.example.user.carnage.R;
import com.example.user.carnage.logic.main.PlayCharacter;

public class SmallHeal extends Skill {
    public SmallHeal(PlayCharacter player) {
        super(
                new Builder(player, null, SkillTypes.HEAL_SMALL)
                        .setAddition(player.getHPPercent(15))
                        .setMagnification(0)
                        .setMagnifiedStat(PlayCharacter.Stats.INTELLIGENCE)
                        .setAffectedSubstat(null)
                        .setDefBoundTaker(1)
                        .setAtkBoundTaker(0)
                        .setIsEffectOnPlayer(true)
                        .setInfo(R.string.magic_info_heal)
                        .setManaCost(50)
                        .setIcon("skill/Heal.png")
        );
    }
}
