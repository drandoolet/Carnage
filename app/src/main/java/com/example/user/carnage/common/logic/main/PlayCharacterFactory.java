package com.example.user.carnage.common.logic.main;

public final class PlayCharacterFactory {
    public static PlayCharacter findInDB(String profile) {
        return newTestPlayer(profile);
        //return new PlayCharacter(profile, "REPLACE_GET_DB");
    }

    public static PlayCharacter createEnemy(PlayCharacter character) {
        return new PlayCharacter(character, "ENEMY");
    }

    public static PlayCharacter newTestPlayer(String name) {
        return PlayCharacter.newTestPlayer(name);
    }
}
