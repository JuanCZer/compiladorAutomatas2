import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Pattern;

public class triplo {
	static int lin = 1;
	static boolean variosDo = false;
	static InfijoAporstfijo postfijo = new InfijoAporstfijo();
	static ArrayList<String> inicioDo = new ArrayList<String>();
	static ArrayList<String> linea = new ArrayList<String>();
	static ArrayList<String> Dato_Origen = new ArrayList<String>();
	static ArrayList<String> Dato_Fuente = new ArrayList<String>();
	static ArrayList<String> Operador = new ArrayList<String>();
	static ArrayList<String> opti = new ArrayList<String>();
	static ArrayList<String> var = new ArrayList<String>();
	static ArrayList<String> nuevasVar = new ArrayList<String>();
	static boolean elseOn  = false;
	static boolean enUso = false;
	static int contadorVar = 0;
	static String codigoOptimizado = "";
	static int veces = 2;

	public static void hacer_triplo(String archivo) {
		// TODO Auto-generated method stub
		String infijo =  "";
		String content = null;                          //contenido del fichero
		File file = new File(archivo); //ruta completa al fichero que deseamos leer

		try {
			//leyendo el archivo completo
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
			boolean do_while = false, inicioWhile = false, inicioIf = false;
			//eliminamos espacios en blanco (opcional) \\s o saltos de linea con \n
			String [] optimizado2 = content.split("\n");
			String [] optim = content.split("\n");
			content = content.replaceAll("\n","").replace("\r", "");

			String optimizado = new String(chars);
			optimizado = content.replaceAll(" ","").replace("\r", "");
			

			optimizado = optimizado.replace("int", "");
			optimizado = optimizado.replace("string", "");
			optimizado = optimizado.replace("float", "");
			optimizado = optimizado.replace("boolean", "");
			optimizado = optimizado.replace("	", "");
			
			for (int i=0; i< optimizado2.length;i++) {
				optimizado2[i] = optimizado2[i].replace("int ", "");
				optimizado2[i] = optimizado2[i].replace("string ", "");
				optimizado2[i] = optimizado2[i].replace("float ", "");
				optimizado2[i] = optimizado2[i].replace("boolean ", "");
				optimizado2[i] = optimizado2[i].replace("char ", "");
				optimizado2[i] = optimizado2[i].replace("	", "");
//				System.out.print(optimizado2[i]);
			}
			String pal = "";
			String variable = "";
			boolean bandera = false;
			for(int i = 0; i < optimizado.length(); i++) {
				if(optimizado.charAt(i)=='=') {
					variable = ""+optimizado.charAt((i-1));
					//System.out.print(variable);
					bandera = true;
				}
//				if(bandera) {
//
//					if(optimizado.charAt(i+1)==';') {
//						pal += optimizado.charAt(i);
//					}else  if(optimizado.charAt(i)==';') {
//						pal += optimizado.charAt(i);
//					}
//					else{
//						pal += optimizado.charAt(i)+" ";
//					}
//				}
				if(bandera) {
					
					if(optimizado.charAt(i+1)==';') {
						pal += optimizado.charAt(i);
					}else  if(optimizado.charAt(i)==';') {
						pal += optimizado.charAt(i);
					}
					else{
						try {
							Integer.parseInt(""+optimizado.charAt(i+1));
							Integer.parseInt(""+optimizado.charAt(i));
							pal += optimizado.charAt(i)+"";
						}catch (NumberFormatException e) {
							pal += optimizado.charAt(i)+" ";
						}		
					}
				}
				String alt = "" + optimizado.charAt(i);
				if(alt.contains(";")) {
//					System.out.println(pal);
					opti.add(pal);
					var.add(variable);
					pal = "";
					variable = "";
					bandera = false;
				}
			}

//			System.out.println(optimizado2[0].charAt(0));
			for(int i =1; i < optimizado2.length; i++) {
				for(int j =0; j < opti.size(); j++) {
					if(!(""+optimizado2[i].charAt(0)).equals(var.get(j))) {
						String guardar = (""+optimizado2[i].charAt(0));
						if(optimizado2[i].contains(opti.get(j))) {
//						System.out.println("++++++++++++++++++++ " + optimizado2[i] + "** " + var.get(j));
							optimizado2[i] = "linea";
							optim[i] = "no";
							for(int m =0; m < optimizado2.length; m++) {
								if(optimizado2[m].contains(guardar)) {
									optimizado2[m] = optimizado2[m].replaceAll(guardar, var.get(j));
								}
							}
							for(int o =0; o < var.size(); o++) {
								if(var.get(o).equals(guardar)) {
									var.remove(o);
									opti.remove(o);
								}
							}
						}
					}
				}
			}

			boolean noSeUsa = false;
			int linea4 = 10000;
			for(int j =0; j < var.size(); j++) {
				for(int i =0; i < optimizado2.length; i++) {
					if(!optimizado2[i].contains(opti.get(j))) {
						if(optimizado2[i].contains(var.get(j))) {
							noSeUsa = true;
						}
					}else {
						linea4 = i;
					}
				}
				if(noSeUsa == false) {
					optimizado2[linea4] = "linea";
					optim[linea4] = "no";
					linea4 = 10000;
				}
				noSeUsa = false;
			}
			//sustituir por codigo ya usado
			for(int j =0; j < opti.size(); j++) {
				String palAux = opti.get(j);
				palAux = palAux.substring(2, opti.get(j).length());
				for(int i =0; i < optimizado2.length; i++) {
					optimizado2[i] = optimizado2[i].replaceAll("\n","").replace("\r", "");
					try {
						String compararPal = optimizado2[i].substring(4);

//				System.out.println("nuevo " + compararPal);
						if( (!compararPal.equals(palAux))) {
							if(optimizado2[i].contains("a + b;")) {
								optimizado2[i] = optimizado2[i].replace(palAux, var.get(j));
								if(!optimizado2[i].contains(";")) {
									optimizado2[i] = optimizado2[i] +";";
								}
							}
						}
					}catch(IndexOutOfBoundsException e) {}
				}
			}

			for(int i =0; i < optimizado2.length; i++) {
				optimizado2[i] = optimizado2[i].replace(" - 0", "");
				optimizado2[i] = optimizado2[i].replace(" + 0", "");
				optimizado2[i] = optimizado2[i].replace(" * 1", "");
				optimizado2[i] = optimizado2[i].replace(" / 1", "");
			}


			for(int i =0; i < optimizado2.length; i++) {
				if(optimizado2[i].contains("if")) {
					String optimizado3 = optimizado2[i];
					optimizado3 = optimizado3.replace("if (", "");
					optimizado3 = optimizado3.replace(") {", "");
					String [] nose = optimizado3.split(" && ");
					String variableNueva [] = new String [2];
					int cont = 0;
					variableNueva [0] = "i";
					variableNueva [1] = "j";
					for(int k=0; k < nose.length; k++) {
						String nuevo = nose[k].substring(4);
						nuevasVar.add(variableNueva[cont] + " = "+nuevo+";");
						optimizado2[i] = optimizado2[i].replace(nuevo, variableNueva[cont++]);

					}
				}
			}
			String ruta = "src\\textoOptimizado.txt";
			File f = new File(ruta);
			FileWriter fw = null;
			try {
				fw = new FileWriter(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PrintWriter txt = new PrintWriter(fw);


			String contentOpt1 ="";
			for(int i=0; i < nuevasVar.size(); i++) {
				contentOpt1 += nuevasVar.get(i)+"";
				txt.println("int " + nuevasVar.get(i));
			}
			String contentOpt = contentOpt1;
			//String contentOpt = "";
//		System.out.println(contentOpt);
			for(int i =0; i < optimizado2.length; i++) {
				optimizado2[i] = optimizado2[i].replaceAll("\n","").replace("\r", "");
				if(!optimizado2[i].contains("linea")) {
//				System.out.println(optimizado2[i]);

					
							if(optim[i].contains("int")) {
								txt.println("int "+optimizado2[i]);
							}else if(optim[i].contains("string")) {
								txt.println("string "+optimizado2[i]);
							}else if(optim[i].contains("float")) {
								txt.println("float "+optimizado2[i]);
							}else if(optim[i].contains("long")) {
								txt.println("long "+optimizado2[i]);
							}else if(optim[i].contains("char")) {
								txt.println("char "+optimizado2[i]);
							}else {
								txt.println(""+optimizado2[i]);								
							}
							
					contentOpt += optimizado2[i];
				}
			}
			txt.close();
			//Ya esta optimizado y lo pasamos a content que hace el triplo
			content = contentOpt;
			for(int i = 0; i < content.length(); i++) { //recorre cada caractere del archivo  					EMPIEZA
				if(content.charAt(i) == ';') { //indica fin de linea  				FIN DE LINEA
//        		System.out.println(infijo);
					String operadores = "=+-*/";
					boolean operador = false;
					for(int k = 0; k < infijo.length(); k++) {
						if(operadores.contains("" + infijo.charAt(k))) operador = true; //identificamos si tiene operadores para considerarlo como una aritmetica
					}
					if(operador) { //si hay una aritmetica se hace el triplo
						triploAritmetica(infijo);
						operador = false;
					}
//        		System.out.println(infijo);
					infijo = ""; //reseteamos la variable
				}else if(content.charAt(i) == '{'){ //tenemos la linea con la aritmetica en infijo	SE DETECTA UN IF
					triploIf(infijo, "No SE1");
					infijo = ""; //reseteamos la variable
				}else if(content.charAt(i) == '}'){ //FIN DE IF
					infijo = ""; //reseteamos la variable

					if(elseOn) {
						linea.add("" + lin);
						Dato_Origen.add("");
						Dato_Fuente.add("Fin If");
						Operador.add("Fin If");
						elseOn = false;
						for (int o=0; o<Dato_Fuente.size(); o++) {
							if(Dato_Fuente.get(o).equals("Nose")) {
								Dato_Fuente.set(o, (""+lin));
							}
						}

					}else {
						linea.add("" + lin++);
						Dato_Origen.add("");
						Dato_Fuente.add("Nose");
						Operador.add("JMP");
						for (int o=0; o<Operador.size(); o++) {
							if(Operador.get(o).equals("No SE1")) {
								Operador.set(o, (""+lin));
							}
						}
					}

				}else {
					infijo += content.charAt(i);
				}
			}
			//imprimir();
			//Limpia el salto de linea si no hay else
			for (int o=0; o<Dato_Fuente.size(); o++) {
				if(Dato_Fuente.get(o).equals("Nose")) {
					Dato_Fuente.set(o, (""+(Integer.parseInt((linea.get(o)))+1)));
//				Operador.remove(o);
//				Dato_Fuente.remove(o);
//				Dato_Origen.remove(o);
//				Dato_Origen.add(" ");
//  				Dato_Fuente.add("Fin If");
//  				Operador.add("Fin If");
				}
			}
			generarArchivo();
			generarArchivoEnsamblador();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static int triploIf(String aux, String lin2) {
		String aux2 ="";
		String f = "()";
		if(aux.contains("else")) {
		elseOn = true;
		return 0;
		}
		boolean condicion = false;
		for(int i=0;i<aux.length();i++) {
			if(f.contains(("" + aux.charAt(i)))) {
				condicion = true;
				i++;
			}
			if(condicion) {
				aux2 += aux.charAt(i);
			}
		}	
		boolean condicionY = false;
		boolean condicionO = false;

		
		String[] auxiliar3 = aux2.split(" ");
		for (int i=0; i< auxiliar3.length; i++) {
			if(auxiliar3[i].equals("&&")) {
				condicionY = true;
			}
			if(auxiliar3[i].equals("|")) {
				condicionO = true;
			}
		}
		if(condicionY) {
			condicionY = false;
			linea.add("" + lin++);
			Dato_Origen.add("T1");
			Dato_Fuente.add(auxiliar3[0]);
			Operador.add("=");
			
			linea.add("" + lin++);
			Dato_Origen.add("T1");
			Dato_Fuente.add(auxiliar3[2]);
			Operador.add(auxiliar3[1]);
			
			linea.add("" + lin++);
			Dato_Origen.add("TR1");
			Dato_Fuente.add("TRUE");
			Operador.add("" + (lin+1));
			
			linea.add("" + lin++);
			Dato_Origen.add("TR1");
			Dato_Fuente.add("FALSE");
			Operador.add("" + (lin2));
			
			///////////////////////////////////////////////////////////
			linea.add("" + lin++);
			Dato_Origen.add("T1");
			Dato_Fuente.add(auxiliar3[4]);
			Operador.add("=");
			
			linea.add("" + lin++);
			Dato_Origen.add("T1");
			Dato_Fuente.add(auxiliar3[6]);
			Operador.add(auxiliar3[5]);
			
			linea.add("" + lin++);
			Dato_Origen.add("TR1");
			Dato_Fuente.add("TRUE");
			Operador.add("" + (lin+1));
			
			linea.add("" + lin++);
			Dato_Origen.add("TR1");
			Dato_Fuente.add("FALSE");
			Operador.add("" + (lin2));
		}else if(condicionO){
			System.out.println("***************NO FUNCIONA CON `|` EL TRIPLO ESTA INCORRECTO***************"); 
		}else {
			linea.add("" + lin++);
			Dato_Origen.add("T1");
			Dato_Fuente.add(auxiliar3[0]);
			Operador.add("=");
			
			linea.add("" + lin++);
			Dato_Origen.add("T1");
			Dato_Fuente.add(auxiliar3[2]);
			Operador.add(auxiliar3[1]);
			
			linea.add("" + lin++);
			Dato_Origen.add("TR1");
			Dato_Fuente.add("TRUE");
			Operador.add("" + (lin+1));
			
			linea.add("" + lin++);
			Dato_Origen.add("TR1");
			Dato_Fuente.add("FALSE");
			Operador.add("" + (lin2));	
		}
		return 16;
	}
	public static void triploAritmetica(String infijo) {
//		System.out.println("cadena original: " + infijo); //imprime la cadena original
		String operadores = "=+-*/";
		postfijo.aPost(infijo);
		String cadena = postfijo.getPostfijo();
//		System.out.println("++++++++cadena+++++   " +cadena);
		String auxiliar = "", auxiliar2 = "";
		boolean ultimo = false;
		while (!cadena.equals("")) {
			//System.out.println("cadena original: " + cadena); //imprime la cadena original
			for(int k = 0; k<cadena.length(); k++) {
				auxiliar += "" + cadena.charAt(k);
				if(operadores.contains("" + cadena.charAt(k))) {
//					System.out.println("cadena auxiliar: " + auxiliar); //imprime la cadena auxiliar
					String palabraAux = "";
					for(int h = (k+1); h < cadena.length(); h++) { // se pone 1 para no contar el operador
						palabraAux += cadena.charAt(h);
					}
					cadena = palabraAux;
					break;
				}
				
			}
			int contador = 0;
			for(int j = 0; j<auxiliar.length(); j++) {
				if(auxiliar.charAt(j) == ' ') contador++;
			}			
			if(contador == 2) {
				for(int j = 0; j<auxiliar.length(); j++) {
					auxiliar2 += "" + auxiliar.charAt(j);
				}
				ultimo = true;
			}else {//tiene mas de 2 espacios
				if(auxiliar.length()==9){
					for(int j = 4; j<auxiliar.length(); j++) {
						auxiliar2 += "" + auxiliar.charAt(j);
					}
				}else {
					for(int j = 2; j<auxiliar.length(); j++) {
						auxiliar2 += "" + auxiliar.charAt(j);
					}
				}				
			}
			
//			System.out.println("cadena auxiliar2: " + auxiliar2); //imprime la cadena auxiliar2
			String palabraAux = "";
			for(int j = 0; j<(auxiliar.length() - auxiliar2.length()); j++) {
				palabraAux += "" + auxiliar.charAt(j);
			}
			auxiliar = palabraAux;
			
			String[] auxiliar3 = auxiliar2.split(" ");
//			System.out.println("aqui falla   " +auxiliar2);
			String T="T1";
			if(!auxiliar3[0].equals(T)) {
				if(ultimo) {
//					System.out.println("aqui entra   " +auxiliar2);
					linea.add("" + lin++);
					Dato_Origen.add(auxiliar3[0]);
					Dato_Fuente.add(auxiliar3[1]);
					Operador.add(auxiliar3[2]);
    				auxiliar2 = "";
					
				}else {
					linea.add("" + lin++);
					try {
						int a = Dato_Origen.size()-1;
//						System.out.println("+++++++++++++++++++++++++" + Dato_Origen.get(a)+"     " + Dato_Fuente.get(a) + "     " + Operador.get(a));
						if(Dato_Origen.get(a).equals("T1")) {
//							System.out.println("+++++++++++++HOAAAAAAA++++++++++++" );
							enUso = true;
						}else {
							enUso = false;
						}
					}catch (Exception e) {}

//					System.out.println("+++++++++++++HOAAAAAAA++++++++++++" +  auxiliar3[1]);
					if(enUso) {
						Dato_Origen.add("T2");
						Dato_Fuente.add(auxiliar3[0]);
						Operador.add("=");
						auxiliar += "T2";
						linea.add("" + lin++);
						Dato_Origen.add("T2");
						Dato_Fuente.add(auxiliar3[1]);
						Operador.add(auxiliar3[2]);
	    				auxiliar2 = "";
					}else {
						Dato_Origen.add("T1");
						Dato_Fuente.add(auxiliar3[0]);
						Operador.add("=");
						auxiliar += "T1";
						linea.add("" + lin++);
						Dato_Origen.add("T1");
						Dato_Fuente.add(auxiliar3[1]);
						Operador.add(auxiliar3[2]);
	    				auxiliar2 = "";
					}
					
				}
			}else {
				linea.add("" + lin++);
				auxiliar += "T1";
				Dato_Origen.add("T1");
				Dato_Fuente.add(auxiliar3[1]);
				Operador.add(auxiliar3[2]);
				auxiliar2 = "";
			}
				
//			System.out.println("+++++++++++++++++++++++++");
//			System.out.println("cadena original: " + cadena); //imprime la cadena original
//			System.out.println("cadena auxiliar: " + auxiliar); //imprime la cadena auxiliar
//			System.out.println("cadena auxiliar2: " + auxiliar2); //imprime la cadena auxiliar2
//			cadena = "";
			
		}
		
	}
	public static void imprimir() { 
        System.out.println("   | Dato Origen | Dato Fuente | Tipo "); //imprimimos el diseño de lo que sera la tabla de simbolos
        System.out.println("---|-------------|-------------|------");	//seran los espacios que delimitan las columnas
        Iterator<String> nombreIterator = Dato_Origen.iterator();	//codigo para poder recorrer las listas 
        Iterator<String> nombreIterator2 = Dato_Fuente.iterator();
        Iterator<String> nombreIterator3 = Operador.iterator();
        Iterator<String> nombreIterator4 = linea.iterator();
        while(nombreIterator.hasNext()){	//se recorren las listas
            String elemento = nombreIterator.next();
            String elemento2 = nombreIterator2.next();
            String elemento3 = nombreIterator3.next();
            String elemento4 = nombreIterator4.next();
//            System.out.println(elemento);
            for (int j = elemento.length(); j<8; j++) { //le agregamos los espacios en blanco necesarios para que en la impresion se vea estetica
                elemento += " "; //agregamos espacios
            }
            for (int j = elemento2.length(); j<8; j++) { //le agregamos los espacios en blanco necesarios para que en la impresion se vea estetica
                elemento2 += " "; //agregamos espacios
            }
            for (int j = elemento3.length(); j<10; j++) { //le agregamos los espacios en blanco necesarios para que en la impresion se vea estetica
                elemento3 += " "; //agregamos espacios
            }
            for (int j = elemento4.length(); j<3; j++) { //le agregamos los espacios en blanco necesarios para que en la impresion se vea estetica
                elemento4 += " "; //agregamos espacios
            }
            	System.out.println(elemento4 + "|     " + elemento + "|     " + elemento2 + "|   " + elemento3);	//imprimimos las 3 listas en forma de columnas
            	
            
        }
    }
	
	public static void generarArchivoEnsamblador() {
		String ruta = "src\\ensamblador.txt";
	    File f = new File(ruta);
	    FileWriter fw = null;
		try {
			fw = new FileWriter(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    PrintWriter txt = new PrintWriter(fw);
	    
	    String aux = "";
	    int contador = 1;
	    boolean salta = false;
	    
	    // Se itera el for por cada linea de la tabla de triplos
	    for(int i=0;i<Dato_Origen.size();i++){
	    	String datoOrigen = Dato_Origen.get(i);
	    	String datoFuente = Dato_Fuente.get(i);
	    	String tipoOperador = Operador.get(i);
	    	String numeroLinea = linea.get(i);
	    	
	    	String lineaEnsamblador = "";
	    	
	    	switch (tipoOperador) {
	    	
	    	case "=":
	    	
	    		if(salta) {
	    			lineaEnsamblador += "SI" + contador + ": MOV AX," + datoFuente + ";" + "\n";
                	lineaEnsamblador += "MOV " + datoOrigen + ",AX;";
                	salta = false;
	    		} else {
	    			if (Pattern.compile("[T][0-9]+").matcher(datoFuente).matches()) {
	    				lineaEnsamblador += "MOV " + datoOrigen + ",AX;";
	    			}
	    			else {
	    				lineaEnsamblador += "MOV AX," + datoFuente + ";" + "\n";
	    				lineaEnsamblador += "MOV " + datoOrigen + ",AX;";
	    			}
	    		}
                break;
	    	
			case "+":
				lineaEnsamblador += "ADD AX," + datoFuente + ";";
				break;
				
			case "-":
				lineaEnsamblador += "SUB AX," + datoFuente + ";";
				break;
				
			case "*":
				lineaEnsamblador += "MOV BL," + datoFuente + ";";
				lineaEnsamblador += "MUL BL;";
				break;
				
			case "/":
				lineaEnsamblador += "MOV BL," + datoFuente + ";";
				break;
				
			case "<":
                aux = "LT";
                break;
                
            case "<=":
                aux = "LE";
                break;
                
            case ">":
                aux = "GT";
                break;
                
            case ">=":
                aux = "GE";
                break;
                
            case "==":
                aux = "EQ";
                break;
                
            case "!=":
                aux = "NE";
                break;
                
            case "JMP":
            	contador++;
            	lineaEnsamblador += "JMP SALIR" + contador + ";\n";
            	lineaEnsamblador += "SALIR" + (contador - 1) + ":";
                break;

			default:
				lineaEnsamblador = "";
				break;
			}
	    	

	    	if (!aux.equals("")) {
                lineaEnsamblador += "CMP " + datoOrigen + "," + datoFuente + ";" + "\n";
                lineaEnsamblador += aux + " SI" + contador + ":" + "\n";
                lineaEnsamblador += "JMP SALIR" + contador + ";";
                aux = "";
                salta = true;
            }
	    	
	    	if(!lineaEnsamblador.equals("")) {
	    		txt.println(lineaEnsamblador);	    		
	    	}
	         
	    }
	    
	    txt.println("SALIR" + contador + ":");
	    txt.println("EXIT;");
	    txt.close();
	}
	
	public static void generarArchivo() {
		String ruta = "src\\triplo.txt";
	    File f = new File(ruta);
	    FileWriter fw = null;
		try {
			fw = new FileWriter(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    PrintWriter txt = new PrintWriter(fw);
	    txt.println("   | Dato Origen | Dato Fuente | Tipo ");
	    txt.println("---|-------------|-------------|------");
	    
	    // Se itera el for por cada linea de la tabla de triplos
	    for(int i=0;i<Dato_Origen.size();i++){
	    	String elemento = Dato_Origen.get(i);
	    	String elemento2 = Dato_Fuente.get(i);
	    	String elemento3 = Operador.get(i);
	    	String elemento4 = linea.get(i);
	    	for (int j = elemento.length(); j<8; j++) { //le agregamos los espacios en blanco necesarios para que en la impresion se vea estetica
                elemento += " "; //agregamos espacios
            }
            for (int j = elemento2.length(); j<8; j++) { //le agregamos los espacios en blanco necesarios para que en la impresion se vea estetica
                elemento2 += " "; //agregamos espacios
            }
            for (int j = elemento3.length(); j<10; j++) { //le agregamos los espacios en blanco necesarios para que en la impresion se vea estetica
                elemento3 += " "; //agregamos espacios
            }
            for (int j = elemento4.length(); j<3; j++) { //le agregamos los espacios en blanco necesarios para que en la impresion se vea estetica
                elemento4 += " "; //agregamos espacios
            }
	    	
	         txt.println(elemento4 + "|     " + elemento + "|     " + elemento2 + "|   " + elemento3);
	         
	    }
	    txt.close();
	}
}