package com.knockit.android.activities;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.knockit.android.fragments.MainFragment;
import com.knockit.android.fragments.SettingsFragment;
import com.knockit.android.R;
import com.knockit.android.utils.LocalPreferences;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = MainActivity.class.getSimpleName() + "@asw!";

    private final MainFragment MainFragment = new MainFragment();

    private final SettingsFragment settingsFragment = new SettingsFragment();

    private enum FragmentType {
        Main,
        Settings
    }

    private FragmentType currentShownFragment = FragmentType.Main;

    public final Context context = this;

    private Snackbar snackbar = null;

    private LocalPreferences localPreferences = null;

    private boolean doubleBackToExitPressedOnce = false;

    private int doubleBackToExitPressedOnceDuration = 2000; // length of Toast.LENGTH_SHORT

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "enters onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Set localPreferences
        if (localPreferences == null) {
            localPreferences = new LocalPreferences(context);
        }

        setupFragments();
        showFragment(FragmentType.Main);

//        Log.d(TAG, "exits onCreate");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // first close drawer, then close search bar, then go back to search fragment, and lastly
        // go use default behavior

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;

        } else if (currentShownFragment == FragmentType.Settings) {
            showFragment(FragmentType.Main);
            return;

        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToast(getResources().getString(R.string.back_twcie_to_exit));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, doubleBackToExitPressedOnceDuration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Show as Snackbar with OnClickListener binding
     * Dismisses older Snackbars.
     *
     * @param text           The text to display
     * @param length         the length
     * @param buttonTxt      The text on button
     * @param buttonListener The on OnClickListener
     */
    public void showSnack(String text, int length, String buttonTxt, View.OnClickListener buttonListener) {
        dismissSnack();
        View parentLayout = findViewById(android.R.id.content);
        snackbar = Snackbar.make(parentLayout, text, length)
                .setAction(buttonTxt, buttonListener)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
        snackbar.show();
    }

    /**
     * Show as Snackbar without OnClickListener binding
     * Dismisses older Snackbars.
     *
     * @param text      The text to display
     * @param length    the length
     * @param buttonTxt The text on button
     */
    public void showSnack(String text, int length, String buttonTxt) {
        showSnack(text, length, buttonTxt, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do nothing
            }
        });
    }

    /**
     * Dismiss the active Snackbar
     */
    public void dismissSnack() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }


    /**
     * Shows a long Toast
     *
     * @param msg the Toast's message
     */
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * The default share functionality
     */
    public void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_app_subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_app_body));
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
    }

    /**
     * The default send functionality
     */
    public void send() {
        share();
    }

    /**
     * The default refresh functionality
     */
    public void refresh() {
        // Do nothing
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            showFragment(FragmentType.Main);

        } else if (id == R.id.nav_manage) {
            showFragment(FragmentType.Settings);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Displays the desired fragment, and hides the other one.
     *
     * @param fragmentType the fragment to show
     */
    public void showFragment(FragmentType fragmentType) {
        currentShownFragment = fragmentType;
        Fragment fragmentToShow = fragmentType == FragmentType.Main ? MainFragment : settingsFragment;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragmentToShow);

        fragmentTransaction.commit();
    }

    /**
     * Adds the 2 fragments to the fragments list.
     * If the fragments are already added, we remove them and add them again.
     *
     */
        public void setupFragments() {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.main_container, MainFragment);
            fragmentTransaction.commit();
        }

    /**
     * Crated a nice Snakbar the notifies the user that no internet connection is available.
     */
    public void notifyIfOffline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(cm !=null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting()))
        {
            showSnack(getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE, getResources().getString(R.string.dismiss));
        }
    }

    /**
     * Retrieve the LocalPreferences object.
     * @return the LocalPreferences object.
     */
    public LocalPreferences getLocalPreferences() {
        return localPreferences;
    }
}