package example.com.projectone;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
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

    class MyViewHolder{
        ImageView imageView;
        MyViewHolder(View v){

            //If we use ViewHolder patten, then findViewById is called exactly once when constructor is used.
            imageView = (ImageView) v.findViewById(R.id.grid_image);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MyViewHolder myViewHolder = null;
        // create the new row and holder for the first time.
        if(row == null){
            //inflates only for the first time.
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.grid_item_layout, parent, false);
            myViewHolder = new MyViewHolder(row);
            row.setTag(myViewHolder);
            Log.d("Abhinav", "Creating the row");
        }
        //Now whenever i'm recycling i'm using the same holder
        else{
            myViewHolder = (MyViewHolder)row.getTag();
            Log.d("Abhinav", "Recycling the row");
        }

        Picasso.with(mContext)
                .load(moviePosters.get(position))
                .placeholder(new ColorUtil().getRandomDrawbleColor())
                .into(myViewHolder.imageView);
        return row;
    }

}
