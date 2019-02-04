package com.example.mymusicplayer.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusicplayer.Model.Song;
import com.example.mymusicplayer.Model.SongRepository;
import com.example.mymusicplayer.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListOfSongsFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected int mCurrentPositionOfHolder;
    public MyAdapter mySongAdapter;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onSongSelected(Song song);
    }

    public ListOfSongsFragment() {
        // Required empty public constructor
    }

    public static ListOfSongsFragment newInstance() {

        Bundle args = new Bundle();


        ListOfSongsFragment fragment = new ListOfSongsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_of_songs, container, false);

        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return v;
    }


    protected class MyHolder extends RecyclerView.ViewHolder {
        private ImageView mSongImage;
        private TextView mSongName;
        private TextView mSongArtist;
        private Song mCurrentSongOfholder;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mSongName = itemView.findViewById(R.id.song_name_of_viewholder);
            mSongArtist = itemView.findViewById(R.id.song_artist_of_viewholder);
            mSongImage = itemView.findViewById(R.id.song_image_of_viewholder);
            mCurrentPositionOfHolder = getAdapterPosition();


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onSongSelected(mCurrentSongOfholder);
                    FragmentManager fm=getFragmentManager();
                    if(fm.findFragmentById(R.id.player_container)==null){
                        fm.beginTransaction().add(R.id.player_container, PlayingFragment.newInstance(mCurrentSongOfholder.getFilePath()))
                                .commit();


                    }



                    Toast.makeText(getActivity(), "click item", Toast.LENGTH_SHORT).show();

                }
            });
        }

        public void bind(Song song) {
            mCurrentSongOfholder=song;
            mSongName.setText(song.getSongName());
            mSongArtist.setText(song.getArtistName());
            mSongImage.setImageBitmap(SongRepository.getInstance(getActivity()).getSongImage(song.getAlbumId()));
//            SongRepository.getInstance(getActivity()).updateMusicImage(imgMusic, mCurrentSongOfholder.getImageUri(), imgWidth, imgHeight);



        }
    }

    protected class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        private List<Song> mSongsList;

        public MyAdapter(List<Song> mysongsList) {
            mSongsList = mysongsList;
        }


        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater myInf = LayoutInflater.from(getActivity());
            View v = myInf.inflate(R.layout.list_item_view, parent, false);
            MyHolder mh = new MyHolder(v);
            return mh;

        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.bind(mSongsList.get(position));
        }

        @Override
        public int getItemCount() {
//            return 5;
            return mSongsList.size();
        }
    }

    public void updateUI(){
        SongRepository instance=SongRepository.getInstance(getActivity());
        List<Song> sl=instance.getAllSongs();

        if (mySongAdapter == null) {
            mySongAdapter= new MyAdapter(sl);
            mRecyclerView.setAdapter(mySongAdapter);
        }
        else {
            mySongAdapter.notifyItemChanged(mCurrentPositionOfHolder);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

}

