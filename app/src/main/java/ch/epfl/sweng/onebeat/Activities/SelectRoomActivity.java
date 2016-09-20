package ch.epfl.sweng.onebeat.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.onebeat.Exceptions.NotDefinedUserInfosException;
import ch.epfl.sweng.onebeat.Network.BackendDataProvider;
import ch.epfl.sweng.onebeat.R;
import ch.epfl.sweng.onebeat.RetrievedData.Room;

public class SelectRoomActivity extends AppCompatActivity {
    public static final String ROOM_ID_MESSAGE = "ch.epfl.sweng.onebeat.ROOM_ID_MESSAGE";

    private ArrayList<Room> roomsArray;
    private ArrayAdapter<Room> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_room);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        ListView listViewRooms = (ListView) findViewById(R.id.roomListView);

        Log.d("KEINFO", "About to launch req to get list of rooms");
        roomsArray = new ArrayList<Room>();
        try {
            new BackendDataProvider(this).getListOfRooms();
        } catch (NotDefinedUserInfosException e) {
            //TODO: What to do if we cant pull rooms?
            Log.d("KEINFO", "Could not get list of rooms");
        }
        Log.d("KEINFO", "Launched req to get list of rooms");

        adapter = new RoomListAdapter(this, roomsArray);
        listViewRooms.setAdapter(adapter);
        Log.d("KEINFO", "Created room list adapter");

        listViewRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Room room = adapter.getItem(position);

                Intent intent = new Intent(SelectRoomActivity.this, RoomActivity.class);

                intent.putExtra(ROOM_ID_MESSAGE, room.getId());
                startActivity(intent);
            }
        });

        // This next bit of code allows the "live" filtering feature to work
        EditText roomSearch = (EditText) findViewById(R.id.searchRoomTextView);
/*        roomSearch.setText(R.string.roomSearchWaitingText);
        roomSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SelectRoomActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        roomSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    new BackendDataProvider(SelectRoomActivity.this).joinRoom(v.getText().toString().trim(), "");
                    handled = true;
                }
                return handled;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:
                RoomCreatorDialogFragment dialog = new RoomCreatorDialogFragment();
                dialog.show(getSupportFragmentManager(), "Room Creator");
            }
        });
    }

    // when we get the list of rooms a user is in.
    public void setListOfRooms(List<Room> roomsList) {
        if (roomsList.size() > 0) {
            roomsArray.addAll(roomsList);
        }
        EditText roomSearch = (EditText) findViewById(R.id.searchRoomTextView);
        roomSearch.setText("");
        adapter.notifyDataSetChanged();
    }

    // when a room has been created
    public void onNewRoomMessage(int roomID) {
        Intent intent = new Intent(this, RoomActivity.class);
        intent.putExtra(ROOM_ID_MESSAGE, roomID);
        Log.d("KEINFO", "About to launch room activity");
        startActivity(intent);
    }

    public void onJoinRoomSuccess(int roomId) {
        onNewRoomMessage(roomId);
    }

    public void askPassword(String roomName) {
        Log.d("#Matt", "Before opening fill password Fragment");
        FillPasswordFragment dialog = new FillPasswordFragment(roomName);
        dialog.show(getSupportFragmentManager(), "Password Filler");

    }

    private class RoomCreatorDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectRoomActivity.this);
            // Get the layout inflater
            final LayoutInflater inflater = SelectRoomActivity.this.getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            @SuppressLint("InflateParams") final View v = inflater.inflate(R.layout.dialog_create_room, null);
            builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.partyOn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText roomNameField = (EditText) v.findViewById(R.id.roomName);
                        EditText roomPasswordField = (EditText) v.findViewById(R.id.roomPassword);

                        String roomName = roomNameField.getText().toString().trim();
                        String roomPassword = roomPasswordField.getText().toString().trim();

                        new BackendDataProvider(SelectRoomActivity.this).createRoom(roomName, roomPassword);
                    }
                })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            RoomCreatorDialogFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }

    private class FillPasswordFragment extends DialogFragment {
        private final String roomName;

        public FillPasswordFragment(String roomName) {
            this.roomName = roomName;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectRoomActivity.this);
            // Get the layout inflater
            final LayoutInflater inflater = SelectRoomActivity.this.getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            @SuppressLint("InflateParams") final View v = inflater.inflate(R.layout.dialog_fill_password, null);
            builder.setView(v)
                    // Add action buttons
                    .setPositiveButton(R.string.joinRoom, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            EditText roomPasswordField = (EditText) v.findViewById(R.id.roomPasswordJoin);

                            String roomPassword = roomPasswordField.getText().toString();

                            new BackendDataProvider(SelectRoomActivity.this).joinRoom(roomName, roomPassword);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FillPasswordFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }
}