package com.serenegiant.usb;

public class UVCCamera {

    static {
        System.loadLibrary("test2");
    }


    public UVCCamera() {

    }

    public native String getStr();

    public native int addInt(int a, int b);




//    public int UVCExtWrite(int addr, byte[] pdat, int len) {
//        if (mCtrlBlock != null) {
//            return nativeUVCExtWrite(mNativePtr, addr, pdat, len);
//        } else {
//            return -1;
//        }
//    }
//
//    public int UVCExtRead(int addr, byte[] pdat, int len) {
//        if (mCtrlBlock != null) {
//            return nativeUVCExtRead(mNativePtr, addr, pdat, len);
//        } else {
//            return -1;
//        }
//    }
//
//
//    //开灯
//    public void operate_light(int address) {//0xfea6
//        byte[] pdat = new byte[1];
//        UVCExtRead(address, pdat, 1);
//        if ((pdat[0] & 0x80) == 0) {//打开-灯
//            pdat[0] |= 0x80;
//        } else {//关闭-灯
//            pdat[0] &= (~0x80);
//        }
//        UVCExtWrite(address, pdat, 1);
//    }



    // add by qzm
    private static final native int nativeUVCExtWrite(final long id_camera, int addr, byte[] pdat, int len);

    private static final native int nativeUVCExtRead(final long id_camera, int addr, byte[] pdat, int len);

}
