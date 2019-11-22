package com.example.user.carnage.server;

import com.example.user.carnage.common.logic.main.PlayCharacter;
import com.example.user.carnage.common.logic.main.PlayCharacterFactory;
import com.example.user.carnage.common.logic.main.PlayerChoice;
import com.example.user.carnage.common.parsing.PlayerChoiceParser;
import com.example.user.carnage.common.parsing.ResponseParser;
import com.example.user.carnage.server.roundprocessor.BattleRoundProcessor;
import com.example.user.carnage.server.roundprocessor.Query;
import com.example.user.carnage.server.roundprocessor.roundelement.GameOverException;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerModel {
    private final ExecutorService executor;
    private final Map<Integer, Session> sessions;
    private final Map<Integer, Session.Builder> waitingForPlayersSessions;
    private final Random random = new SecureRandom();

    private final int MAX_ROOM_NUMBER = 999;

    public ServerModel(int threads) {
        this.executor = Executors.newFixedThreadPool(threads);
        sessions = Collections.synchronizedMap(new HashMap<>());
        waitingForPlayersSessions = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Connects PlayCharacter to Session.Builder and waits for it to build a Session,
     * then returns it.
     *
     * @param playerProfile : String value stored in SharedPrefs (TODO impl DB)
     * @param sessionNumber : room number
     * @return Session
     */
    public SessionAdapter connectToExisting(String playerProfile, int sessionNumber) {
        if (!waitingForPlayersSessions.containsKey(sessionNumber))
            throw new IllegalArgumentException("No opened Session with number " + sessionNumber);

        try {
            return new SessionAdapter(
                    waitingForPlayersSessions
                            .get(sessionNumber)
                            .connect(PlayCharacterFactory.findInDB(playerProfile))
                            .build(),
                    RoundResults.Players.PLAYER_2,
                    sessionNumber);
        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Error in connect()");
    }

    public SessionAdapter connectPlayer(String playerProfile, int sessionNumber) {
        if (!waitingForPlayersSessions.containsKey(sessionNumber))
            throw new IllegalArgumentException("No opened Session with number " + sessionNumber);

        try {
            return new SessionAdapter(
                    waitingForPlayersSessions
                            .get(sessionNumber)
                            .connect(PlayCharacterFactory.findInDB(playerProfile))
                            .build(),
                    RoundResults.Players.PLAYER_1,
                    sessionNumber);
        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Error in connect()");
    }

    public SessionAdapter connectEnemy(int sessionNumber) {
        if (!waitingForPlayersSessions.containsKey(sessionNumber))
            throw new IllegalArgumentException();

        try {
            return SessionAdapter.newInstance(
                    waitingForPlayersSessions
                            .get(sessionNumber)
                            .connectAsEnemy()
                            .build(),
                    RoundResults.Players.PLAYER_2,
                    sessionNumber
            );
        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException();
    }



    public static class SessionAdapter {
        private final Session session;
        private final RoundResults.Players player;
        private final int sessionNumber;

        public Session getSession() {
            return session;
        }

        public RoundResults.Players getPlayer() {
            return player;
        }

        public int getSessionNumber() {
            return sessionNumber;
        }

        private SessionAdapter(Session session, RoundResults.Players player, int sessionNumber) {
            this.session = session;
            this.player = player;
            this.sessionNumber = sessionNumber;
        }

        public static SessionAdapter newInstance(Session session, RoundResults.Players player, int sessionNumber) {
            return new SessionAdapter(session, player, sessionNumber);
        }
    }

    /**
     * Creates a new Session.Builder and waits for another PlayCharacter to enter the Session
     * using ServerModel.connect(int)
     *
     * @return int = session number, yet non-existing until second player enters
     */
    public int newSession() {
        int sessionNumber = random.nextInt(MAX_ROOM_NUMBER + 1);
        waitingForPlayersSessions.put(sessionNumber, new Session.Builder(sessionNumber));
        return sessionNumber;
    }

    /**
     * Everything connected with PlayCharacters' insides lies here, gets changed here.
     * Clients get updates from Session.
     * Clients connect to Session via ServerModel.connect(int).
     * PlayCharacters return updates after interacting with each other.
     */
    public static class Session {
        private final int sessionNumber;
        private final BattleRoundProcessor processor;
        private RoundResults results;
        private PlayerChoice playerChoice_1, playerChoice_2;
        private RoundResults.Players firstPlayer;
        private final CyclicBarrier barrier;
        private final AtomicBoolean isOpen = new AtomicBoolean(true);

        private Session(Builder builder) {
            this.sessionNumber = builder.sessionNumber;
            firstPlayer = RoundResults.Players.PLAYER_2;

            processor = new BattleRoundProcessor(builder.player_1, builder.player_2);

            barrier = new CyclicBarrier(2,
                    () -> {
                        try {
                            results = processor.getResponse(getNextQuery());
                        } catch (GameOverException e) {
                            isOpen.set(false);
                        }
                    }
            );
        }

        public int getSessionNumber() {
            return sessionNumber;
        }

        private Query getNextQuery() {
            if (firstPlayer == RoundResults.Players.PLAYER_1) {
                firstPlayer = RoundResults.Players.PLAYER_2;
            }
            else firstPlayer = RoundResults.Players.PLAYER_1;

            return new Query(playerChoice_1, playerChoice_2, firstPlayer);
        }

        boolean isOpen() {
            return isOpen.get();
        }

        public JSONObject getResponse(JSONObject playerChoice) throws BrokenBarrierException, InterruptedException, JSONException, GameOverException {
            RoundResults.Players player = RoundResults.Players.valueOf(playerChoice.getString("PLAYER"));

            if (player == RoundResults.Players.PLAYER_1)
                playerChoice_1 = PlayerChoiceParser.fromJson(playerChoice);
            else if (player == RoundResults.Players.PLAYER_2)
                playerChoice_2 = PlayerChoiceParser.fromJson(playerChoice);
            else throw new IllegalArgumentException();

            barrier.await();
            if (!isOpen())
                throw new GameOverException("Game over: "+Thread.currentThread().getName());
            try {
                return ResponseParser.toJson(results);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            throw new IllegalStateException("Error in getResponse()");
        }

        static class Builder {
            private PlayCharacter player_1, player_2;
            private final int sessionNumber;
            private final CyclicBarrier barrier = new CyclicBarrier(2,
                    () -> session = new Session(this));
            private Session session;

            Builder(int sessionNumber) {
                this.sessionNumber = sessionNumber;
            }

            Builder connect(PlayCharacter character) {
                if (player_1 == null) player_1 = character;
                else player_2 = character;
                return this;
            }

            /**
             * Attempts to connect as Player_2 in cycle (blocking)
             * @return this
             */
            Builder connectAsEnemy() {
                while (player_1 == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                player_2 = PlayCharacterFactory.createEnemy(player_1);
                return this;
            }

            Session build() throws BrokenBarrierException, InterruptedException {
                barrier.await();
                return session;
            }
        }
    }
}
