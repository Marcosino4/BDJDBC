package hilo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import conexion.Conexion;
import cliente.Cliente;

public class HiloCliente extends Thread {
	public Socket socket;

	public HiloCliente(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		String mensaje;

		try (Connection con = Conexion.open()) {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			do {
				dos.writeUTF("1.Insert \n2.Delete \n3.Update \n4.Ver \n5.Salir\n");
				mensaje = dis.readUTF();
				
				if (mensaje.equals("1")) {
					dos.writeUTF("Introduzca nombre: ");
					String nombre = dis.readUTF();

					dos.writeUTF("Introduzca primer apellido:  ");
					String apellido = dis.readUTF();

					dos.writeUTF("Introduzca segundo apellido:  ");
					String apellido2 = dis.readUTF();

					dos.writeUTF("Introduzca edad: ");
					int edad = Integer.parseInt(dis.readUTF());

					dos.writeUTF("Introduzca fecha de nacimiento(Ano-mes-dia): ");
					String nacimiento = dis.readUTF();

					Cliente nuevoCliente = new Cliente(0, nombre, apellido, apellido2, edad, nacimiento);
					int nFilas = insertar(con, nuevoCliente);
					dos.writeUTF("Se han insertado " + nFilas + " registro(s) correctamente");

				} else if (mensaje.equals("2")) {
					dos.writeUTF("Introduzca id a eliminar: ");
					int nFilas = borrar(con, Integer.parseInt(dis.readUTF()));

					dos.writeUTF("Se han borrado " + nFilas + " registro(s) correctamente");

				} else if (mensaje.equals("3")) {
					dos.writeUTF("Introduzca id: ");
					int id = Integer.parseInt(dis.readUTF());

					dos.writeUTF("Introduzca nombre: ");
					String nombre = dis.readUTF();

					dos.writeUTF("Introduzca primer apellido:  ");
					String apellido = dis.readUTF();

					dos.writeUTF("Introduzca segundo apellido:  ");
					String apellido2 = dis.readUTF();

					dos.writeUTF("Introduzca edad: ");
					int edad = Integer.parseInt(dis.readUTF());

					dos.writeUTF("Introduzca fecha de nacimiento(Ano-mes-dia): ");
					String nacimiento = dis.readUTF();

					Cliente nuevoCliente = new Cliente(id, nombre, apellido, apellido2, edad, nacimiento);
					int nFilas = update(con, nuevoCliente);
					dos.writeUTF("Se han actualizado " + nFilas + " registro(s) correctamente");

				} else if (mensaje.equals("4")) {
					mensaje = get(con, dos);
					dos.writeUTF(mensaje);

				} else if (mensaje.equals("5")) {
					mensaje = "EXIT";
					dos.writeUTF("EXIT");
				}
			} while (!mensaje.equals("EXIT"));

			dis.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private static int update(Connection con, Cliente cliente) {
		String sql = "UPDATE cliente SET nombre = ?, apellido1 = ?, apellido2 = ?, edad = ?, nacimiento = ? WHERE id = ?";
		int nFilas = 0;
		
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, cliente.getNombre());
			ps.setString(2, cliente.getApellido1());
			ps.setString(3, cliente.getApellido2());
			ps.setInt(4, cliente.getEdad());
			ps.setString(5, cliente.getNacimiento());
			ps.setInt(6, cliente.getId());

			nFilas = ps.executeUpdate();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return nFilas;
	}

	private static String get(Connection con, DataOutputStream dos) {
		String sql = "SELECT * FROM cliente";
		String mensaje = " ";
		try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				int id = rs.getInt("id");
				String nombre = rs.getString("nombre");
				String apellido1 = rs.getString("apellido1");
				String apellido2 = rs.getString("apellido2");
				int edad = rs.getInt("edad");
				Date nacimiento = rs.getDate("nacimiento");
				mensaje += "ID: " + id + ", Nombre: " + nombre + ", Apellidos: " + apellido1 + " " + apellido2
						+ ", Edad: " + edad + ", Nacimiento: " + nacimiento + "\n ";
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return mensaje;
	}

	private static int borrar(Connection con, int primarykey) {
		String sql = "DELETE FROM cliente WHERE id = ?";
		int nFilas = 0;
		
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, primarykey);

			nFilas = ps.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return nFilas;
	}

	private static int insertar(Connection con, Cliente clienteNuevo) {
		String sql = "INSERT INTO cliente values(NULL, ?, ?, ?, ?, ?)";
		int nFilas = 0;
		
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, clienteNuevo.getNombre());
			ps.setString(2, clienteNuevo.getApellido1());
			ps.setString(3, clienteNuevo.getApellido2());
			ps.setInt(4, clienteNuevo.getEdad());
			ps.setString(5, clienteNuevo.getNacimiento());
			nFilas = ps.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return nFilas;
	}
}
