package mik.cool.muzix.model;

import android.os.Environment;
import android.widget.ListView;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mik.cool.muzix.view.CustomAdapter;
import mik.cool.muzix.controller.ListViewListener;
import mik.cool.muzix.view.MainActivity;

public class PermissionListenner implements MultiplePermissionsListener {

    private ListView listView;
    private String[] items;
    private MainActivity activity;
    public PermissionListenner(MainActivity main) {
        this.listView = main.getListView();
        this.items = main.getItems();
        this.activity = main;
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
        displaySongs();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
        permissionToken.continuePermissionRequest();
    }

    // Cette méthode permet de recherher les fichiers mp3 et wav qui sont sur l'appareil
    public ArrayList<File> findSong (File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();

        for (File singlefile: files) {
            if (singlefile.isDirectory() && !singlefile.isHidden()) {
                arrayList.addAll(findSong(singlefile));
            } else {
                if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")) {
                    arrayList.add(singlefile);
                }
            }
        }

        return arrayList;
    }

    // Ceete méthode permet d'aaficher les musiques trouvées
    public void displaySongs() {
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());

        // Pour moi seul, je dois fais
        // final ArrayList<File> mySongs = findSong(new File(Environment.getExternalStorageDirectory() + "/Download/"));

        this.items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
            this.activity.setItems(items);
        }
        /*ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this.activity, android.R.layout.simple_list_item_1, this.items);
        this.listView.setAdapter(myAdapter);*/

        CustomAdapter customAdapter = new CustomAdapter(this.activity);
        this.listView.setAdapter(customAdapter);

        ListViewListener listlistener = new ListViewListener(this.activity, mySongs);
        this.listView.setOnItemClickListener(listlistener);
    }
}
