package com.example.user.carnage.server;

import android.os.Build;
import android.provider.Settings;

import com.example.user.carnage.logic.main.BodyPart;
import com.example.user.carnage.logic.main.PlayCharacter;
import com.example.user.carnage.logic.main.PlayerChoice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerModel {
    private final ExecutorService executor;
    private final Map<Integer, Session> sessions;
    private final Map<Integer, Session.Builder> waitingForPlayersSessions;
    private final Random random = new SecureRandom();

    private final int MAX_ROOM_NUMBER = 999_999;

    public ServerModel(int threads) {
        this.executor = Executors.newFixedThreadPool(threads);
        sessions = Collections.synchronizedMap(new HashMap<>());
        waitingForPlayersSessions = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Connects PlayCharacter to Session.Builder and waits for it to build a Session,
     * then returns it.
     *
     * @param character
     * @param sessionNumber
     * @return
     */
    public Session connect(PlayCharacter character, int sessionNumber) {
        // check if session is waiting for players
        if (Build.VERSION.SDK_INT >23 ?
                waitingForPlayersSessions.keySet().stream().anyMatch((v) -> v == sessionNumber) :
                waitingForPlayersSessions.keySet().contains(sessionNumber))
        {
            Session.Builder builder = waitingForPlayersSessions
                    .get(sessionNumber)
                    .connect(character);
            while (true) { // TODO awful
                try {
                    return builder.build();
                } catch (IllegalStateException e) {
                    Thread.yield();
                }
            }
        } else throw new IllegalStateException("No such session number: "+sessionNumber+"!");
    }

    /**
     * Creates a new Session.Builder and waits for another PlayCharacter to enter the Session
     * using ServerModel.connect(int)
     *
     * @return int = session number, yet non-existing until second player enters
     */
    public int newSession() {
        int sessionNumber = random.nextInt(MAX_ROOM_NUMBER+1);
        waitingForPlayersSessions.put(sessionNumber, new Session.Builder(sessionNumber));
        return sessionNumber;
    }

    /**
     * Everything connected with PlayCharacters' insides lies here, gets changed here.
     * Clients get updates from Session.
     * Clients connect to Session via ServerModel.connect(int).
     * PlayCharacters return updates after interacting with each other.
     *
     */
    private static class Session {
        private final PlayCharacter player_1, player_2;
        private final int sessionNumber;

        private Session(Builder builder) {
            this.player_1 = builder.player_1;
            this.player_2 = builder.player_2;
            this.sessionNumber = builder.sessionNumber;
        }

        static class Builder {
            private PlayCharacter player_1, player_2;
            private final int sessionNumber;

            Builder(int sessionNumber) {
                this.sessionNumber = sessionNumber;
            }

            Builder connect(PlayCharacter character) {
                if (player_1 == null) player_1 = character;
                else player_2 = character;
                return this;
            }

            Session build() { // TODO CountDownLatch or smth
                if (player_1 != null && player_2 != null)
                    return new Session(this);
                else throw new IllegalStateException(String.format("Session.Builder is not ready: player(s) %snot set.",
                        (player_1==null? "1 ":"") + (player_2==null? "2 ":"")));
            }
        }

        /**
         *
         * @param player
         * JSON string: hit power, phys/magic, atks, defs
         *
         * @return
         * JSON string: { dmg received, round status, body part } x0-4
         */
        public JSONObject getResponse(JSONObject player) {
            return new JSONObject();
        }
    }

    class FightProcessor {
        private final PlayCharacter player_1, player_2;

        FightProcessor(PlayCharacter player_1, PlayCharacter player_2) {
            this.player_1 = player_1;
            this.player_2 = player_2;
        }

        void process(JSONObject player1Turn, JSONObject player2Turn) {

        }
    }

    public static class JsonTurnParser {
        public static PlayerChoice parsePlayerChoice(JSONObject parse) throws JSONException {
            return JsonPlayerChoice.getPlayerChoice(parse);
        }

        public static JSONObject convert(PlayerChoice choice) throws JSONException {
            return JsonPlayerChoice.getJson(choice);
        }

        /**
         * @format { "attacked" : {"BodyPartNames", ...}, "defended" : {...}, "skills" : {...} }
         */
        private static class JsonPlayerChoice {

            private static BodyPart.BodyPartNames getBodyPartName(String s) {
                return BodyPart.BodyPartNames.valueOf(s);
            }

            private static <T> void fill(JSONArray array, List<T> list) {
                if (Build.VERSION.SDK_INT >23) list.forEach(array::put);
                else {
                    for (T o : list) array.put(o);
                }
            }
            private static void fillPlayerChoiceJson(List<BodyPart.BodyPartNames> list, JSONArray array) throws JSONException {
                for (int i = 0; i < array.length(); i++) {
                    list.add(getBodyPartName(array.get(i).toString()));
                }
            }

            static PlayerChoice getPlayerChoice(JSONObject object) throws JSONException {
                ArrayList<BodyPart.BodyPartNames> attacked = new ArrayList<>(), defended = new ArrayList<>();
                JSONArray atk = object.getJSONArray("attacked");
                JSONArray def = object.getJSONArray("defended");

                fillPlayerChoiceJson(attacked, atk);
                fillPlayerChoiceJson(defended, def);

                return new PlayerChoice(attacked, defended);
            }

            static JSONObject getJson(PlayerChoice choice) throws JSONException {
                JSONObject o = new JSONObject();
                JSONArray atk = new JSONArray();
                JSONArray def = new JSONArray();

                fill(atk, choice.getAttacked());
                fill(def, choice.getDefended());

                return o.put("attacked", atk).put("defended", def);
            }
        }
    }
}
