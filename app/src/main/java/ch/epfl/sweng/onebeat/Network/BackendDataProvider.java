package ch.epfl.sweng.onebeat.Network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import ch.epfl.sweng.onebeat.Exceptions.NotDefinedUserInfosException;
import ch.epfl.sweng.onebeat.Parsers.CreateRoomParser;
import ch.epfl.sweng.onebeat.Parsers.JoinRoomParser;
import ch.epfl.sweng.onebeat.Parsers.ListOfRoomsParser;
import ch.epfl.sweng.onebeat.Parsers.RoomInfosParser;
import ch.epfl.sweng.onebeat.Parsers.StringToJsonParser;
import ch.epfl.sweng.onebeat.RetrievedData.Song;
import ch.epfl.sweng.onebeat.RetrievedData.SpotifyUser;

/**
 * Created by Matt
 */

public class BackendDataProvider extends DataProvider {

    public BackendDataProvider(Context callingActivity) {
        super(callingActivity);
    }

    public void getListOfRooms() throws NotDefinedUserInfosException {
        super.setParser(new ListOfRoomsParser());
        super.setRequestType(RequestTypes.GET_LIST_OF_ROOMS);
        new DownloadWebpageTask(this).start(serverURL + "getUser?id=" + SpotifyUser.getInstance().getSpotifyID());
    }

    public void getRoom(int RoomId) {
        super.setParser(new RoomInfosParser());
        super.setRequestType(RequestTypes.GET_ROOM_INFOS);
        new DownloadWebpageTask(this).start(serverURL + "getRoom2?id=" + RoomId);
    }

    public void addUser() throws NotDefinedUserInfosException, JSONException {
        super.setParser(new StringToJsonParser());
        super.setRequestType(RequestTypes.ADD_USER);
        JSONObject jsonToSend = new JSONObject();
        jsonToSend.put("id", SpotifyUser.getInstance().getSpotifyID());
        jsonToSend.put("name", SpotifyUser.getInstance().getPseudo());

        new SendDataTask(this).execute(serverURL + "addUser/", jsonToSend.toString());
    }

    public void createRoom(String roomName, String roomPassword) {
        JSONObject jsonToSend = new JSONObject();
        try {
            jsonToSend.put("creator", SpotifyUser.getInstance().getSpotifyID());
            jsonToSend.put("name", roomName);
            jsonToSend.put("password", roomPassword);
        } catch (JSONException | NotDefinedUserInfosException e) {
            e.printStackTrace();
        }

        super.setParser(new CreateRoomParser());
        super.setRequestType(RequestTypes.CREATE_ROOM);
        new SendDataTask(this).execute(serverURL + "createRoom/", jsonToSend.toString());
    }

    public void addSong(Song aSong, int roomID) {
        super.setParser(new StringToJsonParser());
        super.setRequestType(RequestTypes.ADD_SONG);
        new SendDataTask(this).execute(serverURL + "addSong/", aSong.toSendFormat(roomID));
    }

    public void removeSong(Song song, int roomID) {
        //TODO: Implement delete on server side
        super.setParser(new StringToJsonParser());
        //super.setRequestType(RequestTypes.REMOVE_SONG);
        //new SendDataTask(this).execute(serverURL + "removeSong/", song.toSendFormat(roomID));
    }

    public void joinRoom(String roomName, String password) {
        JSONObject jsonToSend = new JSONObject();
        try {
            jsonToSend.put("user", SpotifyUser.getInstance().getSpotifyID());
            jsonToSend.put("name", roomName);
            jsonToSend.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NotDefinedUserInfosException e) {
            e.printStackTrace();
        }
        super.setParser(new JoinRoomParser(roomName));
        super.setRequestType(RequestTypes.JOIN_ROOM);
        new SendDataTask(this).execute(serverURL + "joinRoom/", jsonToSend.toString());
    }
}



