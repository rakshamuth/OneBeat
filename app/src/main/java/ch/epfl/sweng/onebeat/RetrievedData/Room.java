package ch.epfl.sweng.onebeat.RetrievedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.onebeat.Exceptions.NotDefinedRoomInfosException;

/**
 * Created by Matthieu on 28.11.2015.
 */
public class Room {

    private List<User> users;
    private String name;
    private String creator;
    private List<Song> songs;
    private String password;
    private int id;

    public Room(String name, String creator, List<Song> songs, String password, int id) {
        this.name = name;
        this.creator = creator;
        this.songs = new ArrayList<>(songs);
        this.password = password;
        this.id = id;
    }

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Room(String name, String creator, List<Song> songs, List<User> users, String password, int id) {
        this.name = name;
        this.creator = creator;
        this.songs = new ArrayList<>(songs);
        this.password = password;
        this.id = id;
        this.users = new ArrayList<>(users);
    }

    public String getName() throws NotDefinedRoomInfosException {
        if (name == null) {
            throw new NotDefinedRoomInfosException("Name not defined");
        }
        return name;
    }

    public int getId() {
        return id; }

    public String getCreator() throws NotDefinedRoomInfosException {
        if (creator == null) {
            throw new NotDefinedRoomInfosException("Creator not defined");
        }
        return creator;
    }

    public List<Song> getSongs() throws NotDefinedRoomInfosException {
        if (songs == null) {
            throw new NotDefinedRoomInfosException("List of songs not defined");
        }
        return songs;
    }

    public List<User> getUsers() throws NotDefinedRoomInfosException {
        if (users == null) {
            throw new NotDefinedRoomInfosException("List of users not defined");
        }
        return users;
    }

    public String getPassword() {
        return password;
    }
}