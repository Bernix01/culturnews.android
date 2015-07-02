package culturnews.culturnews.fragments;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import culturnews.culturnews.R;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment implements ScreenShotable {


    private View containerView;
    private Bitmap bitmap;

    public MoreFragment() {
        // Required empty public constructor
    }

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false);
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
                            MoreFragment.this.bitmap = bitmap;
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
