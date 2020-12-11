
import java.util.Scanner;
import java.util.Stack;
public class InfijoAporstfijo { 
	private static String postfijo;
	private static String postfix;
	public static void main(String[] args) {
		aPost("");
	}
	public static void aPost(String infijo) {
		//Entrada de datos 
//		System.out.println("*Escribe una expresin algebraica: ");
		Scanner leer = new Scanner(System.in);
		//Depurar la expresion algebraica 
//		String expr = depurar(leer.nextLine());
		String expr = depurar(infijo);
		String[] arrayInfix = expr.split(" ");
		//Declaracin de las pilas
		Stack< String > E = new Stack < String >();
		//Pila entrada
		Stack< String > P = new Stack < String >();
		//Pila temporal para operadores
		Stack< String > S = new Stack < String >();
		//Pila salida
		//Aadir la array a la Pila de entrada (E) 
		boolean tieneIgual = false;
		for (int i = arrayInfix.length - 1; i >= 0; i--){
//			System.out.println();
			E.push(arrayInfix[i]); 
		} 
		try {
			//Algoritmo Infijo a Postfijo
			while (!E.isEmpty()) { //mientras no este vacia
				if(E.peek().equals("=")) {
					E.pop();
					tieneIgual = true;
				}
				switch (pref(E.peek())){ //el parametro es el valor asignado a la jerarquia de operaciones
				case 1: P.push(E.pop()); // borra el elemento de la pila de entrada y la pone en la pila temporal para operadores
						break; 
				case 3: 
				case 4: 
					while(pref(P.peek()) >= pref(E.peek())) {
						S.push(P.pop()); 
						}
					P.push(E.pop());
					break; 
				case 2: 
					while(!P.peek().equals("(")) {
						S.push(P.pop());
						} 
					P.pop();
					E.pop();
					break; 
				default: 
					S.push(E.pop());
					}
				}//Eliminacion de impurezas en la expresiones algebraicas 
			if(tieneIgual) S.push("=");
			postfix = S.toString().replaceAll("[\\]\\[,]", "");
			postfijo = postfix.replaceAll("\\s", "");
			//Mostrar resultados: 
//			System.out.println("Expresion Postfija: " + postfix); 
//			System.out.println("Expresion Postfija: " + postfijo); 
			}catch(Exception ex){ 
				System.out.println("Error en la expresion algebraica"); 
				System.err.println(ex); 
				} 
		}
	//Depurar expresin algebraica 
	private static String depurar(String s) {
		String palabra = "";
		for(int k = 0 ; k<s.length(); k++) {
			palabra += s.charAt(k);
			if(palabra.equals("int") || palabra.equals("double") || palabra.equals("String")) palabra = "";  //aqui se agregan palabras reservadas
		}
		palabra = palabra.replaceAll("\\s", "");
		//Elimina espacios en blanco 
		palabra = "(" + palabra + ")"; 
		String simbols = "=+-*/()"; 
		String str = "";
		//Deja espacios entre operadores 
		for (int i = 0; i < palabra.length(); i++) { 
			if (simbols.contains("" + palabra.charAt(i))) { 
				str += " " + palabra.charAt(i) + " ";
			}else {
				str += palabra.charAt(i);
			}
		} 
		return str.replaceAll("\\s+", " ").trim();
	}
	//Jerarquia de los operadores 
	private static int pref(String op) {
		int prf = 99;
		if (op.equals("^")) prf = 5; 
		if (op.equals("*") ||op.equals("/")) prf = 4;
		if (op.equals("+") ||op.equals("-")) prf = 3;
		if (op.equals(")")) prf = 2; 
		if (op.equals("(")) prf = 1; 
		return prf; 
		}
	public static String getPostfijo() {
		return postfix;
	}
	public static String getPostfix() {
		return postfijo;
	}
	
}
