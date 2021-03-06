package com.example.mymusicplayer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.mymusicplayer.Fragment.FullPlayingFragment;
import com.example.mymusicplayer.Model.Song;
import com.example.mymusicplayer.Model.SongRepository;
import com.example.mymusicplayer.R;

import java.util.List;

public class FullPlayingActivity extends AppCompatActivity {
    private static final String EXTERA_SONG_PATH ="com.example.mymusicplayer.Activity.Current_song_filepath";
    private static final String EXTERA_SONG_INDEX ="com.example.mymusicplayer.Activity.Current_song_index";
    private int mCurrentSongIndex;
    private String mSongPath;
private Song mCurrentSong;
private List<Song> mPlayList;

    public static Intent newIntent(Context c, String filePath){
        Intent myInt=new Intent(c, FullPlayingActivity.class);
        myInt.putExtra(EXTERA_SONG_PATH,filePath);
        return myInt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mSongPath =  getIntent().getStringExtra(EXTERA_SONG_PATH);
        mCurrentSongIndex=getIntent().getIntExtra(EXTERA_SONG_INDEX,0);
        mPlayList= SongRepository.getInstance(this).getPlayingList();
        mCurrentSong=SongRepository.getInstance(this).getSongByPathFile(mSongPath);



        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.full_frag_container, FullPlayingFragment.newInstance(mSongPath))
                .commit();

    }
}
