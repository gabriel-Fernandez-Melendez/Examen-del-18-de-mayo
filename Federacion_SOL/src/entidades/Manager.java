package entidades;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import utils.ConexBD;
import utils.Datos;
import validaciones.Validaciones;

public class Manager implements operacionesCRUD<Manager> {
	private long id;
	private String telefono;
	private String direccion;

	private DatosPersona persona;

	public Manager(long id, String telefono, String direccion) {
		super();
		this.id = id;
		this.telefono = telefono;
		this.direccion = direccion;
		this.persona = Datos.buscarPersonaPorId(id);
	}

	public Manager(long id, String telefono, String direccion, DatosPersona dp) {
		super();
		this.id = id;
		this.telefono = telefono;
		this.direccion = direccion;
		this.persona = dp;
	}

	public Manager() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public DatosPersona getPersona() {
		return this.persona;
	}

	public void setPersona(DatosPersona persona) {
		this.persona = persona;
	}

	// Examen 5 Ejercicio 4
	/***
	 * Función que pregunta al usuario por cada uno de los campos para un nuevo
	 * Manager, los valida y si son correctos devuelve un objeto Manager completo
	 * 
	 * @return un objeto Manager completo válido o null si hubo algún error
	 */
	public static Manager nuevoManager() {
		Manager ret = null;
		long id = -1;
		String telefono = "";
		String direccion = "";
		DatosPersona dp = null;
		Scanner in;
		boolean valido = false;
		do {
			System.out.println("Introduzca el id del nuevo mánager:");
			in = new Scanner(System.in);
			id = in.nextInt();
			if (id > 0)
				valido = true;
			else
				System.out.println("Valor incorrecto para el identificador.");
		} while (!valido);

		valido = false;
		do {
			in = new Scanner(System.in);
			System.out.println("Introduzca el telefono de empresa del nuevo mánager");
			telefono = in.nextLine();
			valido = Validaciones.validarTelefono(telefono);
			if (!valido)
				System.out.println("ERROR: El valor introducido para el teéfono no es válido.");
		} while (!valido);

		valido = false;
		do {
			in = new Scanner(System.in);
			System.out.println("Introduzca la dirección del nuevo mánager:");
			direccion = in.next();
			valido = Validaciones.validarDireccion(direccion);
			if (!valido)
				System.out.println("ERROR: El valor introducido para la dirección no es válido.");
		} while (!valido);

		System.out.println("Introduzca ahora los datos personales:");
		in = new Scanner(System.in);
		dp = DatosPersona.nuevaPersona();

		ret = new Manager(id, telefono, direccion, dp);
		return ret;
	}

	/***
	 * Función que devuelve una cadena de caracteres con los datos del mánager en el
	 * siguiente formato: <idManager> <nombre> ” (” <documentacion> ”) del año ”
	 * <fechaNac.año> ” Tfno1: <Manager.telefono>” ,Tfno2: ” <DatosPersona.telefono>
	 */
	@Override
	public String toString() {
		return "" + id + ". " + persona.getNombre() + " (" + persona.getNifnie().mostrar() + ") del año "
				+ persona.getFechaNac().getYear() + " Tfno1: " + telefono + " , Tfno2:" + persona.getTelefono() + "]";
	}

	//// Examen 6 Ejercicio 3
	/**
	 * Función que devuelve una cadena de caracteres con la siguiente estructura
	 * <DatosPersona.id>|<DatosPersona.nombre>|<DatosPersona.documentacion>|<DatosPersona.fec
	 * haNac>|<DatosPersona.telefono>|<Manager.id>|<Manager.telefono>|<Manager.direccion>
	 * Cada campo se separa mediante el caracter '|'
	 * 
	 * @return
	 */
	public String data() {
		return "" + persona.getId() + "|" + persona.getNombre() + "|" + persona.getNifnie().mostrar() + "|"
				+ persona.getFechaNac().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "|" + persona.getTelefono()
				+ "|" + this.id + "|" + this.telefono + "|" + this.direccion;
	}

