package com.serenegiant.myapplication;

import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.widget.TextView;
import android.widget.Toast;

import com.serenegiant.usb.DeviceFilter;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private USBMonitor mUSBMonitor;
    private List<UsbDevice> list_devices = new ArrayList<>();
    // Used to load the 'native-lib' library on application startup.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUSBMonitor = new USBMonitor(this, mOnDeviceConnectListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mUSBMonitor.register();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mUSBMonitor != null) {
            mUSBMonitor.unregister();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDevices();
        request_permission();
    }

    /**
     * 请求预览权限
     */
    private void request_permission() {
        UsbDevice usbDevice = list_devices.get(0);
        new Handler().postDelayed(() -> {
            if (usbDevice instanceof UsbDevice) {
                mUSBMonitor.requestPermission(usbDevice);
            }
        }, 300);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUSBMonitor != null) {
            mUSBMonitor.destroy();
            mUSBMonitor = null;
        }
    }

    /**
     * 搜索UVC摄像头列表
     */
    public void updateDevices() {
//		mUSBMonitor.dumpDevices();
        final List<DeviceFilter> filter = DeviceFilter.getDeviceFilters(MainActivity.this, com.serenegiant.uvccamera.R.xml.device_filter);
        list_devices = mUSBMonitor.getDeviceList(filter.get(0));

    }


    private final USBMonitor.OnDeviceConnectListener mOnDeviceConnectListener = new USBMonitor.OnDeviceConnectListener() {

        @Override
        public void onAttach(UsbDevice device) {

        }

        @Override
        public void onDettach(UsbDevice device) {

        }

        @Override
        public void onConnect(UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock, boolean createNew) {
            final UVCCamera camera = new UVCCamera();
            camera.open(ctrlBlock);
        }

        @Override
        public void onDisconnect(UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock) {

        }

        @Override
        public void onCancel(UsbDevice device) {

        }
    };



}
