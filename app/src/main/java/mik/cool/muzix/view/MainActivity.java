package mik.cool.muzix.view;


import android.Manifest;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.karumi.dexter.Dexter;

import mik.cool.muzix.model.PermissionListenner;
import mik.cool.muzix.R;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        this.listView = findViewById(R.id.listViewSong);
        this.listView.setNestedScrollingEnabled(true);
        runtimePermission();
    }



    public void runtimePermission() {
        PermissionListenner permission = new PermissionListenner(this);
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .withListener(permission).check();
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }
}