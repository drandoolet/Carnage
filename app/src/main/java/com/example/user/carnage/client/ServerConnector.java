package com.example.user.carnage.client;

import com.example.user.carnage.common.logic.main.PlayerChoice;
import com.example.user.carnage.common.parsing.PlayerChoiceParser;
import com.example.user.carnage.common.parsing.ResponseParser;
import com.example.user.carnage.server.ServerModel;
import com.example.user.carnage.server.roundprocessor.roundelement.GameOverException;
import com.example.user.carnage.server.roundprocessor.roundelement.RoundResults;

import org.json.JSONException;

import java.util.concurrent.BrokenBarrierException;

/**
 * Imitation of HTML layer
 */
final class ServerConnector {
    private final ServerModel.Session session;
    private final RoundResults.Players turn;

    private ServerConnector(Builder builder) {
        session = builder.session;
        turn = builder.turn;
    }

    // TODO write a normal connect()

    static ServerConnector connectSinglePlayer(String playerProfile) {
        int number = MainActivity.server().newSession();

        connectEnemy(number);
        return connect(MainActivity.server().connectPlayer(playerProfile, number));
    }

    private static ServerConnector connect(ServerModel.SessionAdapter adapter) {
        return new Builder()
                .setSession(adapter.getSession())
                .setTurn(adapter.getPlayer())
                .build();
    }

    private static void connectEnemy(int sessionNumber) {
        new Thread(() -> {
            ServerConnector connector =
                    connect(
                            MainActivity
                                    .server()
                                    .connectEnemy(sessionNumber));

            while (true) {
                try {
                    connector.getResults(new PlayerChoice());
                } catch (GameOverException e) {
                    break;
                } catch (InterruptedException | BrokenBarrierException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    RoundResults getResults(PlayerChoice choice) throws JSONException, BrokenBarrierException, InterruptedException, GameOverException {
        return ResponseParser.fromJson(
                session.getResponse(
                        PlayerChoiceParser.toJson(choice)
                                .put("PLAYER", turn.toString()))
        );
    }

    private static class Builder {
        private ServerModel.Session session;
        private RoundResults.Players turn;

        private Builder setSession(ServerModel.Session session) {
            this.session = session;
            return this;
        }

        private Builder setTurn(RoundResults.Players players) {
            turn = players;
            return this;
        }

        ServerConnector build() {
            return new ServerConnector(this);
        }
    }
}
