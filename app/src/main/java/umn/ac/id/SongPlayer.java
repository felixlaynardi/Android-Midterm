package umn.ac.id;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import java.util.ArrayList;
import java.util.LinkedList;

public class SongPlayer extends AppCompatActivity {
    TextView tvTitle, tvArtist, tvCurrentTime, tvDurationTime;
    SeekBar seekbar, seekBarVolume;
    Button btnPlay, btnForward, btnReverse;
    ArrayList<Song> songsList;
    int position;
    Song song;
    private static final String TAG = "MyActivity";

    MediaPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);

        //add back navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get song from intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
//        song = (Song) bundle.getSerializable("song");
        songsList = (ArrayList<Song>) bundle.getSerializable("songsList");
        position = (int) bundle.getSerializable("position");

        song = songsList.get(position);

        tvArtist = findViewById(R.id.artist);
        tvTitle = findViewById(R.id.title);
        tvCurrentTime = findViewById(R.id.currentTime);
        tvDurationTime = findViewById(R.id.durationTime);
        seekbar = findViewById(R.id.seekbar);
        seekBarVolume = findViewById(R.id.seekbarVolume);
        btnPlay = findViewById(R.id.playBtn);
        btnForward = findViewById(R.id.forward);
        btnReverse = findViewById(R.id.reverse);

        //Create mediaplayer from song uri
        musicPlayer = MediaPlayer.create(this, Uri.parse(song.getSongURI()));
        musicPlayer.setLooping(true);
        musicPlayer.seekTo(0);
        musicPlayer.setVolume(0.5f, 0.5f);

        //Get song duration
        String duration = millisecondsToString(musicPlayer.getDuration());

        //Set text view
        tvDurationTime.setText(duration);
        tvTitle.setText(song.getTitle());
        tvArtist.setText(song.getArtist());

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicPlayer.isPlaying()) {
                    musicPlayer.stop();
                }
                finish();
                if(position == songsList.size() - 1){
                    position = 0;
                }
                else{
                    position++;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("songsList", songsList);
                bundle.putSerializable("position", position);
                Intent intent = new Intent(SongPlayer.this, SongPlayer.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btnReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicPlayer.isPlaying()) {
                    musicPlayer.stop();
                }
                finish();
                if(position == 0){
                    position = songsList.size() - 1;
                }
                else{
                    position--;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("songsList", songsList);
                bundle.putSerializable("position", position);
                Intent intent = new Intent(SongPlayer.this, SongPlayer.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.playBtn) {
                    if(musicPlayer.isPlaying()) {
                        // is playing
                        musicPlayer.pause();
                        btnPlay.setBackgroundResource(R.drawable.play_black);
                    } else {
                        // on pause
                        musicPlayer.start();
                        btnPlay.setBackgroundResource(R.drawable.pause_black);
                    }
                }
            }
        });

        seekBarVolume.setProgress(50);
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                float volume = progress / 100f;
                musicPlayer.setVolume(volume,volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbar.setMax(musicPlayer.getDuration());
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if(isFromUser) {
                    musicPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (musicPlayer != null) {
                    if(musicPlayer.isPlaying()) {
                        try {
                            final double current = musicPlayer.getCurrentPosition();
                            final String elapsedTime = millisecondsToString((int) current);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvCurrentTime.setText(elapsedTime);
                                    seekbar.setProgress((int) current);
                                }
                            });

                            Thread.sleep(1000);
                        }catch (InterruptedException e) {}
                    }
                }
            }
        }).start();

    } // end main

    //get music time
    public String millisecondsToString(int time) {
        String elapsedTime = "";
        int minutes = time / 1000 / 60;
        int seconds = time / 1000 % 60;
        elapsedTime = minutes+":";
        if(seconds < 10) {
            elapsedTime += "0";
        }
        elapsedTime += seconds;

        return  elapsedTime;
    }

//    @Override
//    public void onClick(View view) {
//        if(view.getId() == R.id.playBtn) {
//            if(musicPlayer.isPlaying()) {
//                // is playing
//                musicPlayer.pause();
//                btnPlay.setBackgroundResource(R.drawable.play_black);
//            } else {
//                // on pause
//                musicPlayer.start();
//                btnPlay.setBackgroundResource(R.drawable.pause_black);
//            }
//        }
//    }

    //end song on exit
    @Override
    public void onBackPressed() {
        if (musicPlayer.isPlaying()) {
            musicPlayer.stop();
        }
        finish();
        super.onBackPressed();
    }

    //Back navigation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
