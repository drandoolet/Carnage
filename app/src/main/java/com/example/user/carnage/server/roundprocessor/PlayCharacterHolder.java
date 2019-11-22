package com.example.user.carnage.server.roundprocessor;

import com.example.user.carnage.common.logic.main.PlayCharacter;
import com.example.user.carnage.server.roundprocessor.roundelement.GameOverException;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;
import com.example.user.carnage.common.logic.main.PlayCharacter.SubtractableValue.Value;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

final class PlayCharacterHolder {
    private final PlayCharacter player_1, player_2;
    private final AtomicInteger counter = new AtomicInteger(0);

    PlayCharacterHolder(PlayCharacter player_1, PlayCharacter player_2) {
        this.player_1 = player_1;
        this.player_2 = player_2;
    }

    void update(RoundResults results) throws GameOverException {
        //System.out.println("PCH before update:\n"+player_1.toString()+'\n'+player_2.toString());
        int php = player_1.getState(PlayCharacter.MainScales.HP, Value.MAX_VALUE);
        int pmp = player_1.getState(PlayCharacter.MainScales.MP, Value.MAX_VALUE);
        int ehp = player_2.getState(PlayCharacter.MainScales.HP, Value.MAX_VALUE);

        System.out.println(String.format(Locale.ENGLISH,
                "Round %d:\n%s",
                counter.get() +1,
                results));
        try {
            for (RoundResults.RoundStage stage : results.getStages()) {
                if (stage.getActor() == RoundResults.Players.PLAYER_1) {
                    player_1.applySubtraction(stage.getSubtractor().getActorSubtraction());
                    player_2.applySubtraction(stage.getSubtractor().getEnemySubtraction());
                } else if (stage.getActor() == RoundResults.Players.PLAYER_2) {
                    player_1.applySubtraction(stage.getSubtractor().getEnemySubtraction());
                    player_2.applySubtraction(stage.getSubtractor().getActorSubtraction());
                } else throw new IllegalArgumentException();
            }
        } catch (GameOverException e) {
            System.out.println(String.format(Locale.ENGLISH,
                    "Game finished at Round %d. Winner: %s",
                    counter.get(),
                    player_1.getState(PlayCharacter.MainScales.HP, Value.CURRENT_VALUE) >0?
                        player_1.getName() : player_2.getName()));
            throw new GameOverException("Game over!");
        }

        //System.out.println("PCH updated PCs:\n"+player_1.toString()+'\n'+player_2.toString());
        System.out.println(String.format(Locale.ENGLISH,
                "Round %d results: Player HP:(%d/%d) MP:(%d/%d), Enemy (%d/%d)\n",
                counter.incrementAndGet(),
                player_1.getState(PlayCharacter.MainScales.HP, Value.CURRENT_VALUE),
                php,
                player_1.getState(PlayCharacter.MainScales.MP, Value.CURRENT_VALUE),
                pmp,
                player_2.getState(PlayCharacter.MainScales.HP, Value.CURRENT_VALUE),
                ehp));
    }
}
