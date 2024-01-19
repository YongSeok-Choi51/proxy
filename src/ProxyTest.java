import net.sf.cglib.proxy.Enhancer;
import proxy.aop.TransactionAOP;
import proxy.dymic.CglibPerson;
import proxy.dymic.CglibProxy;
import proxy.dymic.internal.InternalCallError;
import proxy.dymic.internal.InternalCallSuccess;
import proxy.interfaceProxy.Person;
import proxy.interfaceProxy.PersonImpl;
import proxy.interfaceProxy.PersonProxy;

import java.lang.reflect.Proxy;

public class ProxyTest {
    public static void main(String[] args) {
//        jdkInterfaceProxyBasic();
//        jdkInterfaceProxyBasicFailTest();
//        cglibProxyBasic();
//        cglibProxyInternalCallError();
        cglibProxyInternalCallSuccess();
    }

    static void jdkInterfaceProxyBasic() {

        // person instance 는 프록시 객체.
        // spring boot 에서는 이렇게 생성된 객체를 Singleton bean 으로 등록
        Person person = (Person) Proxy.newProxyInstance(
                Person.class.getClassLoader(), // Person 인터페이스의 구현체만 클래스 등록 가능
                new Class[]{Person.class},
                new PersonProxy(new PersonImpl(), new TransactionAOP()));

        person.saying();
        person.waking();
    }

    static void jdkInterfaceProxyBasicFailTest() {
        // IllegalArgumentException: object is not an instance of declaring class

        Person person = (Person) Proxy.newProxyInstance(
                Person.class.getClassLoader(),
                new Class[]{Person.class},
                new PersonProxy(new CglibPerson(), new TransactionAOP()));

        person.saying();
        person.waking();
    }


    static void cglibProxyBasic() {
        // 인터페이스 필요없이 프록시를 Target 클래스의 하위클래스로 지정해 프록시 구현
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CglibPerson.class);
        enhancer.setCallback(new CglibProxy(new CglibPerson(), new TransactionAOP()));

        // Person 인터페이스를 구현한 객체들도 cgLib 으로 프록시 생성 가능
        Enhancer enhancer1 = new Enhancer();
        enhancer1.setSuperclass(Person.class);
        enhancer1.setCallback(new CglibProxy(new PersonImpl(), new TransactionAOP()));

        CglibPerson cglibPerson = (CglibPerson) enhancer.create();
        Person person = (Person) enhancer1.create();

        cglibPerson.running();
        cglibPerson.jumping();

        person.waking();
        person.saying();
    }


    // 프록시의 target 객체 안에서 this.another() 같은 다른 메서드 호출 시 문제.
    static void cglibProxyInternalCallError() {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(InternalCallError.class);
        enhancer.setCallback(new CglibProxy(new InternalCallError(), new TransactionAOP()));


        InternalCallError err =  (InternalCallError) enhancer.create();

        // Client Code 에서 직접 호출하는경우 DB Transaction connection 획득 가능
        err.internal();

        // DB Transaction connection 획득하지 못함.
        err.external();
    }


    // 내부 호출 문제 솔루션 1. 자기자신을 Setter 로 lazy injection
    // spring boot 에서는 Application Context, ObjectProvider<T> 등으로 proxy로 등록된 자기자신을 가져와서 내부호출을 수행해야 함.
    // 생성자는 사용불가. Why? 객체를 생성할때 생성자에 이미 생성된 자신이 넘어와야한다. 불가능
    static void cglibProxyInternalCallSuccess() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(InternalCallSuccess.class);
        enhancer.setCallback(new CglibProxy(new InternalCallSuccess(), new TransactionAOP()));

        InternalCallSuccess internalCallSuccess = (InternalCallSuccess) enhancer.create();
        internalCallSuccess.setInternalSuccess(internalCallSuccess);

        internalCallSuccess.external();
    }

}
