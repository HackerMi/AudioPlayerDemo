package com.genius.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.genius.demo.R.id;

import android.app.Activity;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AudioPlayerDemoActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    
	private TextView mTextViewState;				// ����״̬
	
	private Button mBtnPlayButton;					// ����
	
	private Button mBtnPauseButton;					// ��ͣ
	
	private Button mBtnStopButton;					// ֹͣ
	
	private AudioPlayer mAudioPlayer;				// ������
	
	private Handler mHandler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initView();
        
        initLogic();
    }
    
    
    
    
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		mAudioPlayer.release();
	}



    
	public void initView()
    {
    	mTextViewState = (TextView) findViewById(R.id.tvPlayState);
    	
    	mBtnPlayButton = (Button) findViewById(R.id.buttonPlay);
    	mBtnPlayButton.setOnClickListener(this);
    	
    	mBtnPauseButton = (Button) findViewById(R.id.buttonPause);
    	mBtnPauseButton.setOnClickListener(this);
    	
    	mBtnStopButton = (Button) findViewById(R.id.buttonStop);
    	mBtnStopButton.setOnClickListener(this);
    }
    
    public void initLogic()
    {
    	mHandler = new Handler()
    	{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what)
				{
				case AudioPlayer.STATE_MSG_ID:
					showState((Integer)msg.obj);
					break;
				}
			}
    		
    		
    	};
    	
    	mAudioPlayer = new AudioPlayer(mHandler);
    	
    	// ��ȡ��Ƶ����
    	AudioParam audioParam = getAudioParam();
    	mAudioPlayer.setAudioParam(audioParam);
    	
    	// ��ȡ��Ƶ���
    	byte[] data = getPCMData();
    	mAudioPlayer.setDataSource(data);
    	
    	// ��ƵԴ����
    	mAudioPlayer.prepare();
    	
    	if (data == null)
    	{
    		mTextViewState.setText(filePath + "����·���²������ļ���");
    	}
    }


    @Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId())
		{
		case R.id.buttonPlay:
			play();
			break;
		case R.id.buttonPause:
			pause();
			break;
		case R.id.buttonStop:
			stop();
			break;
		}
	}
    
    public void play()
    {
    	mAudioPlayer.play();
    }
    
    public void pause()
    {
    	mAudioPlayer.pause();
    }
    
    public void stop()
    {
    	mAudioPlayer.stop();
    }
    
    
    public void showState(int state)
    {
    	String showString = "";
    	
    	switch(state)
    	{
    	case PlayState.MPS_UNINIT:
    		showString = "MPS_UNINIT";
    		break;
    	case PlayState.MPS_PREPARE:
    		showString = "MPS_PREPARE";
    		break;
    	case PlayState.MPS_PLAYING:
    		showString = "MPS_PLAYING";
    		break;
    	case PlayState.MPS_PAUSE:
    		showString = "MPS_PAUSE";
    		break;
    	}
    	
    	showState(showString);
    }
    
    
    public void showState(String str)
    {
    	mTextViewState.setText(str);
    }


	
    
	
	
	
	 /*
     * ���PCM��Ƶ��ݲ���
     */
	public AudioParam getAudioParam()
	{
		AudioParam audioParam = new AudioParam();
    	audioParam.mFrequency = 44100;
    	audioParam.mChannel = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
    	audioParam.mSampBit = AudioFormat.ENCODING_PCM_16BIT;
    	
    	return audioParam;
	}
	
	
	
	String  filePath  = "/sdcard/voice.pcm";
    /*
     * ���PCM��Ƶ���
     */
    public byte[] getPCMData()
    {
    	
    	File file = new File(filePath);
    	if (file == null){
    		Log.e("@@@@@", "not get file");
    		return null;
    	}
    	
    	Log.e("@@@@@", "file name: " + file.getName());
    	FileInputStream inStream;
		try {
			inStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		byte[] data_pack = null;
    	if (inStream != null){
    		long size = file.length();
    		
    		data_pack = new byte[(int) size];
    		try {
				inStream.read(data_pack);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
  
    	}
    	
    	return data_pack;
    }
    
    
    
    
    
    
}