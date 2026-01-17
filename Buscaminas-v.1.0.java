package com.emiliojimeno.daw.buscaminas;

import java.util.Scanner;

import static java.lang.Math.random;
import static java.lang.Math.round;

public class Buscaminas {
    static final int LADO_TABLERO = 9;
    static final int NUMERO_MINAS = 10;
    static String[][] minas = new String[LADO_TABLERO][LADO_TABLERO];
    static String[][] jugador = new String[LADO_TABLERO][LADO_TABLERO];
    static boolean victoria = false, minaPisada = false;
    static int contadorCasillas = LADO_TABLERO * LADO_TABLERO;
    static Scanner sc = new Scanner(System.in);

    //este metodo pone los caracteres por defecto en los tableros
    static void inicializarTableros(){
        //establecer todas las casillas de la tabla minas a " " y las de jugador a "?"
        for(int i = 0; i < LADO_TABLERO; i++){
            for(int j = 0; j < LADO_TABLERO; j++){
                minas[i][j] = " ";
                jugador[i][j] = "?";
            }
        }
    }

    //este metodo muestra un tablero dado por pantalla
    static void pintarTableros(String[][] array, String nombre){
        System.out.println("Pintando tablero " + nombre + ": \n");
        for(int c = 0; c< LADO_TABLERO; c++){
            System.out.print( c + "  ");
        }
        System.out.println(" ");
        for(int c = 0; c< LADO_TABLERO; c++){
            System.out.print("|  ");
        }

        System.out.println(" \n");
        for(int i = 0; i < LADO_TABLERO; i++){
            for(int j = 0; j < LADO_TABLERO; j++){

                System.out.print(array[i][j]+ "  ");
            }
            System.out.println(" -- " + i);
        }
        System.out.println("\n");
    }

    //este tablero coloca N minas aleatoriamente por el tablero, evitando que se solapen en casillas repetidas
    static void posicionarMinas(){
        boolean hayMina;
        int randomY, randomX;
        for(int i = 0; i < NUMERO_MINAS; i++){ //vamos a realizar esta operación tantas veces como minas haya
            do {
                hayMina = false; //reiniciar variable en cada ciclo
                randomY = (int)round(random()*(LADO_TABLERO-1)); //coordenada Y aleatoria
                randomX = (int)round(random()*(LADO_TABLERO-1)); //coordenada X aleatoria

                if(minas[randomY][randomX].equals("*")){
                    hayMina = true;//si ya hubiera una mina en esa posición, repetimos
                }else{
                    minas[randomY][randomX] = "*"; //si no, pintar mina
                }
            }while(hayMina);
        }
    }

    //este metodo comprueba, para cada casilla no-mina, cuántas minas adyacentes hay
    static void calcularNumeros(){
        int contador;
        //recorrer el tablero haciendo los cálculos
        for(int i = 0; i < LADO_TABLERO; i++){
            for(int j = 0; j < LADO_TABLERO; j++){
                contador = 0; //reiniciar para cada mina
                //ahora establecemos los 8 filtros para evitar que busque fuera del tablero y pete
                if(j-1 >= 0){ //las 3 casillas de la izquierda son j-1
                    if(i-1 >= 0){
                            if(minas[i-1][j-1].equals("*")){ //arriba a la izquierda
                                contador++;
                            }
                    }
                    if(minas[i][j-1].equals("*")){ //a la izquierda
                        contador++;
                    }
                    if(i+1 < LADO_TABLERO){
                        if(minas[i+1][j-1].equals("*")){ //abajo a la izquierda
                            contador++;
                        }
                    }
                }
                if(j+1 < LADO_TABLERO){ //las 3 casillas de la derecha son j+1
                    if(i-1 >= 0){
                        if(minas[i-1][j+1].equals("*")){ //arriba a la derecha
                            contador++;
                        }
                    }
                    if(minas[i][j+1].equals("*")){ //a la derecha
                        contador++;
                    }
                    if(i+1 < LADO_TABLERO){
                        if(minas[i+1][j+1].equals("*")){ //abajo a la derecha
                            contador++;
                        }
                    }
                }
                if(i-1 >= 0){
                    if(minas[i-1][j].equals("*")){ //casilla de arriba
                        contador++;
                    }
                }
                if(i+1 < LADO_TABLERO){
                    if(minas[i+1][j].equals("*")){ //casilla de abajo
                        contador++;
                    }
                }
                //una vez que hemos hecho los cálculos, escribimos en la casilla
                if(minas[i][j].equals("*")){ //sólo se escribe algo si en la casilla no hay una mina

                }else{
                    if(contador != 0){ //si el contador es cero se queda como está. Si no, escribimos el número
                        minas[i][j]=contador+"";
                    }
                }
            }
        }
    }

