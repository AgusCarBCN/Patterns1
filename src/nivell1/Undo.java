package nivell1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;

public class Undo {

	// instancia de Undo
	private static Undo instancia;
	/*
	 * ArrayList donde se almacenan los ultimos metodos introducidos. Se utilizará
	 * en el método history para mostrar un listado de los últimos métodos
	 * introducidos.
	 */
	/*
	 * Para poder simular algo parecido a los comandos undo y redo ,utilizo dos
	 * pilas.Las pilas son estructuras de datos LIFO (el último elemento en entrar
	 * es el primero en salir),por lo que son ideales para crear los métodos undo y
	 * redo.
	 */
	private Stack<String> undoList = new Stack<>();
	private Stack<String> redoList = new Stack<>();
	// Contador y arrayList para almacenar historial de comandos
	private int counter;
	private ArrayList<String> historyList = new ArrayList<>();

	/*
	 * Para implementar Singleton hemos de asegurarnos que no se creará ningún
	 * objeto fuera de la clase Undo. Es por eso que el constructor es private
	 */
	private Undo() {
	}

	/**
	 * Método para obtener instancia. Si no hay una instancia .
	 * 
	 * @return Retorna la instancia.
	 */
	public static Undo getInstancia() {
		if (instancia == null) {
			instancia = new Undo();
		}

		return instancia;
	}

	/**
	 * Método getter de la variable contador para el historial de comandos
	 * 
	 * @return Retorna contador de comandos para historial.
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * Método setter de la variable contador para el historial de comandos
	 * 
	 * @param counter nuevo valor de variable contador
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}

	/**
	 * Método que permite escribir cadenas de texto. Cada vez que se invoque al
	 * método se almacena la cadena de texto en la pila undoList.
	 * 
	 * @param texto
	 */
	public void write(String texto) {
		System.out.println(texto);
		undoList.push(texto);
		historyList.add("write(\"" + texto + "\")");

	}

	/**
	 * Método undo deshace o borra los últimos cambios introducidos por metodo
	 * write.
	 * 
	 */
	public void undo() {
		try {
			// Almacena en variable aux ,el elemento de la cima de la pila,que es el
			// último elemento cuando se invoca al método write.
			String aux = (String) undoList.peek();
			// Borra elemento de la cima de la pila
			undoList.pop();
			// añade elemento en la cima de la pila redoList, de esta manera cuando se
			// invoque al metodo redo(rehacer) podemos rehacer el texto.
			redoList.push(aux);
		} catch (EmptyStackException e) {
			System.err.println("No hay nada que deshacer");
		}
		// muestra los cambios despues de invocar al método undo.
		showtext();
		historyList.add("undo()");

	}

	/**
	 * Método que permite rehacer o recuperar lo deshecho por undo.
	 */
	public void redo() {
		try {
			// Almacena el elemento de la cima de la pila redoList
			String aux = (String) redoList.peek();
			// Borra el elemento de la cima de la pila redoList
			redoList.pop();
			// Añade elemento en la cima de la pila
			undoList.push(aux);
		} catch (EmptyStackException e) {
			System.err.println("No hay nada que rehacer");
		}
		showtext();
		historyList.add("redo()");

	}

	public void showtext() {
		// pila que almacena las cadenas de texto en orden inverido, de
		// esta manera se podrán mostrar en el orden que fueron escritas con el método
		// write.
		Stack<String> undoInver = new Stack<>();
		// Bucle para poder colocar los elementos de manera inversa a undoInver de
		// undoList
		while (undoList.size() > 0) {
			// Apila el primer elemento de la pila undoList en la cima de la pila undoInver.
			undoInver.push(undoList.peek());
			// Borra elemeno de la pila undoList. Esto es necesario para poder acceder al
			// siguiente elemento de undoList
			undoList.pop();
		}

		// Bucle para mostrar el texto en orden de insercion y recuperación de los
		// elementos de la pila en undoList
		while (undoInver.size() > 0) {
			// Imprime elemento de la cima
			System.out.println(undoInver.peek());
			// Recupera elemento de la pila undoInver y lo apila en undoList
			undoList.push(undoInver.peek());
			// Borra elemento de la cima de undoInver..
			undoInver.pop();
		}

	}

	/**
	 * Método que guarda historial de los métodos introducido en fichero de texto.
	 * 
	 */
	public void guardarHistorial() {

		cargarContador();

		try {
			PrintWriter pw = new PrintWriter(new FileWriter("history.txt", true));
			for (String element : historyList) {
				pw.println(" " + counter + " " + element);
				counter++;
			}
			pw.close();
		} catch (Exception e) {
			System.err.println("Error al guardar fichero " + e);
		}
		guardarContador();
	}

	/**
	 * Método que lee el historial del archivo de texto history
	 * 
	 */
	public void history() {

		String line = null;
		try (BufferedReader readFast = new BufferedReader(new FileReader("history.txt"));) {
			while ((line = readFast.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			System.err.println("No hay historial que mostrar");
		}
	}

	// Guarda variable contador en un archivo de texto de manera permanente.
	public void guardarContador() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter("counter.txt"));
			pw.println(counter + "|");
			pw.close();
		} catch (Exception e) {
			System.err.println("Error al guardar contador de historial " + e);
		}
	}

	// Recupera variable contador desde archivo de texto
	public void cargarContador() {
		try {
			File archivo = new File("counter.txt");
			if (archivo.exists()) {
				BufferedReader br = new BufferedReader(new FileReader("counter.txt"));
				String linea;
				while ((linea = br.readLine()) != null) {
					StringTokenizer stoken1 = new StringTokenizer(linea, "|");
					int counter = Integer.parseInt(stoken1.nextToken());
					setCounter(counter);

				}
				br.close();
			} 
		} catch (Exception e) {
			System.err.println("Error al cargar fichero" + e);
		}

	}

}
