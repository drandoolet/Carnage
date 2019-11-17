package com.example.user.carnage.common.logic.skills;

import com.example.user.carnage.R;
import com.example.user.carnage.common.logic.main.PlayCharacter;
import com.example.user.carnage.common.logic.main.attack.effect.Subtraction;

class SmallHealNew extends SkillNew {
    SmallHealNew() {
        super(new Builder(SkillTypes.HEAL_SMALL)
                .setEffectOnPlayer(true)
                .setInfo(R.string.magic_info_heal)
                .setSkillCalculations(new SkillCalculations.Builder()
                        .setManaCost(50, Subtraction.SubtractionType.ABSOLUTE)
                        .addSubtractionValue( // HP restoration: sub < 0
                                PlayCharacter.MainScales.HP, -15, Subtraction.SubtractionType.RELATIVE_MAX)
                        .build())
                .setAtkBoundTaker(0)
                .setDefBoundTaker(1)
                .setIcon("skill/Heal.png")
        );
    }
}
