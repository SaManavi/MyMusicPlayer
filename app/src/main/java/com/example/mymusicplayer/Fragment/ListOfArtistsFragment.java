package com.example.mymusicplayer.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymusicplayer.Model.Album;
import com.example.mymusicplayer.Model.Song;
import com.example.mymusicplayer.Model.SongRepository;
import com.example.mymusicplayer.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListOfArtistsFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected int mCurrentPositionOfHolder;
    public MyAdapter myAlbumAdapter;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onAlbumSelected(Song song);
    }

    public ListOfArtistsFragment() {
        // Required empty public constructor
    }

    public static ListOfArtistsFragment newInstance() {

        Bundle args = new Bundle();


        ListOfArtistsFragment fragment = new ListOfArtistsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragmen_recycler, container, false);

        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        updateUI();
        return v;
    }


    protected class MyHolder extends RecyclerView.ViewHolder {
        private ImageView mAlbumImage;
        private TextView mAlbumName;
        private TextView mAlbumArtist;
        private Album mCurrentAlbumOfholder;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mAlbumName = itemView.findViewById(R.id.album_name_of_viewholder);
            mAlbumArtist = itemView.findViewById(R.id.album_artist_of_viewholder);
            mAlbumImage = itemView.findViewById(R.id.album_image_of_viewholder);
            mCurrentPositionOfHolder = getAdapterPosition();


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
////                    onSongSelected(mCurrentSongOfholder);
//                    FragmentManager fm=getFragmentManager();
//                    if(fm.findFragmentById(R.id.player_container)==null) {
//                        fm.beginTransaction()
//                                .replace(R.id.player_container, PlayingFragment.newInstance(mCurrentAlbumOfholder.getFilePath()))
//                                .commit();
//                    }
//
//                    else{
//                        fm.beginTransaction()
//                                .replace(R.id.player_container, PlayingFragment.newInstance(mCurrentAlbumOfholder.getFilePath()))
//                                .commit();
//                    }
                }
            });
        }

        public void bind(Album album) {
            mCurrentAlbumOfholder=album;
            mAlbumName.setText(album.getAlbumName());
            mAlbumArtist.setText(album.getAlbumArtist());
            mAlbumImage.setImageBitmap(SongRepository.getInstance(getActivity()).getAlbumImage(album.getAlbumId()));
//            mSongImage.setImageBitmap(SongRepository.getInstance(getActivity()).getAlbumImage(song.getAlbumId()));
//            SongRepository.getInstance(getActivity()).updateMusicImage(imgMusic, mCurrentSongOfholder.getImageUri(), imgWidth, imgHeight);



        }
    }

    protected class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        private List<Album> mAlbumsList;

        public MyAdapter(List<Album> myAlbumsList) {
            mAlbumsList = myAlbumsList;
        }


        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater myInf = LayoutInflater.from(getActivity());
            View v = myInf.inflate(R.layout.list_item_album_view, parent, false);
            MyHolder mh = new MyHolder(v);
            return mh;

        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.bind(mAlbumsList.get(position));
        }

        @Override
        public int getItemCount() {
            return mAlbumsList.size();
        }
    }

    public void updateUI(){
        SongRepository instance=SongRepository.getInstance(getActivity());
        List<Album> allAlbumsList=instance.getAllAlbums();

        if (myAlbumAdapter == null) {
            myAlbumAdapter= new MyAdapter(allAlbumsList);
            mRecyclerView.setAdapter(myAlbumAdapter);
        }
        else {
            myAlbumAdapter.notifyItemChanged(mCurrentPositionOfHolder);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

}

