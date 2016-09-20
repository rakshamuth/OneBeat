package ch.epfl.sweng.onebeat.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ch.epfl.sweng.onebeat.R;
import ch.epfl.sweng.onebeat.RetrievedData.Song;
import ch.epfl.sweng.onebeat.RetrievedData.User;


/**
 * Created by Karim on 06-Nov-15.
 *
 * This custom class will specify to ListView objects how to properly display a Song class object
 */
public class SongListAdapter extends ArrayAdapter<Song> {

    private final SpotifyPlayer player;

    public SongListAdapter(Context context, ArrayList<Song> songs, SpotifyPlayer player) {
        super(context, R.layout.song_item_list_view, songs);
        this.player = player;
    }

    /**
     * ViewHolders are used to cache fields of the song_list_item_view. Since it is expensive to
     * search for views by id, we just store the separate view objects in the tag field of each
     * view. Thus saving time == better performance
     */
    private static class ViewHolder {
        TextView title;
        TextView artist;
        TextView duration;
        ImageView player;
        TextView addedBy;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the song for this position
        Song song = getItem(position);
        ViewHolder viewHolder; // View lookup cache stored in tag

        // Check if an existing view is being reused
        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.song_item_list_view, parent, false);

            viewHolder.title = (TextView) convertView.findViewById(R.id.song_title);
            viewHolder.artist = (TextView) convertView.findViewById(R.id.artist);
            viewHolder.duration = (TextView) convertView.findViewById(R.id.duration);
            viewHolder.player = (ImageView) convertView.findViewById(R.id.list_image);
            viewHolder.addedBy = (TextView) convertView.findViewById(R.id.addedByInfo);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(song.getTitle());
        viewHolder.artist.setText(song.getArtist());
        viewHolder.duration.setText(String.valueOf(song.getFormattedDuration()));
        viewHolder.addedBy.setText(song.getAddedBy());

        if (player.getCurrentPosition() == position && player.getPlayerState() == SpotifyPlayer.PlayerState.PLAYING) {
            viewHolder.player.setImageResource(R.drawable.player_pause);
            viewHolder.player.setTag(R.id.PLAYING_STATUS, true);
            viewHolder.player.setTag(R.id.IS_ON_PAUSE, false);
        }
            else if (player.getCurrentPosition() == position && player.getPlayerState() == SpotifyPlayer.PlayerState.ON_PAUSE) {
            viewHolder.player.setImageResource(R.drawable.player_play);
            viewHolder.player.setTag(R.id.PLAYING_STATUS, false);
            viewHolder.player.setTag(R.id.IS_ON_PAUSE, true);
        }
        else {
            viewHolder.player.setImageResource(R.drawable.player_play);
            viewHolder.player.setTag(R.id.PLAYING_STATUS, false);
            viewHolder.player.setTag(R.id.IS_ON_PAUSE, false);
        }

        viewHolder.player.setTag(R.id.BUTTON_POSITION, position);

        return convertView;
        //return super.getView(position, convertView, parent);
    }
}