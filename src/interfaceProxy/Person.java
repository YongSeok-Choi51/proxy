package interfaceProxy;

import proxy.annotation.YSTransaction;

public interface Person {

    @YSTransaction
    void waking();

    void saying();

}
