package com.example.user.carnage.server.roundprocessor;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.user.carnage.logic.main.BodyPart;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.Subtraction;
import com.example.user.carnage.logic.skills.Fireball;
import com.example.user.carnage.logic.skills.Skill;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class BattleRoundProcessor {
    private final PlayCharacter player_1, player_2;

    public BattleRoundProcessor(PlayCharacter player_1, PlayCharacter player_2) {
        this.player_1 = player_1;
        this.player_2 = player_2;
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
     *      player 1 subtract (can be negative, means addition) : {Subtraction hp, sp, mp}, p2 {-//-},
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

    public JSONObject getResponse(RoundResults results) throws JSONException {
        JSONObject main = new JSONObject();
        JSONArray array = new JSONArray();
        for (RoundResults.RoundStage stage: results.getStages()) {
            array.put(new JSONObject()
                    .put("subtraction", new JSONArray().put(new JSONObject()
                            .put("player1", stage.subtraction_player_1.toJson())
                            .put("player2", stage.subtraction_player_2.toJson())))
                    .put("actor", stage.actor)
                    .put("skill", stage.skill)
                    .put("status", stage.status)
                    .put("target", stage.target)
            );
        }
        return main.put("stages", array);
    }

    /**
     * Show-only
     *
     * Meaning: Player 1 spends 30 points of mana to inflict damage
     * that equals 50% of current HP and SP of Player 2
     *
     * Actor is Player 1 (and he is who spends mana, needs to be first)
     *
     * Then specifies a Skill - Fireball (TODO SkillFactory.class, add Subtractions to Skill.class)
     * @throws JSONException
     */
    private void t() throws JSONException {
        JSONObject object = getResponse(new RoundResults.Builder()
                .addStage(RoundResults.RoundStage.newStageBuilder(
                        Subtraction.SubtractionFactory.newSubtractionBuilder()
                                .setSubtraction(PlayCharacter.MainScalesType.MP, 30, Subtraction.SubtractionType.ABSOLUTE)
                                .build(),
                        Subtraction.SubtractionFactory.newSubtractionBuilder()
                                .setSubtraction(PlayCharacter.MainScalesType.HP, 50, Subtraction.SubtractionType.RELATIVE_CURRENT)
                                .setSubtraction(PlayCharacter.MainScalesType.SP, 50, Subtraction.SubtractionType.RELATIVE_CURRENT)
                                .build(),
                        new PlayCharacter(player_1, "test ACTOR")
                )
                        .setSkill(new Fireball(player_1, player_2))
                        .build())
                //.addStage( bla bla line 2)
                .build());
    }

    static class RoundResults {
        private final List<RoundStage> stages;

        private RoundResults(Builder builder) {
            this.stages = builder.stages;
        }

        static class Builder {
            private List<RoundStage> stages;

            public Builder() {
                stages = new ArrayList<>();
            }

            Builder addStage(RoundStage stage) {
                stages.add(stage);
                return this;
            }

            RoundResults build() {
                return new RoundResults(this);
            }
        }

        List<RoundStage> getStages() {
            return stages;
        }

        /**
         * An entity that represents a single kick or magic usage.
         * i.e. "Player 1 aims at Head, dealing 100 points of damage."
         *
         * Transferred to Server to make JSON for Clients
         */
        static class RoundStage {
            @NonNull private final Subtraction subtraction_player_1, subtraction_player_2;
            @NonNull private final PlayCharacter actor; // or maybe a String? boolean?
            @Nullable private final Skill skill;
            @Nullable private final PlayCharacter.RoundStatus status;
            @Nullable private final BodyPart.BodyPartNames target;

            public static Builder newStageBuilder(Subtraction pl1, Subtraction pl2, PlayCharacter actor) {
                return new Builder(pl1, pl2, actor);
            }

            private RoundStage(Builder builder) {
                subtraction_player_1 = builder.subtraction_player_1;
                subtraction_player_2 = builder.subtraction_player_2;
                actor = builder.actor;
                skill = builder.skill;
                status = builder.status;
                target = builder.target;
            }

            static class Builder {
                private final Subtraction subtraction_player_1, subtraction_player_2;
                private final PlayCharacter actor;
                @Nullable private Skill skill = null;
                @Nullable private PlayCharacter.RoundStatus status = null;
                @Nullable private BodyPart.BodyPartNames target = null;

                private Builder(Subtraction subtraction_player_1, Subtraction subtraction_player_2, PlayCharacter actor) {
                    this.subtraction_player_1 = subtraction_player_1;
                    this.subtraction_player_2 = subtraction_player_2;
                    this.actor = actor;
                }

                Builder setSkill(Skill skill) {
                    this.skill = skill;
                    return this;
                }

                Builder setRoundStatus(PlayCharacter.RoundStatus status) {
                    this.status = status;
                    return this;
                }

                Builder setTarget(BodyPart.BodyPartNames target) {
                    this.target = target;
                    return this;
                }

                RoundStage build() {
                    return new RoundStage(this);
                }
            }
        }
    }

    enum Players {
        PLAYER_1, PLAYER_2
    }
}
