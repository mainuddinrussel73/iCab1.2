package com.example.mainuddin.icab12;

/**
 * Created by mainuddin on 5/29/2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Stack;


public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Album> albumList;
    private Stack<String> comment = new Stack<>();

    public Resources getResources() {
        return mContext.getResources();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView destination, rating,comments;
        public ImageView thumbnail, overflow;
        public ProgressBar pb;
        public MyViewHolder(View view) {
            super(view);
            destination = (TextView) view.findViewById(R.id.title);
            rating = (TextView) view.findViewById(R.id.count);
            comments = (TextView) view.findViewById(R.id.comment);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            pb = (ProgressBar) view.findViewById(R.id.Progress);
            //overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_cart, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.destination.setText(album.getDestination());
        holder.rating.setText(album.getRating() + " Stars");
        //comment.push(albumList.get(position).getComments());
        holder.comments.setText(album.getComments());
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.backcolor);
        holder.pb.setVisibility(View.VISIBLE);
        holder.pb.setMax(10);
        int i = Integer.decode(album.getRating());
        holder.pb.setProgress(i);
        holder.pb.setSecondaryProgress(i);
        holder.pb.setIndeterminate(false);
        holder.pb.setProgressDrawable(drawable);
        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

        //holder.overflow.setOnClickListener(new View.OnClickListener() {
           // @Override
          //  public void onClick(View view) {
             //   showPopupMenu(holder.overflow);
            //}
        //});
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    /*private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }
    */
    /**
     * Click listener for popup menu items
     */

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    //Toast.makeText(mContext,"nghgv", Toast.LENGTH_LONG).show();


                    return true;

                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
