package ventana;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.IOException;

public class Ventana extends JFrame {
    private static final long serialVersionUID = 1L;

    // Área de mensajes
    private final JTextArea mensajes = new JTextArea();
    // Prompt de envío
    private final JTextField prompt = new JTextField();
    // Botón de envío
    private final JButton boton = new JButton();

    private DataOutputStream dos;

    public Ventana() {
        // Distribución de los componentes por zonas de la ventana
        setLayout(new BorderLayout());

        // Agregar el JTextArea a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(mensajes);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        prompt.setPreferredSize(new Dimension(200, 30));
        boton.setPreferredSize(new Dimension(20, 30));
        prompt.setToolTipText("Escribe aquí tu mensaje...");
        boton.setText("Enviar");
        boton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // Método que se ejecuta cuando hagamos click
                enviar();
            }
        });

        add(scrollPane, BorderLayout.NORTH); // Agregar el JScrollPane en lugar directamente del JTextArea
        add(prompt, BorderLayout.CENTER);
        add(boton, BorderLayout.SOUTH);

        pack();

        setTitle("Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void enviar() {
        String mensaje = prompt.getText();

        try {
            dos.writeUTF(mensaje);
            mensajes.append(mensaje + "\n");

            // Pequeño retraso para permitir que la GUI se actualice completamente
            SwingUtilities.invokeLater(() -> {
                mensajes.setCaretPosition(mensajes.getDocument().getLength());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        prompt.setText("");
    }

    public void actualizarPrompt(String mensaje) {
        mensajes.append(mensaje + "\n");
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Ventana ventana = new Ventana();
            ventana.setVisible(true);
        });
    }
}
