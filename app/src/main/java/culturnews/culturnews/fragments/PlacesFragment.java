package culturnews.culturnews.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.google.android.gms.common.api.GoogleApiClient;

import culturnews.culturnews.MainActivity;
import culturnews.culturnews.R;
import culturnews.culturnews.util.RestApiInterface;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

public class PlacesFragment extends Fragment implements ScreenShotable {
    public static final String CLOSE = "Close";
    public static final String BUILDING = "Building";
    public static final String BOOK = "Book";
    public static final String MOVIE = "Movie";
    protected ImageView mImageView;
    protected int res;
    GoogleApiClient mGoogleApiClient;
    private View containerView;
    private Bitmap bitmap;
    private String loc;
    private MaterialViewPager mViewPager;
    public static PlacesFragment newInstance(String loc) {
        PlacesFragment contentFragment = new PlacesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("loc", loc);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loc = getArguments().getString("loc");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity) getActivity()).setactionBar();
        ((MainActivity) getActivity()).sync();
        RestApiInterface.CancelR(getActivity().getApplicationContext(), true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.places_layout, container, false);
        getActivity().setTitle("");
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.hide();
        final Bundle bundle = this.getArguments();
        mViewPager = (MaterialViewPager) rootView.findViewById(R.id.materialViewPager);

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {

            int oldPosition = -1;

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    default:
                        return RecyclerViewFragment.newInstance(11 + position,loc);
                }
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                super.setPrimaryItem(container, position, object);

                //only if position changed
                if (position == oldPosition)
                    return;
                oldPosition = position;

                String imageUrl = "";
                switch (position) {
                    case 0:
                        imageUrl = "http://culturnews.com/images/teatroGRANDE.jpg";
                        break;
                    case 1:
                        imageUrl = "http://culturnews.com/images/musica2GRANDE.jpg";
                        break;
                    case 2:
                        imageUrl = "http://culturnews.com/images/espacioculturalesGRANDE.jpg";
                        break;
                    case 3:
                        imageUrl = "http://culturnews.com/images/danzaGRANDE.jpg";
                        break;
                    case 4:
                        imageUrl = "http://culturnews.com/images/musicaGRANDE.jpg";
                        break;
                    case 5:
                        imageUrl = "http://culturnews.com/images/pinturaGRANDE.jpg";
                        break;
                    default:
                        imageUrl = "http://culturnews.com/images/teatroGRANDE.jpg";
                        break;
                }

                final int fadeDuration = 100;
                mViewPager.setImageUrl(imageUrl, fadeDuration);

            }

            @Override
            public int getCount() {
                return 6;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "TEATRO";
                    case 1:
                        return "MUSICA CONTEMPORANEA";
                    case 2:
                        return "ESPACIOS CULTURALES";
                    case 3:
                        return "DANZA";
                    case 4:
                        return "MUSICA CLASICA";
                    case 5:
                        return "PINTURA";
                }
                return "";
            }
        });
        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        mViewPager.getViewPager().setCurrentItem(0);
        Toolbar toolbar = mViewPager.getToolbar();

        if (toolbar != null) {
            ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar2 = ((ActionBarActivity) getActivity()).getSupportActionBar();
            actionBar2.setDisplayHomeAsUpEnabled(true);
            actionBar2.setDisplayShowHomeEnabled(true);
            actionBar2.setDisplayShowTitleEnabled(true);
            actionBar2.setDisplayUseLogoEnabled(false);
            actionBar2.setHomeButtonEnabled(true);
            ((MainActivity) getActivity()).sync();
        }
        ((MainActivity) getActivity()).drawerToggle.setDrawerIndicatorEnabled(true);
        return rootView;
    }

    @Override
    public void takeScreenShot() {
        runThread();

    }

    private void runThread() {

        new Thread() {
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
                                    containerView.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            containerView.draw(canvas);
                            PlacesFragment.this.bitmap = bitmap;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}

