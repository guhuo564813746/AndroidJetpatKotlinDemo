package com.wawa.wawaandroid_ep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.BannerListQuery;
import com.wawa.baselib.utils.glide.GlideManager;
import com.wawa.wawaandroid_ep.R;

import java.util.List;


//或者使用其他三方框架，都是支持的，如：BRVAH
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<BannerListQuery.BannerList> bannerLists;
    private GlideManager glideManager;
    public ImageAdapter(Context context,List<BannerListQuery.BannerList> banners){
        this.context=context;
        this.bannerLists=banners;
        this.glideManager=new GlideManager();
        glideManager.initGlide(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.home_banner_item_lay,parent,false);
        ImageViewHolder imageViewHolder=new ImageViewHolder(view);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder imageViewHolder= (ImageViewHolder) holder;
//        glideManager.displayCornerImg(bannerLists.get(position).pictureUrl(),imageViewHolder.imBanner,0.1f);
        glideManager.displayImg(bannerLists.get(position).pictureUrl(),imageViewHolder.imBanner);
    }

    @Override
    public int getItemCount() {
        return bannerLists==null?0:bannerLists.size();
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imBanner;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imBanner=itemView.findViewById(R.id.im_item_banner);
        }
    }
}