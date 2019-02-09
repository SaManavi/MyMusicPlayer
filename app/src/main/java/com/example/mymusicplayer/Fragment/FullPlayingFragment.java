package com.example.mymusicplayer.Fragment;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mymusicplayer.Model.Song;
import com.example.mymusicplayer.Model.SongRepository;
import com.example.mymusicplayer.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class FullPlayingFragment extends Fragment {

    private static String ARG_SONG_filePath ="com.example.mymusicplayer.file_path_Of_music";
    private static String ARG_SONG_POSITION="com.example.mymusicplayer.Song Position";
    private SeekBar seekbar;
    private TextView mEndTime, mStartTime;
    private ImageButton Btn_forward, Btn_play, Btn_backward, Btn_next, Btn_previous;
    private MediaPlayer mediaPlayer;

    private Timer timer;
    private String mSongPath;
    private Song mCurrentSong;
    private Integer mSongPosition;
    private ImageView mFullImageView;
    private TextView mTitle;
    private List<Song> playList;


    public FullPlayingFragment() {
        // Required empty public constructor
    }


    public static FullPlayingFragment newInstance(String filePath) {

        Bundle args = new Bundle();
        args.putString(ARG_SONG_filePath,filePath);
//        args.putInt(ARG_SONG_POSITION,position);


        FullPlayingFragment fragment = new FullPlayingFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSongPath =getArguments().getString(ARG_SONG_filePath);
//        mSongPosition=getArguments().getInt(ARG_SONG_POSITION);
        mCurrentSong= SongRepository.getInstance(getActivity()).getSongByPathFile(mSongPath);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_full_playing, container, false);
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
        mFullImageView=v.findViewById(R.id.full_image_View);
        mFullImageView.setImageBitmap(SongRepository.getInstance(getActivity()).getSongImage(mCurrentSong.getFilePath()));
        mTitle=v.findViewById(R.id.title_of_song);
        mTitle.setText(mCurrentSong.getSongName()+" - "+mCurrentSong.getArtistName()+" - "+mCurrentSong.getAlbumName());


    }


    void setUpMusicPlayer() {
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(getActivity(), Uri.parse(mCurrentSong.getFilePath()));
//            mediaPlayer.setDataSource(getActivity(), Uri.parse(mSongPath));

            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    playMusic();
                    backAndForward();
                    setupSeekBar();
                    nextAndPrevious();
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

    void nextAndPrevious()
    {
        Btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSongPosition=mSongPosition--;
                setUpMusicPlayer();
                mediaPlayer.start();

            }
        });

        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSongPosition=mSongPosition++;
                setUpMusicPlayer();
                mediaPlayer.start();
//                    Btn_play.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_pause));
                    Btn_play.setImageResource(R.drawable.ic_pause);

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
        timer.schedule(new timerTask(),0,1000);

    }

    private String defineTime(long progress)
    {
        int sec=(int) progress / 1000;
        int min=sec / 60;
        sec %= 60;
        return String.format(Locale.ENGLISH,"%02d",min) + ":" + String.format(Locale.ENGLISH,"%02d",sec);
    }


    public class timerTask extends TimerTask{
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