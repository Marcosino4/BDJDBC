package main;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import ventana.Ventana;

public class MainCliente {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		try {			
			Socket socket = new Socket("localhost", 6565);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			DataInputStream dis = new DataInputStream(socket.getInputStream());		
			String mensajeRecibido;
			Ventana vent = new Ventana();
			vent.setVisible(true);
			vent.setDos(dos);
			do{
				mensajeRecibido = dis.readUTF();
				vent.actualizarPrompt(mensajeRecibido);
			}while (!mensajeRecibido.equals("EXIT"));
			vent.setVisible(false);
			dos.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}