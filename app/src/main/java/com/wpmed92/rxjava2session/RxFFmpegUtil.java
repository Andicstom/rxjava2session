package com.wpmed92.rxjava2session;


import android.content.Context;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.Single;

public class RxFFmpegUtil {

    private FFmpeg ffmpeg;

    public RxFFmpegUtil(Context context) {
        ffmpeg = FFmpeg.getInstance(context);
    }

    private void init() {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess() {
                    Log.d("FfmpegUtil", "Successful initialization");
                }

                @Override
                public void onFailure() {
                    Log.d("FfmpegUtil", "Init failure");
                }

                @Override
                public void onFinish() {
                    //Ignore
                }
            });
        } catch (FFmpegNotSupportedException e) {
            Log.d("FfmpegUtil", "Not supported: " + e.getMessage());
        }
    }

    private void exec(final String command) {
        try {
            String[] cmd = command.split(" ");

            if (cmd.length != 0) {
                ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {
                    @Override
                    public void onStart() {
                        //Ignore
                    }

                    @Override
                    public void onSuccess(String s) {
                        Log.d("FfmpegUtil", "Command exec succesful");
                    }

                    @Override
                    public void onFailure(String s) {
                        Log.d("FfmpegUtil", "Command failure: " + s);
                    }

                    @Override
                    public void onFinish() {
                        //Ignore
                    }
                });
            }
        } catch (FFmpegCommandAlreadyRunningException e) {
            Log.d("FfmpegUtil", "Command failure: " + e.getMessage());
        }
    }

    public Observable<File> copyRawFile(Context ctx, int resid, File file) {
        return Observable.defer(() -> {
            try {
                final String abspath = file.getAbsolutePath();
                // Write the iptables binary
                final FileOutputStream out = new FileOutputStream(file);
                final InputStream is = ctx.getResources().openRawResource(resid);
                byte buf[] = new byte[1024];
                int len;

                while ((len = is.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                out.close();
                is.close();

                return Observable.just(file);
            } catch (FileNotFoundException e) {
                return Observable.error(e);
            } catch (IOException e) {
                return Observable.error(e);
            }
        });
    }

    public void executeCommand(final String command) {
        //TODO
    }
}

