package ch.epfl.sweng.onebeat.Parsers;

import ch.epfl.sweng.onebeat.Exceptions.ParseException;


/**
 * Created by hugo on 24.10.2015.
 * Can transform JSONObject into a specified class T
 */
public interface Parser<T> {

    T parse(String JSONStringToParse) throws ParseException;
}
