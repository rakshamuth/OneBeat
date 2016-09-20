package ch.epfl.sweng.onebeat.Parsers;

import org.json.JSONException;
import org.json.JSONObject;

import ch.epfl.sweng.onebeat.Exceptions.ParseException;
import ch.epfl.sweng.onebeat.RetrievedData.SpotifyUser;

/**
 * Created by Matthieu on 01.12.2015.
 */
public class SpotifyUserInfosParser implements Parser {

    @Override
    public SpotifyUser parse(String JSONStringToParse) throws ParseException {
        try {
            JSONObject baseJSON = new JSONObject(JSONStringToParse);
            String name = baseJSON.getString("display_name");
            String spotifyID = baseJSON.getString("id");

            SpotifyUser.getInstance().setInfos(name, spotifyID);
            return SpotifyUser.getInstance();
        }
        catch (JSONException e) {
            throw new ParseException(e);
        }
    }
}
