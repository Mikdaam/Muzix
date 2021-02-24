package mik.cool.muzix.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import mik.cool.muzix.R;
import mik.cool.muzix.view.MainActivity;

public class CustomAdapter extends BaseAdapter {

    private MainActivity activity;
    private String[] items;
    public CustomAdapter(MainActivity main) {
        this.activity = main;
        this.items = main.getItems();
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myView = this.activity.getLayoutInflater().inflate(R.layout.list_item, null);
        TextView textsong = myView.findViewById(R.id.txtsongname);
        textsong.setSelected(true);
        textsong.setText(items[position]);

        return myView;
    }
}
