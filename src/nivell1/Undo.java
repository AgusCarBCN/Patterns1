package nivell1;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class Undo {

	//instancia de Undo
	private static Undo instancia;
	/*ArrayList donde se almacenan los ultimos metodos introducidos. Se utilizará en el método history para mostrar un listado 
	de los últimos métodos introducidos.
	*/
	private ArrayList<String> historyList = new ArrayList<>();
	/*
	 * Para poder simular algo parecido a los comandos undo y redo ,utilizo dos
	 * pilas.Las pilas son estructuras de datos LIFO (el último elemento en entrar
	 *es el primero en salir),por lo que son ideales para crear los métodos undo y redo.
	 */
	private Stack<String> undoList = new Stack<>();
	private Stack<String> redoList = new Stack<>();
	
	/*Para implementar Singleton hemos de asegurarnos que no se creará ningún objeto fuera de la clase Undo.
	Es por eso que el constructor es private*/
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
	 * Método que permite escribir cadenas de texto. Cada vez que se invoque al método 
	 * se almacena la cadena de texto en la pila undoList.
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
			//Borra elemento de la cima de la pila
			undoList.pop();
			//añade elemento en la cima de la pila redoList, de esta manera cuando se invoque al metodo redo(rehacer) podemos rehacer el texto.
			redoList.push(aux);
		} catch (EmptyStackException e) {
			System.err.println("No hay nada que deshacer");		
		}
		//muestra los cambios despues de invocar al método undo.
		showtext();
		historyList.add("undo()");
	}
/**
 * Método que  permite rehacer o recuperar lo deshecho por undo.
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
		//pila que almacena las cadenas de texto en orden inverido, de 
		//esta manera se podrán mostrar en el orden que fueron escritas con el método write.
		Stack<String> undoInver = new Stack<>();	
		//Bucle para poder colocar los elementos de manera inversa a undoInver de undoList
		while (undoList.size() > 0) {
		//Apila el primer elemento de la pila undoList en la cima de la pila undoInver.
			undoInver.push(undoList.peek());
		//Borra elemeno de la pila undoList. Esto es necesario para poder acceder al siguiente elemento de undoList
			undoList.pop();
		}

		//Bucle para mostrar el texto en orden de insercion y recuperación de los elementos de la pila en undoList
		while (undoInver.size() > 0) {
		//Imprime elemento de la cima
			System.out.println(undoInver.peek());
		//Recupera elemento de la pila undoInver y lo apila en undoList
			undoList.push(undoInver.peek());
		//Borra elemento de la cima de undoInver..
			undoInver.pop();
		}
		
	}
/**
 * Método que muestra historial de los métodos introducidos
 * 
 */
	public void history() {
		int i = 1;
		for (String element : historyList) {
			System.out.println(" " + i + "  " + element);
			i++;
		}

	}

}
