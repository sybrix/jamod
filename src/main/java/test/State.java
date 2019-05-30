package test;

public enum State {

        STARTING(1), PAUSED(3), BUSY(4), STOPPING(5), STOPPED(6), ESTOPPED(7), READY(8);

        int val;

        private State(int val) {
                this.val = val;

        }

        public int getVal() {
                return val;
        }

        public static State parseVal(int v){
                for (State s : State.values()) {
                        if (s.getVal() == v) {
                                return s;
                        }
                }

                return null;
        }
}