    //menú para mostrar al jugador cómo se juega
    static void mostrarMenu(){
        System.out.println("""
                Bienvenido al juego del buscaminas de Kevin, Fernando y Pablo!
                
                Aquí van unas pequeñas indicaciones para poder jugar:
                1.- elije coordenadas de la casilla sobre la que vas a actuar
                    OJO! la primera coordenada corresponde a las FILAS y la segunda a las COLUMNAS
                         la coordenada 0,0 es la ESQUINA SUPERIOR IZQUIERDA
                2.- elije tu opción, Romper casilla o Marcar casilla
                         Al romper la casilla desvelas lo que hay debajo pero CUIDADO, si hay una mina, has perdido
                         Al marcar casilla, la proteges para no romperla por accidente, pero nada más, las marcas no te ayudarán a ganar
                3.- si ROMPES todas las casillas que no tienen mina, ENHORABUENA! habrás ganado el juego
                """);
        System.out.println("Hay " + NUMERO_MINAS + " minas");
        System.out.println("Jugaremos en un tablero de " + LADO_TABLERO + "*" + LADO_TABLERO + " casillas.");
        System.out.println("""
                
                Antes de comenzar, aquí va una pequeña leyenda para que conozcas los símbolos:
                ? casilla desconocida
                1-8 número de minas adyacentes a esa casilla
                * mina
                ! casilla marcada por el jugador (No se puede romper sin quitar la marca)
                " " (espacio vacío) esta casilla no es adyacente a ninguna mina.
                
                Comienza el juego
                
                """);
    }

    static void expandirCasillaBlanca(int y, int x){
        /*
        *  La idea es esta:
        *   Hay que comprobar 8 casillas cada vez. Estas casillas están situadas en
        *   i-1,j-1         i-1,j           i-1,j+1
        *   i, j-1      <casilla jugador>   i,j+1
        *   i+1, j-1        i+1,j           i+1,j+1
        *
        *   Empezando por la esquina superior izquierda y en sentido de las agujas del reloj,
        *   metemos los coeficientes de ambas variables en un array. De esta forma, usando un
        *   contador de 0 al largo del array, tendremos los valores para hacer operaciones con la
        *   coordenada del jugador.
        * */
        int[] valoresI = {-1, -1, -1, 0, 0, 1, 1, 1}; //modificadores de fila ordenados - se relaciona con "y"
        int[] valoresJ = {-1, 0, 1, -1, 1, -1, 0, 1}; //modificadores de columna ordenados - se relaciona con "x"

        if(minas[y][x].equals(" ") && !jugador[y][x].equals(" ")){ //detiene la recursividad
            jugador[y][x] = " "; //borrar casilla
            for(int contador = 0; contador < valoresI.length; contador++){
                //ahora evitamos el outOfRange haciendo cálculos con las coordenadas
                if(y+valoresI[contador] >= 0 && y+valoresI[contador] < LADO_TABLERO
                && x+valoresJ[contador] >= 0 && x+valoresJ[contador] < LADO_TABLERO){
                    expandirCasillaBlanca(y+valoresI[contador], x+valoresJ[contador]); //recursividad
                }
            }
        }else{
            switch(minas[y][x]){ //menos engorroso de escribir que los if
                case "1", "2", "3", "4", "5", "6", "7", "8":
                    jugador[y][x] = minas[y][x]; //revelar número
            }
        }
    }

