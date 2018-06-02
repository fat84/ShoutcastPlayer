package com.withmbh.shoutcastplayer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.withmbh.shoutcastplayer.adapter.Shoutcast;
import com.withmbh.shoutcastplayer.adapter.ShoutcastHelper;
import com.withmbh.shoutcastplayer.adapter.ShoutcastListAdapter;
import com.withmbh.shoutcastplayer.lib.MyLog;
import com.withmbh.shoutcastplayer.player.PlaybackStatus;
import com.withmbh.shoutcastplayer.player.RadioManager;
import com.withmbh.shoutcastplayer.remote.RemoteService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = getClass().getSimpleName();


    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.artist)
    TextView artist;

    @BindView(R.id.album_art)
    ImageView album_art;

    @BindView(R.id.playTrigger)
    ImageButton trigger;

    @BindView(R.id.imageView2)
    ImageView imageView2;

    @BindView(R.id.listview)
    ListView listView;

    @BindView(R.id.sub_player)
    View subPlayer;

    RadioManager radioManager;

    Boolean firstStartActivity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ButterKnife.bind(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        TextView title = (TextView) findViewById(R.id.title);
//        TextView artist = (TextView) findViewById(R.id.artist);
//        ImageView album_art = (ImageView) findViewById(R.id.album_art);
//        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);

        title.setText("사람은 아름다워");
        artist.setText("사람은 아름다워");
        album_art.setImageResource(R.mipmap.ic_launcher);
        imageView2.setImageResource(R.mipmap.ic_launcher);

        radioManager = RadioManager.with(this);
        listView.setAdapter(new ShoutcastListAdapter(this, ShoutcastHelper.retrieveShoutcasts(this)));


         ((MyApp) getApplication()).setStreamURL("http://god.inlive.co.kr:2848");

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
//ToDO 종료 ?
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onStart() {

        super.onStart();

        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {

        EventBus.getDefault().unregister(this);

        super.onStop();

    }

    @Override
    protected void onDestroy() {

        radioManager.unbind();

        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

        radioManager.bind();


    }


    @Subscribe
    public void onEvent(String status){
        MyLog.d(TAG, "onEvent " + status);

        switch (status){

            case PlaybackStatus.LOADING:

                // loading

                break;

            case PlaybackStatus.ERROR:

                Toast.makeText(this, R.string.no_stream, Toast.LENGTH_SHORT).show();

                break;

            case "ServiceConnected":

                if(firstStartActivity){
                    //streamURL = "http://god.inlive.co.kr:2848";
                    String streamURL = ((MyApp) getApplication()).getStreamURL();
                    radioManager.playOrPause(streamURL);
                }
                break;
        }

        trigger.setImageResource(status.equals(PlaybackStatus.PLAYING)
                ? R.drawable.ic_pause_black
                : R.drawable.ic_play_arrow_black);

    }

    @OnClick(R.id.playTrigger)
    public void onClicked(){

        if(TextUtils.isEmpty(((MyApp) getApplication()).getStreamURL())) return;
        radioManager.playOrPause(((MyApp) getApplication()).getStreamURL());

    }

    @OnItemClick(R.id.listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

//        Shoutcast shoutcast = (Shoutcast) parent.getItemAtPosition(position);
//        if(shoutcast == null){
//            return;
//        }
//
//        textView.setText(shoutcast.getName());
//        subPlayer.setVisibility(View.VISIBLE);
//        streamURL = "http://god.inlive.co.kr:2848";
//        radioManager.playOrPause(streamURL);
    }

}
