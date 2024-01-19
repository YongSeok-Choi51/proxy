package aop;



public class TransactionAOP {

    public void getConnectionToDB () {
        System.out.println("Connected Database Connection...");
    }

    public void disconnect() {
        System.out.println("disconnected...");
    }

}



