package mik.cool.muzix.controller;

import android.view.View;
import android.widget.Button;

import mik.cool.muzix.R;

import static mik.cool.muzix.view.PlayerActivity.mediaPalyer;

public class PlayListener implements View.OnClickListener {

    private Button bouton;
    public PlayListener(Button btn) {
        this.bouton = btn;
    }

    @Override
    public void onClick(View v) {
        if (mediaPalyer.isPlaying()) {
            this.bouton.setBackgroundResource(R.drawable.ic_play);
            mediaPalyer.pause();
        } else {
            this.bouton.setBackgroundResource(R.drawable.ic_pause);
            mediaPalyer.start();
        }
    }
}
