package mik.cool.muzix.controller;

import android.view.View;
import android.widget.Button;


public class NextListener implements View.OnClickListener{
    private Button bouton;
    public NextListener(Button btn) {
        this.bouton = btn;
    }

    @Override
    public void onClick(View v) {
        bouton.setEnabled(true);
    }
}
