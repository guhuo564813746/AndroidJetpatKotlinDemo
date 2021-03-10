package com.wawa.wawaandroid_ep.utils.loginutils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.robotwar.app.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class PhoneLoginDialog extends Dialog {
    private Disposable mCountdownDisposable;
    private View.OnClickListener wxBtnListener;
    private ConfirmListener confirmListener;
    private PhoneLoginDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void onDestroy() {
        wxBtnListener = null;
        confirmListener = null;
        if (mCountdownDisposable != null){
            mCountdownDisposable.dispose();
            mCountdownDisposable = null;
        }
    }

    public static class Builder implements View.OnClickListener, TextWatcher {
        private static final int MAX_SECONDS = 300;
        private Context mContext;
        private TextView wxBtn;
        private EditText phonetInput;
        private EditText codeInput;
        private TextView sendCodeBtn;
        private TextView mConfirm;
        private PhoneLoginDialog mDialog;


        public Builder(Context context) {
            mContext = context;
            mDialog = new PhoneLoginDialog(context, R.style.dialog);
            mDialog.setContentView(R.layout.dialog_phone_login);
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(true);
            wxBtn = mDialog.findViewById(R.id.btn_wx);
            wxBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            /*if (StringUtils.isZhRCN(mContext)){

            }else {
                wxBtn.setText(mContext.getString(R.string.facebook_logintips));
            }*/
            wxBtn.setText(mContext.getString(R.string.LOGIN_WX_RECOMMEND));
            phonetInput = mDialog.findViewById(R.id.input_phone);
            codeInput = mDialog.findViewById(R.id.input_code);
            sendCodeBtn = mDialog.findViewById(R.id.btn_send_code);
            sendCodeBtn.setClickable(true);
            mConfirm = mDialog.findViewById(R.id.btn_confirm);
            phonetInput.addTextChangedListener(this);
            wxBtn.setOnClickListener(this);
            sendCodeBtn.setOnClickListener(this);
            mConfirm.setOnClickListener(this);
        }
        public Builder setWxBtnOnClickListener(View.OnClickListener listener){
            mDialog.wxBtnListener = listener;
            return this;
        }
        public Builder setConfirmOnClickListener(ConfirmListener listener){
            mDialog.confirmListener = listener;
            return this;
        }
        public PhoneLoginDialog create() {
            return mDialog;
        }

        public void onClick(View v){
            switch (v.getId()) {
                case R.id.btn_wx:
                    if (mDialog.wxBtnListener != null){
                        mDialog.wxBtnListener.onClick(v);
                    }
                    break;
                case R.id.btn_send_code:
                    getValidateCode();
                    break;
                case R.id.btn_confirm:
                    if (mDialog.confirmListener != null){
                        String phoneNum = phonetInput.getText().toString();
                        String code = codeInput.getText().toString();
                        mDialog.confirmListener.onClick(v, phoneNum, code);
                    }
                    break;
            }
        }

        public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4){

        }

        public void onTextChanged(CharSequence var1, int var2, int var3, int var4){
            if (mDialog.mCountdownDisposable == null) {
                clearCountdown();
            }
        }

        public void afterTextChanged(Editable var1){

        }

        /**
         * 获取验证码
         */
        private void getValidateCode() {
            String phoneNum = phonetInput.getText().toString();
            if ("".equals(phoneNum) || phoneNum .length() != 11) {
                Toast.makeText(mContext,mContext.getString(R.string.please_input_phone_num),Toast.LENGTH_SHORT).show();
                return;
            }
            mDialog.mCountdownDisposable = Observable
                    .interval(0, 1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(count -> {
                        if (count < MAX_SECONDS) {
                            setCountdowning(MAX_SECONDS - count.intValue());
                        } else {
                            clearCountdown();
                        }
                    });
            sendCodeBtn.setClickable(false);
//            HttpUtil.getValidateCode(phoneNum, 86, getValidateCodeCallback);
        }

        /*private HttpCallback getValidateCodeCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    ToastUtil.show(getString(R.string.validate_code_send_success));
                } else {
                    ToastUtil.showLong(msg, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, BarUtils.getActionBarHeight());
                }
                switch (code) {
                    case 1001:
                    case 1004:
                    case 1005:
                        clearCountdown();
                        break;
                }
            }
        };*/

        private void clearCountdown(){
            sendCodeBtn.setText(R.string.LOGIN_CLICK_CATPCHA);
            String phoneNum = phonetInput.getText().toString();
            if (phoneNum.length() == 11) {
                sendCodeBtn.setBackgroundResource(R.mipmap.bg_phone_login_blue);
                sendCodeBtn.setClickable(true);
                sendCodeBtn.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            } else {
                sendCodeBtn.setBackgroundResource(R.mipmap.bg_phone_login_gray);
                sendCodeBtn.setClickable(false);
                sendCodeBtn.setTextColor(ContextCompat.getColor(mContext, R.color.charge_gray));
            }
            if (mDialog.mCountdownDisposable != null) {
                mDialog.mCountdownDisposable.dispose();
                mDialog.mCountdownDisposable = null;
            }
        }

        private void setCountdowning(int seconds){
            sendCodeBtn.setText(String.format(mContext.getString(R.string.LOGIN_GETTING_CAPTCHA), seconds));
            sendCodeBtn.setClickable(false);
            sendCodeBtn.setBackgroundResource(R.mipmap.bg_phone_login_red_border);
            sendCodeBtn.setTextColor(ContextCompat.getColor(mContext, R.color.global));
        }
    }
    public interface ConfirmListener{
        void onClick(View v, String phoneNum, String code);
    }
}
