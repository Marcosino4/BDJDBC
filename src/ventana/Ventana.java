package ventana;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.IOException;

public class Ventana extends JFrame {
    private static final long serialVersionUID = 1L;

    private final JTextArea mensajes = new JTextArea();
    private final JTextField prompt = new JTextField();
    private final JButton boton = new JButton();

    private DataOutputStream dos;

    public Ventana() {
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(mensajes);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        prompt.setPreferredSize(new Dimension(200, 30));
        boton.setPreferredSize(new Dimension(20, 30));
        prompt.setToolTipText("Escribe aquí tu mensaje...");
        boton.setText("Enviar");
        boton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                enviar();
            }
        });

        add(scrollPane, BorderLayout.NORTH); 
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



