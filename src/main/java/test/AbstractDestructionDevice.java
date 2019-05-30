package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractDestructionDevice implements DestructionDevice {
        private String name;
        private Random random;
        private int min = 1;
        private int max = 30;
        private int successfulScanCounter = 0;
        private int failedScanCounter = 0;
        private State state;
        public List<Integer> errorCodes = new ArrayList<>();

        private DeviceEventListener deviceEventListener;

        public AbstractDestructionDevice(String name) {
                System.out.println(name + " created");
                random = new Random();
                this.name = name;
        }

        public DeviceEventListener getDeviceEventListener() {
                return deviceEventListener;
        }

        public void setDeviceEventListener(DeviceEventListener deviceEventListener) {
                this.deviceEventListener = deviceEventListener;
        }

        @Override
        public void start() {
                System.out.println(name + " starting...");
                deviceEventListener.onStateChange(State.STARTING.getVal());
                randomWait();
                System.out.println(name + " ready");
                deviceEventListener.onStateChange(State.READY.getVal());
        }

        @Override
        public void stop() {
                System.out.println(name + " stopping...");
                deviceEventListener.onStateChange(State.STOPPING.getVal());
                randomWait();
                System.out.println(name + " stopped");
                deviceEventListener.onStateChange(State.STOPPED.getVal());
        }

        @Override
        public void pause() {
                System.out.println(name + " pausing...");
                deviceEventListener.onStateChange(State.PAUSED.getVal());
                randomWait();
                System.out.println(name + " paused");
        }

        @Override
        public void resume() {

                System.out.println(name + " resuming...");
                randomWait();
                System.out.println(name + " ready");
                deviceEventListener.onStateChange(State.READY.getVal());
        }

        @Override
        public void scanMedia() {
                if (!scanFailed()) {
                        successfulScanCounter++;
                } else {
                        failedScanCounter++;
                        throw new RuntimeException("scan failed");
                }
        }

        @Override
        public void processMedia(int count) {
                for (int i = 0; i < count; i++) {
                        System.out.println("processing media..");
                        deviceEventListener.onStateChange(State.BUSY.getVal());
                        if (randomError()) {
                                deviceEventListener.onError(randomErrorCode());
                        }

                        try {
                                scanMedia();
                                deviceEventListener.onMediaScan(successfulScanCounter);
                                System.out.println("destroying media..");
                                Thread.sleep(4000);
                                System.out.println("media destroyed");
                                deviceEventListener.onStateChange(State.READY.getVal());

                        } catch (Exception e) {
                                deviceEventListener.onMediaScanFailed(failedScanCounter);
                        }
                }
        }

        @Override
        public void resetCounter() {
                failedScanCounter = 0;
                successfulScanCounter = 0;
        }

        @Override
        public void clearErrors() {

        }


        @Override
        public void randomWait() {
                try {
                        Thread.sleep(getRandomNumber());
                } catch (InterruptedException e) {
                        //e.printStackTrace();
                }
        }

        private int getRandomNumber(int max) {
                return random.nextInt((max - min) + 1) + min;
        }

        private int getRandomNumber() {
                return random.nextInt((max - min) + 1) + min;
        }

        private boolean scanFailed() {
                return 1 == random.nextInt((20 - min) + 1) + min;
        }

        private boolean randomError() {
                return 1 == random.nextInt((30 - min) + 1) + min;
        }

        private int randomErrorCode() {
                int i = random.nextInt((errorCodes.size() - min) + 1) + min;
                return errorCodes.get(i);
        }
}
