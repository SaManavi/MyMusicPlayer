package com.example.mymusicplayer.Fragment;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusicplayer.Activity.FullPlayingActivity;
import com.example.mymusicplayer.Model.Song;
import com.example.mymusicplayer.Model.SongRepository;
import com.example.mymusicplayer.R;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;




/**
 * A simple {@link Fragment} subclass.
 */
public class PlayingFragment extends Fragment {

    private static String ARG_SONG_filePath ="com.example.mymusicplayer.file_path_Of_music";
    private SeekBar seekbar;
    private TextView mEndTime, mStartTime;
    private ImageButton Btn_forward, Btn_play, Btn_backward, Btn_next, Btn_previous;
    private MediaPlayer mediaPlayer;
    private ImageView mPlayingIcon;
    private Timer timer;
    private String file;
    private Song mCurrentSong;
    private int mCurrentSongIndex;



    public PlayingFragment() {
        // Required empty public constructor
    }

    public static PlayingFragment newInstance(String filePath) {

        Bundle args = new Bundle();
        args.putString(ARG_SONG_filePath,filePath);

        PlayingFragment fragment = new PlayingFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        file=getArguments().getString(ARG_SONG_filePath);
        mCurrentSong= SongRepository.getInstance(getActivity()).getSongByPathFile(file);
//        mCurrentSong=SongRepository.getInstance(getActivity()).getPlayingList().get(mCurrentSongIndex);
    }

    @Override
    public void onPause() {
        mediaPlayer.stop();
        super.onPause();
        timer.cancel();
        mediaPlayer.release();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_playing, container, false);
        DefiningComponents(v);
        setUpMusicPlayer();

        mPlayingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myInt= FullPlayingActivity.newIntent(getActivity(),file);
                startActivity(myInt);
// FragmentManager fm=getFragmentManager();
//                fm.beginTransaction()
//                        .replace(R.id.full_container, FullPlayingFragment.newInstance(mCurrentSong.getFilePath(),mCurrentSongIndex))
//                        .commit();
            }
        });

        return v;
    }

    void DefiningComponents(View v) {

        seekbar = v.findViewById(R.id.seekbar);
        mEndTime = v.findViewById(R.id.Tv_endtime);
        mStartTime = v.findViewById(R.id.Tv_start);
        Btn_forward = v.findViewById(R.id.Btn_forward);
        Btn_play = v.findViewById(R.id.Btn_play);
        Btn_backward = v.findViewById(R.id.Btn_backward);
        Btn_next = v.findViewById(R.id.Btn_next);
        Btn_previous = v.findViewById(R.id.Btn_previous);
        mPlayingIcon =v.findViewById(R.id.imageView_playing);
        mPlayingIcon.setImageBitmap(SongRepository.getInstance(getActivity()).getSongImage(mCurrentSong.getFilePath()));

    }

    void setUpMusicPlayer() {
        mediaPlayer = new MediaPlayer();

        try {

            mediaPlayer.setDataSource(getActivity(), Uri.parse(mCurrentSong.getFilePath()));
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    playMusic();
                    backAndForward();
                    setupSeekBar();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    void playMusic(){
        Btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mediaPlayer!=null && mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
//                    Btn_play.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.play_music));
                    Btn_play.setImageResource(R.drawable.play_music);
                }
                else
                {
                    mediaPlayer.start();
//                    Btn_play.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_pause));
                    Btn_play.setImageResource(R.drawable.ic_pause);

                }

            }
        });
        seekbar.setProgress(0);
        mediaPlayer.seekTo(0);
        mStartTime.setText(defineTime(0));
        mEndTime.setText(defineTime(mediaPlayer.getDuration()));

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getActivity(), "song finished...", Toast.LENGTH_SHORT).show();
                mCurrentSongIndex += 1;
//                mCurrentSong=
            }
        });

    }

    void backAndForward(){
        Btn_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
            }
        });

        Btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+ 5000);
            }
        });
    }

    void setupSeekBar(){

        seekbar.setMax(mediaPlayer.getDuration());
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                    mStartTime.setText(defineTime(mediaPlayer.getCurrentPosition()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mStartTime.setText(defineTime(mediaPlayer.getCurrentPosition()));
                mediaPlayer.start();

            }
        });
        timer=new Timer();
        timer.schedule(new timertask(),0,1000);

    }

    private String defineTime(long progress){
        int sec=(int) progress / 1000;
        int min=sec / 60;
        sec %= 60;
        return String.format(Locale.ENGLISH,"%02d",min) + ":" + String.format(Locale.ENGLISH,"%02d",sec);
    }

    public class  timertask extends TimerTask{
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer.isPlaying()) {
                        seekbar.setProgress(mediaPlayer.getCurrentPosition());
                        mStartTime.setText(defineTime(mediaPlayer.getCurrentPosition()));
                    }
                }

            });

        }
    }





}