package cms.java.htmltoweb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import java.io.IOException;
import static android.webkit.WebView.*;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ImageButton streamButton;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // WebView setup
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://wprk.org/about-us/contact-us/");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // MediaPlayer setup
        setupMediaPlayer();

        // Stream button setup
        setupStreamButton();
    }

    private void setupMediaPlayer() {
        // MediaPlayer setup
        Uri wprkStream = Uri.parse("http://s9.voscast.com:7024");
        try {
            mediaPlayer.setDataSource(getApplicationContext(), wprkStream);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error (e.g., show a toast, log the error)
        }

        // Prepare the stream, and don't enable the button
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                streamButton.setImageResource(R.drawable.baseline_play_circle_filled_24);
                streamButton.setEnabled(true);
            }
        });
    }

    private void setupStreamButton() {
        // Stream button setup
        streamButton = findViewById(R.id.streamButton);
        streamButton.setEnabled(false);
        streamButton.setImageResource(R.drawable.baseline_loop_24);

        // When the button is clicked, pause or play the music
        streamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    streamButton.setImageResource(R.drawable.baseline_play_circle_filled_24);
                } else {
                    mediaPlayer.start();
                    streamButton.setImageResource(R.drawable.baseline_pause_circle_filled_24);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
