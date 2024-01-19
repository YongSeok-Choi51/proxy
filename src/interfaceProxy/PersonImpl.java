package interfaceProxy;

public class PersonImpl implements Person{
    @Override
    public void waking() {
        System.out.println("Waking to Home");
    }

    @Override
    public void saying() {
        System.out.println("Saying that Hi");
    }
}
