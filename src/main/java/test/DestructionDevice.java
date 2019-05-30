package test;

public interface DestructionDevice {
        void start();

        void stop();

        void pause();

        void resume();

        void scanMedia();

        void processMedia(int count);

        void resetCounter();

        void clearErrors();

        void randomWait();

}
