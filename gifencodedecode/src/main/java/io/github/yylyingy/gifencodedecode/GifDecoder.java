package io.github.yylyingy.gifencodedecode;

import android.graphics.Bitmap;

public class GifDecoder {

    static {
        System.loadLibrary("androidndkgif");
    }

    private native long nativeInit();
    private native void nativeClose(long handle);

    private native boolean nativeLoad(long handle, String fileName);

    private native int nativeGetFrameCount(long handle);

    private native Bitmap nativeGetFrame(long handle, int n);
    private native int nativeGetDelay(long handle, int n);

    private native int nativeGetWidth(long handle);
    private native int nativeGetHeight(long handle);

    private int width = 0;
    private int height = 0;

    private Bitmap[] bitmaps = new Bitmap[0];
    private int[] delays = new int[0];
    private int frameNum;

    public boolean load(String fileName) {
        long handle = nativeInit();
        if (!nativeLoad(handle, fileName)) {
            nativeClose(handle);
            return false;
        }
        width = nativeGetWidth(handle);
        height = nativeGetHeight(handle);

        frameNum = nativeGetFrameCount(handle);
        bitmaps = new Bitmap[frameNum];
        delays = new int[frameNum];
        for (int i = 0; i < frameNum; ++i) {
            bitmaps[i] = nativeGetFrame(handle, i);
            delays[i] = nativeGetDelay(handle, i);
        }

        nativeClose(handle);
        return true;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    /**
     *
     * @return pictures's num.
     */
    public int frameNum() {
        return frameNum;
    }

    /**
     * To get one picture's bitmap
     * @param idx the picture's index
     * @return picture's bitmap .
     */
    public Bitmap frame(int idx) {
        if (0 == frameNum) {
            return null;
        }
        return bitmaps[idx % frameNum];
    }

    /**
     * Current picture's display time
     * @param idx the picture index
     * @return The display time
     */
    public int delay(int idx) {
        if (0 == frameNum) {
            return 0;
        }
        return delays[idx % frameNum];
    }
}
