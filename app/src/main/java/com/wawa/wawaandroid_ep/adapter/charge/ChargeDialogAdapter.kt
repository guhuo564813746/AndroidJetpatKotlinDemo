package com.wawa.wawaandroid_ep.adapter.charge

import android.content.Context
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ChargeItemListQuery
import com.blankj.utilcode.util.ToastUtils
import com.robotwar.app.R
import com.wawa.wawaandroid_ep.pay.PayManager
import com.wawa.wawaandroid_ep.MainActivity
import com.wawa.wawaandroid_ep.fragment.ChargeFragment
import java.util.*
import kotlin.collections.ArrayList

class ChargeDialogAdapter(context: Context,private val payManager: PayManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var chargeList: List<ChargeItemListQuery.ChargeItemList> = ArrayList()
    private val layoutInflater: LayoutInflater
    private val mContext: Context
    //item 的显示类型，1为熊猫币，2为砖石
    var listType: Int = ChargeFragment.GOODS_TYPE_COIN
    init {
        mContext = context
        layoutInflater = LayoutInflater.from(mContext)
    }

     override fun onCreateViewHolder( viewGroup: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val contentView: View = layoutInflater.inflate(R.layout.paydialog_item, viewGroup, false)
        return ChargeDialogVH(contentView)
    }

    override fun onBindViewHolder(@NonNull viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ChargeDialogVH
        val isFirst: Int = 0
        //1-普通，2-周卡，3-月卡
        val cardType: Int = 1
        val promotion: Int = 0
        if (listType == ChargeFragment.GOODS_TYPE_COIN) {
            //显示熊猫币的item
            //赠送的币
            val getGive: Int = 0
            if (isFirst == 1) {
                //首充
                holder.im_isactive.setVisibility(View.GONE)
                holder.view_active_bottom.setVisibility(View.GONE)
                val firstDr: Drawable? = ResourcesCompat.getDrawable(
                    mContext.getResources(),
                    R.mipmap.paydialog_firstpay_bg,
                    null
                )
                if (/*chargeList[position].getLimit() === 1*/false) {
                    firstDr?.mutate()
                    val cMatrix = ColorMatrix()
                    cMatrix.setSaturation(0f)
                    val colorFilter = ColorMatrixColorFilter(cMatrix)
                    firstDr?.setColorFilter(colorFilter)
                } else {
                }
                holder.rl_paydialog_item.setBackground(firstDr)
                holder.tv_big_pay.setVisibility(View.VISIBLE)
                holder.tv_big_paytips.setVisibility(View.VISIBLE)
                holder.tv_pay_card.setVisibility(View.GONE)
                holder.tv_smallpay.setVisibility(View.GONE)
                holder.tv_smallpay_tips.setVisibility(View.GONE)
                holder.tv_big_pay.setTextColor(Color.parseColor("#C96322"))
                holder.tv_big_paytips.setTextColor(Color.parseColor("#C96322"))
                holder.tv_big_pay.setText(
                    chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.name()
                )
                //                holder.tv_smallpay.setText(chargeList.get(position).getCoin()+"币");
                if (getGive > 0) {
                    holder.tv_big_paytips.setText(""
                        /*String.format(
                            WordUtil.getString(R.string.give_coin),
                            getGive.toString() + "",
                            App.getInstance().getConfigBean().getName_coin()
                        )*/
                    )
                } else {
                    holder.tv_big_paytips.setText("")
                }
            } else {
                if (promotion == 1) {
                    holder.im_isactive.setVisibility(View.VISIBLE)
                    holder.view_active_bottom.setVisibility(View.VISIBLE)
                    val promotionDr: Drawable? = ResourcesCompat.getDrawable(
                        mContext.getResources(),
                        R.drawable.pay_active_bg,
                        null
                    )
                    if (/*chargeList[position].getLimit() === 1*/false) {
                        //灰度化
                        promotionDr?.mutate()
                        val cMatrix = ColorMatrix()
                        cMatrix.setSaturation(0f)
                        val colorFilter = ColorMatrixColorFilter(cMatrix)
                        promotionDr?.setColorFilter(colorFilter)
                    } else {
                    }
                    holder.rl_paydialog_item.setBackground(promotionDr)
                    holder.tv_big_pay.setVisibility(View.VISIBLE)
                    holder.tv_big_paytips.setVisibility(View.VISIBLE)
                    holder.tv_pay_card.setVisibility(View.GONE)
                    holder.tv_smallpay.setVisibility(View.GONE)
                    holder.tv_smallpay_tips.setVisibility(View.GONE)
                    holder.tv_big_pay.setTextColor(Color.parseColor("#C96322"))
                    holder.tv_big_paytips.setTextColor(Color.parseColor("#C96322"))
                    holder.tv_big_pay.setText(
                        chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.name()
                    )
                    //                holder.tv_smallpay.setText(chargeList.get(position).getCoin()+"币");
                    if (getGive > 0) {
                        holder.tv_big_paytips.setText(""
                            /*String.format(
                                WordUtil.getString(R.string.give_coin),
                                getGive.toString() + "",
                                App.getInstance().getConfigBean().getName_coin()
                            )*/
                        )
                    } else {
                        holder.tv_big_paytips.setText("")
                    }
                } else {
                    if (cardType == 1) {
                        holder.im_isactive.setVisibility(View.GONE)
                        holder.view_active_bottom.setVisibility(View.GONE)
                        val normalDr: Drawable? = ResourcesCompat.getDrawable(
                            mContext.getResources(),
                            R.mipmap.pay_dialog_normal_bg,
                            null
                        )
                        if (/*chargeList[position].getLimit() === 1*/false) {
                            //灰度化
                            normalDr?.mutate()
                            val cMatrix = ColorMatrix()
                            cMatrix.setSaturation(0f)
                            val colorFilter =
                                ColorMatrixColorFilter(cMatrix)
                            normalDr?.setColorFilter(colorFilter)
                        } else {
                        }
                        holder.rl_paydialog_item.setBackground(normalDr)
                        holder.tv_big_pay.setVisibility(View.VISIBLE)
                        holder.tv_big_paytips.setVisibility(View.VISIBLE)
                        holder.tv_pay_card.setVisibility(View.GONE)
                        holder.tv_smallpay.setVisibility(View.GONE)
                        holder.tv_smallpay_tips.setVisibility(View.GONE)
                        holder.tv_big_pay.setTextColor(Color.parseColor("#333333"))
                        holder.tv_big_paytips.setTextColor(Color.parseColor("#333333"))
                        holder.tv_big_pay.setText(
                            chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.name()
                        )
                        //                holder.tv_smallpay.setText(chargeList.get(position).getCoin()+"币");
                        if (getGive > 0) {
                            holder.tv_big_paytips.setText(""
                               /* String.format(
                                    WordUtil.getString(
                                        R.string.give_coin
                                    ),
                                    getGive.toString() + "",
                                    App.getInstance().getConfigBean().getName_coin()
                                )*/
                            )
                        } else {
                            holder.tv_big_paytips.setText("")
                        }
                    } else if (cardType == 2) {
                        holder.im_isactive.setVisibility(View.GONE)
                        holder.view_active_bottom.setVisibility(View.GONE)
                        val weekcardDr: Drawable? = ResourcesCompat.getDrawable(
                            mContext.getResources(),
                            R.mipmap.paydialog_weekcard_bg,
                            null
                        )
                        if (/*chargeList[position].getLimit() === 1*/false) {
                            //灰度化
                            weekcardDr?.mutate()
                            val cMatrix = ColorMatrix()
                            cMatrix.setSaturation(0f)
                            val colorFilter =
                                ColorMatrixColorFilter(cMatrix)
                            weekcardDr?.setColorFilter(colorFilter)
                        } else {
                        }
                        holder.rl_paydialog_item.setBackground(weekcardDr)
                        holder.tv_pay_card.setVisibility(View.VISIBLE)
                        holder.tv_pay_card.setText(chargeList[position].name())
                        holder.tv_big_pay.setVisibility(View.GONE)
                        holder.tv_big_paytips.setVisibility(View.GONE)
                        holder.tv_smallpay.setVisibility(View.VISIBLE)
                        holder.tv_smallpay_tips.setVisibility(View.VISIBLE)
                        holder.tv_smallpay.setText(
                            chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.name()
                        )
                        holder.tv_smallpay.setTextColor(Color.parseColor("#C97535"))
                        holder.tv_smallpay_tips.setTextColor(Color.parseColor("#C97535"))
                        if (getGive > 0) {
                            holder.tv_smallpay_tips.setText(""
                                /*String.format(
                                    mContext.getString(
                                        R.string.FREE_EVERYDAY_TIPS
                                    ),
                                    getGive.toString() + "",
                                    App.getInstance().getConfigBean().getName_coin()
                                )*/
                            )
                        } else {
                            holder.tv_smallpay_tips.setText("")
                        }
                        //                        holder.tv_smallpay_tips.setText("");
                    } else if (cardType == 3) {
                        holder.im_isactive.setVisibility(View.GONE)
                        holder.view_active_bottom.setVisibility(View.GONE)
                        val monDr: Drawable? = ResourcesCompat.getDrawable(
                            mContext.getResources(),
                            R.mipmap.paydialog_moncard_bg,
                            null
                        )
                        //                        Drawable wrappedDrawable = DrawableCompat.wrap(monDr);
//                        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(Color.parseColor("#B2000000")));
                        if (/*chargeList[position].getLimit() === 1*/false) {
                            //灰度化
                            monDr?.mutate()
                            val cMatrix = ColorMatrix()
                            cMatrix.setSaturation(0f)
                            val colorFilter =
                                ColorMatrixColorFilter(cMatrix)
                            monDr?.setColorFilter(colorFilter)
                        } else {
                        }
                        holder.rl_paydialog_item.setBackground(monDr)
                        holder.tv_pay_card.setVisibility(View.VISIBLE)
                        holder.tv_pay_card.setText(chargeList[position].name())
                        holder.tv_big_pay.setVisibility(View.GONE)
                        holder.tv_big_paytips.setVisibility(View.GONE)
                        holder.tv_smallpay.setVisibility(View.VISIBLE)
                        holder.tv_smallpay_tips.setVisibility(View.VISIBLE)
                        holder.tv_smallpay.setTextColor(Color.parseColor("#5D37BB"))
                        holder.tv_smallpay_tips.setTextColor(Color.parseColor("#5D37BB"))
                        holder.tv_smallpay.setText(
                            chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.name()
                        )
                        if (getGive > 0) {
                            holder.tv_smallpay_tips.setText(""
                                /*String.format(
                                    mContext.getString(
                                        R.string.FREE_EVERYDAY_TIPS
                                    ),
                                    getGive.toString() + "",
                                    App.getInstance().getConfigBean().getName_coin()
                                )*/
                            )
                        } else {
                            holder.tv_smallpay_tips.setText("")
                        }
                        //                        holder.tv_smallpay_tips.setText("");
                    }
                }
            }
            // 英文的系统显示美金
            if (Locale.getDefault().getLanguage().startsWith("zh")) {
                holder.tv_paycount.setText(
                    chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.price().toString()
                )
            } else {
                holder.tv_paycount.setText(
                    chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.price().toString()
                )
            }
           /* if (App.getInstance().getAppChannel().equals("coinpusher_google")) {
                holder.tv_paycount.setText(
                    String.format(
                        "$ %s",
                        chargeList[position].usd
                    )
                )
            }*/
        } else if (listType == ChargeFragment.GOODS_TYPE_DIAMOND) {
            //显示砖石的item
            val diamenGive: Int = 0
            val price: Double? = chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.price()
//            val originPirce: Float = chargeList[position].getOrigin_price().toFloat()
            if (isFirst == 1) {
                //首充
                holder.im_isactive.setVisibility(View.GONE)
                holder.view_active_bottom.setVisibility(View.GONE)
                val firstDr: Drawable? = ResourcesCompat.getDrawable(
                    mContext.getResources(),
                    R.mipmap.paydialog_firstpay_bg,
                    null
                )
                if (/*chargeList[position].getLimit() === 1*/false) {
                    setDrawableGray(firstDr)
                    //灰度化
//                        monDr.mutate();
//                        ColorMatrix cMatrix  = new ColorMatrix();
//                        cMatrix.setSaturation(0);
//                        ColorMatrixColorFilter colorFilter  = new ColorMatrixColorFilter(cMatrix);
//                        monDr.setColorFilter(colorFilter);
                } else {
                }
                holder.rl_paydialog_item.setBackground(firstDr)
                holder.tv_big_pay.setVisibility(View.VISIBLE)
                holder.tv_big_paytips.setVisibility(View.VISIBLE)
                holder.tv_pay_card.setVisibility(View.GONE)
                holder.tv_smallpay.setVisibility(View.GONE)
                holder.tv_smallpay_tips.setVisibility(View.GONE)
                holder.tv_big_pay.setTextColor(Color.parseColor("#C96322"))
                holder.tv_big_paytips.setTextColor(Color.parseColor("#C96322"))
                holder.tv_big_pay.setText(chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.name())
                //                holder.tv_smallpay.setText(chargeList.get(position).getCoin()+"币");
                if (diamenGive > 0) {
                    holder.tv_big_paytips.setText(""
                        /*String.format(
                            WordUtil.getString(R.string.give_coin),
                            diamenGive.toString() + "",
                            WordUtil.getString(R.string.DIAMOND_TIPS)
                        )*/
                    )
                } else {
                    holder.tv_big_paytips.setText("")
                }
            } else {
                if (promotion == 1) {
                    holder.im_isactive.setVisibility(View.VISIBLE)
                    holder.view_active_bottom.setVisibility(View.VISIBLE)
                    val promotionDr: Drawable? = ResourcesCompat.getDrawable(
                        mContext.getResources(),
                        R.drawable.pay_active_bg,
                        null
                    )
                    if (false/*chargeList[position].getLimit() === 1*/) {
//                        setDrawableGray();
                        setDrawableGray(promotionDr)
                    } else {
                    }
                    holder.rl_paydialog_item.setBackground(promotionDr)
                    holder.tv_big_pay.setVisibility(View.VISIBLE)
                    holder.tv_big_paytips.setVisibility(View.VISIBLE)
                    holder.tv_smallpay.setVisibility(View.GONE)
                    holder.tv_smallpay_tips.setVisibility(View.GONE)
                    holder.tv_big_pay.setTextColor(Color.parseColor("#C96322"))
                    holder.tv_big_paytips.setTextColor(Color.parseColor("#C96322"))
                    holder.tv_big_pay.setText(
                        chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.name()
                    )
                    //                holder.tv_smallpay.setText(chargeList.get(position).getCoin()+"币");
                    if (diamenGive > 0) {
                        holder.tv_big_paytips.setText(""
                            /*String.format(
                                WordUtil.getString(R.string.give_coin),
                                diamenGive.toString() + "",
                                WordUtil.getString(R.string.DIAMOND_TIPS)
                            )*/
                        )
                    } else {
                        holder.tv_big_paytips.setText("")
                    }
                } else {
                    if (cardType == 1) {
                        holder.im_isactive.setVisibility(View.GONE)
                        holder.view_active_bottom.setVisibility(View.GONE)
                        val normalDr: Drawable? = ResourcesCompat.getDrawable(
                            mContext.getResources(),
                            R.mipmap.pay_dialog_normal_bg,
                            null
                        )
                        if (/*chargeList[position].getLimit() === 1*/false) {
                            setDrawableGray(normalDr)
                        } else {
                        }
                        holder.rl_paydialog_item.setBackground(normalDr)
                        holder.tv_big_pay.setVisibility(View.VISIBLE)
                        holder.tv_big_paytips.setVisibility(View.VISIBLE)
                        holder.tv_smallpay.setVisibility(View.GONE)
                        holder.tv_smallpay_tips.setVisibility(View.GONE)
                        holder.tv_big_pay.setTextColor(Color.parseColor("#333333"))
                        holder.tv_big_paytips.setTextColor(Color.parseColor("#333333"))
                        holder.tv_big_pay.setText(
                            chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.name()
                        )
                        //                holder.tv_smallpay.setText(chargeList.get(position).getCoin()+"币");
                        if (diamenGive > 0) {
                            holder.tv_big_paytips.setText(""
                                /*String.format(
                                    WordUtil.getString(
                                        R.string.give_coin
                                    ),
                                    diamenGive.toString() + "",
                                    WordUtil.getString(R.string.DIAMOND_TIPS)
                                )*/
                            )
                        } else {
                            holder.tv_big_paytips.setText("")
                        }
                    } else if (cardType == 2) {
                        holder.im_isactive.setVisibility(View.GONE)
                        holder.view_active_bottom.setVisibility(View.GONE)
                        val weekcardDr: Drawable? = ResourcesCompat.getDrawable(
                            mContext.getResources(),
                            R.mipmap.paydialog_weekcard_bg,
                            null
                        )
                        if (/*chargeList[position].getLimit() === 1*/false) {
                            setDrawableGray(weekcardDr)
                        } else {
                        }
                        holder.rl_paydialog_item.setBackground(weekcardDr)
                        holder.tv_pay_card.setVisibility(View.VISIBLE)
                        holder.tv_pay_card.setText(chargeList[position].name())
                        holder.tv_big_pay.setVisibility(View.GONE)
                        holder.tv_big_paytips.setVisibility(View.GONE)
                        holder.tv_smallpay.setVisibility(View.VISIBLE)
                        holder.tv_smallpay_tips.setVisibility(View.VISIBLE)
                        holder.tv_smallpay.setTextColor(Color.parseColor("#C97535"))
                        holder.tv_smallpay_tips.setTextColor(Color.parseColor("#C97535"))
                        holder.tv_smallpay.setText(
                            chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.name()
                        )
                        if (diamenGive > 0) {
                            holder.tv_smallpay_tips.setText(""
                                /*String.format(
                                    mContext.getString(
                                        R.string.FREE_EVERYDAY_TIPS
                                    ),
                                    diamenGive.toString() + "",
                                    WordUtil.getString(R.string.DIAMOND_TIPS)
                                )*/
                            )
                        } else {
                            holder.tv_smallpay_tips.setText("")
                        }
                    } else if (cardType == 3) {
                        holder.im_isactive.setVisibility(View.GONE)
                        holder.view_active_bottom.setVisibility(View.GONE)
                        val monDr: Drawable? = ResourcesCompat.getDrawable(
                            mContext.getResources(),
                            R.mipmap.paydialog_moncard_bg,
                            null
                        )
                        if (false) {
                            setDrawableGray(monDr)
                        } else {
                        }
                        holder.rl_paydialog_item.setBackground(monDr)
                        holder.tv_pay_card.setVisibility(View.VISIBLE)
                        holder.tv_pay_card.setText(chargeList[position].name())
                        holder.tv_big_pay.setVisibility(View.GONE)
                        holder.tv_big_paytips.setVisibility(View.GONE)
                        holder.tv_smallpay.setVisibility(View.VISIBLE)
                        holder.tv_smallpay_tips.setVisibility(View.VISIBLE)
                        holder.tv_smallpay.setTextColor(Color.parseColor("#5D37BB"))
                        holder.tv_smallpay_tips.setTextColor(Color.parseColor("#5D37BB"))
                        holder.tv_smallpay.setText(
                            chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.name()
                        )
                        if (diamenGive > 0) {
                            holder.tv_smallpay_tips.setText(""
                               /* String.format(
                                    mContext.getString(
                                        R.string.FREE_EVERYDAY_TIPS
                                    ),
                                    diamenGive.toString() + "",
                                    WordUtil.getString(R.string.DIAMOND_TIPS)
                                )*/
                            )
                        } else {
                            holder.tv_smallpay_tips.setText("")
                        }
                    }
                }
            }
            // 英文的系统显示美金
            if (Locale.getDefault().getLanguage().startsWith("zh")) {
                holder.tv_paycount.setText(
                    chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.price().toString()
                    /*String.format(
                        WordUtil.getString(R.string.price),
                        chargeList[position].getMoney()
                    )*/
                )
            } else {
                holder.tv_paycount.setText(
                    chargeList[position].goods()?.fragments()?.chargeGoodsFields()?.price().toString()
                )
            }
            /*if (App.getInstance().getAppChannel().equals("coinpusher_google")) {
                holder.tv_paycount.setText(
                    String.format(
                        "$ %s",
                        chargeList[position].usd
                    )
                )
            }*/
        }
    }

    private fun setDrawableGray(drawable: Drawable?) {
        //灰度化
        drawable?.mutate()
        val cMatrix = ColorMatrix()
        cMatrix.setSaturation(0f)
        val colorFilter = ColorMatrixColorFilter(cMatrix)
        drawable?.setColorFilter(colorFilter)
    }

    fun setData(list: List<ChargeItemListQuery.ChargeItemList>) {
        chargeList = list
        notifyDataSetChanged()
    }

    inner class ChargeDialogVH(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var rl_paydialog_item: RelativeLayout
        var tv_big_pay: TextView
        var tv_big_paytips: TextView
        var tv_paycount: TextView
        var tv_pay_card: TextView
        var tv_smallpay: TextView
        var tv_smallpay_tips: TextView
        var ll_paydialogitem: RelativeLayout
        var im_isactive: ImageView
        var view_active_bottom: View

        init {
            ll_paydialogitem = itemView.findViewById(R.id.ll_paydialogitem)
            im_isactive = itemView.findViewById(R.id.im_isactive)
            view_active_bottom = itemView.findViewById(R.id.view_active_bottom)
            rl_paydialog_item = itemView.findViewById(R.id.rl_paydialog_item)
            tv_big_pay = itemView.findViewById(R.id.tv_big_pay)
            tv_big_paytips = itemView.findViewById(R.id.tv_big_paytips)
            tv_paycount = itemView.findViewById(R.id.tv_paycount)
            tv_pay_card = itemView.findViewById(R.id.tv_pay_card)
            tv_smallpay = itemView.findViewById(R.id.tv_smallpay)
            tv_smallpay_tips = itemView.findViewById(R.id.tv_smallpay_tips)
            rl_paydialog_item.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    /*if (chargeList[getAdapterPosition()].getLimit() === 1) {
                        ToastUtil.show(chargeList[getAdapterPosition()].getLimitDesc())
                        return
                    }*/
                    if (listType == ChargeFragment.GOODS_TYPE_COIN) {
                        //跳转充值熊猫币

                    }
                    if (listType == ChargeFragment.GOODS_TYPE_DIAMOND) {
                        //跳转充值砖石

                    }
                    chargeList.get(adapterPosition).goods()?.let {
                        chargeList.get(adapterPosition).chargeItemId()?.let { it1 ->
                            payManager.showPayTypeDialog(
                                it, it1,
                                object: PayManager.PayCallback{
                                    override fun paySuccess(payType: Int) {
                                        (mContext as MainActivity).setUpDataSource()
                                    }

                                    override fun payErr(msg: String) {
                                        ToastUtils.showShort(msg)
                                    }
                                })
                        }
                    }
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return chargeList.size
    }


}