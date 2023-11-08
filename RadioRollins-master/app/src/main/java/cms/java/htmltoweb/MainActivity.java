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
import android.widget.Button;
import android.widget.ImageButton;

import java.io.IOException;

import static android.webkit.WebView.*;

public class MainActivity extends AppCompatActivity {

    private ImageButton b1;
    MediaPlayer mediaPlayer = new MediaPlayer();

    private WebView webView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://wprk.org/about-us/contact-us/");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        //TODO: add text/image to button and change when ready/play/paused
        //TODO: If using image, must convert Button to MediaButton

        //set streaming url
        Uri wprkStream = Uri.parse("http://s9.voscast.com:7024");
        try {

            mediaPlayer.setDataSource(getApplicationContext(),wprkStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //prepare the stream, and dont enable the button
        mediaPlayer.prepareAsync();
        b1 = (ImageButton) findViewById(R.id.streamButton);
        b1.setEnabled(false);
        b1.setImageResource(R.drawable.baseline_loop_24);

        //when mediaplayer is prepared and ready to stream, enable the button
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                b1.setImageResource(R.drawable.baseline_play_circle_filled_24);
                b1.setEnabled(true);
            }
        });

        //when the button is clicked, pause or play the music
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    b1.setImageResource(R.drawable.baseline_play_circle_filled_24);

                } else {
                    mediaPlayer.start();
                    b1.setImageResource(R.drawable.baseline_pause_circle_filled_24);

                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
        {
            webView.goBack();
        }
        else
        {
            super.onBackPressed();
        }

    }
}
