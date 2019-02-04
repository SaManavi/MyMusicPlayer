package com.example.mymusicplayer.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.mymusicplayer.Fragment.ListOfSongsFragment;
import com.example.mymusicplayer.Model.Song;
import com.example.mymusicplayer.R;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;


//    private TabAdapter mTabAdapter;


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
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.viewPager);
//        mTabLayout = findViewById(R.id.tabLayout);

//        mTabAdapter = new TabAdapter(getSupportFragmentManager());
//        mTabAdapter.addFragment(new ListOfSongsFragment.newInstance());
//        mTabAdapter.addFragment(new ListOfSongsFragment.newInstance());
//        mTabAdapter.addFragment(new ListOfSongsFragment.newInstance());
//        mViewPager.setAdapter(mTabAdapter);
//        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()){
            private Fragment fragment;

            @Override
            public int getCount() {
                return 1;
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {

               return  ListOfSongsFragment.newInstance();

//                switch (position){
//
//                   case 1:
//                        fragment=   ListOfSongsFragment.newInstance();
//break;
//
//                   case 2:
//                       fragment = ListOfSongsFragment.newInstance();
//                       break;
////                   case 2:
//////
//////                       break;
//////
//               }
//               return fragment;
            }
        });



    }
}
