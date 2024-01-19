package dymic.internal;

import proxy.annotation.YSTransaction;

public class InternalCallSuccess {

    private InternalCallSuccess internalCallSuccess;


    // Setter 호출에도 프록시 적용됨.
    public void setInternalSuccess (InternalCallSuccess internalCallSuccess) {
        System.out.println("Setter Called");
        this.internalCallSuccess = internalCallSuccess;
    }

    public void external() {
        System.out.println("External call");
//        this.internal();
        internalCallSuccess.internal(); // 자기자신이 아닌 주입받은 Proxy 호출. Spring Boot 에서는, 등록된 자기 자신의 Bean을 찾아 호출

    }

    @YSTransaction
    public void internal() {
        System.out.println("Internal call");
    }
}
