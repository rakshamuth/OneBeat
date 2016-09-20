package ch.epfl.sweng.onebeat.Parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.onebeat.Exceptions.ParseException;
import ch.epfl.sweng.onebeat.RetrievedData.Song;

/**
 * Created by Matthieu on 01.12.2015.
 */
public class SongFromSearchParser implements Parser<List<Song>> {

    @Override
    public List<Song> parse(String JSONStringToParse) throws ParseException {
        try {
            JSONObject json = new JSONObject(JSONStringToParse);
            JSONObject info = json.getJSONObject("info");

            int numResults = info.getInt("num_results");
            int numPages = info.getInt("page");

            final int limit = 7;

            JSONArray jsonTracks = json.optJSONArray("tracks");

            List<Song> tracksFound = new ArrayList<>();

            for (int i = 0; i < Math.min(jsonTracks.length(), limit); i++) {
                JSONObject actualTrack = jsonTracks.getJSONObject(i);

                String artist = actualTrack.getJSONArray("artists").getJSONObject(0).getString("name");
                String songName = actualTrack.getString("name");
                String spotifyRef = actualTrack.getString("href");
                double duration = actualTrack.getDouble("length");

                tracksFound.add(new Song(songName, artist, duration, spotifyRef));

            }
            return tracksFound;
        } catch (JSONException e) {
            throw new ParseException(e);
        }
    }
}
