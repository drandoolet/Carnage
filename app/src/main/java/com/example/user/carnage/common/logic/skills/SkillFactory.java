package com.example.user.carnage.common.logic.skills;

import com.example.user.carnage.common.logic.main.PlayCharacter;

public class SkillFactory {
    public static Skill newSkill(Skill.SkillTypes type, PlayCharacter player, PlayCharacter enemy) {
        if (player == null) throw new IllegalArgumentException("Player must be NotNull");

        switch (type) {
            case HEAL_SMALL: return new SmallHeal(player);
            case FIREBALL: if (enemy != null) return new Fireball(player, enemy);
        }
        throw new IllegalArgumentException();
    }

    public static Skill newSkill(Skill.SkillTypes type, PlayCharacter player) {
        return newSkill(type, player, null);
    }

    public static SkillNew newSkill(SkillNew.SkillTypes type) {
        return type.getSkill();
    }
}
