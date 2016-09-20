package ch.epfl.sweng.onebeat.RetrievedData;

/**
 * Created by M4ttou on 03.12.2015.
 */
public class User {

    private String pseudo = null;
    private String spotifyRef;

    public User(String pseudo, String spotifyRef) {
        this.pseudo = pseudo;
        this.spotifyRef = spotifyRef;
    }

    public User(String spotifyRef) {
        this.spotifyRef = spotifyRef;
    }

}
