package com.example.user.carnage.server.roundprocessor;

import com.example.user.carnage.client.ClientTest;
import com.example.user.carnage.common.logic.main.PlayCharacter;
import com.example.user.carnage.server.roundprocessor.roundelement.GameOverException;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PlayCharacterSubtractionTest {
    @Test
    public void testSubtractionsFromRoundResultsAppliedRight() throws JSONException {
        PlayCharacter character = PlayCharacter.newTestPlayer("PLAYER");
        PlayCharacter enemy = PlayCharacter.newTestPlayer("ENEMY");
        PlayCharacterHolder holder = new PlayCharacterHolder(character, enemy);

        RoundResults roundResults = ClientTest.getRoundResults(character, enemy);

        System.out.println("character:\n"+character.getStateInfo());
        System.out.println("enemy:\n"+enemy.getStateInfo());
        try {
            holder.update(roundResults);
        } catch (GameOverException e) {
            System.out.println("    ------  GAME OVER!  ------ ");
        }
        System.out.println("character after:\n"+character.getStateInfo());
        System.out.println("enemy after:\n"+enemy.getStateInfo());
    }
}
