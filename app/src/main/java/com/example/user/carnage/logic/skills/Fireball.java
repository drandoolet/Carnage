package com.example.user.carnage.logic.skills;

import com.example.user.carnage.animation.SkillsAnimator;
import com.example.user.carnage.logic.main.PlayCharacter;

import static com.example.user.carnage.MainActivity.currentSkill;

public class Fireball extends Skill {
    public Fireball(PlayCharacter playCharacter, PlayCharacter enemy) {
        super(playCharacter, enemy, 4, false);
        magnification = -1.0;
        addition = 0;
        atkBoundTaker = 1;
        name = SkillsAnimator.SkillsAnimations.FIREBALL;
        currentSkill = name;
    }
}