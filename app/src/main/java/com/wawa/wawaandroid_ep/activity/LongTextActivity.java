package com.wawa.wawaandroid_ep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.robotwar.app.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * @ProjectName: wawa-android
 * @Package: com.coinhouse777.wawa.activity
 * @ClassName: LongTextActivity
 * @Description:
 * @Author: Juice
 * @CreateDate: 2019-07-11 17:23
 * @UpdateUser: Juice
 * @UpdateDate: 2019-07-11 17:23
 * @UpdateRemark: 说明
 * @Version: 1.0
 */
public class LongTextActivity extends AppCompatActivity {
    public static final String INTENT_TITLE = "BUNDLE_TITLE";
    public static final String INTENT_TEXT_FILE = "BUNDLE_TEXT_FILE";
    private static final String XMZZID="com.wowgotcha.wawa";
    private static final String TBDRID="com.dakashi.wawa";
    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_text);
        TextView titleView=findViewById(R.id.title);
        mTextView=findViewById(R.id.text_view);
        Intent intent = getIntent();
        String title = intent.getStringExtra(INTENT_TITLE);
        String textFile = null;
        String processName=getApplicationInfo().processName;
        textFile="long_text/privacy.txt";
        titleView.setText(title);

        try {
            InputStream inputStream = getAssets().open(textFile);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            for (String line; (line = r.readLine()) != null; ) {
                mTextView.append(line);
                mTextView.append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    public void back(View view){
        finish();
    }


}