    //esto no es lo más eficiente, pero el contador que teníamos pensado no funciona como debería.
    static int contarCasillasRestantes(){
        int restantes = 0;

        for(int i = 0; i < LADO_TABLERO; i++){
            for(int j = 0; j < LADO_TABLERO; j++){
                if(jugador[i][j].equals("?")){
                    restantes++;
                }
            }
        }

        return restantes;
    }

    static void partida(){
        int y,x;
        String accion;
        String[] accionesPermitidas = {"R", "r", "M", "m"}; //listado de acciones permitidas para el usuario
        boolean accionCorrecta = false;

        do {
            System.out.println("Quedan " + contadorCasillas + " casillas por descubrir");
            pintarTableros(jugador, "Jugador");

            do { //validación coordenada Y
                System.out.print("Dime tu coordenada Y (fila) de 0 a " + (LADO_TABLERO-1) + ": ");
                y = sc.nextInt();
                if(y < 0 || y >= LADO_TABLERO){
                    System.out.println("Error. Número fuera de rango.");
                }
            }while(y < 0 || y >= LADO_TABLERO);

            do { //validación coordenada X
                System.out.print("Ahora dime tu coordenada X (columna) de 0 a " + (LADO_TABLERO-1) + ": ");
                x = sc.nextInt();
                if(x < 0 || x >= LADO_TABLERO){
                    System.out.println("Error. Número fuera de rango.");
                }
            }while(x < 0 || x >= LADO_TABLERO);

            do { //validación acción
                System.out.println("Por último, dime si quieres (R)omper o (M)arcar la casilla [" + y + ", " + x + "]: ");
                accion = sc.next();
                for(int i = 0; i < accionesPermitidas.length; i++){
                    if(accion.equals(accionesPermitidas[i])){
                        accionCorrecta = true;
                        break;
                    }
                }
            }while(!accionCorrecta);

            //a partir de este punto, ya tenemos los datos para poder realizar la jugada
            if(accion.equals(accionesPermitidas[2]) || accion.equals(accionesPermitidas[3])){ //si la jugada es marcar, realizamos el marcaje o desmarcaje
                if(jugador[y][x].equals("!")){ //si ya estaba marcada la casilla, la desmarcamos
                    jugador[y][x] = "?";
                }else{ //si no, la marcamos
                    jugador[y][x] = "!";
                }
            }else{//si no, es romper, comprobamos que no esté marcada y, si no lo está, llamamos a la función que se encarga de romper casillas
                if(jugador[y][x].equals("!")){
                    System.out.println("No puedes romper una casilla marcada como '!'.");
                    System.out.println("Si realmente quieres romperla, desmárcala primero.");
                }else{
                    switch (minas[y][x]){ //menos engorroso que los if
                        case "*":
                            victoria = true;
                            minaPisada = true;
                            break;
                        case " ":
                            expandirCasillaBlanca(y,x);
                            break;
                        case "1", "2", "3", "4", "5", "6", "7", "8":
                            jugador[y][x] = minas[y][x]; //revelar número
                            break;
                    }
                }

            }
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++\n");
            contadorCasillas = contarCasillasRestantes();
            if(contadorCasillas == NUMERO_MINAS){ //comprobar si el jugador ha ganado
                victoria = true;
            }
        }while(!victoria);

        if(minaPisada){ //derrota
            System.out.println("*********************************************");
            System.out.println("Lo siento, has pisado una mina!");
            pintarTableros(minas, "Minas");
            System.out.println("*********************************************");
        }else{ //victoria
            System.out.println("=============================================");
            System.out.println("Enhorabuena! has desactivado todas las minas!");
            System.out.println("=============================================");
        }
    }

    //ejecución principal del programa
    public static void main(String[] args) {
        inicializarTableros();
        posicionarMinas();
        pintarTableros(minas, "Minas"); //pintar para ver cómo va
        pintarTableros(jugador, "Jugador"); //pintar para ver cómo va
        calcularNumeros();
        pintarTableros(minas, "Minas"); //pintar para ver los cálculos de los números
        mostrarMenu();
        partida();
    }
}
