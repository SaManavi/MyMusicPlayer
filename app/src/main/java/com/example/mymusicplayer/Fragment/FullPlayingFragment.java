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
import android.widget.Toast;

import com.example.mymusicplayer.Model.Song;
import com.example.mymusicplayer.Model.SongRepository;
import com.example.mymusicplayer.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class FullPlayingFragment extends Fragment {

    private static String ARG_SONG_filePath ="com.example.mymusicplayer.file_path_Of_music";
    private static String ARG_SONG_INDEX ="com.example.mymusicplayer.Song Position";
    private SeekBar seekbar;
    private TextView mEndTime, mStartTime;
    private ImageButton Btn_forward, Btn_play, Btn_backward, Btn_next, Btn_previous,Btn_repeat,Btn_shuffle;
    private MediaPlayer mediaPlayer;

    private Timer timer;
    private String mSongPath;
    private Song mCurrentSong;
    private Integer mCurrentSongIndex;
    private ImageView mFullImageView;
    private TextView mTitle;
    private List<Song> playList;

    private boolean mShuffle;
    private boolean mRepeatOne;


    public FullPlayingFragment() {
        // Required empty public constructor
    }

    public static FullPlayingFragment newInstance(String filePath) {

        Bundle args = new Bundle();
        args.putString(ARG_SONG_filePath,filePath);

        FullPlayingFragment fragment = new FullPlayingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSongPath =getArguments().getString(ARG_SONG_filePath);
        playList=SongRepository.getInstance(getActivity()).getPlayingList();
        mCurrentSong= SongRepository.getInstance(getActivity()).getSongByPathFile(mSongPath);
        mCurrentSongIndex=playList.indexOf(mCurrentSong);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_full_playing, container, false);
        DefiningComponents(v);
        setUpMusicPlayer(mCurrentSongIndex);

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
        Btn_repeat = v.findViewById(R.id.Btn_repeat);
        Btn_shuffle= v.findViewById(R.id.Btn_shuffle);
        mFullImageView=v.findViewById(R.id.full_image_View);
        mTitle=v.findViewById(R.id.title_of_song);


    }

    void setUpMusicPlayer(int songIndex) {
        if(mediaPlayer==null)mediaPlayer = new MediaPlayer();
        mCurrentSong=playList.get(songIndex);

        mFullImageView.setImageBitmap(SongRepository.getInstance(getActivity()).getSongImage(mCurrentSong.getFilePath()));

        mTitle.setText((playList.get(songIndex).getSongName())+
                " - "+(playList.get(songIndex).getArtistName())+
                " - "+(playList.get(songIndex).getAlbumName()));



        try {
            mediaPlayer.setDataSource(getActivity(), Uri.parse(mCurrentSong.getFilePath()));
//            mediaPlayer.setDataSource(getActivity(), Uri.parse(mSongPath));

            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    checkPlayingBtnState();
                    playMusic();
                    backAndForward();
                    setupSeekBar();
                    nextAndPrevious();
                    repeatAndShuffle();
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    functionOfNext();
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

                if(mediaPlayer!=null && mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
//                    Btn_play.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.play_music));
                    Btn_play.setImageResource(R.drawable.play_music);
                }
                else{
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

    void repeatAndShuffle()
    {
        Btn_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShuffle=!mShuffle;
                if(mShuffle){
                    mShuffle=true;
                    Btn_shuffle.setImageResource(R.drawable.ic_pause);
                    Toast.makeText(getActivity(), "Shuffle:"+mShuffle, Toast.LENGTH_SHORT).show();
                }
                else{
                    Btn_shuffle.setImageResource(R.drawable.play_music);
                    Toast.makeText(getActivity(), "Shuffle:"+mShuffle, Toast.LENGTH_SHORT).show();
                }

            }
        });

        Btn_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRepeatOne=!mRepeatOne;
                if(mRepeatOne){
                    mRepeatOne=true;
                    Btn_repeat.setImageResource(R.drawable.ic_pause);
                    Toast.makeText(getActivity(), "Repeat One:"+mRepeatOne, Toast.LENGTH_SHORT).show();
                }
                else{
                    Btn_repeat.setImageResource(R.drawable.play_music);
                    Toast.makeText(getActivity(), "Repeat One:"+mRepeatOne, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    void nextAndPrevious()
    {
        Btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.stop();
                if(mRepeatOne){mCurrentSongIndex=mCurrentSongIndex+0;}

                else if(mShuffle) {
                    Random randomIndex = new Random();
                    mCurrentSongIndex = randomIndex.nextInt(playList.size());
                }

                else{mCurrentSongIndex = mCurrentSongIndex-1;}


                mediaPlayer.reset();
                setUpMusicPlayer(mCurrentSongIndex);
            }
        });

        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                functionOfNext();

            }
        });
    }

    private void functionOfNext() {

        mediaPlayer.stop();
        if(mRepeatOne){mCurrentSongIndex=mCurrentSongIndex+0;}

        else if(mShuffle) {
            Random randomIndex = new Random();
            mCurrentSongIndex = randomIndex.nextInt(playList.size());
        }

        else{mCurrentSongIndex = mCurrentSongIndex+1;}


        mediaPlayer.reset();
        setUpMusicPlayer(mCurrentSongIndex);
    }

    private void checkPlayingBtnState() {
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            Btn_play.setImageResource(R.drawable.ic_pause );}
        else{
            Btn_play.setImageResource(R.drawable.play_music);
        }
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