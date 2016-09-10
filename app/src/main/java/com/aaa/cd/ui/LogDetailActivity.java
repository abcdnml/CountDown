package com.aaa.cd.ui;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.dao.LogDao;
import com.aaa.cd.model.LogItem;
import com.aaa.cd.util.Constants;

public class LogDetailActivity extends AppCompatActivity {

    int mode;

    TextView tv_content;
    ImageView iv_left;

    TextInputLayout til_title;
    TextInputLayout til_content;
    EditText et_title;
    EditText et_content;
    Button btn_submit;

    LogItem current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_detail);

        initTitle();
        initView();
        initData();

    }
    public void initData()
    {
        Intent intent= getIntent();
        mode=intent.getIntExtra(Constants.INTENT_KEY_LOG_MODE,Constants.INTENT_VALUE_LOG_MODE_ADD);

        if(mode==Constants.INTENT_VALUE_LOG_MODE_ADD){
            tv_content.setText(R.string.add_log);
        }else if(mode==Constants.INTENT_VALUE_LOG_MODE_EDIT){
            current=intent.getParcelableExtra(Constants.INTENT_KEY_LOG_CURRENT);
            tv_content.setText(R.string.edit_log);
            et_title.setText(current.getTitle());
            et_content.setText(current.getContent());
        }
    }
    public void initTitle(){
        tv_content=(TextView)findViewById(R.id.tv_title_content);
        iv_left=(ImageView)findViewById(R.id.iv_title_left);
        iv_left.setImageResource(R.mipmap.back);
        iv_left.setVisibility(View.VISIBLE);
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
    public void initView()
    {
        til_title=(TextInputLayout)findViewById(R.id.til_log_title);
        til_content=(TextInputLayout)findViewById(R.id.til_log_content);

        et_title=(EditText)findViewById(R.id.et_log_title);
        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()!=0){
                    til_title.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_content=(EditText)findViewById(R.id.et_log_content);
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()!=0){
                    til_content.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btn_submit=(Button)findViewById(R.id.btn_log_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(et_title.getText())){
                    til_title.setError(getString(R.string.error_log_title_null));
                    til_title.setErrorEnabled(true);
                    return;
                }
                if(TextUtils.isEmpty(et_content.getText())){
                    til_content.setError(getString(R.string.error_log_content_null));
                    til_content.setErrorEnabled(true);
                    return;
                }

                if(mode==Constants.INTENT_VALUE_LOG_MODE_ADD){
                    LogItem li=new LogItem();
                    li.setTime(System.currentTimeMillis());
                    li.setTitle(et_title.getText().toString());
                    li.setContent(et_content.getText().toString());
                    LogItem last=LogDao.getLastLog(System.currentTimeMillis());
                    if(last!=null)
                    {
                        li.setDuration(System.currentTimeMillis()-last.getTime());
                    }else{
                        li.setDuration(0);
                    }
                    LogDao.addLog(li);

                }else if(mode==Constants.INTENT_VALUE_LOG_MODE_EDIT){
                    current.setTitle(et_title.getText().toString());
                    current.setContent(et_content.getText().toString());
                    LogDao.updateLog(current);
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
