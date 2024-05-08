package web;

import data.DataCache;
import dataaccess.DataAccess;
import dataaccess.memory.MemoryDataAccess;
import model.*;
import service.ChessServerException;
import service.GameService;
import service.UserService;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ServerFacade {

    private final String url;
    private final DataAccess dataAccess;


    public ServerFacade(String url) {
        this.url = url;
        dataAccess = new MemoryDataAccess();
    }


    public GameData createGame(GameData request) {
        try {
            return new GameService(dataAccess).createGame(request, DataCache.getInstance().getAuthToken());
        } catch (ChessServerException e) {
            throw new ResponseException("Error connecting to server", e);
        }
    }


    public void joinGame(JoinGameRequest request) {
        try {
            new GameService(dataAccess).joinGame(request, DataCache.getInstance().getAuthToken());
        } catch (ChessServerException e) {
            throw new ResponseException("Error connecting to server", e);
        }
    }


    public ListGamesResponse listGames() {
        try {
            return new GameService(dataAccess).listGames(DataCache.getInstance().getAuthToken());
        } catch (ChessServerException e) {
            throw new ResponseException("Error connecting to server", e);
        }
    }


    public AuthData login(UserData request) {
        try {
            AuthData out = new UserService(dataAccess).login(request);
            DataCache.getInstance().setAuthToken(out.authToken());
            return out;
        } catch (ChessServerException e) {
            throw new ResponseException("Error connecting to server", e);
        }
    }


    public void logout() {
        try {
            new UserService(dataAccess).logout(DataCache.getInstance().getAuthToken());
        } catch (ChessServerException e) {
            throw new ResponseException("Error connecting to server", e);
        }
    }


    public AuthData register(UserData request) {
        try {
            AuthData out = new UserService(dataAccess).register(request);
            DataCache.getInstance().setAuthToken(out.authToken());
            return out;
        } catch (ChessServerException e) {
            throw new ResponseException("Error connecting to server", e);
        }
    }
}
