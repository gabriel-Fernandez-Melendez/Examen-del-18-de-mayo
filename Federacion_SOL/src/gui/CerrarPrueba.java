package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import DAO.AtletaDAO;
import DAO.MetalDAO;
import DAO.PatrocinadorDAO;
import DAO.PruebaDAO;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;

import entidades.Atleta;
import entidades.Lugar;
import entidades.Manager;
import entidades.Metal;
import entidades.Patrocinador;
import entidades.Prueba;
import utils.ConexBD;
import utils.Datos;
import validaciones.Validaciones;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.JCheckBox;

public class CerrarPrueba extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNombre;
	private final ButtonGroup buttonGroupTipo = new ButtonGroup();
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					int idprueba = 1;
					CerrarPrueba frame = new CerrarPrueba(idprueba);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CerrarPrueba(int idprueba) {
		PruebaDAO pDAO = new PruebaDAO(ConexBD.getCon());
		Prueba prueba = pDAO.buscarPorID(idprueba);
		if(prueba!=null) {
			setTitle("Cerrar Prueba" + idprueba);
		}
		else
		setTitle("Cerrar Prueba INDETERMINADA");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 453, 478);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(
				new TitledBorder(null, "Datos de la prueba", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 21, 424, 199);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblIdPrueba = new JLabel("IdPrueba:");
		lblIdPrueba.setBounds(22, 21, 77, 14);
		panel_1.add(lblIdPrueba);

		textField = new JTextField(""+prueba.getId());
		textField.setBounds(86, 14, 86, 20);
		panel_1.add(textField);
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setColumns(10);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(22, 48, 77, 14);
		panel_1.add(lblNombre);

		textFieldNombre = new JTextField(prueba.getNombre());
		textFieldNombre.setBounds(86, 41, 261, 20);
		panel_1.add(textFieldNombre);
		textFieldNombre.setEnabled(false);
		textFieldNombre.setEditable(false);
		textFieldNombre.setColumns(10);

		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setBounds(22, 73, 46, 14);
		panel_1.add(lblFecha);

		JLabel lblLugar = new JLabel("Lugar:");
		lblLugar.setBounds(200, 73, 46, 14);
		panel_1.add(lblLugar);

		JComboBox comboBoxLugar = new JComboBox();
		comboBoxLugar.setBounds(245, 69, 169, 22);
		panel_1.add(comboBoxLugar);
		comboBoxLugar.setEnabled(false);
		comboBoxLugar.setModel(new DefaultComboBoxModel(Lugar.values()));

		JPanel panel = new JPanel();
		panel.setBounds(22, 98, 210, 52);
		panel_1.add(panel);
		panel.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Tipo de Prueba:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		JRadioButton rbIndividual = new JRadioButton("Individual");
	
		rbIndividual.setEnabled(false);
		buttonGroupTipo.add(rbIndividual);
		panel.add(rbIndividual);

		JRadioButton rbEquipos = new JRadioButton("Por Equipos");
		rbEquipos.setEnabled(false);
		buttonGroupTipo.add(rbEquipos);
		panel.add(rbEquipos);
		if(prueba.isIndividual()) 
			rbIndividual.setSelected(true);
		else
			rbEquipos.setSelected(true);

		//aqui entan las labels de los puestos
		JLabel lblPatrocinador = new JLabel("Patrocinador:");
		lblPatrocinador.setBounds(22, 161, 87, 14);
		panel_1.add(lblPatrocinador);

		JLabel lblprimerpuesto = new JLabel("Primer puesto *:");
		lblprimerpuesto.setBounds(20, 219, 109, 14);
		contentPane.add(lblprimerpuesto);

		JLabel lblsegundopuesto = new JLabel("Segundo puesto *:");
		lblsegundopuesto.setBounds(10, 294, 109, 14);
		contentPane.add(lblsegundopuesto);

		JLabel lbltercerpuesto = new JLabel("Tercer puesto *:");
		lbltercerpuesto.setBounds(10, 362, 109, 14);
		contentPane.add(lbltercerpuesto);

		DefaultComboBoxModel<Atleta> atletasModel = new DefaultComboBoxModel<Atleta>();
		AtletaDAO aDAO = new AtletaDAO(ConexBD.getCon());
		ArrayList<Atleta> atletassList = (ArrayList<Atleta>) aDAO.buscarTodos();
		/// Pero el modelo de comboBox básico requiere Strings
		//aqui  he cambiado el tamaño de size a un  numero concreto, no los importo todos al array de cadenas pero ejecuta la ventana 
		String[] atletasStr = new String[81];
		for (int i = 0; i < 81; i++) {
			//esta linea que los imprime por consola fue para comprobar a nivel personal si daba un nuloen algun punto 
			System.out.println(atletassList.get(i).getPersona().data());
			atletasStr[i] = atletassList.get(i).getPersona().data();
		}
		
		DefaultComboBoxModel<Metal> metalModel = new DefaultComboBoxModel<Metal>();
		MetalDAO mDAO = new MetalDAO(ConexBD.getCon());
		ArrayList<Metal> metalList = new ArrayList<Metal>();
		/// Pero el modelo de comboBox básico requiere Strings
		//aqui  he cambiado el tamaño de size a un  numero concreto, no los importo todos al array de cadenas pero ejecuta la ventana 
		String[] metalStrOro = new String[20];
		for (Metal o:Datos.OROS) {
			metalList.add(o);
		}
		for(int i=0;i<20;i++) {
			System.out.println(metalList.get(i).toString());
			metalStrOro[i]=metalList.get(i).toString();
		}
		
		DefaultComboBoxModel<Metal> metalModel2 = new DefaultComboBoxModel<Metal>();
		ArrayList<Metal> metalList2 = new ArrayList<Metal>();
		/// Pero el modelo de comboBox básico requiere Strings
		//aqui  he cambiado el tamaño de size a un  numero concreto, no los importo todos al array de cadenas pero ejecuta la ventana 
		String[] metalStrplata = new String[20];
		for (Metal o:Datos.PLATAS) {
			metalList.add(o);
		}
		for(int i=0;i<20;i++) {
			System.out.println(metalList.get(i).toString());
			metalStrplata[i]=metalList.get(i).toString();
		}
		
		DefaultComboBoxModel<Metal> metalModel3 = new DefaultComboBoxModel<Metal>();
		ArrayList<Metal> metalList3 = new ArrayList<Metal>();
		/// Pero el modelo de comboBox básico requiere Strings
		//aqui  he cambiado el tamaño de size a un  numero concreto, no los importo todos al array de cadenas pero ejecuta la ventana 
		String[] metalStrbronce = new String[20];
		for (Metal o:Datos.BRONCES) {
			metalList.add(o);
		}
		for(int i=0;i<20;i++) {
			System.out.println(metalList.get(i).toString());
			metalStrbronce[i]=metalList.get(i).toString();
		}
		
		
		
		


		JComboBox<Atleta> comboBoxPuesto1 = new JComboBox<Atleta>();
		comboBoxPuesto1.setModel(new DefaultComboBoxModel(atletasStr));		
		lblprimerpuesto.setLabelFor(comboBoxPuesto1);
		comboBoxPuesto1.setBounds(99, 215, 287, 22);
		contentPane.add(comboBoxPuesto1);

		JComboBox<Atleta> comboBoxPuesto2 = new JComboBox<Atleta>();
		comboBoxPuesto2.setModel(new DefaultComboBoxModel(atletasStr));
		lblsegundopuesto.setLabelFor(comboBoxPuesto2);
		comboBoxPuesto2.setBounds(117, 290, 287, 22);
		contentPane.add(comboBoxPuesto2);

		JComboBox<Atleta> comboBoxPuesto3 = new JComboBox<Atleta>();
		comboBoxPuesto3.setModel(new DefaultComboBoxModel(atletasStr));
		lbltercerpuesto.setLabelFor(comboBoxPuesto3);
		comboBoxPuesto3.setBounds(99, 358, 287, 22);
		contentPane.add(comboBoxPuesto3);
		

		LocalDate hoyMas1MesLD = LocalDate.now().plusMonths(1);
		java.util.Date hoyMas1Mes = new Date(hoyMas1MesLD.getYear() - 1900, hoyMas1MesLD.getMonthValue() - 1,
				hoyMas1MesLD.getDayOfMonth());

		/// Las siguientes lineas seria lo deseable: trabajar diectamente con objetos en
		/// el model del comboBox
		DefaultComboBoxModel<Patrocinador> patrocinadoresModel = new DefaultComboBoxModel<Patrocinador>();
		JComboBox<Patrocinador> comboBoxPatrocinador = new JComboBox<Patrocinador>(patrocinadoresModel);
		PatrocinadorDAO patDAO = new PatrocinadorDAO(ConexBD.getCon());
		ArrayList<Patrocinador> patrocinadoresList = (ArrayList<Patrocinador>) patDAO.buscarTodos();
		for (Patrocinador pat : patrocinadoresList)
			comboBoxPatrocinador.addItem(pat);

		/// Pero el modelo de comboBox básico requiere Strings
		String[] patrocinadoresStr = new String[patrocinadoresList.size()];
		for (int i = 0; i < patrocinadoresList.size(); i++)
			patrocinadoresStr[i] = patrocinadoresList.get(i).mostrarBasico();
		comboBoxPatrocinador.setModel(new DefaultComboBoxModel(patrocinadoresStr));
		comboBoxPatrocinador.setBounds(96, 157, 261, 22);
		panel_1.add(comboBoxPatrocinador);
		comboBoxPatrocinador.setEnabled(false);

		JSpinner spinnerFecha = new JSpinner();
		spinnerFecha.setBounds(86, 69, 86, 20);
		panel_1.add(spinnerFecha);
		spinnerFecha.setEnabled(false);
		spinnerFecha.setModel(new SpinnerDateModel(hoyMas1Mes, hoyMas1Mes, null, Calendar.DAY_OF_YEAR));

		JButton buttonAceptar = new JButton("Aceptar");
		buttonAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				boolean valido = false;
				String titulo = "";
				String msj = "";
				String errores = "";
				/// Tomar cada campo y validarlo. Si alguno no es correcto, avisar al usuario
//				String nombre = textFieldNombre.getText();
//				valido = Validaciones.validarNombrePrueba(nombre);
//				if (!valido) {
//					errores += "El nombre de la prueba no es válido (5-150 caracteres).\n";
//					lblNombre.setForeground(Color.RED);
//				} else
//					nueva.setNombre(nombre);
//				valido = false;
//
//				java.util.Date fecha = (java.util.Date) spinnerFecha.getValue();
//				valido = Validaciones.validarFechaNuevaPrueba(fecha);
//				if (!valido) {
//					errores += "La fecha de la prueba no es válido (posterior a 1 mes desde hoy).\n";
//					lblFecha.setForeground(Color.RED);
//				} else {
//					LocalDate fechaLD = LocalDate.of(fecha.getYear() + 1900, fecha.getMonth() + 1, fecha.getDate());
//					nueva.setFecha(fechaLD);
//				}
//				valido = false;
//				valido = (comboBoxLugar.getSelectedIndex() != -1);
//				if (!valido) {
//					errores += "Debe seleccionar el lugar de celebración de la nueva prueba.\n";
//					lblLugar.setForeground(Color.RED);
//				} else {
//					Lugar lugar = (Lugar) comboBoxLugar.getSelectedItem();
//					nueva.setLugar(lugar);
//				}
//				valido = false;
//				valido = !(!rbIndividual.isSelected() && !rbEquipos.isSelected())
//						|| (rbIndividual.isSelected() && rbEquipos.isSelected());
//				if (!valido) {
//					errores += "Debe seleccionar si la nueva prueba es individual O por equipos.\n";
//					rbIndividual.setForeground(Color.RED);
//					rbEquipos.setForeground(Color.RED);
//				} else {
//					nueva.setIndividual(rbIndividual.isSelected() ? true : false);
//				}
//				valido = false;
//				valido = (comboBoxPatrocinador.getSelectedIndex() != -1);
//				if (!valido) {
//					errores += "Debe seleccionar el Patrocinador de la nueva prueba.\n";
//					lblPatrocinador.setForeground(Color.RED);
//				} else {
//					PatrocinadorDAO patDAO = new PatrocinadorDAO(ConexBD.getCon());
//					String seleccionado = (String) comboBoxPatrocinador.getSelectedItem();
//					String[] aux = seleccionado.split("\\.");
//					long idPat = Long.valueOf(aux[0]);
//					Patrocinador patrocinador = patDAO.buscarPorID(idPat);
//					ConexBD.cerrarConexion();
//					if (patrocinador == null)
//						valido = false;
//					else
//						nueva.setPatrocinador(patrocinador);
//				}
				
				
				
				valido = errores.isEmpty();

				if (!valido) {
					titulo = "ERROR: Campos inválidos";
					msj = "ERROR: los siguientes campos NO son válidos:\n\n";
					if (!errores.isEmpty())
						msj += errores + "\n";
					JOptionPane.showMessageDialog(null, msj, titulo, JOptionPane.ERROR_MESSAGE);
					return;
				}

				/// Si todos los datos son correctos, llamar a PruebaDAO para insertar en la BD
				PruebaDAO pruebadao = new PruebaDAO(ConexBD.establecerConexion());
				boolean correcto = pruebadao.modificar(prueba);
				/// Tanto si la inserción de la nueva prueba tiene éxito como si no, avisar al
				/// usuario
				if (!correcto) {
					// hubo error al insertar en BD y no se generó la nueva prueba
					titulo = "ERROR al cerrar la Prueba en la BD";
					msj = "Hubo un error y NO se ha cerrado la prueba en la BD.";
					JOptionPane.showMessageDialog(null, msj, titulo, JOptionPane.ERROR_MESSAGE);
				} else {
					titulo = "Prueba "+prueba.getId()+" cerrada en la BD";
					msj = "Se ha cerrado correctamente la  prueba:\n" + prueba.toString();
					JOptionPane.showMessageDialog(null, msj, titulo, JOptionPane.INFORMATION_MESSAGE);
					/// Aqui se redirigiría al usuario hacia la pantalla principal
					/// TODO
				}
			}
		});
		buttonAceptar.setBounds(230, 405, 89, 23);
		contentPane.add(buttonAceptar);

		JButton buttonCancelar = new JButton("Cancelar");
		buttonCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String titulo = "Cerrar ventana";
				String msj = "¿Realmente desea cerrar la ventana?";
				int opcion = JOptionPane.showConfirmDialog(null, msj, titulo, JOptionPane.OK_CANCEL_OPTION);
				if (opcion == JOptionPane.YES_OPTION) {
					/// Aqui se redirigiría al usuario hacia la pantalla principal o se saldria
					System.exit(0); /// SALIR directamente
				}

			}
		});
		buttonCancelar.setBounds(335, 405, 89, 23);
		contentPane.add(buttonCancelar);
		
		JLabel lblNewLabel = new JLabel("id Oro*: ");
		lblNewLabel.setBounds(53, 269, 46, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("id Plata*:");
		lblNewLabel_1.setBounds(35, 338, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("id Bronce*:");
		lblNewLabel_2.setBounds(10, 405, 72, 14);
		contentPane.add(lblNewLabel_2);
		
		JSpinner spinner = new JSpinner();
		spinner.setBounds(83, 244, 30, 20);
		contentPane.add(spinner);
		
		JLabel lblNewLabel_3 = new JLabel("Hora");
		lblNewLabel_3.setBounds(53, 244, 46, 14);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Minutos");
		lblNewLabel_4.setBounds(117, 244, 46, 14);
		contentPane.add(lblNewLabel_4);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setBounds(160, 244, 30, 20);
		contentPane.add(spinner_1);
		
		JLabel lblNewLabel_5 = new JLabel("Segundos");
		lblNewLabel_5.setBounds(196, 248, 58, 14);
		contentPane.add(lblNewLabel_5);
		
		JSpinner spinner_2 = new JSpinner();
		spinner_2.setBounds(257, 244, 30, 20);
		contentPane.add(spinner_2);
		
		JLabel lblNewLabel_6 = new JLabel("Centesimas");
		lblNewLabel_6.setBounds(292, 247, 46, 14);
		contentPane.add(lblNewLabel_6);
		
		JSpinner spinner_3 = new JSpinner();
		spinner_3.setBounds(347, 244, 30, 20);
		contentPane.add(spinner_3);
		
		JComboBox comboBoxOro = new JComboBox<Metal>();
		comboBoxOro.setModel(new DefaultComboBoxModel(metalStrOro));
		comboBoxOro.setBounds(99, 268, 155, 22);
		contentPane.add(comboBoxOro);
		
		
		JLabel lblNewLabel_7 = new JLabel("Hora");
		lblNewLabel_7.setBounds(53, 313, 46, 14);
		contentPane.add(lblNewLabel_7);
		
		JSpinner spinner_4 = new JSpinner();
		spinner_4.setBounds(83, 319, 30, 14);
		contentPane.add(spinner_4);
		
		JLabel lblNewLabel_8 = new JLabel("Minutos");
		lblNewLabel_8.setBounds(117, 319, 46, 14);
		contentPane.add(lblNewLabel_8);
		
		JSpinner spinner_5 = new JSpinner();
		spinner_5.setBounds(160, 318, 30, 14);
		contentPane.add(spinner_5);
		
		JLabel lblNewLabel_9 = new JLabel("Segundos");
		lblNewLabel_9.setBounds(196, 319, 58, 14);
		contentPane.add(lblNewLabel_9);
		
		JSpinner spinner_6 = new JSpinner();
		spinner_6.setBounds(257, 319, 30, 14);
		contentPane.add(spinner_6);
		
		JLabel lblNewLabel_10 = new JLabel("Centesimas");
		lblNewLabel_10.setBounds(292, 313, 46, 20);
		contentPane.add(lblNewLabel_10);
		
		JSpinner spinner_7 = new JSpinner();
		spinner_7.setBounds(335, 316, 30, 14);
		contentPane.add(spinner_7);
		
		JComboBox comboBoxplata = new JComboBox<Metal>();
		comboBoxplata.setModel(new DefaultComboBoxModel(metalStrplata));
		comboBoxplata.setBounds(83, 334, 155, 22);
		contentPane.add(comboBoxplata);
		
		JLabel lblNewLabel_11 = new JLabel("Hora");
		lblNewLabel_11.setBounds(53, 384, 46, 14);
		contentPane.add(lblNewLabel_11);
		
		JSpinner spinner_8 = new JSpinner();
		spinner_8.setBounds(83, 381, 30, 20);
		contentPane.add(spinner_8);
		
		JLabel lblNewLabel_12 = new JLabel("Minutos");
		lblNewLabel_12.setBounds(117, 384, 46, 14);
		contentPane.add(lblNewLabel_12);
		
		JSpinner spinner_9 = new JSpinner();
		spinner_9.setBounds(160, 381, 30, 20);
		contentPane.add(spinner_9);
		
		JLabel lblNewLabel_13 = new JLabel("Segundos");
		lblNewLabel_13.setBounds(192, 380, 62, 14);
		contentPane.add(lblNewLabel_13);
		
		JSpinner spinner_10 = new JSpinner();
		spinner_10.setBounds(257, 381, 30, 20);
		contentPane.add(spinner_10);
		
		JLabel lblNewLabel_14 = new JLabel("Centesimas");
		lblNewLabel_14.setBounds(292, 380, 46, 14);
		contentPane.add(lblNewLabel_14);
		
		JSpinner spinner_11 = new JSpinner();
		spinner_11.setBounds(335, 381, 30, 20);
		contentPane.add(spinner_11);
		
		JComboBox comboBoxbronce = new JComboBox<Metal>();
		comboBoxbronce.setModel(new DefaultComboBoxModel(metalStrbronce));
		comboBoxbronce.setBounds(63, 405, 157, 14);
		contentPane.add(comboBoxbronce);
		
		JLabel lblNewLabel_15 = new JLabel("Establecer como DEFINITIVO: ");
		lblNewLabel_15.setBounds(10, 425, 153, 14);
		contentPane.add(lblNewLabel_15);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("");
		chckbxNewCheckBox.setBounds(160, 421, 30, 23);
		contentPane.add(chckbxNewCheckBox);

	}
	private static void exportar(Prueba b) {
		String path = "resultado_prueba"+b.getId()+".txt";
		File fichero = new File(path);
		FileWriter escritor = null;
		PrintWriter buffer = null;
		String ret="";
		try {
			try {
				escritor = new FileWriter(fichero, false);
				buffer = new PrintWriter(escritor);
			
					buffer.println("Resultado de la prueba "+b.getId()+b.getNombre()+" el pasado" 
							+b.getFecha()+ "en" +b.getFecha()+
							//no tuve tiempo a cambiar todo el string pero dejo con el comentario que el proceso seria ir usando get y set hasta comentar la informacion que debe de entrar ne el fichero 
							"Primer puesto para <nombreAtleta_1> (<NIFNIEAtleta_1>), con un tiempo de \r\n"
							+ "<tiempo1(hh:mm:ss,cc)>. Se le otorga el oro <idOro> de pureza <purezaOro>%. \r\n"
							+ "Segundo puesto para <nombreAtleta_2> (<NIFNIEAtleta_2>), con un tiempo de \r\n"
							+ "<tiempo2(hh:mm:ss,cc)>. Se le otorga la plata <idPlata> de pureza \r\n"
							+ "<purezaPlata>%. \r\n"
							+ "Tercer puesto para <nombreAtleta_3> (<NIFNIEAtleta_3>), con un tiempo de \r\n"
							+ "<tiempo3(hh:mm:ss,cc)>. Se le otorga el bronce <idBronce> de pureza \r\n"
							+ "<purezaBronce>%. \r\n"
							+ "Resultado <idResultado> cerrado a las <hh:mm:ss> del día <dd/MM/yyyy>. ");
				
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
}
