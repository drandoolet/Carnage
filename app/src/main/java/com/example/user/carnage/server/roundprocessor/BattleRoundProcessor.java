package com.example.user.carnage.server.roundprocessor;

import com.example.user.carnage.common.parsing.QueryParser;
import com.example.user.carnage.common.parsing.ResponseParser;
import com.example.user.carnage.logic.main.PlayCharacter;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Get JSON from Server
 * Parse JSON , using: Parser
 * Process , using: Processor
 * Return: RoundResults
 * Pass to Parser
 * Create JSON, return to Server
 */
class BattleRoundProcessor {
    private final BattleHandler handler;

    public BattleRoundProcessor(PlayCharacter player_1, PlayCharacter player_2) {
        handler = new BattleHandler(player_1, player_2);
    }

    /**
     * Clients make plans, but actually do nothing
     *
     * Clients can have their own names, but internally are called player1 and player2
     *
     * Clients send (JSON):
     * atk, def, skills
     *
     * Server processes, creates a list of round elements (n)
     *
     * Server sends (JSON RoundStage):
     * {
     *      player 1 subtract (can be negative, means addition) : {MainScalesSubtraction hp, sp, mp}, p2 {-//-},
     *      player 1 or 2 (who acts),
     *      Nullable Skill (if null == normal),
     *      Nullable RoundStatus (considered null if Skill != null),
     *      Nullable BodyPartNames target (considered null if Skill != null)
     * } * n
     *
     * First skills, then attacks, all in turns (order provided by Server)
     *
     * For Skills: Client checks if a Skill can be used => uses or skips (saying smth)
     */

    /**
     *
     * @param queryObj: (from JSON) PlayerChoice x2 (Pl_1, Pl_2), Players (whose turn)
     * @return RoundResults in JSON
     * @throws JSONException
     */
    public JSONObject getResponse(JSONObject queryObj) throws JSONException {
        Query query = QueryParser.fromJson(queryObj);
        /*
        for (RoundResults.RoundStage stage: results.getStages()) {
            array.put(new JSONObject()
                    .put("subtraction", new JSONArray().put(new JSONObject()
                            .put("player1", stage.getSubtraction_player_1().toJson())
                            .put("player2", stage.getSubtraction_player_2().toJson())))
                    .put("actor", stage.getActor())
                    .put("skill", stage.getSkill())
                    .put("status", stage.getStatus())
                    .put("target", stage.getTarget())
            );
        }
        */
        return ResponseParser.toJson(handler.process(query));
    }

}
