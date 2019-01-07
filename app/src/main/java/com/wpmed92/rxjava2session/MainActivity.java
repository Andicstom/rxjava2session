package com.wpmed92.rxjava2session;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.jakewharton.rxbinding3.view.RxView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RXJAVA_SESSION";
    private static final String SAMPLE_VIMAGE = "android.resource://com.wpmed92.rxjava2session/" + R.raw.heavy_rain;
    private static final String SAMPLE_SOUND = "android.resource://com.wpmed92.rxjava2session/" + R.raw.sound;
    private RxFFmpegUtil rxFFmpegUtil;

    @BindView(R.id.video_view)
    VideoView videoView;
    @BindView(R.id.btn_add_sound)
    Button btnAddSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        rxFFmpegUtil = new RxFFmpegUtil(this);
        letTheDataFlow();
        initAddSoundButton();
        initializePlayer();
    }

    //Introduction
    private void helloRxJava2() {
        Flowable.just("Hello world").subscribe(s -> Log.d(TAG, s));
    }

    private Flowable<Integer> counterFlowable() {
        return Flowable.range(1, 5)
                .map(v -> v * v);
    }

    private void letTheDataFlow() {
        Flowable<Integer> counterFlowable = counterFlowable();

        counterFlowable.subscribe(s -> Log.d(TAG, "Tick: " + s));
    }

    //Real world use cases
    private void initializePlayer() {
        Uri videoUri = Uri.parse(SAMPLE_VIMAGE);

        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            videoView.start();
        });
    }

    //File helpers
    public void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    /*private Single<File> addSoundToVimage() {
        trimCache(this);
        File vimage = new File(this.getCacheDir(), "vimage.mp4");
        File sound = new File(this.getCacheDir(), "sound.mp3");

        return Observable.merge(
                rxFFmpegUtil.copyRawFile(this, R.raw.heavy_rain, vimage),
                rxFFmpegUtil.copyRawFile(this, R.raw.sound, sound))
                .toList()
                .flatMap(files -> {
                    File vimageWithSound = new File(this.getCacheDir(), "vimageWithSound.mp4");
                    return rxFFmpegUtil.executeCommand("-i " + files.get(0) + " -i " + files.get(1) + " -codec copy -shortest " + vimageWithSound)
                            .map(success -> vimageWithSound);
                });
    }*/

    private void initAddSoundButton() {
        /*ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("processing...");

        Disposable disposable = RxView.clicks(btnAddSound)
                .switchMap(aVoid -> addSoundToVimage().toObservable()
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(d -> dialog.show())
                        .doOnTerminate(() -> dialog.hide()))
                .onErrorReturn(e -> {
                    Log.d(TAG, e.getMessage());
                    return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(vimageWithSound -> {
                    if (vimageWithSound != null) {
                        videoView.setVideoURI(Uri.fromFile(vimageWithSound));
                    }

                    //btnAddSound.setVisibility(View.GONE);
                }, e -> {
                    Log.d(TAG, e.getMessage());
                });*/
    }

}
