import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import javax.swing.*;

public class Cliente {
    static final int PUERTO = 1234;

    public static void main(String[] args) {
        double a, b, c;
        try {
            Registry r = LocateRegistry.getRegistry("127.0.0.1");
            Calculadora ca = (Calculadora) Naming.lookup("//localhost:" + PUERTO + "/Calculadora");
            while (true) {
                String menu = JOptionPane.showInputDialog("Escoge la operacion que quieres realizar\n" + "1. Suma\n"
                        + "2. Resta\n" + "3. Multiplicacion\n" + "4. Division\n" + "5. Salir");

                if (menu == null || (menu != null && ("".equals(menu)))) {
                    System.exit(0);
                } else {
                    switch (menu) {
                    case "1":
                        a = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el primer numero"));
                        b = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el segundo numero"));
                        c = ca.suma(a, b);
                        JOptionPane.showMessageDialog(null, "El resultado es: " + c, "Resultado",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "2":
                        a = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el primer numero"));
                        b = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el segundo numero"));
                        c = ca.resta(a, b);
                        JOptionPane.showMessageDialog(null, "El resultado es: " + c, "Resultado",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "3":
                        a = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el primer numero"));
                        b = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el segundo numero"));
                        c = ca.multi(a, b);
                        JOptionPane.showMessageDialog(null, "El resultado es: " + c, "Resultado",
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "4":
                        a = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el primer numero"));
                        b = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el segundo numero"));
                        if (b == 0) {
                            JOptionPane.showMessageDialog(null, "No puedes hacer division entre 0");
                        } else {
                            c = ca.divi(a, b);
                            JOptionPane.showMessageDialog(null, "El resultado es: " + c, "Resultado",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    case "5":
                        System.exit(0);
                    default:
                        JOptionPane.showMessageDialog(null, "Elige una opción válida", "Elegir opción",
                                JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en el cliente: " + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
