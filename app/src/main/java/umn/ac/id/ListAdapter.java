package umn.ac.id;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder>{
    private final ArrayList<Song> songsList;
    private LayoutInflater inflater;
    private Context context;
    private static final String TAG = "MyActivity";
    private int hop = 0;

    public ListAdapter(Context currContext, ArrayList<Song> currSongsList){
        this.context = currContext;
        this.inflater = LayoutInflater.from(context);
        this.songsList = currSongsList;
    }

    //Create Viewholder
    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.songs_list, parent, false);
        if(hop % 2 == 0){
            itemView.setBackgroundColor(itemView.getResources().getColor(R.color.dark_gray));
        }
        else{
            itemView.setBackgroundColor(itemView.getResources().getColor(R.color.light_gray));
        }
        hop += 1;
        return new ListHolder(itemView, this);
    }

    //Bind viewholder value with listholder
    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {
        Song song = songsList.get(position);
        holder.tvTitle.setText(song.getTitle());
        holder.tvArtist.setText(song.getArtist());
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    //List holder to connect to recycler view
    public class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvTitle;
        private TextView tvArtist;
        private TextView tvUri;
        private ListAdapter adapter;
        private Song song;
        private int position;

        public ListHolder(@NonNull View itemView, ListAdapter currAdapter) {
            super(itemView);
            adapter = currAdapter;
            tvTitle = (TextView) itemView.findViewById(R.id.title);
            tvArtist = (TextView) itemView.findViewById(R.id.artist);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            position = getLayoutPosition();
//            song = songsList.get(position);
//            String uri = song.getSongURI().toString();
            Bundle bundle = new Bundle();
//            bundle.putSerializable("song", song);
            bundle.putSerializable("songsList", songsList);
            bundle.putSerializable("position", position);
            Intent intent = new Intent(context, SongPlayer.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }
}
