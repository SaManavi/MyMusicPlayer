package com.example.mymusicplayer.Fragment;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

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

    private Timer timer;
    private String file;
    private Song mCurrentSong;

    public static PlayingFragment newInstance() {

        Bundle args = new Bundle();

        PlayingFragment fragment = new PlayingFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
//        mCurrentSong= SongRepository.getInstance(getActivity()).
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_playing, container, false);
        DefiningComponents(v);
        setUpMusicPlayer();

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

    }


    void setUpMusicPlayer() {
        mediaPlayer = new MediaPlayer();

        try {
//            mediaPlayer.setDataSource(getActivity(), Uri.parse(mCurrentSong.getFilePath()));
            mediaPlayer.setDataSource(getActivity(), Uri.parse(file));
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

    void playMusic()
    {
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

    }

    void backAndForward()
    {
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

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        timer=new Timer();
        timer.schedule(new timertask(),0,1000);

    }

    private String defineTime(long progress)
    {
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
                    seekbar.setProgress(mediaPlayer.getCurrentPosition());
                    mStartTime.setText(defineTime(mediaPlayer.getCurrentPosition()));
                }
            });
        }
    }





}