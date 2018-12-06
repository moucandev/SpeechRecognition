package com.example.moucan.speechrecognition;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TimeUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_collection)
    ImageView ivCollection;
    @BindView(R.id.rv_sound_text)
    RecyclerView rvSoundText;
    @BindView(R.id.iv_sound)
    ImageView ivSound;
    @BindView(R.id.translate_text)
    TextView translateText;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_wrong)
    ImageView ivWrong;
    private TranslateTextAdapter adapter;
    private List<String> list;
    private File file;
    private AudioRecorderUtils recorderUtils;
    private long downTime;
    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initView();
    }
    private void initView(){
        rvSoundText.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter=new TranslateTextAdapter(this,list);
        rvSoundText.setAdapter(adapter);
        tvTitle.setText("语音识别");
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }
    private void initData(){
        list=new ArrayList<>();
        list.add("This is a test data");
    }
    @OnClick({R.id.iv_sound,R.id.iv_collection,R.id.translate_text,R.id.iv_right,R.id.iv_wrong})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_sound:
                showPopupWindow(ivSound);
                list.add("This is a test data");
                adapter.notifyDataSetChanged();
                break;
            case R.id.iv_collection:
                CollectionActivity.actionStart(this,adapter.getList());
                break;
            case R.id.translate_text:
                break;
        }
    }
    private void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_popup_window, null);
        PopupWindow popupWindow = new PopupWindow(contentView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAsDropDown(view);
        setAlpha(0.5f);
        TextView start = (TextView) contentView.findViewById(R.id.tv_start);
        TextView end = (TextView) contentView.findViewById(R.id.tv_end);
        TextView cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        TextView displayTime = (TextView) contentView.findViewById(R.id.tv_timer);
        ImageView record = (ImageView) contentView.findViewById(R.id.recording);

        end.setEnabled(false);
        File filePath=new File(Environment.getExternalStorageDirectory()+"/sound");
        if (!filePath.exists()){
            filePath.mkdir();
        }
        file=new File(Environment.getExternalStorageDirectory()+"/sound/"+"mySound"+".mp3");
        Log.e("Sound",file.getAbsolutePath());
        recorderUtils=new AudioRecorderUtils(file);
        recorderUtils.setOnAudioStatusUpdateListener(db ->{
            record.getDrawable().setLevel ((int)(3000 + 6000 * db / 100));
            displayTime.setText(ProgressTextUtils.getProgressText(System.currentTimeMillis()-downTime));
        });
        start.setOnClickListener(v -> {
            recorderUtils.startRecord();
            downTime=System.currentTimeMillis();
            end.setEnabled(true);
            start.setEnabled(false);
        });
        end.setOnClickListener(v -> {
            if (recorderUtils!=null){
                start.setEnabled(true);
                end.setEnabled(false);
                recorderUtils.stopRecord();
                popupWindow.dismiss();
                setAlpha(1.0f);
            }
            Toast.makeText(this,"语音存放在"+Environment.getExternalStorageDirectory()+"/sound"+"路径下",Toast.LENGTH_SHORT).show();

        });
        cancel.setOnClickListener(v -> {
            if (recorderUtils!=null){
                recorderUtils.cancelRecord();
            }
            popupWindow.dismiss();
            setAlpha(1.0f);
        });

    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis()-exitTime > 1500){
            Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }else {
            finish();
        }
    }

    private void setAlpha(float alpha){
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=alpha;
        getWindow().setAttributes(lp);
    }

}
