package mik.cool.muzix.view;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

import mik.cool.muzix.R;
import mik.cool.muzix.controller.PlayListener;

public class PlayerActivity extends AppCompatActivity {

    private Button btnplay, btnnext, btnprev, btnff, btnfr;
    private TextView txtname, txtstart, txtstop;
    private SeekBar seekmusic;
    private BarVisualizer visualizer;
    private String sname;
    public static final String EXTRA_NAME = "song_name";
    public static MediaPlayer mediaPalyer;
    public int position;
    ArrayList<File> mySongs;
    private View imageView;
    Thread updateseekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("Titre à mettre");
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);

        btnprev = findViewById(R.id.btnprev);
        btnnext = findViewById(R.id.btnnext);
        btnplay = findViewById(R.id.playbtn);
        btnff = findViewById(R.id.btnff);
        btnfr = findViewById(R.id.btnfr);

        txtname = findViewById(R.id.txtsn);
        txtstart = findViewById(R.id.txtstart);
        txtstop = findViewById(R.id.txtstop);

        seekmusic = findViewById(R.id.seekbar);

        visualizer = findViewById(R.id.blast);

        imageView = findViewById(R.id.imageview);


        if (mediaPalyer != null) {
            mediaPalyer.stop();
            mediaPalyer.release();
        }

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = intent.getStringExtra("songname");
        position = bundle.getInt("pos", 0);
        txtname.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(position).toString());
        sname = mySongs.get(position).getName();

        txtname.setText(sname);

        mediaPalyer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPalyer.start();

        updateseekbar = new Thread() {
            @Override
            public void run() {
                int totalDuration = mediaPalyer.getDuration();
                int currentposition = 0;
                
                while(currentposition < totalDuration) {
                    try {
                        sleep(500);
                        currentposition = mediaPalyer.getCurrentPosition();
                        seekmusic.setProgress(currentposition);
                    } catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        seekmusic.setMax(mediaPalyer.getDuration());
        updateseekbar.start();
        seekmusic.getProgressDrawable().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.MULTIPLY);
        seekmusic.getThumb().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);

        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPalyer.seekTo(seekBar.getProgress());
            }
        });

        String endTime = parseTime(mediaPalyer.getDuration());
        txtstop.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = parseTime(mediaPalyer.getCurrentPosition());
                txtstart.setText(currentTime);
                handler.postDelayed(this, delay);
            }
        }, delay);

        // Gestion des evenements des boutons
        PlayListener playlisten = new PlayListener(this.btnplay);
        this.btnplay.setOnClickListener(playlisten);

        //next listener
        mediaPalyer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnnext.performClick();
            }
        });

        int audiosessionId = mediaPalyer.getAudioSessionId();
        if (audiosessionId != -1) {
            visualizer.setAudioSessionId(audiosessionId);
        }

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPalyer.stop();
                mediaPalyer.release();
                position = ((position + 1) % mySongs.size());
                Uri urinext = Uri.parse(mySongs.get(position).toString());
                mediaPalyer = MediaPlayer.create(getApplicationContext(), urinext);
                sname = mySongs.get(position).getName();
                txtname.setText(sname);
                mediaPalyer.start();
                btnplay.setBackgroundResource(R.drawable.ic_pause);
                startAnimation(imageView);
                int audiosessionId = mediaPalyer.getAudioSessionId();
                if (audiosessionId != -1) {
                    visualizer.setAudioSessionId(audiosessionId);
                }
            }
        });

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPalyer.stop();
                mediaPalyer.release();
                /*if ((position - 1) < 0){
                    position = mySongs.size() - 1;
                } else {
                    position = position - 1;
                }*/
                position = ((position - 1 ) % mySongs.size());
                Uri uriprev = Uri.parse(mySongs.get(position).toString());
                mediaPalyer = MediaPlayer.create(getApplicationContext(), uriprev);
                sname = mySongs.get(position).getName();
                txtname.setText(sname);
                mediaPalyer.start();
                btnplay.setBackgroundResource(R.drawable.ic_pause);
                startAnimation(imageView);
                int audiosessionId = mediaPalyer.getAudioSessionId();
                if (audiosessionId != -1) {
                    visualizer.setAudioSessionId(audiosessionId);
                }
            }
        });

        btnff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPalyer.isPlaying()) {
                    mediaPalyer.seekTo(mediaPalyer.getCurrentPosition() + 10000);
                }
            }
        });

        btnfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPalyer.isPlaying()) {
                    mediaPalyer.seekTo(mediaPalyer.getCurrentPosition() - 10000);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (visualizer != null) {
            visualizer.release();
        }
        super.onDestroy();
    }

    public void startAnimation(View view)
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public String parseTime (int duration) {
        String time = "";
        int min = duration/60000;
        int sec = (duration/1000) % 60;

        time += min + ":";

        if (sec < 10) {
            time += "0";
        }
        time += sec;
        return time;
    }
}