package dymic.internal;

import proxy.annotation.YSTransaction;

public class InternalCallError {

    public void external() {
        System.out.println("External call");
        this.internal();

    }

    @YSTransaction
    public void internal() {
        System.out.println("Internal call");
    }
}
