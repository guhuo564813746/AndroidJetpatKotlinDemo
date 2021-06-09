package com.wawa.wawaandroid_ep.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.robotwar.app.R;


/**
 * Created by cxf on 2017/8/8.
 */

public class DialogUitl {
    //第三方登录的时候用显示的dialog
    public static Dialog loginAuthDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_login_auth);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static Dialog confirmDialog(Context context, String title, String message, final Callback callback) {
        return confirmDialog(context, title, message, "", "", true, callback);
    }

    public static Dialog confirmDialog(Context context, String title, String message, boolean cancel, final Callback callback) {
        return confirmDialog(context, title, message, "", "", cancel, callback);
    }

    public static Dialog confirmDialog(Context context, String title, String message, String confirmText, String cancelText, boolean cancel, final Callback callback) {
        final Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.setContentView(R.layout.dialog_confirm);
        dialog.setCancelable(cancel);
        dialog.setCanceledOnTouchOutside(cancel);
        TextView titleView = (TextView) dialog.findViewById(R.id.title);
        titleView.setText(title);
        TextView content = (TextView) dialog.findViewById(R.id.content);
        content.setText(message);
        TextView cancelBtn = (TextView) dialog.findViewById(R.id.cancel_btn);
        if (!"".equals(cancelText)) {
            cancelBtn.setText(cancelText);
        }
        TextView confirmBtn = (TextView) dialog.findViewById(R.id.confirm_btn);
        if (!"".equals(confirmText)) {
            confirmBtn.setText(confirmText);
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cancel_btn:
                        if (callback != null) {
                            callback.cancel(dialog);
                        }
                        break;
                    case R.id.confirm_btn:
                        if (callback != null) {
                            callback.confirm(dialog);
                        }
                        break;
                }
            }
        };
        cancelBtn.setOnClickListener(listener);
        confirmBtn.setOnClickListener(listener);
        return dialog;
    }

    public static Dialog confirmDialog(Context context, String title, SpannableString message, String confirmText, String cancelText, boolean cancel, final Callback callback) {
        final Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.setContentView(R.layout.dialog_confirm);
        dialog.setCancelable(cancel);
        dialog.setCanceledOnTouchOutside(cancel);
        TextView titleView = (TextView) dialog.findViewById(R.id.title);
        titleView.setText(title);
        TextView content = (TextView) dialog.findViewById(R.id.content);
        content.setMovementMethod(LinkMovementMethod.getInstance());
        content.setText(message);
        TextView cancelBtn = (TextView) dialog.findViewById(R.id.cancel_btn);
        if (!"".equals(cancelText)) {
            cancelBtn.setText(cancelText);
        }
        TextView confirmBtn = (TextView) dialog.findViewById(R.id.confirm_btn);
        if (!"".equals(confirmText)) {
            confirmBtn.setText(confirmText);
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cancel_btn:
                        if (callback != null) {
                            callback.cancel(dialog);
                        }
                        break;
                    case R.id.confirm_btn:
                        if (callback != null) {
                            callback.confirm(dialog);
                        }
                        break;
                }
            }
        };
        cancelBtn.setOnClickListener(listener);
        confirmBtn.setOnClickListener(listener);
        return dialog;
    }

    public static Dialog inputDialog(Context context, String title, final Callback3 callback) {
        return inputDialog(context, title, "", "", callback);
    }

    public static Dialog inputDialog(Context context, String title, String confirmText, String cancelText, final Callback3 callback) {
        final Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.setContentView(R.layout.dialog_input);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView titleView = (TextView) dialog.findViewById(R.id.title);
        titleView.setText(title);
        final EditText input = (EditText) dialog.findViewById(R.id.input);
        TextView cancelBtn = (TextView) dialog.findViewById(R.id.cancel_btn);
        if (!"".equals(cancelText)) {
            cancelBtn.setText(cancelText);
        }
        TextView confirmBtn = (TextView) dialog.findViewById(R.id.confirm_btn);
        if (!"".equals(confirmText)) {
            confirmBtn.setText(confirmText);
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cancel_btn:
                        dialog.dismiss();
                        break;
                    case R.id.confirm_btn:
                        if (callback != null) {
                            String text = input.getText().toString();
                            callback.confirm(dialog, text);
                        }
                        break;
                }
            }
        };
        cancelBtn.setOnClickListener(listener);
        confirmBtn.setOnClickListener(listener);
        return dialog;
    }

    public static Dialog messageDialog(Context context, String title, String message, String confirmText, final Callback2 callback) {
        final Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.setContentView(R.layout.dialog_message);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView titleView = (TextView) dialog.findViewById(R.id.title);
        titleView.setText(title);
        TextView content = (TextView) dialog.findViewById(R.id.content);
        content.setText(message);
        TextView confirmBtn = (TextView) dialog.findViewById(R.id.confirm_btn);
        if (!"".equals(confirmText)) {
            confirmBtn.setText(confirmText);
        } else {
            confirmBtn.setVisibility(View.GONE);
        }
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback != null) {
                    callback.confirm(dialog);
                }
            }
        });
        return dialog;
    }

    public static Dialog messageDialog(Context context, String title, String message, final Callback2 callback) {
        return messageDialog(context, title, message, "", callback);
    }

    public static Dialog postPayDialog(Context context, String content, final Runnable runnable) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_confirm_2);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        TextView contentText = (TextView) dialog.findViewById(R.id.content);
        contentText.setText(content);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (v.getId() == R.id.btn_other) {
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            }
        };
        dialog.findViewById(R.id.btn_other).setOnClickListener(listener);
        dialog.findViewById(R.id.btn_confirm).setOnClickListener(listener);
        return dialog;
    }


    /**
     * 用于网络请求等耗时操作的LoadingDialog
     */
    public static Dialog loadingDialog(Context context, String text) {
        Dialog dialog = new Dialog(context, R.style.dialogLoading);
        dialog.setContentView(R.layout.dialog_system_loading);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (!"".equals(text)) {
            TextView titleView = (TextView) dialog.findViewById(R.id.text);
            titleView.setText(text);
        }
        return dialog;
    }

    public static Dialog loadBigDialog(Context context, String text) {
        Dialog dialog = new Dialog(context, R.style.dialogLoading);
//        View view= LayoutInflater.from(context).inflate(R.layout.dialog_system_loading,null);
//        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
//        params.height=DpUtil.dp2px(140);
//        params.width=DpUtil.dp2px(140);
//        view.setLayoutParams(params);
        dialog.setContentView(R.layout.dialog_bigload_lay);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (!"".equals(text)) {
            TextView titleView = (TextView) dialog.findViewById(R.id.text);
            titleView.setText(text);
        }
        return dialog;
    }


    public static Dialog loadingDialog(Context context) {
        return loadingDialog(context, "");
    }

    public interface Callback {
        void confirm(Dialog dialog);

        void cancel(Dialog dialog);
    }

    public interface Callback2 {
        void confirm(Dialog dialog);
    }

    public interface Callback3 {
        void confirm(Dialog dialog, String text);
    }



}
