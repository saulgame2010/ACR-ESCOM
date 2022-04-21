import java.rmi.server.UnicastRemoteObject;
import java.rmi.*;

public class RMI extends UnicastRemoteObject implements Calculadora{

    public RMI() throws RemoteException {
        int a, b;
    }

    @Override
    public double suma(double a, double b) throws RemoteException {
        System.out.println("Se recibio en el servidor a = " + a + " y b = " + b);        
        return a + b;
    }

    @Override
    public double resta(double a, double b) throws RemoteException {        
        System.out.println("Se recibio en el servidor a = " + a + " y b = " + b);
        return a - b;
    }

    @Override
    public double multi(double a, double b) throws RemoteException {        
        System.out.println("Se recibio en el servidor a = " + a + " y b = " + b);
        return a * b;
    }

    @Override
    public double divi(double a, double b) throws RemoteException {        
        System.out.println("Se recibio en el servidor a = " + a + " y b = " + b);
        return a / b;
    }
    
}