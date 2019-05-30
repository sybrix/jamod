package test;

import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.procimg.*;
import net.wimpi.modbus.util.Observer;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;


public class ModbusServer implements Observer {
        private static final Logger logger = LoggerFactory.getLogger(ModbusServer.class);
        private static final org.apache.logging.log4j.Logger logger_ = LogManager.getLogger(ModbusServer.class);

        private DestructionDevice destructionDevice;
        private SimpleProcessImage spi = null;

        public ModbusServer(DestructionDevice destructionDevice) {
                this.destructionDevice = destructionDevice;
        }

        public SimpleProcessImage getRegisters() {
                return spi;
        }

        public void start() {

                ModbusTCPListener listener = null;

                int port = 5502;//Modbus.DEFAULT_PORT;


                spi = new SimpleProcessImage();

                //Intialize Registers
                for (int i = 0; i < 22; i++) {
                        spi.addRegister(new SimpleRegister(i));
                }

                ObservableRegister observableRegister = new ObservableRegister();
                observableRegister.addObserver(this);
                spi.setRegister(1, observableRegister);

                spi.getRegister(6).setValue(1); //MAJOR_VERSION
                spi.getRegister(7).setValue(11); //MINOR_VERSION
                spi.getRegister(8).setValue(2); //BUILD_VERSION

                spi.getRegister(11).setValue(0); //AUTOMATIC_MODE


//3. Set the image on the coupler
                ModbusCoupler.getReference().setProcessImage(spi);
                ModbusCoupler.getReference().setMaster(false);
                ModbusCoupler.getReference().setUnitID(15);

//2. Create the coupler and set the slave identity
                listener = new ModbusTCPListener(3);
                listener.setPort(port);
                listener.start();
        }


        public static void main(String args[]) {

                ModbusTCPListener listener = null;
                SimpleProcessImage spi = null;
                int port = 5502;//Modbus.DEFAULT_PORT;


                //2. Prepare a process image
                spi = new SimpleProcessImage();
                //spi.setDigitalOut(1, new SimpleDigitalOut(true));
//                spi.addDigitalOut(new SimpleDigitalOut(true));
//                spi.addDigitalOut(new SimpleDigitalOut(false));
//                spi.addDigitalIn(new SimpleDigitalIn(false));
//                spi.addDigitalIn(new SimpleDigitalIn(true));
//                spi.addDigitalIn(new SimpleDigitalIn(false));
//                spi.addDigitalIn(new SimpleDigitalIn(true));

                //Intialize Registers
                for (int i = 0; i < 22; i++) {
                        spi.addRegister(new SimpleRegister(i));
                }

                spi.getRegister(6).setValue(1); //MAJOR_VERSION
                spi.getRegister(7).setValue(11); //MINOR_VERSION
                spi.getRegister(8).setValue(2); //BUILD_VERSION

                spi.getRegister(11).setValue(0); //AUTOMATIC_MODE


//                spi.addInputRegister(new SimpleInputRegister(45)); // input register


//3. Set the image on the coupler
                ModbusCoupler.getReference().setProcessImage(spi);
                ModbusCoupler.getReference().setMaster(false);
                ModbusCoupler.getReference().setUnitID(15);

//2. Create the coupler and set the slave identity
                listener = new ModbusTCPListener(3);
                listener.setPort(port);
                listener.start();


        }


        @Override
        public void update(net.wimpi.modbus.util.Observable o, Object arg) {
                Register r = (Register) o;
                System.out.println(r.getValue());

                switch (r.getValue()) {
                        case 1:
                                destructionDevice.start();
                                break;
                        case 2:
                                destructionDevice.pause();
                                break;
                        case 4:
                                destructionDevice.resume();
                                break;
                        case 0:
                                destructionDevice.stop();
                                break;
                        case 32:
                                destructionDevice.resetCounter();
                                break;


                }

        }
}
