package example.com.projectone;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import example.com.projectone.util.ColorUtil;

/**
 * Created by Abhinav Ravi on 23/10/16.
 */
public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> moviePosters = new ArrayList<>();

    //Constructor
    public GridViewAdapter(Context mContext, List<String> moviePosters) {
        this.mContext = mContext;
        this.moviePosters = moviePosters;
    }

    @Override
    public int getCount() {
        return moviePosters.size();
    }

    @Override
    public Object getItem(int i) {
        return moviePosters.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageView imageView;
        if(row == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.grid_item_layout, parent, false);

        }
        imageView = (ImageView) row.findViewById(R.id.grid_image);

        Picasso.with(mContext)
                .load(moviePosters.get(position))
                .placeholder(new ColorUtil().getRandomDrawbleColor())
                .into(imageView);
        return row;
    }

}
