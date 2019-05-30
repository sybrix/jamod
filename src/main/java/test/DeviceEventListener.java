package test;

public interface DeviceEventListener {
        void onMediaScan(int count);

        void onMediaScanFailed(int count);

        void onError(int errorCode);

        void onStateChange(int state);
}
