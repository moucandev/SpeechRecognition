package com.example.moucan.speechrecognition;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import butterknife.internal.Utils;

public class MainActivity extends AppCompatActivity implements AudioRecorderUtils.OnAudioStatusUpdateListener {

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
        TextView start = (TextView) contentView.findViewById(R.id.tv_start);
        TextView end = (TextView) contentView.findViewById(R.id.tv_end);
        TextView cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        TextView displayTime = (TextView) contentView.findViewById(R.id.tv_timer);
        ImageView record = (ImageView) contentView.findViewById(R.id.iv_recording);
        File filePath=new File(Environment.getExternalStorageDirectory()+"/sound");
        if (!filePath.exists()){
            filePath.mkdir();
        }
        file=new File(Environment.getExternalStorageDirectory()+"/sound/"+TimeUtils.getTimeZoneDatabaseVersion()+".mp3");
        Log.e("Sound",file.getAbsolutePath());
        start.setOnClickListener(v -> {
            recorderUtils=new AudioRecorderUtils(file);
            recorderUtils.startRecord();
        });
        end.setOnClickListener(v -> {
            if (recorderUtils!=null){
                recorderUtils.stopRecord();
                popupWindow.dismiss();
            }

        });
        cancel.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

    }
    public void setLevel(int level) {
        ivSound.getDrawable().setLevel(3000 + 6000 * level / 100);
    }

    @Override
    public void onUpdate(double db) {
        setLevel((int) db);

    }
}
