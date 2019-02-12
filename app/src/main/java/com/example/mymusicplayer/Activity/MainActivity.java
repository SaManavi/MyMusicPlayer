package com.example.mymusicplayer.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TableLayout;

import com.example.mymusicplayer.Fragment.ListOfAlbumsFragment;
import com.example.mymusicplayer.Fragment.ListOfArtistsFragment;
import com.example.mymusicplayer.Fragment.ListOfSongsFragment;
import com.example.mymusicplayer.Model.Song;
import com.example.mymusicplayer.R;
import com.example.mymusicplayer.TabAdapter;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TabAdapter mTabAdapter;



//    @Override
    public void onSongSelected(Song song) {
//        if (findViewById(R.id.fragment_detail_container) == null) {
//            //Phone
//            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
//            startActivity(intent);
//        } else {
//            //Tablet
//            CrimeDetailFragment crimeDetailFragment = CrimeDetailFragment.newInstance(crime.getId());
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_detail_container, crimeDetailFragment)
//                    .commit();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list_of_items);

        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tablayout);

        mTabAdapter = new TabAdapter(getSupportFragmentManager());

        mTabAdapter.addFragment(new ListOfSongsFragment().newInstance(),"All My Songs");
        mTabAdapter.addFragment(new ListOfArtistsFragment().newInstance(),"All Artists");
        mTabAdapter.addFragment(new ListOfAlbumsFragment().newInstance(),"All Albums");
        mTabAdapter.addFragment(new ListOfAlbumsFragment().newInstance(),"My Play List");
        mTabAdapter.addFragment(new ListOfAlbumsFragment().newInstance(),"My Search List");

        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mViewPager.setOffscreenPageLimit(5);


    }
}
