package ch.epfl.sweng.onebeat.RetrievedData;

import ch.epfl.sweng.onebeat.Exceptions.NotDefinedUserInfosException;

/**
 * Created by Matthieu on 13.11.2015.
 */
public class SpotifyUser {

    private static final SpotifyUser instance = new SpotifyUser();

    private String pseudo = null;
    private String spotifyID = null;
    private String token = null;

    private SpotifyUser() {
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static SpotifyUser getInstance() { return instance; }

    public String getToken() throws NotDefinedUserInfosException {
        if (this.token == null) {
            throw new NotDefinedUserInfosException("Token not registered yet.");
        }
        return this.token;
    }
    public String getPseudo() throws NotDefinedUserInfosException {
        if (this.pseudo == null) {
            throw new NotDefinedUserInfosException("Pseudo not registered yet.");
        }
        return this.pseudo;
    }
    public String getSpotifyID() throws NotDefinedUserInfosException {
        if (this.spotifyID == null) {
            throw new NotDefinedUserInfosException("Pseudo not registered yet.");
        }
        return this.spotifyID;
    }

    public void setInfos(String pseudo, String spotifyID) {
        if (this.pseudo == null) {
            this.pseudo = pseudo;
        }
        if (this.spotifyID == null) {
            this.spotifyID = spotifyID;
        }
    }

    public void setToken(String token) {
        this.token = token;
    }
}
