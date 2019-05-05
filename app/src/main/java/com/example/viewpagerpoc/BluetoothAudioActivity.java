package com.example.viewpagerpoc;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class BluetoothAudioActivity  extends AppCompatActivity {

    BluetoothAdapter mBtAdapter;
    BluetoothA2dp mA2dpService;

    AudioManager mAudioManager;
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        bluetoothSetting();
    }

    private void bluetoothSetting() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        registerReceiver(mReceiver, new IntentFilter(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED));
        registerReceiver(mReceiver, new IntentFilter(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED));

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        mBtAdapter.getProfileProxy(this, mA2dpListener , BluetoothProfile.A2DP);

    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context ctx, Intent intent) {
            String action = intent.getAction();
            Log.d("", "receive intent for action : " + action);
            if (action.equals(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, BluetoothA2dp.STATE_DISCONNECTED);
                if (state == BluetoothA2dp.STATE_CONNECTED) {
                    setIsA2dpReady(true);
                    playMusic();
                } else if (state == BluetoothA2dp.STATE_DISCONNECTED) {
                    setIsA2dpReady(false);
                }
            } else if (action.equals(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, BluetoothA2dp.STATE_NOT_PLAYING);
                if (state == BluetoothA2dp.STATE_PLAYING) {
                    Log.d("", "A2DP start playing");
                    Toast.makeText(BluetoothAudioActivity.this, "A2dp is playing", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("", "A2DP stop playing");
                    Toast.makeText(BluetoothAudioActivity.this, "A2dp is stopped", Toast.LENGTH_SHORT).show();
                }
            }
        }

    };

    boolean mIsA2dpReady = false;
    void setIsA2dpReady(boolean ready) {
        mIsA2dpReady = ready;
        Toast.makeText(this, "A2DP ready ? " + (ready ? "true" : "false"), Toast.LENGTH_SHORT).show();
    }

    private BluetoothProfile.ServiceListener mA2dpListener = new BluetoothProfile.ServiceListener() {

        @Override
        public void onServiceConnected(int profile, BluetoothProfile a2dp) {
            if (profile == BluetoothProfile.A2DP) {
                mA2dpService = (BluetoothA2dp) a2dp;
                if (mAudioManager.isBluetoothA2dpOn()) {
                    setIsA2dpReady(true);
                    playMusic();
                } else {
                    Log.d("----", "bluetooth a2dp is not on while service connected");
                }
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            setIsA2dpReady(false);
        }

    };

    private void playMusic() {
        // mPlayer = new MediaPlayer();
       /* AssetManager assetManager = this.getAssets();
        AssetFileDescriptor fd;
        try {
            fd = assetManager.openFd("Radioactive.mp3");
            Log.d("", "fd = " + fd);
            mPlayer.setDataSource(fd.getFileDescriptor());
            mPlayer.prepare();
            Log.d("", "start play music");
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        mPlayer.start();

    }

    @Override
    protected void onDestroy() {
        mBtAdapter.closeProfileProxy(BluetoothProfile.A2DP, mA2dpService);
        releaseMediaPlayer();
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        releaseMediaPlayer();
        super.onPause();
    }

    private void releaseMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }


}
