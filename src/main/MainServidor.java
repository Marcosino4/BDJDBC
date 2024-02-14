package main;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import hilo.HiloCliente;

public class MainServidor {
	public static ArrayList<HiloCliente> listaHilos = new ArrayList<HiloCliente>();

	public static void main(String[] args) {
		
		try (ServerSocket serverSocket = new ServerSocket(6565);) {
			while (true) {
				Socket socketCliente = serverSocket.accept();
				listaHilos.add(new HiloCliente(socketCliente));
				listaHilos.get(listaHilos.size()-1).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Socket getSoketDestino(String namePC) {
		for(HiloCliente h : listaHilos) {
			if(h.socket.getInetAddress().getHostName().equals(namePC)) {
				return h.socket;
			}
		}
		return null;
	}

}
