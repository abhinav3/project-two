package example.com.projectone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import example.com.projectone.R;
import example.com.projectone.models.youTube.VideosResults;
import example.com.projectone.util.ColorUtil;

/**
 * Created by Abhinav Ravi on 12/02/17.
 */

public class CustomerTrailerAdapter extends RecyclerView.Adapter<CustomerTrailerAdapter.MyViewHolder> {


    private Context ctx;
    private VideosResults [] dataSet;
    private String youTubeThumbNailUrl = "http://img.youtube.com/vi/"; // I referred https://developers.google.com/youtube/2.0/developers_guide_protocol

    public CustomerTrailerAdapter( VideosResults[] data, Context ctx) {
        this.ctx = ctx;
        this.dataSet = data;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView trailerImage;
        TextView trailerName;
        Context ctx;
        private VideosResults [] dataSet;

        public MyViewHolder(View itemView, Context ctx, VideosResults[] dataSet) {
            super(itemView);
            this.dataSet = dataSet;
            this.ctx = ctx;
            itemView.setOnClickListener(this);
            this.trailerName = (TextView) itemView.findViewById(R.id.trailerName);
            this.trailerImage = (ImageView) itemView.findViewById(R.id.trailerImageView);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            String videoKey = dataSet[position].getKey();
            String videoUrl = "https://www.youtube.com/watch?v=" + videoKey;
            Log.d("Abhinav", "playing" + videoUrl);
            Intent openVideo = new Intent(Intent.ACTION_VIEW);
            // TODO : Ask why not beffering seamlessly wehn opening with other video players.
            // openVideo.setDataAndType(Uri.parse(videoUrl), "video/*");
            openVideo.setData(Uri.parse("vnd.youtube:" + videoKey));
            this.ctx.startActivity(openVideo);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_trailer_layout, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view, ctx, dataSet);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.trailerName;
        ImageView imageView = holder.trailerImage;

        textViewName.setText(dataSet[listPosition].getName());
        Log.d("Abhinav" , dataSet[listPosition].getId());
        Picasso.with(ctx)
                .load(youTubeThumbNailUrl + dataSet[listPosition].getKey() + "/mqdefault.jpg")
                .placeholder(new ColorUtil().getRandomDrawbleColor())
                .into(imageView);


    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }


}