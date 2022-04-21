import java.rmi.*;
import java.rmi.registry.Registry;
import javax.swing.*;

public class Server {
    public static void main(String[] args) {
        try {
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(1234);
            System.setProperty("rmi.server.hostname", "127.0.0.1");
            r.rebind("Calculadora", new RMI());
            JOptionPane.showMessageDialog(null, "Se conectó correctamente al servidor", "Se logró bandera", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hubo un error en el servidor: " + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
