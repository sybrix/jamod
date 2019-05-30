package test;

public class Hopper extends AbstractDestructionDevice implements DeviceEventListener {
        ModbusServer modbusServer;

        public Hopper() {
                super("hopper");
                modbusServer = new ModbusServer(this);
                modbusServer.start();

                setDeviceEventListener(this);
        }

        @Override
        public void onMediaScan(int count) {
                System.out.println("hopper: " + count + " drive(s) scanned successfully");
        }

        @Override
        public void onMediaScanFailed(int count) {
                System.out.println("hopper: " + count + " drive(s) scanned failed");
        }

        @Override
        public void onError(int errorCode) {
                System.out.println("hopper: error occurred. code: " + errorCode);
        }

        @Override
        public void onStateChange(int state) {
                System.out.println("hopper: state changed: " + State.parseVal(state));
        }

        public static void main(String args[]) {
                Hopper h = new Hopper();

        }
}
