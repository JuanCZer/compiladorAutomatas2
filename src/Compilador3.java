import java.io.BufferedReader;
import java.io.File;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
   
public class Compilador3  {
    static String [][]PalabrasReservadas = new String[40][4]; // Arreglo donde se encuentran todas las palabras reservadas
    //static String [] palabras;
    static ArrayList<String> lexema = new ArrayList<String>(); //lista que contiene la primera columna de la tabla
    static ArrayList<String> token = new ArrayList<String>(); //lista que contiene la segunda columna de la tabla
    static ArrayList<String> tipo = new ArrayList<String>(); //lista que contiene la tercera columna de la tabla
    static ArrayList<String> lexema1 = new ArrayList<String>(); //lista que contiene la primera columna de la tabla
    static ArrayList<String> token1 = new ArrayList<String>(); //lista que contiene la segunda columna de la tabla
    static ArrayList<String> tipo1 = new ArrayList<String>(); //lista que contiene la tercera columna de la tabla
    static ArrayList<String> linea = new ArrayList<String>(); //lista que contiene la tercera columna de la tabla
    static String alternativa [] [];
    static int contadorIDE = 1;	//contador de Identificadores IDE
    static int contadorDIG = 1;	//contador de digitos DIG
    static int contadorPR = 1;
    static int valorI ;
    static int errsem = 1;
    
    static String nuevaPalabra = "";
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        for(int i=0; i<PalabrasReservadas.length;i++) { 
        	PalabrasReservadas[i][3] = "0";//llenamos con 0 la ultima columna del arreglo para saber si la palabra ya a sido agregada a la tabla de simbolos y no volverla a agregar
        	PalabrasReservadas[i][2] = "0";//la columna 2 del arreglo se utiliza para llevar un conteo y saber cual token sigue
        }
                                                    //Se llena una especie de tabla que en sí es un arreglo que contiene las palabras reservadas
        PalabrasReservadas[0][0]="=";
        PalabrasReservadas[0][1]="ASG"; 
        PalabrasReservadas[1][0]=";";
        PalabrasReservadas[1][1]="TER";
        PalabrasReservadas[2][0]=" ";
        PalabrasReservadas[2][1]="ESP";
           
        PalabrasReservadas[3][0]="int";
        PalabrasReservadas[3][1]="TDL";
        PalabrasReservadas[4][0]="double";
        PalabrasReservadas[4][1]="TDL";
        PalabrasReservadas[5][0]="String";
        PalabrasReservadas[5][1]="TDL";
        PalabrasReservadas[6][0]="char";
        PalabrasReservadas[6][1]="TDL";
        PalabrasReservadas[7][0]="float";
        PalabrasReservadas[7][1]="TDL";
        PalabrasReservadas[8][0]="long";
        PalabrasReservadas[8][1]="TDL";
             
        PalabrasReservadas[9][0]="+";
        PalabrasReservadas[9][1]="OPA";
        PalabrasReservadas[10][0]="-";
        PalabrasReservadas[10][1]="OPA";
        PalabrasReservadas[11][0]="*";
        PalabrasReservadas[11][1]="OPA";
        PalabrasReservadas[12][0]="/";
        PalabrasReservadas[12][1]="OPA";
        PalabrasReservadas[13][0]="%";
        PalabrasReservadas[13][1]="OPA";
           
        PalabrasReservadas[14][0]="<";
        PalabrasReservadas[14][1]="OPR";
        PalabrasReservadas[15][0]=">";
        PalabrasReservadas[15][1]="OPR";
        PalabrasReservadas[16][0]="<=";
        PalabrasReservadas[16][1]="OPR";
        PalabrasReservadas[17][0]=">=";
        PalabrasReservadas[17][1]="OPR";
        PalabrasReservadas[18][0]="==";
        PalabrasReservadas[18][1]="OPR";
        PalabrasReservadas[19][0]="!=";
        PalabrasReservadas[19][1]="OPR";
       
        PalabrasReservadas[20][0]="&&";
        PalabrasReservadas[20][1]="OPL";
        PalabrasReservadas[21][0]="||";
        PalabrasReservadas[21][1]="OPL";
        PalabrasReservadas[22][0]="!";
        PalabrasReservadas[22][1]="OPL";
        PalabrasReservadas[23][0]="&";
        PalabrasReservadas[23][1]="OPL";
        PalabrasReservadas[24][0]="|||";
        PalabrasReservadas[24][1]="OPL";
          
