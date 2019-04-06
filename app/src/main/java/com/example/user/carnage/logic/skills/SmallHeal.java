package com.example.user.carnage.logic.skills;

import com.example.user.carnage.animation.SkillsAnimator;
import com.example.user.carnage.logic.main.PlayCharacter;

import static com.example.user.carnage.MainActivity.currentSkill;

public class SmallHeal extends Skill {
    public SmallHeal(PlayCharacter playCharacter, PlayCharacter enemy) {
        super(playCharacter, enemy, 4, true);
        magnification = 1.0;
        addition = 0;
        defBoundTaker = 1;
        name = SkillsAnimator.SkillsAnimations.HEAL_SMALL;
        currentSkill = name;
    }
}
