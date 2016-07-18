package org.codeforcoffee.mapmylocation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by codeforcoffee on 7/18/16.
 */
public class LocationArrayAdapter extends ArrayAdapter<Location> {

    // constructor
    public LocationArrayAdapter(Context context,
                                List<Location> locations) {
        super(context, -1, locations);
    }

    // define a custom ViewHolder
    // this class represents our XML
    // it can be a child class or the Adapter (or not)
    private static class ViewHolder {
        TextView nameTxt;
        TextView cityTxt;
        TextView phoneTxt;
    }

    // get our view
    // aka our location_row_item.xml
    // set some data
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Location loc = getItem(position);
        ViewHolder vh;

        if (convertView == null) {
            // make a holder for our xml data
            vh = new ViewHolder();
            // inflate our layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.location_row_item,
                    parent,
                    false);
            // find & select views from convertView to ViewHolder
            vh.nameTxt = (TextView) convertView.findViewById(R.id.loc_name);
            vh.cityTxt = (TextView) convertView.findViewById(R.id.loc_city);
            vh.phoneTxt = (TextView) convertView.findViewById(R.id.loc_phone);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        // find our context
        Context ctx = getContext();
        // update ViewHolder widget values
        vh.nameTxt.setText(loc.name);
        vh.cityTxt.setText(loc.city);
        vh.phoneTxt.setText(loc.phone);

        return convertView;

    }


}
