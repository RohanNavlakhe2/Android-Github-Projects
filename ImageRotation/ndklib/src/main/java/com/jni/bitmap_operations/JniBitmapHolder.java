package com.jni.bitmap_operations;

import android.graphics.Bitmap;
import android.util.Log;

import java.nio.ByteBuffer;

public class JniBitmapHolder {
    ByteBuffer _handler = null;

    static {
        System.loadLibrary("JniBitmapOperationsLibrary");
        //System.loadLibrary("imagerotation");
    }

    public JniBitmapHolder() {
    }

    public JniBitmapHolder(final Bitmap bitmap) {
        storeBitmap(bitmap);
    }

    private native ByteBuffer jniStoreBitmapData(Bitmap bitmap);

    private native Bitmap jniGetBitmapFromStoredBitmapData(ByteBuffer handler);

    private native void jniFreeBitmapData(ByteBuffer handler);

    private native void jniRotateBitmapCcw90(ByteBuffer handler);

    private native void jniRotateBitmapCw90(ByteBuffer handler);

    private native void jniRotateBitmap180(ByteBuffer handler);

    private native void jniCropBitmap(ByteBuffer handler, final int left, final int top, final int right, final int bottom);

    private native void jniScaleNNBitmap(ByteBuffer handler, final int newWidth, final int newHeight);

    private native void jniScaleBIBitmap(ByteBuffer handler, final int newWidth, final int newHeight);

    private native void jniFlipBitmapHorizontal(ByteBuffer handler);

    private native void jniFlipBitmapVertical(ByteBuffer handler);

    public void storeBitmap(final Bitmap bitmap) {
        if (_handler != null)
            freeBitmap();
        _handler = jniStoreBitmapData(bitmap);
    }

    public void rotateBitmapCcw90() {
        if (_handler == null)
            return;
        jniRotateBitmapCcw90(_handler);
    }

    public void rotateBitmapCw90() {
        if (_handler == null)
            return;
        jniRotateBitmapCw90(_handler);
    }

    public void rotateBitmap180() {
        if (_handler == null)
            return;
        jniRotateBitmap180(_handler);
    }

    public void cropBitmap(final int left, final int top, final int right, final int bottom) {
        if (_handler == null)
            return;
        jniCropBitmap(_handler, left, top, right, bottom);
    }

    public Bitmap getBitmap() {
        if (_handler == null)
            return null;
        return jniGetBitmapFromStoredBitmapData(_handler);
    }

    public Bitmap getBitmapAndFree() {
        final Bitmap bitmap = getBitmap();
        freeBitmap();
        return bitmap;
    }

    public void scaleBitmap(final int newWidth, final int newHeight, final ScaleMethod scaleMethod) {
        if (_handler == null)
            return;
        switch (scaleMethod) {
            case BilinearInterpolation:
                jniScaleBIBitmap(_handler, newWidth, newHeight);
                break;
            case NearestNeighbour:
                jniScaleNNBitmap(_handler, newWidth, newHeight);
                break;
        }
    }

    /**
     * Flips the bitmap on the <b>horizontal line</b>. I.e.:
     * <br/>
     * <pre>
     * 0  1   2   3         8  9  10  11
     * 4  5   6   7    >    4  5   6   7
     * 8  9  10  11         0  1   2   3
     * </pre>
     */
    public void flipBitmapHorizontal() {
        if (_handler == null)
            return;
        jniFlipBitmapHorizontal(_handler);
    }

    /**
     * Flips the bitmap on the <b>vertical line</b>. I.e.:
     * <br/>
     * <pre>
     * 0  1   2       2  1  0
     * 3  4   5   >   5  4  3
     * 6  7   8       8  7  6
     * </pre>
     */
    public void flipBitmapVertical() {
        if (_handler == null)
            return;
        jniFlipBitmapVertical(_handler);
    }

    public void freeBitmap() {
        if (_handler == null)
            return;
        jniFreeBitmapData(_handler);
        _handler = null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (_handler == null)
            return;
        Log.w("DEBUG", "JNI bitmap wasn't freed nicely.please remember to free the bitmap as soon as you can");
        freeBitmap();
    }

    public enum ScaleMethod {
        NearestNeighbour, BilinearInterpolation
    }
}
