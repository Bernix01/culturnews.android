package culturnews.culturnews;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import culturnews.culturnews.fragments.MainFragment;
import culturnews.culturnews.fragments.NewsFragment;
import culturnews.culturnews.fragments.PlacesFragment;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;

public class MainActivity extends ActionBarActivity implements ViewAnimator.ViewAnimatorListener,ConnectionCallbacks, OnConnectionFailedListener {
    public ActionBarDrawerToggle drawerToggle;
    GoogleApiClient mGoogleApiClient;
    private DrawerLayout drawerLayout;
    private List<SlideMenuItem> list = new ArrayList<>();
    private ViewAnimator viewAnimator;
    private LinearLayout linearLayout;
    private String loc;

    private String SENDER_ID = "721300573378";
    private String HubName = "gbm";
    private String HubListenConnectionString = "Endpoint=sb://gbmnotify.servicebus.windows.net/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=Mug+gFESrGGXaYsXHsuJjb91rkn+yvR4FuoyhC1hJpM=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > 18) {
            FrameLayout view = (FrameLayout) findViewById(R.id.conteiner_frame);
            FrameLayout view2 = (FrameLayout) findViewById(R.id.scrollView);
            view.setPadding(0, getStatusBarHeight(), 0, getNavigationBarHeight());
            view2.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        buildGoogleApiClient();
        MainFragment mainFragment = MainFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mainFragment)
                .commit();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        setactionBar();
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, mainFragment, drawerLayout, this);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getNavigationBarHeight() {
        Resources resources = getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(MainFragment.CLOSE, R.drawable.nexit);
        list.add(menuItem0);
        SlideMenuItem menuItem7 = new SlideMenuItem(MainFragment.MOVIE, R.drawable.n010);
        list.add(menuItem7);
        SlideMenuItem menuItem2 = new SlideMenuItem(MainFragment.BOOK, R.drawable.nn005);
        list.add(menuItem2);
        SlideMenuItem menuItem = new SlideMenuItem(MainFragment.BUILDING, R.drawable.n005);
        list.add(menuItem);
        /*SlideMenuItem menuItem3 = new SlideMenuItem(MainFragment.MORE, R.drawable.n005);
        list.add(menuItem3);*/
    }


    public void setactionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Eventos");
        getSupportActionBar().show();
        // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(R.color.colorPrimary));
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.open_dscrp,  /* "open drawer" description */
                R.string.close_dscrp  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    public void sync() {
        drawerToggle.syncState();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_contact:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://culturnews.com/index.php/contactenos"));
                startActivity(i);
                return true;
            case R.id.action_feedback:
                Intent i2 = new Intent(Intent.ACTION_VIEW);
                i2.setData(Uri.parse("http://culturnews.com/index.php/contactenos/sugerencias"));
                startActivity(i2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ScreenShotable replaceFragment(ScreenShotable screenShotable, int topPosition, String name) {
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        findViewById(R.id.content_overlay).setBackgroundDrawable(new BitmapDrawable(getResources(), screenShotable.getBitmap()));
        animator.start();
        switch (name) {
            case MainFragment.MOVIE:
                MainFragment mainFragment = MainFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mainFragment).commit();
                getSupportActionBar().setTitle("Eventos");
                return mainFragment;
            case MainFragment.BOOK:
                Log.d("News", "yep");
                NewsFragment newsFragment = NewsFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, newsFragment).commit();
                getSupportActionBar().setTitle("Noticias");
                return newsFragment;
            case MainFragment.BUILDING:
                PlacesFragment placesFragment = PlacesFragment.newInstance(loc);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, placesFragment).commit();
                return placesFragment;
            default:
                return screenShotable;
        }
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        Log.d("position", String.valueOf(position));
        switch (slideMenuItem.getName()) {
            case MainFragment.CLOSE:
                return screenShotable;
            default:
                return replaceFragment(screenShotable, position, slideMenuItem.getName());
        }
    }
    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }

    @Override
    public void onConnected(Bundle bundle) {
       Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            this.loc = mLastLocation.getLatitude()+","+mLastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

