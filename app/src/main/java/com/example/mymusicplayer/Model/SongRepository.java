package com.example.mymusicplayer.Model;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.mymusicplayer.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SongRepository {

    private static SongRepository instance;
    private List<Song> mSongsList;
    private Context mContex;
    private String file;
    private String mDisplayName;
    private String mArtist;
    private String mArtistId;
    private String mArtistKey;
    private String mAlbum;
    private Long   mAlbumId;
    private String mDuration;
    private String mTitle;
    private Bitmap mMusicImg;
    private Context mContext;
    private Bitmap bitmap = null;
    private List<Song> mPlayingList;







    public SongRepository(Context c) {
        mContex=c;
    }

    public static SongRepository getInstance(Context c) {
        if (instance == null)
            instance = new SongRepository(c);
        return instance;
    }

    public List<Song> getAllSongs(){
        mSongsList = new ArrayList<>();
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = mContex.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                null,
                null);

        if (null != cursor) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
//
//            MediaStore.MediaColumns.DISPLAY_NAME
//            MediaStore.MediaColumns.TITLE
//            MediaStore.Audio.AudioColumns.DURATION
//            MediaStore.MediaColumns.DATA
//            MediaStore.Audio.AudioColumns.ARTIST
//            MediaStore.Audio.AudioColumns.ALBUM
//                MediaStore.Audio.Media.ARTIST,
//                        MediaStore.Audio.Media.TITLE,
//                        MediaStore.Audio.Media.DATA,
//                        MediaStore.Audio.Media.DISPLAY_NAME,
//                        MediaStore.Audio.Media.DURATION,
//                        MediaStore.Audio.Media.ALBUM_ID


                file = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                String name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));

                mDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                mArtist= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                mArtistId= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                mArtistKey = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_KEY));
                mAlbum= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
//                 String mSongIm= cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.));
                mAlbumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                mDuration= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                mTitle= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));

                Song song = new Song(file);
                song.setSongName(name);
                song.setArtistName(artist);
                song.setAlbumName(album);
                song.setAlbumId(mAlbumId);

                mSongsList.add(song);

                cursor.moveToNext();
            }
        }


        cursor.close();
        return mSongsList;

    }
    public void updateMusicImage(ImageView imageView, Uri albumArtUri, int width, int height) {
        try {
            mMusicImg = MediaStore.Images.Media.getBitmap(
                    mContext.getContentResolver(), albumArtUri);
            mMusicImg = Bitmap.createScaledBitmap(mMusicImg, width, height, true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mMusicImg = BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.music_default_cover);

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            imageView.setImageBitmap(mMusicImg);
        }
    }

    public List<Song> getAlbumSongs(String album){
        List<Song> allSongsList=getAllSongs();
        List<Song> albumSongsList=new ArrayList<>();

        for (Song s:allSongsList) {
            if(s.getAlbumName().equals(album))
                albumSongsList.add(s);
        }

        return albumSongsList;
    }

    public List<Song> getArtistSongs(String artist){
        List<Song> allSongsList=getAllSongs();
        List<Song> artistSongsList=new ArrayList<>();

        for (Song s:allSongsList) {
            if(s.getAlbumName().equals(artist))
                artistSongsList.add(s);
        }

        return artistSongsList;

    }



    public List<Song> getPlayingList() {
        mPlayingList=getAllSongs();

        Collections.sort(mPlayingList, new Comparator<Song>() {
            @Override
            public int compare(Song s1 , Song s2) {
                return s1.getSongName().compareToIgnoreCase(s2.getSongName());
            }
        });
        return mPlayingList;
    }

    public void setPlayingList(boolean test) {
    }

    public Bitmap getAlbumImage(Long  albumId) {

        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

//    Logger.debug(albumArtUri.toString());

        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                    mContex.getContentResolver(), albumArtUri);
            bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);

        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            if(!(albumId%2==0))    bitmap = BitmapFactory.decodeResource(mContex.getResources(), R.drawable.runningmusic);
            else  bitmap = BitmapFactory.decodeResource(mContex.getResources(), R.drawable.music2);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return bitmap;
    }

    public Song getSongByPathFile(String pathFile){
        Song song=new Song();
        for (Song s:mSongsList) {
            if(s.getFilePath().equals(pathFile))
                song= s;
        }
        return song;
    }


    public Bitmap getSongImage(String path) {

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null) return BitmapFactory.decodeByteArray(data, 0, data.length);
        return BitmapFactory.decodeResource(mContex.getResources(), R.drawable.music2);

    }







    public List<Album> getAllAlbums(){
        List<Album> allAlbumsList=new ArrayList<>();

        String[] projection = new String[] {"DISTINCT "+MediaStore.Audio.AudioColumns.ALBUM};
//        Cursor cursor = mContex.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        Cursor cursor = mContex.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null,
                null);

        if (null != cursor) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

//                MediaStore.Audio.Albums.ALBUM_ID;
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
                Long album_Id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));

//                mAlbum= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
//                Long album_Id  = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID));

                Album mAlbum=new Album();
                mAlbum.setAlbumName(album);
                mAlbum.setAlbumId(album_Id);
                mAlbum.setAlbumArtist(artist);

//                for (Album a:allAlbumsList) {
//
//                    if(!(a.getAlbumName().equals(album)))
//                        allAlbumsList.add(mAlbum);
//                }
                allAlbumsList.add(mAlbum);

                cursor.moveToNext();
            }
        }


        cursor.close();
        return allAlbumsList;

    }



}