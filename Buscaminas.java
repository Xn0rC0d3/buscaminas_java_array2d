package com.emiliojimeno.daw.buscaminas;

import static java.lang.Math.random;
import static java.lang.Math.round;

public class Buscaminas {
    static final int LADO_TABLERO = 9;
    static final int NUMERO_MINAS = 10;
    static String[][] minas = new String[LADO_TABLERO][LADO_TABLERO];
    static String[][] jugador = new String[LADO_TABLERO][LADO_TABLERO];
    static boolean victoria = false;
    static int contadorCasillas = LADO_TABLERO * LADO_TABLERO;

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
        System.out.println("Pintando tablero " + nombre + ": ");
        for(int i = 0; i < LADO_TABLERO; i++){
            for(int j = 0; j < LADO_TABLERO; j++){
                System.out.print(array[i][j]+ "  ");
            }
            System.out.println(" ");
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
        System.out.println("Éste es tu tablero:");
        pintarTableros(jugador, "Jugador");
    }

    //ejecución principal del programa
    static void main(String[] args) {
        inicializarTableros();
        posicionarMinas();
        pintarTableros(minas, "Minas"); //pintar para ver cómo va
        pintarTableros(jugador, "Jugador"); //pintar para ver cómo va
        calcularNumeros();
        pintarTableros(minas, "Minas"); //pintar para ver los cálculos de los números
        mostrarMenu();
    }
}
