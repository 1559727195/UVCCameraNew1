package com.serenegiant.usb;

import android.text.TextUtils;
import android.util.Log;

public class UVCCamera {
    private USBMonitor.UsbControlBlock mCtrlBlock;
    private static final boolean DEBUG = false;    // TODO set false when releasing
    private static final String TAG = UVCCamera.class.getSimpleName();
    private static final String DEFAULT_USBFS = "/dev/bus/usb";
    protected long mNativePtr;
    static {
        System.loadLibrary("UVCCamera");
    }


    public UVCCamera() {
        mNativePtr = nativeCreate();
    }

    /**
     * connect to a UVC camera
     * USB permission is necessary before this method is called
     *
     * @param ctrlBlock
     */
    public synchronized void open(final USBMonitor.UsbControlBlock ctrlBlock) {
        int result;
        try {
            mCtrlBlock = ctrlBlock.clone();
            result = nativeConnect(mNativePtr,
                    mCtrlBlock.getVenderId(), mCtrlBlock.getProductId(),
                    mCtrlBlock.getFileDescriptor(),
                    mCtrlBlock.getBusNum(),
                    mCtrlBlock.getDevNum(),
                    getUSBFSName(mCtrlBlock));
        } catch (final Exception e) {
            Log.w(TAG, e);
            result = -1;
        }
        if (result != 0) {
            throw new UnsupportedOperationException("open failed:result=" + result);
        }
    }


    private final String getUSBFSName(final USBMonitor.UsbControlBlock ctrlBlock) {
        String result = null;
        final String name = ctrlBlock.getDeviceName();
        final String[] v = !TextUtils.isEmpty(name) ? name.split("/") : null;
        if ((v != null) && (v.length > 2)) {
            final StringBuilder sb = new StringBuilder(v[0]);
            for (int i = 1; i < v.length - 2; i++)
                sb.append("/").append(v[i]);
            result = sb.toString();
        }
        if (TextUtils.isEmpty(result)) {
            Log.w(TAG, "failed to get USBFS path, try to use default path:" + name);
            result = DEFAULT_USBFS;
        }
        return result;
    }

    // #nativeCreate and #nativeDestroy are not static methods.
    private final native long nativeCreate();

    private final native void nativeDestroy(final long id_camera);

    private final native int nativeConnect(long id_camera, int venderId, int productId, int fileDescriptor, int busNum, int devAddr, String usbfs);

    private static final native int nativeRelease(final long id_camera);


    // add by qzm
    private static final native int nativeUVCExtWrite(final long id_camera, int addr, byte[] pdat, int len);

    private static final native int nativeUVCExtRead(final long id_camera, int addr, byte[] pdat, int len);




    public int UVCExtWrite(int addr, byte[] pdat, int len) {
        if (mCtrlBlock != null) {
            return nativeUVCExtWrite(mNativePtr, addr, pdat, len);
        } else {
            return -1;
        }
    }

    public int UVCExtRead(int addr, byte[] pdat, int len) {
        if (mCtrlBlock != null) {
            return nativeUVCExtRead(mNativePtr, addr, pdat, len);
        } else {
            return -1;
        }
    }


    //开灯
    public void operate_light(int address) {//0xfea6
        byte[] pdat = new byte[1];
        UVCExtRead(address, pdat, 1);
        if ((pdat[0] & 0x80) == 0) {//打开-灯
            pdat[0] |= 0x80;
        } else {//关闭-灯
            pdat[0] &= (~0x80);
        }
        UVCExtWrite(address, pdat, 1);
    }





}
