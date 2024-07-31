package server;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.mysql.MySqlDataAccess;
import handler.*;
import service.BadRequestException;
import service.ChessServerException;
import service.RequestItemTakenException;
import service.UnauthorizedException;
import spark.Spark;
import websocket.WebSocketHandler;

import java.net.HttpURLConnection;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        DataAccess dataAccess;
        try {
            dataAccess = new MySqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        WebSocketHandler ws = WebSocketHandler.getInstance();
        ws.setDataAccess(dataAccess);

        Spark.webSocket("/ws", ws);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", new RegisterHandler(dataAccess));

        Spark.path("/session", () -> {
            Spark.post("", new LoginHandler(dataAccess));
            Spark.delete("", new LogoutHandler(dataAccess));
        });

        Spark.path("/game", () -> {
            Spark.get("", new ListGamesHandler(dataAccess));
            Spark.post("", new CreateGameHandler(dataAccess));
            Spark.put("", new JoinGameHandler(dataAccess));
        });

        Spark.delete("/db", new ClearHandler(dataAccess));

        Spark.exception(BadRequestException.class, new ChessServerExceptionHandler<>(HttpURLConnection.HTTP_BAD_REQUEST));
        Spark.exception(UnauthorizedException.class, new ChessServerExceptionHandler<>(HttpURLConnection.HTTP_UNAUTHORIZED));
        Spark.exception(RequestItemTakenException.class, new ChessServerExceptionHandler<>(HttpURLConnection.HTTP_FORBIDDEN));
        Spark.exception(ChessServerException.class, new ChessServerExceptionHandler<>(HttpURLConnection.HTTP_INTERNAL_ERROR));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

//    apublic void stop() {
//        bSpark.stop();
//        cSpark.awaitStop();
//    }

//    dpublic void stop() {
//        eSpark.stop();
//        fSpark.awaitStop();
//    }

//    gpublic void stop() {
//        hSpark.stop();
//        iSpark.awaitStop();
//    }

//    jpublic void stop() {
//        kSpark.stop();
//        lSpark.awaitStop();
//    }

//    mpublic void stop() {
//        nSpark.stop();
//        oSpark.awaitStop();
//    }

//    ppublic void stop() {
//        qSpark.stop();
//        rSpark.awaitStop();
//    }

//    spublic void stop() {
//        tSpark.stop();
//        uSpark.awaitStop();
//    }

//    vpublic void stop() {
//        wSpark.stop();
//        xSpark.awaitStop();
//    }


//    ypublic void stop() {
//        zSpark.stop();
//        `Spark.awaitStop();
//    }


//    1public void stop() {
//        2Spark.stop();
//        3Spark.awaitStop();
//    }

//    4public void stop() {
//        5Spark.stop();
//        6Spark.awaitStop();
//    }

//    7public void stop() {
//        8Spark.stop();
//        9Spark.awaitStop();
//    }

}
