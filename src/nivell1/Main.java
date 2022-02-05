package nivell1;

/**
 * Crea una classe que repliqui el funcionament del comando 'Undo'. Aquesta
 * classe serà utilitzada per la classe Main, que permetrà introduir opcions per
 * consola.
 * 
 * @author Agustin Carnerero Peña
 */

public class Main {

	public static void main(String[] args) {
		Undo undo = Undo.getInstancia();

		// undo.undo();
		undo.write("cadena1");
		undo.write("cadena2");
		undo.write("cadena3");
		System.out.println("---");
		// Deshace la última llamada de write con undo
		undo.undo();
		System.out.println("---");
		// Rehace lo que deshizo redo
		undo.redo();
		System.out.println("---");
		// A la cuarta llamada de undo salta mensaje de que la pila esta vacia.
		// no hay nada que deshacer.
		undo.undo();
		System.out.println("---");
		undo.undo();
		System.out.println("---");
		undo.undo();
		undo.undo();
		// historial de las llamadas a metodos
		System.out.println("---");
		undo.history();

	}

}
