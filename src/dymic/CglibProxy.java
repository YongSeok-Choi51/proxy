package dymic;

import net.sf.cglib.proxy.InvocationHandler;
import proxy.annotation.YSTransaction;
import proxy.aop.TransactionAOP;

import java.lang.reflect.Method;

public class CglibProxy implements InvocationHandler {  // net.sf.cglib.proxy.InvocationHandler,  reflection InvocationHandler 와는 다른 인터페이스 구현

    private final Object target;
    private final TransactionAOP tsAop;

    public CglibProxy(Object target, TransactionAOP aop) {
        this.target = target;
        this.tsAop = aop;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Object result = null;

        System.out.println("************ CGLib Proxy Start ***************");

        // 데이터베이스 접근 메서드인 경우
        if(method.isAnnotationPresent(YSTransaction.class)) {
            try{
                tsAop.getConnectionToDB(); // 데이터베이스 커넥션 획득
                result = method.invoke(target, objects);
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
            result = method.invoke(target, objects);
        }

        System.out.println("************ CGLib Proxy End ***************");
        return result;
    }
}
