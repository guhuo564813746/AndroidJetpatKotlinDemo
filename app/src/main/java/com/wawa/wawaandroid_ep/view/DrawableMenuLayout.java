package com.wawa.wawaandroid_ep.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.wawa.baselib.R;
import com.wawa.baselib.utils.logutils.LogUtils;

import java.util.List;

/**
 * 作者：create by 张金 on 2021/2/23 14:27
 * 邮箱：564813746@qq.com
 */
public class DrawableMenuLayout extends RelativeLayout {
    private static final String TAG="DrawableMenuLayout";
    private RecyclerView lvMenusetItem;
    private RelativeLayout btnMenuArrow;
    private List<DrawItemBean> drawItemBeans;
    private Context mContext;
    private OnItemClickListener onItemClickListener = null;
    private DrawMenuAdapter drawMenuAdapter=null;
    private int drawDistance=0;
    private boolean isShow=false;

    public DrawableMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public DrawableMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DrawableMenuLayout(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attr) {
        this.mContext = context;
        View view = inflate(context, R.layout.drawablemenu_lay, this);
        lvMenusetItem = view.findViewById(R.id.lv_menuset_item);
        btnMenuArrow = view.findViewById(R.id.btn_menu_arrow);
        lvMenusetItem.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        drawMenuAdapter=new DrawMenuAdapter();
        lvMenusetItem.setAdapter(drawMenuAdapter);
        btnMenuArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShow){
                    hide();
                }else {
                    show();
                }
            }
        });
    }

    public void show(){
        RelativeLayout layout = this;
//        AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.buyu_anim_menu_show);
        AnimationSet animationSet=new AnimationSet(true);
        animationSet.addAnimation(new TranslateAnimation(0,drawDistance,0f,0f));
        animationSet.setDuration(500);
        animationSet.setFillAfter(true);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation){
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                layout.setLayoutParams(layoutParams);
                layout.clearAnimation();
            }
        });
        this.startAnimation(animationSet);
        isShow = true;
    }

    public void hide(){
        RelativeLayout layout = this;
//        AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.buyu_anim_menu_hide);
        AnimationSet animationSet=new AnimationSet(true);
        animationSet.addAnimation(new TranslateAnimation(0,-drawDistance,0f,0f));
        animationSet.setDuration(500);
        animationSet.setFillAfter(true);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation){
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(-drawDistance, 0, 0, 0);
                layout.setLayoutParams(layoutParams);
                layout.clearAnimation();
            }
        });
        this.startAnimation(animationSet);
        isShow = false;
    }

    public List<DrawItemBean> getDrawItemBeans() {
        return drawItemBeans;
    }

    public void setDrawItemBeans(List<DrawItemBean> drawItemBeans) {
        this.drawItemBeans = drawItemBeans;
        drawMenuAdapter.notifyDataSetChanged();
        lvMenusetItem.post(new Runnable() {
            @Override
            public void run() {
                drawDistance=lvMenusetItem.getWidth();
                LogUtils.Companion.d(TAG,"drawDistance--"+drawDistance);
                RelativeLayout.LayoutParams layoutParams= (LayoutParams) getLayoutParams();
                if (isShow){
                    layoutParams.setMargins(0, 0, 0, 0);
                }else {
                    layoutParams.setMargins(-drawDistance, 0, 0, 0);
                }
                setLayoutParams(layoutParams);
            }
        });

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class DrawItemBean {
        private String itemTitle;
        private int itemImgSrc;

        public String getItemTitle() {
            return itemTitle;
        }

        public void setItemTitle(String itemTitle) {
            this.itemTitle = itemTitle;
        }

        public int getItemImgSrc() {
            return itemImgSrc;
        }

        public void setItemImgSrc(int itemImgSrc) {
            this.itemImgSrc = itemImgSrc;
        }
    }

    private class DrawMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.gamemenu_item_lay, parent, false);
            DrawMenuViewHolder holder = new DrawMenuViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((DrawMenuViewHolder) holder).imTag.setImageResource(drawItemBeans.get(position).getItemImgSrc());
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null){
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return drawItemBeans == null ? 0 : drawItemBeans.size();
        }

        private class DrawMenuViewHolder extends RecyclerView.ViewHolder {
            private ImageView imTag;

            public DrawMenuViewHolder(@NonNull View itemView) {
                super(itemView);
                imTag = itemView.findViewById(R.id.imTag);

            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

}
