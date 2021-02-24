package mik.cool.muzix.controller;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import mik.cool.muzix.view.MainActivity;
import mik.cool.muzix.view.PlayerActivity;

public class ListViewListener implements AdapterView.OnItemClickListener {
    private ListView listView;
    private String[] items;
    private MainActivity activity;
    private ArrayList<File> mySongs;
    public ListViewListener(MainActivity main, ArrayList<File> songs) {
        this.listView = main.getListView();
        this.items = main.getItems();
        this.activity = main;
        this.mySongs = songs;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String songName = (String) listView.getItemAtPosition(position);
        Intent playerIntent = new Intent(this.activity.getApplicationContext(), PlayerActivity.class);
        playerIntent.putExtra("songs", mySongs)
                .putExtra("songname", songName)
                .putExtra("pos", position);
        this.activity.startActivity(playerIntent);
    }
}
