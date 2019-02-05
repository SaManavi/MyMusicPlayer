package com.example.mymusicplayer.Model;

public class Song {

    private String mFilePath;
    private String mSongName;
    private String mAlbumName;
    private String mArtistName;
    private Long mAlbumId;
    private String mArtistId;

    public Song() {
    }

    public Song(String filePath) {
        mFilePath = filePath;
    }


    public String getFilePath() {
        return mFilePath;
    }


    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        mSongName = songName;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(String albumName) {
        mAlbumName = albumName;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(String artistName) {
        mArtistName = artistName;
    }

    public Long getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(Long albumId) {
        mAlbumId = albumId;
    }

    public String getArtistId() {
        return mArtistId;
    }

    public void setArtistId(String artistId) {
        mArtistId = artistId;
    }
}
