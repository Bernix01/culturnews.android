package culturnews.culturnews;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;

public class RadioMan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_man);
        String url = getIntent().getStringExtra("wbrd");
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (url != null) {
            try {
                mediaPlayer.setDataSource(url);
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

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

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
}