        PalabrasReservadas[25][0]="'";
        PalabrasReservadas[25][1]="CES";
        PalabrasReservadas[26][0]="?";
        PalabrasReservadas[26][1]="CES";
        PalabrasReservadas[27][0]="¿";
        PalabrasReservadas[27][1]="CES";
        PalabrasReservadas[28][0]="(";
        PalabrasReservadas[28][1]="CES";
        PalabrasReservadas[29][0]=")";
        PalabrasReservadas[29][1]="CES";
        PalabrasReservadas[30][0]="[";
        PalabrasReservadas[30][1]="CES";
        PalabrasReservadas[31][0]="]";
        PalabrasReservadas[31][1]="CES";
        PalabrasReservadas[32][0]="{";
        PalabrasReservadas[32][1]="CES";
        PalabrasReservadas[33][0]="}";
        PalabrasReservadas[33][1]="CES";
        PalabrasReservadas[34][0]="_";
        PalabrasReservadas[34][1]="CES";
        PalabrasReservadas[35][0]="-";
        PalabrasReservadas[35][1]="CES";
        PalabrasReservadas[36][0]=":";
        PalabrasReservadas[36][1]="CES";
        PalabrasReservadas[37][0]=",";
        PalabrasReservadas[37][1]="CES";
        PalabrasReservadas[38][0]=".";
        PalabrasReservadas[38][1]="CES";
        PalabrasReservadas[39][0]="\"";
        PalabrasReservadas[39][1]="CES";
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        String content = null;                          //contenido del fichero
        File file = new File("src\\texto.txt"); //ruta completa al fichero que deseamos leer
        try {
                //leyendo el archivo completo
                FileReader reader = new FileReader(file);
                char[] chars = new char[(int) file.length()];
                reader.read(chars);
                content = new String(chars);
                reader.close();
                //eliminamos espacios en blanco (opcional) \\s o saltos de linea con \n
                content = content.replaceAll("\n","").replace("\r", " ");
        } catch (IOException e) {
                e.printStackTrace();
        }
        
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        content = content.toLowerCase(); // convertimos todos los caracteres a minusculas para evitar que se consideren 2 variables distintas cuando son la misma
        String [] palabras = content.split(""); // ponemos en un arreglo caracter por caracter
        
