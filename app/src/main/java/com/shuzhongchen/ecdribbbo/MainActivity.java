package com.shuzhongchen.ecdribbbo;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

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
                switch (item.getItemId()) {
                    case R.id.drawer_item_home:
                        Toast.makeText(MainActivity.this, "home clicked", Toast.LENGTH_LONG).show();
                        setTitle(R.string.title_home);
                        break;
                    case R.id.drawer_item_likes:
                        Toast.makeText(MainActivity.this, "likes clicked", Toast.LENGTH_LONG).show();
                        setTitle(R.string.title_likes);
                        break;
                    case R.id.drawer_item_buckets:
                        Toast.makeText(MainActivity.this, "buckets clicked", Toast.LENGTH_LONG).show();
                        setTitle(R.string.title_buckets);
                        break;
                }

                drawerLayout.closeDrawers();

                return true;
            }
        });


    }


}
