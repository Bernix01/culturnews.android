package culturnews.culturnews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import culturnews.culturnews.util.RestApiInterface;


public class Detail_event extends ActionBarActivity implements OnMapReadyCallback  {
    Double Y;
    Double X;
    String titlestr;
    String content;
    String desc;
    String fb,tw,ig,wb;
    String imgurl;

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeb);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        setActionBar();
        Intent intent = getIntent();
        titlestr = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        final TextView contentb = (TextView) findViewById(R.id.event_dtl_cntb);
        X = intent.getDoubleExtra("X", -33.867);
        Y = intent.getDoubleExtra("Y", 151.206);
        desc = intent.getStringExtra("desc");
        setTitle(titlestr);
        contentb.setText(html2text(content));
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fb = intent.getStringExtra("fb");
        tw = intent.getStringExtra("tw");
        ig = intent.getStringExtra("ig");
        wb = intent.getStringExtra("wb");
        imgurl = intent.getStringExtra("imgurl");
        final LinearLayout social = (LinearLayout) findViewById(R.id.sociallinks);
        final ImageView igm = (ImageView) findViewById(R.id.event_detail_himv);
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        ViewGroup.LayoutParams params = igm.getLayoutParams();
        params.height = RestApiInterface.getH(wm);
        igm.setLayoutParams(params);
        Log.d("imgurl", imgurl);
        if (!imgurl.equals("") && imgurl != null) {
            Picasso.with(getApplicationContext()).load(imgurl).fit().centerCrop().into(igm, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    igm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getApplicationContext(), ImageViewActivity.class);
                            i.putExtra("imgurl", imgurl);
                            startActivity(i);
                        }
                    });
                }

                @Override
                public void onError() {

                }
            });
        }
        if (!fb.equals(""))

        {
            ImageView imv;
            imv = new ImageView(getApplicationContext());
            imv.setImageResource(R.drawable.f3);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5, 0, 5, 0);
            imv.setLayoutParams(lp);
            if (Build.VERSION.SDK_INT > 19)
            imv.setElevation(2);
            imv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(fb));
                    startActivity(i);
                }
            });
            social.addView(imv);
        }

        if (!tw.equals(""))

        {
            ImageView imv;
            imv = new ImageView(getApplicationContext());
            imv.setImageResource(R.drawable.t1);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5, 0, 5, 0);
            imv.setLayoutParams(lp);
            if (Build.VERSION.SDK_INT > 19)
                imv.setElevation(2);
            imv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(tw));
                    startActivity(i);
                }
            });
            social.addView(imv);
        }

        if (!ig.equals(""))

        {
            ImageView imv;
            imv = new ImageView(getApplicationContext());
            imv.setImageResource(R.drawable.i2);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5, 0, 5, 0);
            imv.setLayoutParams(lp);
            if (Build.VERSION.SDK_INT > 19)
            imv.setElevation(2);
            imv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(ig));
                    startActivity(i);
                }
            });
            social.addView(imv);
        }

        if (!wb.equals(""))

        {
            ImageView imv;
            imv = new ImageView(getApplicationContext());
            imv.setImageResource(R.drawable.w6);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5, 0, 5, 0);
            imv.setLayoutParams(lp);
            if (Build.VERSION.SDK_INT > 19)
            imv.setElevation(2);
            imv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(wb));
                    startActivity(i);
                }
            });
            social.addView(imv);
        }
        }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(X, Y);

        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        googleMap.setBuildingsEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.addMarker(new MarkerOptions()
                .title(titlestr)
                .snippet(desc)
                .anchor(0.0f, 1.0f)
                .position(sydney));
    }

}
