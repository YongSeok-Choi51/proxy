package interfaceProxy;

import proxy.annotation.YSTransaction;
import proxy.aop.TransactionAOP;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PersonProxy implements InvocationHandler {

    Object target;
    private TransactionAOP tsAop;

    public PersonProxy(Object target, TransactionAOP aop) {
        this.target= target;
        this.tsAop = aop;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;

        System.out.println("********** Proxy Start **************");

        // 데이터베이스 접근 메서드인 경우
        if(method.isAnnotationPresent(YSTransaction.class)) {
            try{
                tsAop.getConnectionToDB(); // 데이터베이스 커넥션 획득
                result = method.invoke(target, args);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw e;
            } finally {
                try {
                    tsAop.disconnect();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    throw e;
                }
            }
        } else {
            result = method.invoke(target, args);
        }


        System.out.println("********** Proxy End **************");

        return result;
    }
}
