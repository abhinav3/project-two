package example.com.projectone.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import example.com.projectone.MainActivity;
import example.com.projectone.R;
import example.com.projectone.models.youTube.TrailerDataModel;
import example.com.projectone.models.youTube.VideosResults;
import example.com.projectone.util.ColorUtil;

/**
 * Created by Abhinav Ravi on 12/02/17.
 */

public class CustomerTrailerAdapter extends RecyclerView.Adapter<CustomerTrailerAdapter.MyViewHolder> {


    private Context mContext;
    private VideosResults [] dataSet;
    private String youTubeThumbNailUrl = "http://img.youtube.com/vi/"; // I referred https://developers.google.com/youtube/2.0/developers_guide_protocol

    public CustomerTrailerAdapter( VideosResults[] data) {
       // this.mContext = mContext;
        this.dataSet = data;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView trailerImage;
        TextView trailerName;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.trailerName = (TextView) itemView.findViewById(R.id.trailerName);
            this.trailerImage = (ImageView) itemView.findViewById(R.id.trailerImageView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.trailerName;
        ImageView imageView = holder.trailerImage;

        textViewName.setText(dataSet[listPosition].getName());
        Log.d("Abhinav" , dataSet[listPosition].getId());
        Picasso.with(mContext)
                .load(youTubeThumbNailUrl + dataSet[listPosition].getId() + "/mqdefault.jpg")
                .placeholder(new ColorUtil().getRandomDrawbleColor())
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }
}