        int tamaño = palabras.length;           //obtenemos el total de caracteres   
        int contador = 0;
        try {
        for(int i = 0; i < tamaño; i++) { //con un for recorremos caracter por caracter
            nuevaPalabra += palabras[i];    //vamos juntando los caracteres en un nuevo String para obtener una palabra
            int valor = i+1; // creamos una variable con el valor de i+1 para 
            if(nuevaPalabra.equals(" ")) { //si la palabra formada es un espacio, le asignamos un valor vacio ""
                nuevaPalabra ="";
            }
            
            buscarEnPalabrasReservadas(nuevaPalabra, palabras[valor]); //conforme se van formando las palabras se mandan a una funcion para ver si estan dentro de las palabras reservadas siempre y cuando el caracter siguiente sea un espacio
            int validacion = checarCaracter(palabras[valor]); //checamos el caracter siguiente para saber si esta como caracter reservado y agregarlo a la tabla de simbolos
            if(validacion == 1) { // si el caracter siguiente esta como caracter reservado continua con lo siguiente
                if(nuevaPalabra != "") {	// si la palabra formada contiene al menos un caracter y no se encontro con las funciones anteriores, entonces checamos con esa funcion
                	checarLista(palabras[valor]);
                }
                guardar(palabras[valor]);
                i++;  //Como ya verificamos el siguiente caracter y fue guardado en la tabla de simbolos, entonces le sumamos al contador para ya no contemplar ese caracter 
            }
        }
       }catch( ArrayIndexOutOfBoundsException e) {
             
       }
        triplo trip = new triplo();
        trip.hacer_triplo("src\\texto.txt");
       tipo();
       crearTablaSimbolos();
        imprimir(); //Codigo para mostrar la tabla de simbolos
        buscarError();
        ErrorNoDefinida();
    }
    public static int buscarEnPalabrasReservadas(String dato, String palabras) {
    	if(dato.equals("string")) {
    		 dato = Character.toUpperCase(dato.charAt(0)) + dato.substring(1,dato.length());
    	}
        for(int i = 0; i < PalabrasReservadas.length; i++) {	//recorremos el arreglo que contiene todas las palabras y caracteres reservados
           if((dato.equals(PalabrasReservadas[i][0]))) {	//si la palabra se encuentra en el arreglo entonces lo agregamos a la lista de lexema y token
//               if((palabras.equals(" ")) || (dato.equals(TablaSimbolos[i][0]))) {	// si el caracter siguiente es un espacio y esta en el arreglo de palabras, entonces lo agregamos
               if((palabras.equals(" ")) || (palabras.equals("=")) || (PalabrasReservadas[i][1].equals("TDL") == false)) {	// si el caracter siguiente es un espacio y esta en el arreglo de palabras, entonces lo agregamos
            	   int numero2 = Integer.parseInt(PalabrasReservadas[i][3]);	//convertimos a entero el valor de la columna 3
               	   lexema.add(dato);	//agregamos a la lista de lexema
                   int numero = Integer.parseInt(PalabrasReservadas[i][2]); //obtenemos el numero del arreglo para saber que numero sigue. ejemplo IDE1, IDE2
                   numero += 1; //como se agrega una nueva palabra e sumamos uno 
                   PalabrasReservadas[i][2] = numero + "";	// y lo actualizamos 
                   if(dato.equals(";")) {
                  	   PalabrasReservadas[i][3] = 0 + "";	//actualizamos el valor en el arreglo   
                  }else if(dato.equals("=")) {
                  	   PalabrasReservadas[i][3] = 0 + "";	//actualizamos el valor en el arreglo   
                  }else {
               	   PalabrasReservadas[i][3] = 1 + "";	//actualizamos el valor en el arreglo
                  }
                   token.add(PalabrasReservadas[i][1] + PalabrasReservadas[i][2]);	// y agregamos a la lista de token
                   nuevaPalabra = "";	//reseteamos la palabra
                   for(int j = 0; j < PalabrasReservadas.length; j++) {	//a todas las palabras y caracteres en el arregllo que sean del mismo tipo, le actualizamos su contador. ejemplo, a todos los IDE le ponemos 1 o 2 segun continue
                       if(PalabrasReservadas[i][1].equals(PalabrasReservadas[j][1])) {	//solo se actualiza a aquellos que sean iguales. Ejemplo, todos los IDE
                       	PalabrasReservadas[j][2] = numero + "";
                       }
                   }
                   return 1;	//se termina la funcion
               }
           }
        }
       return 0;	//se termina la funcion
   }
    public static int checarCaracter(String dato) {
        for(int i = 0; i < PalabrasReservadas.length; i++) {	//recorremos el arreglo con palabras y caracteres reservados
            if((dato.equals(PalabrasReservadas[i][0])) && (dato.equals(".") == false)) {	
            	valorI = i;
               return 1; //se termina la funcion
               }
         }
       return 0;	//se termina la funcion
   }
    public static int guardar(String dato) {
              int numero = Integer.parseInt(PalabrasReservadas[valorI][2]);	//convertimos a entero el valor de la columna 2 
              int numero2 = Integer.parseInt(PalabrasReservadas[valorI][3]);	//convertimos a entero el valor de la columna 3
                  lexema.add(dato);	//agregamos a la lista de lexema
                  numero += 1; 	//sumamos en 1
                  PalabrasReservadas[valorI][2] = numero + "";	//actualizamos el valor en el arreglo
                  if(dato.equals(";")) {
                  	   PalabrasReservadas[valorI][3] = 0 + "";	//actualizamos el valor en el arreglo   
                  }else if(dato.equals("=")) {
                  	   PalabrasReservadas[valorI][3] = 0 + "";	//actualizamos el valor en el arreglo   
                  }else {
               	   PalabrasReservadas[valorI][3] = 1 + "";	//actualizamos el valor en el arreglo
                  }
                  token.add(PalabrasReservadas[valorI][1] + PalabrasReservadas[valorI][2]);	//agregamos a la lista de tokens
                  for(int j = 0; j < PalabrasReservadas.length; j++) { //como agregamos a la lista actualizamos su contador. ejemplo IDE1 IDE1, todos los IDE les actualiza el contador
                      if(PalabrasReservadas[valorI][1].equals(PalabrasReservadas[j][1])) {
                      	PalabrasReservadas[j][2] = numero + "";	//actualizamos el contador
                      }
                  }
                  return 1; //se termina la funcion
  }
    public static int checarLista(String palabras) {
        try {
            double digito = Double.parseDouble(nuevaPalabra); //Si la palabra la podemos convertir a un numero double sin darnos error entonces tenemos un digito
            lexema.add(nuevaPalabra);	//agregamos la palabra a la lista de lexema
            token.add("DIG" + contadorDIG);	//agregamos a la lista de token 
            contadorDIG++;	//sumamos en 1 el contador de digitos
            nuevaPalabra = "";	//reseteamos la palabra pues ya la agregamos
            return 0; // se termina la funcion
              
        }catch(NumberFormatException e) {	//si dio un error la conversion a un numero, entonces tenemos un identificador
//        	System.out.println(nuevaPalabra + "++++++++++");
//                lexema.add(nuevaPalabra);	//lo agregamos a la lista de lexema
//                token.add("IDE" + contadorIDE);	//agregamos a la lista de token
//                contadorIDE++;	//sumamos en 1 el contador de identificadores
//                nuevaPalabra = ""; //reseteamos la palabra pues ya la agregamos
//        	if(nuevaPalabra.equals("do") || nuevaPalabra.equals("while")) {
        	if(nuevaPalabra.equals("if")) {
        		lexema.add(nuevaPalabra);	//lo agregamos a la lista de lexema
                token.add("CON" + contadorPR);	//agregamos a la lista de token
                contadorPR++;	//sumamos en 1 el contador de identificadores
                nuevaPalabra = ""; //reseteamos la palabra pues ya la agregamos
        	}else {
        		lexema.add(nuevaPalabra);	//lo agregamos a la lista de lexema
                token.add("IDE" + contadorIDE);	//agregamos a la lista de token
                contadorIDE++;	//sumamos en 1 el contador de identificadores
                nuevaPalabra = ""; //reseteamos la palabra pues ya la agregamos
        	}
          return 0;	//se termina la funcion
        }
    }     
    public static void tipo () {
     Iterator<String> nombreIterator = lexema.iterator(); //se utiliza para poder recorrer la lista
     Iterator<String> nombreIterator2 = token.iterator();
        int contIDE = 1;	//creamos 2 contadores
        int contDIG = 1;
        String tip = "";
        ArrayList<String> tipoDato = new ArrayList<String>();
        ArrayList<String> tipoDato1 = new ArrayList<String>();
//        tipoDato.add("");
        while(nombreIterator.hasNext()){	//recorremos las listas
            String elemento = nombreIterator.next();	//ponemos el valor de la lista en un String
            String elemento2 = nombreIterator2.next().substring(0,3);
            //System.out.println(elemento);
            int tam = elemento.length();	//como utilizaremos varias veces el tamaño del elemento, lo asignamos a una variable
            //3-8
            for(int i = 3; i <= 8; i++) {	//en el rango 3-8 tenemos guardados los tipos de datos
            	if(elemento.equals(PalabrasReservadas[i][0])) {	//cuando lo encuentra lo guarda
                    tip = elemento;
                    break;
                }
            	if((elemento.equals(";")) || (elemento.equals("=")) || (elemento2.equals("OPA")) || (elemento2.equals("CON") || (elemento2.equals("OPR")))) {	//cuando lo encuentra lo guarda
                    tip = "";
                    break;
                }
            }
                if( (elemento2.equals("TDL") == false) && (elemento2.equals("OPA") == false) && (elemento2.equals("TER") == false) && (elemento2.equals("DIG") == false)) {
                	Iterator<String> nombreIterator3 = tipoDato.iterator();
                	Iterator<String> nombreIterator4 = tipoDato1.iterator();
                	while(nombreIterator3.hasNext()){
                		String nuevo = nombreIterator3.next();
                		String nuevo1 = nombreIterator4.next();
                		if(elemento.equals(nuevo)) {
                			
                			tip = nuevo1;
                		}
                    }
                	if((elemento.equals(",")) || (elemento.equals(" "))) {
                		tipo.add("");
                	}else {
                		//Si el token es un IDE lo guardamos en la lista  de tipo
                        tipo.add(tip);
                        tipoDato.add(elemento);
                        tipoDato1.add(tip);
                	}
                }else { //si es cualquier otro dato lo guardamos sin ningun dato
                    tipo.add("");
                }
                
            }
   }
    public static void crearTablaSimbolos() {
    	crearListaAlternativa();
    	int contador = 0;
    	int contador1 = 1;
    	Iterator<String> nombreIterator = lexema.iterator(); //se utiliza para poder recorrer la lista
    	while(nombreIterator.hasNext()){
    		//recorremos las listas
            String elemento = nombreIterator.next();
            for(int i = 0; i < alternativa.length; i++) {
//                System.out.println("+++++++++" +alternativa[i][3]);
            	if(elemento.equals(alternativa[i][0])) {
          	      contador++;
          	      if(contador > 1) {
          	    	  alternativa [i][0] = null;
          	      }
                }
            }
            contador = 0;
            }
    	int contASG = 0, contTER = 0, contESP = 0, contTDL = 0, contOPA = 0, contOPR = 0 , contOPL = 0, contCES = 0, contIDE = 0, contDIG = 0, contCON = 0;
    	for(int i = 0; i < alternativa.length; i++) {
    		if(alternativa[i][0] != null) {
    			String pal = alternativa[i][1].substring(0,3);
    			switch(pal) {
    			case "ASG" : contASG++;
    						 token1.add(pal + contASG);
    				break;
    			case "TER" : contTER++;
    						 token1.add(pal + contTER);
    				break;
    			case "ESP" : contESP++;
    						 token1.add(pal + contESP);
    				break;
    			case "TDL" : contTDL ++;
    						 token1.add(pal + contTDL);
    				break;
    			case "OPA" : contOPA++;
    						 token1.add(pal + contOPA);
    				break;
    			case "OPR" : contOPR++;
    						 token1.add(pal + contOPR);
    				break;
    			case "OPL" : contOPL++;
    						 token1.add(pal + contOPL);
    				break;
    			case "CES" : contCES++;
    						 token1.add(pal + contCES);
    				break;
    			case "IDE" : contIDE++;
    						 token1.add(pal + contIDE);
				 break;
    			case "DIG" : contDIG++;
    						 token1.add(pal + contDIG);
				 break;
    			case "CON" : contCON++;
				 			token1.add(pal + contCON);
		break;
    			}
    			
    			lexema1.add(alternativa[i][0]);
    			tipo1.add(alternativa[i][2]);
    			linea.add(alternativa[i][3]);
    		}
    	}
    }
    public static void crearListaAlternativa() {
    	int contador = 0;
    	int linea = 1;
    	int tamaño = lexema.size();
    	alternativa = new String [tamaño][4];
    	Iterator<String> nombreIterator = lexema.iterator(); //se utiliza para poder recorrer la lista
    	Iterator<String> nombreIterator1 = token.iterator(); //se utiliza para poder recorrer la lista
    	Iterator<String> nombreIterator2 = tipo.iterator(); //se utiliza para poder recorrer la lista
    	while(nombreIterator.hasNext()){	//recorremos las listas
    		String elemento = nombreIterator.next();	//ponemos el valor de la lista en un String
    		String elemento1 = nombreIterator1.next();	//ponemos el valor de la lista en un String
    		String elemento2 = nombreIterator2.next();	//ponemos el valor de la lista en un String
            alternativa [contador][0] = elemento;
            alternativa [contador][1] = elemento1;
            alternativa [contador][2] = elemento2;
            alternativa [contador][3] = ""+linea;
            if(elemento.equals(";") || (elemento.equals("{"))) {
               	linea++;
               }
            contador++;
            }
    }
    public static void buscarError() {
		 System.out.println("Token Error	Lexema	Linea	");
   	 Iterator<String> nombreIterator = token.iterator(); //se utiliza para poder recorrer la lista
        Iterator<String> nombreIterator2 = tipo.iterator();
        Iterator<String> nombreIterator3 = lexema.iterator();
        String palabra = "";
        String tipoP ="";
        String pal ="", tip = "";
        int linea = 1;
        boolean igual = false;
           while(nombreIterator.hasNext()){	//recorremos las listas
               String token = nombreIterator.next().substring(0,3);	//ponemos el valor de la lista en un String
               String tipo = nombreIterator2.next();
               String lexema = nombreIterator3.next();
               if(lexema.equals(";") || (lexema.equals("{"))) {
               	linea++;
               	pal = "";
               	tip = "";
               }
////////////////////////////////////////////////////////////////Error de variable indefinida
if(token.equals("IDE")){
//	System.out.println("++++++++++++++ " + lexema );
if(tipo.equals("")) {
//System.out.println("Error la variable `" + elemento + "` no esta definida " );
System.out.println("ERRSEM"+errsem+"		   " + lexema + "	  " + linea + "	indefinida la variable");	
errsem++;
}
}
////////////////////////////////////////////////////////////////
               if((lexema.equals("=")) || (token.equals("OPA"))) {
            	   if(lexema.equals("=")){
                  		pal = palabra;
                  		tip = tipoP;
                  		igual = true;
                  	}
            	   if(token.equals("OPA")){
                  		igual = false;
                  	}


               	 token = nombreIterator.next().substring(0,3);	//ponemos el valor de la lista en un String
                    tipo = nombreIterator2.next();
                    lexema = nombreIterator3.next();
                    if(token.equals("IDE")){
//                    	System.out.println("++++++++++++++ " + lexema );
                    if(tipo.equals("")) {
                    //System.out.println("Error la variable `" + elemento + "` no esta definida " );
                    System.out.println("ERRSEM"+errsem+"		   " + lexema + "	  " + linea + "	indefinida la variable");	
                    errsem++;
                    }
                    }
                    if(lexema.equals(" ")) {
                   	 token = nombreIterator.next().substring(0,3);	//ponemos el valor de la lista en un String
                        tipo = nombreIterator2.next();
                        lexema = nombreIterator3.next();
                        if(token.equals("IDE")){
//                        	System.out.println("++++++++++++++ " + lexema );
                        if(tipo.equals("")) {
                        //System.out.println("Error la variable `" + elemento + "` no esta definida " );
                        System.out.println("ERRSEM"+errsem+"		   " + lexema + "	  " + linea + "	indefinida la variable");	
                        errsem++;
                        }
                        }
                    }
                    if(token.equals("DIG")) {
                   	 for(int i = 0 ; i < lexema.length(); i ++) {
                   		 if(lexema.charAt(i) == '.') {
                   			 tipo = "double";
                   			 break;
                   		 }else {
                   			 tipo = "int";
                   		 }
                   	 }
                	}
                    if((tipo.equals(tipoP) == false)) {
//                      	 if((tip.equals("")) || (tipo.equals("")) ) {
//                          	 
//   	                     }else {
//   	                    	 if(tip.equals(tipo) == false) {
//   	                    		 System.out.println("ERRSEM		   " + lexema + "	  " + linea + "	Incompatibilidad de tipos" + tip + "   |   " + tipo);
//   	                         }
//   	                     }
                      	 if((tipoP.equals("")) || (tipo.equals("")) ) {
                          	 
   	                     }else {
   	                    	 if(igual == false) {
   	                    		System.out.println("ERRSEM"+errsem+"		   " + lexema + "	  " + linea + "	Incompatibilidad de tipos");	
   	                    		errsem++;
   	//                    	 System.out.println("Error la linea " + linea + ": Incompatibilidad de tipos ( " + palabra + ": " + tipoP + ")");
   	                    	 }
   	                     } 
   	                 }
               }
               if(lexema.equals(" ") == false) {
                   palabra = lexema;
                   tipoP = tipo;
               }
           }
    }
    public static void imprimir1() { 
        System.out.println("Lexema     |  Token  |  Tipo"); //imprimimos el diseño de lo que sera la tabla de simbolos
        System.out.println("-----------|---------|-------");	//seran los espacios que delimitan las columnas
        Iterator<String> nombreIterator = lexema.iterator();	//codigo para poder recorrer las listas 
        Iterator<String> nombreIterator2 = token.iterator();
        Iterator<String> nombreIterator3 = tipo.iterator();
        int contIDE = 1;
        int contDIG = 1;
        boolean igual = false;
        boolean terminal = false;
        while(nombreIterator.hasNext()){	//se recorren las listas
            String elemento = nombreIterator.next();
            String elemento2 = nombreIterator2.next();
            String elemento3 = nombreIterator3.next();
            if((elemento.equals(" ") == false) && (elemento.equals(" ") == false)) {//si el primer elemento de lexema es diferente de espacio, coma, igual. se imprime
                for (int i = 0; i<PalabrasReservadas.length; i++) {
                    if((elemento.equals(PalabrasReservadas[i][0]))) {
                        if((elemento.equals(";") == false) && (elemento.equals("=") == false)) {
                        	for (int j = elemento.length(); j<11; j++) { //le agregamos los espacios en blanco necesarios para que en la impresion se vea estetica
                                elemento += " "; //agregamos espacios
                            }
                        	System.out.println(elemento + "|  " + elemento2 + "   |  " + elemento3);	//imprimimos las 3 listas en forma de columnas
                        }
                        
                    }
                    if((elemento.equals(";")) && (terminal == false)) {
                		for (int j = elemento.length(); j<11; j++) { //le agregamos los espacios en blanco necesarios para que en la impresion se vea estetica
                            elemento += " "; //agregamos espacios
                        }
                    	System.out.println(elemento + "|  " + elemento2 + "   |  " + elemento3);	//imprimimos las 3 listas en forma de columnas
                    	terminal = true;
                	}
                	if((elemento.equals("=")) && (igual == false)) {
                		for (int j = elemento.length(); j<11; j++) { //le agregamos los espacios en blanco necesarios para que en la impresion se vea estetica
                            elemento += " "; //agregamos espacios
                        }
                    	System.out.println(elemento + "|  " + elemento2 + "   |  " + elemento3);	//imprimimos las 3 listas en forma de columnas
                    	igual = true;
                	}
                }
                if(elemento2.equals("IDE" + contIDE)) {	//si es un IDE en la lista token lo imprimimos con lo siguiente
                    for (int j = elemento.length(); j<11; j++) {	//le agregamos los espacios
                        elemento += " ";
                    }
                    System.out.println(elemento + "|  " + elemento2 + "   |  " + elemento3);	//imprimimos
                    contIDE++; //aumentamos el contador puesto que ya se imprimio
                }
                if(elemento2.equals("DIG" + contDIG)) {	//si es un DIG en la lista token lo imprimimos con lo siguiente
                    for (int j = elemento.length(); j<11; j++) {	//agregamos los espacios
                        elemento += " ";
                    }
                    System.out.println(elemento + "|  " + elemento2 + "   |  " + elemento3);	//imprimimos
                    contDIG++;	//aumentamos el contador puesto que ya se imprimio
                }
            }
        }
    }
    public static void imprimir() { 
        System.out.println("Lexema     |  Token  |  Tipo"); //imprimimos el diseño de lo que sera la tabla de simbolos
        System.out.println("-----------|---------|-------");	//seran los espacios que delimitan las columnas
        Iterator<String> nombreIterator = lexema1.iterator();	//codigo para poder recorrer las listas 
        Iterator<String> nombreIterator2 = token1.iterator();
        Iterator<String> nombreIterator3 = tipo1.iterator();
        int contIDE = 1;
        int contDIG = 1;
        boolean igual = false;
        boolean terminal = false;
        while(nombreIterator.hasNext()){	//se recorren las listas
            String elemento = nombreIterator.next();
            String elemento2 = nombreIterator2.next();
            String elemento3 = nombreIterator3.next();
            if(elemento.equals(" ") == false) {
            	for (int j = elemento.length(); j<11; j++) { //le agregamos los espacios en blanco necesarios para que en la impresion se vea estetica
                    elemento += " "; //agregamos espacios
                }
            	System.out.println(elemento + "|  " + elemento2 + "   |  " + elemento3);	//imprimimos las 3 listas en forma de columnas
            } 	
            
        }
    }
    public static void ErrorNoDefinida() { 
        Iterator<String> nombreIterator = lexema1.iterator();	//codigo para poder recorrer las listas 
        Iterator<String> nombreIterator2 = token1.iterator();
        Iterator<String> nombreIterator3 = tipo1.iterator();
        Iterator<String> nombreIterator4 = linea.iterator();
        int contIDE = 1;
        int contDIG = 1;
        boolean igual = false;
        boolean terminal = false;
        while(nombreIterator.hasNext()){	//se recorren las listas
            String elemento = nombreIterator.next();
            String elemento2 = nombreIterator2.next();
            String elemento3 = nombreIterator3.next();
            String token = elemento2.substring(0,3);
            String linea = nombreIterator4.next();

     	   //////////////////////////////////////////////////////////////// Error de variable indefinida
     	   if(token.equals("IDE")){
     		   if(elemento3.equals("")) {
//     			   System.out.println("Error la variable `" + elemento + "` no esta definida " );
//     			   System.out.println("ERRSEM"+errsem+"		   " + elemento + "	  " + linea + "	indefinida la variable");	
//     			   errsem++;
                }
          	}
     	   ////////////////////////////////////////////////////////////////
            
            
        }
    }
}