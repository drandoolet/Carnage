package com.example.user.carnage.logic.skills;

import com.example.user.carnage.R;
import com.example.user.carnage.logic.main.PlayCharacter;

public class Fireball extends Skill {
    public Fireball(PlayCharacter player, PlayCharacter enemy) {
        super(
                new Builder(player, enemy, SkillTypes.FIREBALL)
                        .setAddition(5.0)
                        .setMagnification(3.5)
                        .setMagnifiedStat(PlayCharacter.Stats.INTELLIGENCE)
                        .setAffectedSubstat(PlayCharacter.Substats.MAGICAL_DEFENCE)
                        .setDefBoundTaker(1)
                        .setAtkBoundTaker(0)
                        .setInfo(R.string.magic_info_fireball)
                        .setManaCost(player.getMPPercent(10))
                        .setIcon("skill/Fireball.png")
        );
    }
}