package ch.epfl.sweng.onebeat.Parsers;

import org.json.JSONException;
import org.json.JSONObject;

import ch.epfl.sweng.onebeat.Exceptions.ParseException;

/**
 * Created by M4ttou on 06.12.2015.
 */
public class JoinRoomParser implements Parser {
    private String roomName = "";

    public JoinRoomParser(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public JSONObject parse(String JSONStringToParse) throws ParseException {
        try {
            return new StringToJsonParser().parse(JSONStringToParse).put("roomName", roomName);
        } catch (JSONException e) {
            throw new ParseException(e);
        }
    }
}
