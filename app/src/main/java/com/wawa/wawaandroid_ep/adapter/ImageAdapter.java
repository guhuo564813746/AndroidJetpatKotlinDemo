package com.wawa.wawaandroid_ep.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.BannerListQuery;
import com.robotwar.app.R;
import com.wawa.baselib.utils.glide.GlideManager;
import com.wawa.baselib.utils.glide.loader.ImageLoader;
import com.wawa.baselib.utils.glide.utils.ImageUtil;
import com.wawa.wawaandroid_ep.activity.web.WebActivity;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


//或者使用其他三方框架，都是支持的，如：BRVAH
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<BannerListQuery.BannerList> bannerLists;
    public ImageAdapter(Context context,List<BannerListQuery.BannerList> banners){
        this.context=context;
        this.bannerLists=banners;
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
        ImageLoader.with(context)
                .url(bannerLists.get(position).pictureUrl())
//                .placeHolder(R.mipmap.ic_launcher)
                .rectRoundCorner(ImageUtil.dip2px(5f), RoundedCornersTransformation.CornerType.ALL)
                .into(imageViewHolder.imBanner);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goWebPage(bannerLists.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return bannerLists==null?0:bannerLists.size();
    }

    public void goWebPage(BannerListQuery.BannerList bean){
        Intent intent=new Intent();
        intent.setClass(context, WebActivity.class);
        intent.putExtra(WebActivity.Companion.getWEB_TITLE(),bean.name());
        intent.putExtra(WebActivity.Companion.getWEB_URL(),bean.url());
        context.startActivity(intent);
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imBanner;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imBanner=itemView.findViewById(R.id.im_item_banner);
        }
    }
}