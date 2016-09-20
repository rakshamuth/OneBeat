package ch.epfl.sweng.onebeat.Parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.onebeat.Exceptions.ParseException;
import ch.epfl.sweng.onebeat.RetrievedData.Room;
import ch.epfl.sweng.onebeat.RetrievedData.Song;
import ch.epfl.sweng.onebeat.RetrievedData.User;

/**
 * Created by M4ttou on 03.12.2015.
 */
public class RoomInfosParser implements Parser {
    @Override
    public Room parse(String JSONStringToParse) throws ParseException {
        try {
            JSONObject json = new JSONObject(JSONStringToParse);
            String name = json.getString("name");
            String creator = json.getString("creator");
            JSONArray JSONsongs = json.getJSONArray("songs");
            JSONArray addedBy = json.getJSONArray("addedBy");
            List<Song> songs = new ArrayList<>();
            List<User> users = new ArrayList<>();
            for (int i = 0; i < JSONsongs.length(); i++) {
                JSONObject aSong = JSONsongs.getJSONObject(i);
                songs.add(new Song(aSong.getString("title"),
                        aSong.getString("artist"),
                        aSong.getDouble("duration"),
                        aSong.getString("spotifyRef"),
                        addedBy.getString(i),
                        aSong.getInt("id")));
                users.add(new User(addedBy.getString(i)));
            }

            String password = json.getString("password");
            int id = json.getInt("id");

            return new Room(name, creator, songs, users, password, id);

        } catch (JSONException e) {
            throw new ParseException(e);
        }
    }
}
