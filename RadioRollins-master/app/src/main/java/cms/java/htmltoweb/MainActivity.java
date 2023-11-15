package cms.java.htmltoweb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String WEBVIEW_URL = "http://wprk.org/about-us/contact-us/";
    private static final String STREAM_URL = "http://s9.voscast.com:7024";

    private WebView webView;
    private ImageButton streamButton;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webView = findViewById(R.id.webview);
        setupWebView();

        mediaPlayer = new MediaPlayer();
        setupMediaPlayer();

        streamButton = findViewById(R.id.streamButton);
        setupStreamButton();
    }

    private void setupWebView() {
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(WEBVIEW_URL);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private void setupMediaPlayer() {
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(STREAM_URL));
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e("MainActivity", "Error setting up MediaPlayer", e);
            // Show a user-friendly error message
        }

        mediaPlayer.setOnPreparedListener(mp -> {
            streamButton.setImageResource(R.drawable.baseline_play_circle_filled_24);
            streamButton.setEnabled(true);
        });

        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.e("MainActivity", "MediaPlayer error occurred: " + what);
            // Handle the error, update UI accordingly
            return true;
        });
    }

    private void setupStreamButton() {
        streamButton.setEnabled(false);
        streamButton.setImageResource(R.drawable.baseline_loop_24);

        streamButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                streamButton.setImageResource(R.drawable.baseline_play_circle_filled_24);
            } else {
                mediaPlayer.start();
                streamButton.setImageResource(R.drawable.baseline_pause_circle_filled_24);
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
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
