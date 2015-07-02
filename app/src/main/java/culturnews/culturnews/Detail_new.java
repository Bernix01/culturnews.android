package culturnews.culturnews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import culturnews.culturnews.util.RestApiInterface;


public class Detail_new extends ActionBarActivity {
    String imgurl;
    public static String html2text(String html) {
        Log.d("unconverted",html);
        Log.d("converted",Jsoup.parse(html).text());
        return Jsoup.parse(html).text();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeb);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_new);
        setActionBar();
        Intent intent = getIntent();
        setTitle(intent.getStringExtra("title"));
        TextView date = (TextView)findViewById(R.id.n_detail_date);
        date.setText(intent.getStringExtra("date"));
        DocumentView documentView = (DocumentView)findViewById(R.id.n_detail_content);  // Support plain text
        documentView.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
        documentView.setText(html2text(intent.getStringExtra("content")));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        imgurl = intent.getStringExtra("imgurl");
        final ImageView igm = (ImageView) findViewById(R.id.n_detail_himv);
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

}
