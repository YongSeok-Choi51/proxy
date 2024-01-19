package dymic;

import proxy.annotation.YSTransaction;

public class CglibPerson {

    @YSTransaction
    public void running() {
        System.out.println("CglibPerson is running..");
    }

    public void jumping() {
        System.out.println("CglibPerson is jumping");
    }
}
