import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexico {

    private int numLinea = 0;

    private void leerArchivo() {
        try {
            BufferedReader bR = new BufferedReader(new FileReader(new File("Lexema.txt")));
            int caracter;
            while ((caracter = bR.read()) != -1) {
                numLinea += 1;
                String aux = bR.readLine();
                aux = (char) caracter + (aux == null ? "" : aux);
                aux = aux.replaceAll(",", " , ").replaceAll(";", " ; ").replaceAll("\\(", " ( ").replaceAll("\\)", " ) ");
                anlizador(aux);
            }
            bR.close();
            imprimirTablaSimbolos();
            imprimirArchivoToken();
            imprimirTablaError();
            imprimirTriplos();
            imprimirPrefijo();
            crearEnsamblador();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    enum Token {

        TD("(String|float|char|int)", 0), IC("(do|while)", 1), ID("([a-zA-Z][a-zA-Z0-9]*)", 2),
        NUM("([0-9]+)", 3), DEC("([0-9]+\\.[0-9]+)", 4), OA("([-+/*=])", 5),
        OR("(!=|==|<|<=|>|>=)", 6), DEL("([{}()])", 7), FN("([,;])", 8);

        private final String exprecion;
        private final int pos;

        Token(String exprecion, int pos) {
            this.exprecion = exprecion;
            this.pos = pos;
        }

    }

    private final HashMap<String, String> tablaSimbolos = new HashMap<>();
    private final ArrayList<String> tablaError = new ArrayList<>();
    private final ArrayList<String> archivoToken = new ArrayList<>();
    private final int[] contador = new int[9];

    private void anlizador(String instruccion) {
        StringTokenizer st = new StringTokenizer(instruccion);
        StringBuilder instToken = new StringBuilder();
        String tipoDato = "";
        boolean bandera1 = false;
        boolean bandera = false;
        while (st.hasMoreTokens()) {
            String lexema = st.nextToken();
            for (Token token : Token.values()) {
                Pattern pattern = Pattern.compile(token.exprecion);
                Matcher matcher = pattern.matcher(lexema);
                if (matcher.matches()) {
                    if (tablaSimbolos.containsKey(lexema)) {
                        instToken.append(tablaSimbolos.get(lexema).split("\t\t")[0]).append(" ");
                    } else {
                        contador[token.pos] += 1;
                        tablaSimbolos.put(lexema, token.name() + contador[token.pos] + "\t\t" + (token.pos == 8 ? "" : tipoDato));
                        instToken.append(token.name()).append(contador[token.pos]).append(" ");
                    }
                    switch (token.pos) {
                        case 0:
                            tipoDato = lexema;
                            break;
                        case 1:
                            if (lexema.equals("while")) {
                                addWhileTriplo(instruccion);
                                bandera1 = true;
                            }
                            break;
                        case 5:
                            bandera = true;
                            break;
                        case 7:
                            if (lexema.equals("}"))
                                triploIR();
                            break;
                    }
                    break;
                }
            }
        }
        if (bandera1) {
            errorIncompatibilida(instruccion);
        }
        if (bandera) {
            prefijo(instruccion.replace(";", "").split(" "));
            addTriplo(instruccion.replace(";", "").split(" "));
            errorIncompatibilida(instruccion.replace(";", "").split(" "));
        }
        archivoToken.add(instToken.toString());
    }

    private final ArrayList<String> prefijas = new ArrayList<>();
    private final ArrayList<String> triplos = new ArrayList<>();
    private int conT = 0;
    private int conTR = 0;
    private int lineaTriplo = 0;
    private final Stack<Integer> saltoFalse = new Stack<>();

    private void crearEnsamblador() {
        ArrayList<String> dato = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("Archivo Triplo.txt")));
            int caracter;
            int contador = 0;
            String aux = "";
            String aux1 = "";
            bufferedReader.readLine();
            while ((caracter = bufferedReader.read()) != -1) {
                String[] triplo = ((char) caracter + bufferedReader.readLine()).trim().split("\\|");
                switch (triplo[1].trim()) {
                    case "=":
                        if (!saltos.isEmpty()) {
                            if (saltos.get(0).equals(triplo[0].trim())) {
                                aux1 = "Inicio" + contador + ": ";
                                saltos.remove(0);
                            }
                        }
                        if (Pattern.compile("[T][0-9]+").matcher(triplo[3].trim()).matches())
                            dato.add("MOV " + triplo[2].trim() + ",AX;");
                        else
                            dato.add(aux1 + "MOV AX," + triplo[3].trim() + ";");
                        aux1 = "";
                        break;
                    case "+":
                        dato.add("ADD AX," + triplo[3].trim() + ";");
                        break;
                    case "-":
                        dato.add("SUB AX," + triplo[3].trim() + ";");
                        break;
                    case "*":
                        dato.add("MOV BL," + triplo[3].trim() + ";");
                        dato.add("MUL BL;");
                        break;
                    case "/":
                        dato.add("MOV BL," + triplo[3].trim() + ";");
                        dato.add("DIV BL;");
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
                    case "IR":
                        dato.add("JMP Inicio" + contador + ":");
                        dato.add("SALIR" + contador + ":");
                        contador += 1;
                        break;
                }
                if (!aux.equals("")) {
                    dato.add("CMP AX," + triplo[3].trim() + ";");
                    dato.add(aux + " SI" + contador + ":");
                    dato.add("JMP SALIR:" + contador + ":");
                    aux1 = "SI" + contador + ": ";
                    aux = "";
                    bufferedReader.readLine();
                    bufferedReader.readLine();
                }
            }
            PrintWriter printWriter = new PrintWriter(new FileWriter(new File("Ensamblador.txt")));
            for (String data : dato) {
                printWriter.println(data);
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final ArrayList<String> saltos = new ArrayList<>();

    private void errorIncompatibilida(String exprecion) {
        StringTokenizer st = new StringTokenizer(exprecion);
        while (st.hasMoreTokens()) {
            String aux = st.nextToken();
            if (Pattern.compile("([{}()]|while)").matcher(aux).find())
                continue;
            String[] lexema = tablaSimbolos.get(aux).split("\t\t");
            if (lexema.length == 2) {
                st.nextToken();
                String aux2 = st.nextToken();
                String[] lexema2 = tablaSimbolos.get(aux2).split("\t\t");
                if (lexema2.length == 2) {
                    if (!lexema2[1].equals(lexema[1])) {
                        tablaError.add(aux + "\t\tE" + lexema[0] + "\t\t" + numLinea);
                        tablaError.add(aux2 + "\t\tE" + lexema[0] + "\t\t" + numLinea);
                        return;
                    }
                }
            }
            return;
        }
    }

    private void errorIncompatibilida(String[] lexema) {
        for (int m = 0; m < lexema.length; m += 2) {
            String[] aux = tablaSimbolos.get(lexema[m]).split("\t\t");
            String tipo;
            if (aux.length == 2) {
                tipo = aux[1];
                for (int k = 2; k < lexema.length; k += 2) {
                    aux = tablaSimbolos.get(lexema[k]).split("\t\t");
                    if (aux.length == 2) {
                        if (!tipo.equals(aux[1])) {
                            tablaError.add(lexema[m] + "\t\tE" + tablaSimbolos.get(lexema[0]).split("\t\t")[0] + "\t\t" + numLinea);
                            tablaError.add(lexema[k] + "\t\tE" + aux[0] + "\t\t" + numLinea);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void triploIR() {
        lineaTriplo += 1;
        int salto = saltoFalse.pop();
        triplos.add(lineaTriplo + "\t\t|\t\tIR\t\t|\t" + salto);
        triplos.set(salto, triplos.get(salto) + (lineaTriplo + 1));
        salto -= 2;
        saltos.add(String.valueOf(salto));
    }

    private void addTriplo(String[] instruccion) {
        for (int k = 2; k < instruccion.length - 1; k += 1) {
            lineaTriplo += 1;
            if (k == 2) {
                conT += 1;
                triplos.add(lineaTriplo + "\t\t|\t\t=\t\t|\t\tT" + conT + "\t\t|\t\t" + instruccion[k]);
            } else {
                triplos.add(lineaTriplo + "\t\t|\t\t" + instruccion[k] + "\t\t|\t\tT" + conT + "\t\t|\t\t" + instruccion[k + 1]);
                k += 1;
            }
        }
        lineaTriplo += 1;
        triplos.add(lineaTriplo + "\t\t|\t\t=\t\t|\t\t" + instruccion[0] + "\t\t|\t\tT" + conT);
    }

    private void addWhileTriplo(String instruccion) {
        StringTokenizer st = new StringTokenizer(instruccion);
        while (st.hasMoreTokens()) {
            String lexema = st.nextToken();
            if (Pattern.compile("([{}()]|while)").matcher(lexema).find())
                continue;
            conTR += 1;
            conT += 1;
            lineaTriplo += 1;
            triplos.add(lineaTriplo + "\t\t|\t\t=\t\t|\t\tT" + conT + "\t\t|\t\t" + lexema);
            lineaTriplo += 1;
            triplos.add(lineaTriplo + "\t\t|\t\t" +st.nextToken() + "\t\t|\t\tT" + conT + "\t\t|\t\t" + st.nextToken());
            lineaTriplo += 1;
            triplos.add(lineaTriplo + "\t\t|\t\tTR" + conTR + "\t\t|\t\tTrue\t\t|\t\t" + (lineaTriplo + 2));
            lineaTriplo += 1;
            triplos.add(lineaTriplo + "\t\t|\t\tTR" + conTR + "\t\t|\t\tFalse\t\t|\t\t");
            saltoFalse.push(lineaTriplo - 1);
            break;
        }
    }

    private void prefijo(String[] exprecion) {
        String prefijo = exprecion[1] + exprecion[0];
        Stack<String> simbolos = new Stack<>();
        Stack<String> variable = new Stack<>();
        for (int k = exprecion.length - 1; k > 1; k -= 1) {
            if (exprecion[k].equals("+") || exprecion[k].equals("-") || exprecion[k].equals("/") || exprecion[k].equals("*")) {
                if (!simbolos.isEmpty()) {
                    String aux = simbolos.pop();
                    if (exprecion[k].equals(aux)) {
                        simbolos.push(aux);
                        simbolos.push(exprecion[k]);
                    } else if (exprecion[k].equals("+") || exprecion[k].equals("-")) {
                        simbolos.push(exprecion[k]);
                        simbolos.push(aux);
                    } else {
                        if (aux.equals("*") || aux.equals("/")) {
                            simbolos.push(exprecion[k]);
                            simbolos.push(aux);
                        } else {
                            simbolos.push(exprecion[k]);
                            variable.push(aux);
                        }
                    }
                } else
                    simbolos.push(exprecion[k]);
            } else
                variable.push(exprecion[k]);
        }
        while (!simbolos.isEmpty()) {
            prefijo += simbolos.pop();
        }
        while (!variable.isEmpty()) {
            prefijo += variable.pop();
        }
        prefijo = prefijo.replace(";", "");
        this.prefijas.add(prefijo);
    }

    private void errorDeclaracion(String lexema) {
        String[] dato = tablaSimbolos.get(lexema).split("\t\t");
        if (dato.length == 1) {
            tablaError.add(lexema + "\t\tE" + dato[0] + "\t\t" + numLinea);
        }
    }

    private void imprimirTriplos() {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(new File("Archivo Triplo.txt")));
            printWriter.println("Linea\t\t|\t\tOperador\t\tDato objeto\t\tDato fuente");
            for (String triplo : triplos) {
                printWriter.println(triplo);
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void imprimirPrefijo() {
        for (String prefijo : prefijas) {
            System.out.println(prefijo);
        }
    }

    private void imprimirTablaError() {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(new File("Tabla de Error.txt")));
            printWriter.println("Lexema\t\tToken Error\t\tLinea");
            for (String error : tablaError) {
                printWriter.println(error);
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void imprimirTablaSimbolos() {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(new File("Tabla de Simbolos.txt")));
            printWriter.println("Lexema\t\tToken\t\tTipo");
            for (String a : tablaSimbolos.keySet()) {
                printWriter.println(a + "\t\t" + tablaSimbolos.get(a));
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void imprimirArchivoToken() {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(new File("Archivo Token.txt")));
            for (String i : archivoToken) {
                printWriter.println(i);
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Lexico().leerArchivo();
    }
}