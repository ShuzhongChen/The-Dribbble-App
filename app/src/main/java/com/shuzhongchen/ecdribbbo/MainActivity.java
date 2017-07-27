package com.shuzhongchen.ecdribbbo;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.shuzhongchen.ecdribbbo.view.bucket_list.BucketListFragment;
import com.shuzhongchen.ecdribbbo.view.shot_list.ShotListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.drawer) NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //setupDrawer();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, ShotListFragment.newInstance())
                    .commit();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.isChecked()) {
                    drawerLayout.closeDrawers();
                    return true;
                }

                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.drawer_item_home:
                        fragment = ShotListFragment.newInstance();
                        setTitle(R.string.title_home);
                        break;
                    case R.id.drawer_item_likes:
                        fragment = ShotListFragment.newInstance();
                        setTitle(R.string.title_likes);
                        break;
                    case R.id.drawer_item_buckets:
                        fragment = BucketListFragment.newInstance();
                        setTitle(R.string.title_buckets);
                        break;
                }

                drawerLayout.closeDrawers();


                if (fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                    return true;
                }

                return true;
            }
        });


    }


}
