package com.example.user.carnage.logic.skills;

import com.example.user.carnage.R;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.PlayCharacter.Stats;
import com.example.user.carnage.logic.main.attack.effect.Subtraction.SubtractionType;

class FireballNew extends SkillNew {
    FireballNew() {
        super(new Builder(SkillTypes.FIREBALL)
                .setEffectOnPlayer(false)
                .setInfo(R.string.magic_info_fireball)
                .setSkillCalculations(new SkillCalculations.Builder()
                        .setAddition(5.0)
                        .addStatEngaged(Stats.INTELLIGENCE, 3.5)
                        .setManaCost(10, SubtractionType.RELATIVE_MAX)
                        .setDefenceSubstat(PlayCharacter.Substats.MAGICAL_DEFENCE)
                        .build())
                .setAtkBoundTaker(0)
                .setDefBoundTaker(1)
                .setIcon("skill/Fireball.png")
        );
    }
}
