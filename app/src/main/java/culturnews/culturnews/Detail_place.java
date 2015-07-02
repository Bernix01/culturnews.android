package culturnews.culturnews;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.io.IOException;

import culturnews.culturnews.util.RestApiInterface;


public class Detail_place extends ActionBarActivity implements OnMapReadyCallback {
    Double Y;
    Double X;
    String titlestr;
    String content;
    String desc;
    String imgurl;
    String fb, tw, ig, wb, wbrd;
    private MediaPlayer mediaPlayer;

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeb);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_place);
        setActionBar();
        Intent intent = getIntent();
        titlestr = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        imgurl = intent.getStringExtra("imgurl");
        final ImageView igm = (ImageView) findViewById(R.id.place_item_h_imv);
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        ViewGroup.LayoutParams params = igm.getLayoutParams();
        params.height = RestApiInterface.getH(wm);
        igm.setLayoutParams(params);
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
        TextView contentb = (TextView) findViewById(R.id.event_dtl_cntb);
        X = intent.getDoubleExtra("X", -1);
        Y = intent.getDoubleExtra("Y", -1);
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
        wbrd = intent.getStringExtra("asd");

        LinearLayout social = (LinearLayout) findViewById(R.id.sociallinks);
        if (!fb.equals("")) {
            ImageView imv;
            imv = new ImageView(getApplicationContext());
            imv.setImageResource(R.drawable.f3);
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
        if (!wbrd.equals("")) {
            final ImageView wbrd_imv;
            wbrd_imv = (ImageView) findViewById(R.id.extrabn);
            ViewGroup.LayoutParams lp = wbrd_imv.getLayoutParams();
            lp.height = 200;
            lp.width = 200;
            wbrd_imv.setLayoutParams(lp);
            wbrd_imv.setImageResource(R.drawable.nplay);
            wbrd_imv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleMusic(wbrd, (ImageView) v);
                }
            });
        }
        if (!tw.equals("")) {
            ImageView imv;
            imv = new ImageView(getApplicationContext());
            imv.setImageResource(R.drawable.t1);
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
        if (!ig.equals("")) {
            ImageView imv;
            imv = new ImageView(getApplicationContext());
            imv.setImageResource(R.drawable.i2);
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
        if (!wb.equals("")) {
            ImageView imv;
            imv = new ImageView(getApplicationContext());
            imv.setImageResource(R.drawable.w6);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void toggleMusic(String url, ImageView v) {

        if (mediaPlayer == null) {
            v.setImageResource(R.drawable.npause);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(wbrd);
                mediaPlayer.prepareAsync();
            } catch (IllegalArgumentException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IllegalStateException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer player) {
                    player.start();
                }
            });
        } else if (mediaPlayer.isPlaying()) {
            v.setImageResource(R.drawable.nplay);
            mediaPlayer.pause();
        } else {
            v.setImageResource(R.drawable.npause);
            mediaPlayer.start();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long

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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(X, Y);
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
