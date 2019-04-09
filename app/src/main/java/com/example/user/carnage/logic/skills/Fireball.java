package com.example.user.carnage.logic.skills;

import com.example.user.carnage.animation.SkillsAnimator;
import com.example.user.carnage.logic.main.PlayCharacter;

import static com.example.user.carnage.MainActivity.currentSkill;

public class Fireball extends AttackingSkill {
    public Fireball(PlayCharacter playCharacter, PlayCharacter enemy) {
        super(playCharacter, enemy, 0.2, 50, PlayCharacter.Stats.INTELLIGENCE,
                PlayCharacter.Substats.MAGICAL_DEFENCE, 1, 1, "fireball info");
        name = SkillsAnimator.SkillsAnimations.FIREBALL;
        currentSkill = name;
    }
}