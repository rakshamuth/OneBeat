package ch.epfl.sweng.onebeat.Parsers;

import org.json.JSONException;
import org.json.JSONObject;

import ch.epfl.sweng.onebeat.Exceptions.ParseException;

/**
 * Created by M4ttou on 04.12.2015.
 */
public class StringToJsonParser implements Parser {
    @Override
    public JSONObject parse(String JSONStringToParse) throws ParseException {
        try {
            return new JSONObject(JSONStringToParse);
        } catch (JSONException e) {
            throw new ParseException(e);
        }
    }
}
