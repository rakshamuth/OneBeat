package ch.epfl.sweng.onebeat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.onebeat.Exceptions.NotDefinedRoomInfosException;
import ch.epfl.sweng.onebeat.Exceptions.NotDefinedUserInfosException;
import ch.epfl.sweng.onebeat.GeneralConstants;
import ch.epfl.sweng.onebeat.Network.BackendDataProvider;
import ch.epfl.sweng.onebeat.Network.SpotifyDataProvider;
import ch.epfl.sweng.onebeat.R;
import ch.epfl.sweng.onebeat.RetrievedData.Room;
import ch.epfl.sweng.onebeat.RetrievedData.Song;
import ch.epfl.sweng.onebeat.RetrievedData.SpotifyUser;

public class RoomActivity extends AppCompatActivity implements PlayerNotificationCallback {

    private ListView listViewSongs;
    private EditText addNextSong;
    private ImageView prevPlayerButton =  null;

    private ArrayList<Song> currentSongs;
    private ArrayAdapter<Song> adapter;

    private Player mPlayer;

    private SpotifyPlayer player;

    private Room actualRoom;

    private View rButton = null;

    /*
     * Between the RoomActivity and the user selecting a song that they want to add to the queue, I
     * will use tempSongs to keep a reference to the songs found on Spotify.
     */
    private List<Song> tempSongs;

    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler = new Handler();

    final Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            refreshListOfSongs();
            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        listViewSongs = (ListView) findViewById(R.id.currentSongsList);
        addNextSong = (EditText) findViewById(R.id.addSongTextBox);

        currentSongs = new ArrayList<>();

        player = new SpotifyPlayer(this, currentSongs);
        player.init();

        adapter = new SongListAdapter(this, currentSongs, player);
        listViewSongs.setAdapter(adapter);

        registerForContextMenu(listViewSongs);

        // Assign the room name by getting it from the intent which opened this room
        Intent intent = getIntent();
        // fetch room infos from backend
        new BackendDataProvider(this).getRoom(intent.getIntExtra(SelectRoomActivity.ROOM_ID_MESSAGE, 0));

        addNextSong.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    findViewById(R.id.search_song_button).performClick();
                    handled = true;
                }
                return handled;
            }
        });

        mStatusChecker.run();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        if (v.getId() == listViewSongs.getId()) {
            inflater.inflate(R.menu.delete_song_context_menu, menu);
        }

        else if (v.getId() == addNextSong.getId()) {
            for (int i = 0; i < tempSongs.size(); i++) {
                Song s = tempSongs.get(i);
                menu.add(tempSongs.hashCode(), i, i, s.getTitle() + " by " + s.getArtist());
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.delete_song_menu) {
            removeSong(info.position);
            return true;
        }
        /*
        else if:
            check to see if the item has an id. If so, it is a song that should be added from the
            list being displayed as a context menu.
         */
        else if (item.getGroupId() == tempSongs.hashCode()) {
            Log.d("KEINFO", item.getTitle().toString());
            addSong(tempSongs.get(item.getItemId()));
            tempSongs = null;
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    public void searchForSong(View view) throws MalformedURLException {
        String searchInput = addNextSong.getText().toString().trim();

        if (searchInput.length() > 0) {
            Button button = (Button) findViewById(R.id.search_song_button);
            button.setEnabled(false);
            button.setText("Searching...");

            new SpotifyDataProvider(this).getListOfSongs(searchInput);
        }
    }

    public void addSong(Song song) {
        new BackendDataProvider(this).addSong(song, actualRoom.getId());
    }

    public void removeSong(int index) {
        Song songToRemove = currentSongs.remove(index);
        new BackendDataProvider(this).removeSong(songToRemove, actualRoom.getId());
        adapter.notifyDataSetChanged();
    }

    public void playerClick(View v) {

        ImageView currPlayerButton = (ImageView) v.findViewById(R.id.list_image);


        int position = (int) currPlayerButton.getTag(R.id.BUTTON_POSITION);

        // Was there a song playing?
        if (prevPlayerButton != null && (boolean) prevPlayerButton.getTag(R.id.PLAYING_STATUS)) {
            prevPlayerButton.setTag(R.id.PLAYING_STATUS, false);
            prevPlayerButton.setImageResource(R.drawable.player_play);
            player.pause();

            if (prevPlayerButton == currPlayerButton) {
                // Were we the ones who were playing? If so, we already stopped playing
                currPlayerButton.setTag(R.id.IS_ON_PAUSE, true);
                prevPlayerButton = null;
            } else {
                // Another song was playing, now we play
                currPlayerButton.setTag(R.id.PLAYING_STATUS, true);
                currPlayerButton.setImageResource(R.drawable.player_pause);
                prevPlayerButton = currPlayerButton;
                player.play(position);
            }
        } else {
            currPlayerButton.setTag(R.id.PLAYING_STATUS, true);
            currPlayerButton.setImageResource(R.drawable.player_pause);
            prevPlayerButton = currPlayerButton;
            if ((boolean) currPlayerButton.getTag(R.id.IS_ON_PAUSE)) {
                currPlayerButton.setTag(R.id.IS_ON_PAUSE, false);
                player.resume();
            } else {
                player.play(position);
            }
            Log.d("KEINFO", "Song Playing: " + currentSongs.get(position).getSpotifyRef());
        }

    }

    // when we have spotify suggestions after search request
    public void setListOfSongsFromSearch(List<Song> parsedResult) {

        // Enable the user to search again
        Button button = (Button) findViewById(R.id.search_song_button);
        button.setEnabled(true);
        button.setText("Search");

        tempSongs = parsedResult;
        openContextMenu(addNextSong);
    }

    // when room informations are retrieved from backend
    public void setRoomInformations(Room room) {
        actualRoom = room;
        try {
            setTitle(actualRoom.getName());
        } catch (NotDefinedRoomInfosException e) {
            //No Room Title Set
            setTitle("");
        }
        try {
            currentSongs.clear();
            currentSongs.addAll(actualRoom.getSongs());
            //currentSongs = new ArrayList<>(actualRoom.getSongs().keySet());
            adapter.notifyDataSetChanged();
        } catch (NotDefinedRoomInfosException e) {
            //There was no previous list of songs, carry on.
        }

        player.updateQueue();
        registerForContextMenu(addNextSong);
    }

    public void refreshListOfSongs() {
        if (actualRoom != null) {
            new BackendDataProvider(this).getRoom(actualRoom.getId());
        }
    }

    // method from Spotify Player. Probably here we're going to manage playing the next song when one is over.
    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        if (eventType == EventType.TRACK_CHANGED) {
            // TODO update icons play/pause when next song is playing
        }
    }

    // let's show error on a Toast
    @Override
    public void onPlaybackError(ErrorType errorType, String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    // when server is done adding the song
    public void onSongAdded() {
        refreshListOfSongs();
    }
}