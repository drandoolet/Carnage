package com.example.user.carnage;

public class Levels {
    private int targetExp;
    private int expReceived;
    private PlayCharacter character;

    Levels(PlayCharacter playCharacter, int expReceived) {
        character = playCharacter;
        this.expReceived = expReceived;
        handle();
    }

    private void handle() {
        character.setExp(character.getCurrentExp() + expReceived);
        if (character.getCurrentExp() > targetExp) {
            do {
                character.levelUp();
                setTargetExp();
            } while (character.getCurrentExp() > targetExp);
        }
    }

    private void setTargetExp() {
        int magnification = 50;
        targetExp = ((character.getLevel()-1) * magnification) + (character.getLevel() * magnification);
    }

    public int getTargetExp() { return targetExp; }
}