	/***
	 * Función para exportar los datos de cada uno de los mánagers de una colección
	 * que se le pasa como parámetro, a través del método data() anterior, separando
	 * la información de cada mánager en una línea distinta.
	 * 
	 * @param managers la coleccion de managers a exportar
	 */
	private static void exportar(Manager[] managers) {
		String path = "managers.txt";
		File fichero = new File(path);
		FileWriter escritor = null;
		PrintWriter buffer = null;
		try {
			try {
				escritor = new FileWriter(fichero, false);
				buffer = new PrintWriter(escritor);
				for (Manager m : managers) {
					buffer.println(m.data());
				}
			} finally {
				if (buffer != null) {
					buffer.close();
				}
				if (escritor != null) {
					escritor.close();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Se ha producido una FileNotFoundException" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Se ha producido una IOException" + e.getMessage());
		} catch (Exception e) {
			System.out.println("Se ha producido una Exception" + e.getMessage());
		}
	}

	/// Examen 10 ejercicio 11
	@Override
	public boolean insertarConID(Manager m) {
		boolean ret = false;
		Connection conex = ConexBD.establecerConexion();
		String consultaInsertStr = "insert into managers(id, idpersona, telefono, direccion) values (?,?,?,?)";
		try {
			PreparedStatement pstmt = conex.prepareStatement(consultaInsertStr);

			pstmt.setLong(1, m.getId());
			pstmt.setLong(2, m.getPersona().getId());
			pstmt.setString(3, m.getTelefono());
			pstmt.setString(4, m.getDireccion());
			int resultadoInsercion = pstmt.executeUpdate();
			ret = (resultadoInsercion == 1);
		} catch (SQLException e) {
			System.out.println("Se ha producido una SQLException:" + e.getMessage());
			e.printStackTrace();
		}
		return ret;
	}

	/// Examen 10 ejercicio 11
	@Override
	public long insertarSinID(Manager m) {
		long ret = -1;
		Connection conex = ConexBD.establecerConexion();
		String consultaInsertStr = "insert into managers(idpersona, telefonoprof, direccion) values (?,?,?)";
		try {
			PreparedStatement pstmt = conex.prepareStatement(consultaInsertStr);
			pstmt.setLong(1, m.getPersona().getId());
			pstmt.setString(2, m.getTelefono());
			pstmt.setString(3, m.getDireccion());
			int resultadoInsercion = pstmt.executeUpdate();
			if (resultadoInsercion == 1) {
				String consultaSelect = "SELECT id FROM managers WHERE (idpersona=? AND telefono=? "
						+ "AND direccion=?)";
				PreparedStatement pstmt2 = conex.prepareStatement(consultaSelect);
				pstmt2.setLong(1, m.getPersona().getId());
				pstmt2.setString(2, m.getTelefono());
				pstmt2.setString(3, m.getDireccion());
				ResultSet result = pstmt2.executeQuery();
				while (result.next()) {
					long id = result.getLong("id");
					if (id != -1)
						ret = id;
				}
				result.close();
				pstmt2.close();
			}
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("Se ha producido una SQLException:" + e.getMessage());
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			System.out.println("Se ha producido una Exception:" + e.getMessage());
			e.printStackTrace();
			return -1;
		}

		return ret;
	}

	/// Examen 10 ejercicio 11
	@Override
	public Manager buscarPorID(long id) {
		Manager ret = null;
		Connection conex = ConexBD.establecerConexion();
		String consultaInsertStr = "select * FROM managers WHERE id=?";
		try {
			PreparedStatement pstmt = conex.prepareStatement(consultaInsertStr);
			pstmt.setLong(1, id);
			ResultSet result = pstmt.executeQuery();
			while (result.next()) {
				long idBD = result.getLong("id");
				long idPersona = result.getLong("idpersona");
				String telefono = result.getString("telefono");
				String direccion = result.getString("direccion");
				ret = new Manager();
				ret.setId(idBD);
				ret.setTelefono(telefono);
				ret.setDireccion(direccion);
				ret.setPersona(Datos.buscarPersonaPorId(idPersona));
			}
		} catch (SQLException e) {
			System.out.println("Se ha producido una SQLException:" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Se ha producido una Exception:" + e.getMessage());
			e.printStackTrace();
		}
		return ret;
	}

}
