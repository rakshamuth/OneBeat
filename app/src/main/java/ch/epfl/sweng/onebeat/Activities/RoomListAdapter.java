package ch.epfl.sweng.onebeat.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.onebeat.Exceptions.NotDefinedRoomInfosException;
import ch.epfl.sweng.onebeat.R;
import ch.epfl.sweng.onebeat.RetrievedData.Room;

/**
 * Created by Karim on 04-Dec-15.
 */
public class RoomListAdapter extends ArrayAdapter<Room> {
    public RoomListAdapter(Context context, ArrayList<Room> rooms) {
        super(context, R.layout.room_item_list_view, rooms);
    }

    /**
     * ViewHolders are used to cache fields of the song_list_item_view. Since it is expensive to
     * search for views by id, we just store the separate view objects in the tag field of each
     * view. Thus saving time == better performance
     */
    private static class ViewHolder {
        TextView roomName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Room room = getItem(position);
        ViewHolder viewHolder; // View lookup cache stored in tag

        // Check if an existing view is being reused
        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.room_item_list_view, parent, false);

            viewHolder.roomName = (TextView) convertView.findViewById(R.id.room_title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            viewHolder.roomName.setText(room.getName());
        } catch (NotDefinedRoomInfosException e) {
            viewHolder.roomName.setText(room.getId());
        }

        return convertView;
    }
}
