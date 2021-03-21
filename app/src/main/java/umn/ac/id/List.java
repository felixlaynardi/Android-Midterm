package umn.ac.id;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.LinkedList;

public class List extends AppCompatActivity {
    private static final int PERMISSION_REQUEST = 1;
//    public ArrayList<String> arrayList;
//    private RecyclerView recyclerView;
//    private ArrayAdapter<String> adapter;
    private RecyclerView rvSongsList;
    private ListAdapter adapter;
    private ArrayList<Song> songsList = new ArrayList<>();
    private static final String TAG = "MyActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        //add welcome dialog
        DialogFragment dialog = new WelcomeFragment();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");

        //remove back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //check permissions
        if(ContextCompat.checkSelfPermission(List.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(List.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(List.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
            else{
                ActivityCompat.requestPermissions(List.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
        }
        else{
            //Add songs to linkedlist
            getMusic();
            //Add songs into recycler view through adapter
            rvSongsList = (RecyclerView) findViewById(R.id.recyclerView);
            adapter = new ListAdapter(this, songsList);
            rvSongsList.setAdapter(adapter);
            rvSongsList.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    public void getMusic(){
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if(songCursor != null && songCursor.moveToFirst()){
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do{
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentLocation = songCursor.getString(songLocation);
                songsList.add(new Song(currentTitle, currentArtist, currentLocation));
            } while(songCursor.moveToNext());
        }
    }

    //Check permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(List.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(List.this, List.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(this, "No permission granted!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    return;
                }
            }
        }
    }

    //Hamburger button
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    //Menu Action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //Redirect to profile
            case R.id.profileBtn:
                Intent intent = new Intent(List.this, Profile.class);
                int code = 1;
                intent.putExtra("code", code);
                startActivity(intent);
                return true;
                //Logout or redirect to login
            case R.id.logoutBtn:
                Intent intent2 = new Intent(List.this, Login.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Prevents back button
    @Override
    public void onBackPressed() {

    }

}
