package ch.epfl.sweng.onebeat.Activities;

import android.content.Context;
import android.util.Log;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.Spotify;

import java.util.List;

import ch.epfl.sweng.onebeat.Exceptions.NotDefinedUserInfosException;
import ch.epfl.sweng.onebeat.GeneralConstants;
import ch.epfl.sweng.onebeat.RetrievedData.Room;
import ch.epfl.sweng.onebeat.RetrievedData.Song;
import ch.epfl.sweng.onebeat.RetrievedData.SpotifyUser;

/**
 * Created by M4ttou on 05.12.2015.
 */
public class SpotifyPlayer {

    private Context callingActivity;

    private Config playerConfig = null;

    private List<Song> currentSongs;

    private int currentPosition = 0;

    public enum PlayerState { IDLE, ON_PAUSE, PLAYING };

    private PlayerState playerState = PlayerState.IDLE;

    private Player mPlayer;

    public SpotifyPlayer(Context callingActivity, List<Song> currentSongs) {
        this.currentSongs = currentSongs;
        this.callingActivity = callingActivity;
        try {
            playerConfig = new Config(callingActivity, SpotifyUser.getInstance().getToken(), GeneralConstants.CLIENT_ID);
        } catch (NotDefinedUserInfosException e) {
            e.printStackTrace();
        }
    }

    public SpotifyPlayer(List<Song> currentSongs) {
        this.currentSongs = currentSongs;
        try {
            playerConfig = new Config(callingActivity, SpotifyUser.getInstance().getToken(), GeneralConstants.CLIENT_ID);
        } catch (NotDefinedUserInfosException e) {
            e.printStackTrace();
        }

    }

    public void init() {
        Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
            @Override
            public void onInitialized(Player player) {
                mPlayer = player;
                mPlayer.addPlayerNotificationCallback((PlayerNotificationCallback) callingActivity);
            }
            @Override
            public void onError(Throwable throwable) {
                Log.e("RoomActivity", "Could not initialize player: " + throwable.getMessage());
            }
        });
    }

    public void pause() {
        mPlayer.pause();
        playerState = PlayerState.ON_PAUSE;
    }

    public void play(int position) {
        this.currentPosition = position;
        playerState = PlayerState.PLAYING;
        mPlayer.play(currentSongs.get(position).getSpotifyRef());
        updateQueue();
    }
    public void resume() {
        mPlayer.resume();
        playerState = PlayerState.PLAYING;
    }

    public PlayerState getPlayerState() { return playerState; }

    public int getCurrentPosition() { return currentPosition; }

    public void updateQueue() {
        if (playerState != PlayerState.IDLE) {
            mPlayer.clearQueue();

            if (currentPosition == currentSongs.size() - 1) {
                mPlayer.queue(currentSongs.get(0).getSpotifyRef());
            } else {
                mPlayer.queue(currentSongs.get(currentPosition + 1).getSpotifyRef());
            }
        }
    }
}
