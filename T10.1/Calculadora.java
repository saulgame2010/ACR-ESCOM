import java.rmi.*;

public interface Calculadora extends Remote {
    public double suma(double a, double b) throws RemoteException;
    public double resta(double a, double b) throws RemoteException;
    public double multi(double a, double b) throws RemoteException;
    public double divi(double a, double b) throws RemoteException;
}