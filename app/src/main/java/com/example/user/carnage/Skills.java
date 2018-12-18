package com.example.user.carnage;

public class Skills {
    protected PlayCharacter character;
    protected int statIdx;
    protected double magnification;

    Skills(PlayCharacter playCharacter, int statIdx) {
        character = playCharacter;
        this.statIdx = statIdx;
    }

    protected void use() {
        character.setHP(character.getHP() + getEffect());
    }

    public int getEffect() {
        return (int) (character.getMainStats()[statIdx]*magnification);
    }
}

class SmallHeal extends Skills {
    //private double magnification = 1.0;

    SmallHeal(PlayCharacter playCharacter, int statIdx) {
        super(playCharacter, statIdx);
        magnification = 1.0;
    }

    public void use() {
        super.use();
    }
}
