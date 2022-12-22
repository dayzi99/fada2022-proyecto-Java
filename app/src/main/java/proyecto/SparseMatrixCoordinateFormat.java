package proyecto;


import lombok.Getter;
import lombok.Setter;
import java.util.LinkedList; //Librería importada para crear listas enlazadas
import java.io.FileNotFoundException;

public class SparseMatrixCoordinateFormat {

    private LoadFile loader = LoadFile.getInstance();
    @Setter
    private int[][] matrix;
    @Getter
    @Setter
    private int[] rows;
    @Getter
    @Setter
    private int[] columns;
    @Getter
    @Setter
    private int[] values;

    public void createRepresentation(String inputFile) throws FileNotFoundException {
        //Cargar datos
        loader.loadFile(inputFile);
        matrix = loader.getMatrix();

        /* Existen varias formas de representar, como por ejemplo recorrer varias veces la matriz para obtener la cantidad
           de valores que tiene y luego volverla a recorrer para agregar los valores a los arrays
           O también por medio de ArraysList, pero decidimos hacerlo por medio de LinkedList "listas enlazadas".*/


        //Creamos nuevos LinkedList para los atributos porque no sabemos que tamaño van a tener los arreglos
        LinkedList<Integer> filas = new LinkedList<Integer>();
        LinkedList<Integer> columna = new LinkedList<Integer>();
        LinkedList<Integer> valor = new LinkedList<Integer>();

        //En estos condicionales se iteran valores para i y j para recorrer la matrix, cuando se encuentre un valor que sea diferente de cero
        //para una posición actual de i y j, se procederá a capturas dichos valores e ingresarlos a las listas enlazadas previamente declaradas.

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != 0) {
                    filas.add(i);
                    columna.add(j);
                    valor.add(matrix[i][j]);
                }
            }
        }

        //Ahora se reemplaza los valores de los linkedList en los arreglos de formato coordenado

        this.rows = new int[filas.size()];
        this.columns = new int[columna.size()];
        this.values = new int[valor.size()];

        int i;

        //Se itera el LinkedList y se van agregando los datos en el arreglo rows

        i = 0;

        for (int fil: filas) {
            this.rows[i] = fil;
            i++;
        }

        //Se itera el LinkedList y se van agregando los datos en el arreglo columns

        i = 0;

        for (int col: columna) {
            this.columns[i] = col;
            i++;
        }

        //Se itera el LinkedList y se van agregando los datos en el arreglo values

        i = 0;

        for (int val: valor) {
            this.values[i] = val;
            i++;
        }
    }

    public int getElement(int i, int j)
    {
        //Se recorre el arreglo values hasta encontrar los datos que coincidan
        // con las posiciones dadas y retorna el valor en esa posición

        for (int k = 0; k < this.values.length; k++) {
            if (this.rows[k] == i && this.columns[k] == j) {
                return this.values[k];
            }
        }
        return 0;
    }

    //Creamos una función auxiliar para buscar el número más grande de un arreglo
    private int max(int[] num) {

        int numeroMax = num[0];

        for (int numero : num) {

            if (numero > numeroMax) {
                numeroMax = numero;
            }
        }

        return numeroMax;
    }


    public int[] getRow(int i)
    {
        //Se crea un arreglo el cual va a tener el valor máximo de la fila y se le suma 1 porque el arreglo inicia desde 0
        int[] valorFila = new int[max(this.columns) + 1];
        int j = 0;
        int fil;

        //Mientras j sea menor al tamaño del arreglo de la fila, si fila en posición j es igual a i se agrega
        // el valor que haya en j al nuevo arreglo creado y la posición j a la variable fil

        while (j < this.rows.length) {

            if (this.rows[j] == i) {
                fil = this.columns[j];
                valorFila[fil] = this.values[j];
            }
            j++;
        }
        return valorFila;
    }


    public int[] getColumn(int j)
    {
        //Se crea un arreglo el cual va a tener el valor máximo de la fila y se le suma 1 porque el arreglo inicia desde 0
        int[] valorColumna = new int[max(this.rows) + 1];
        int i = 0;
        int col;
        //Mientras i sea menor al tamaño del arreglo de columna, si columna en posición i es igual a j se agrega
        // el valor que haya en i al nuevo arreglo creado y la posición i a la variable col

        while (i < this.columns.length) {

            if (this.columns[i] == j) {
                col = this.rows[i];
                valorColumna[col] = this.values[i];
            }
            i++;
        }
        return  valorColumna;
    }

    public void setValue(int i, int j, int value)
    {
        //Se crean nuevos arreglos para ingresar los nuevos datos

        this.matrix[i][j] = value;
        int[] nuevaFila = new int[this.rows.length + 1]; //Ponemos +1 porque estamos agregando un dato más al arreglo.
        int[] nuevaColumna = new int[this.columns.length + 1];
        int[] nuevoValor = new int[this.values.length + 1];

        //A los nuevos arreglos se le agregan los nuevos datos

        int contador = 0; //Saber cuántos datos hay en la fila donde se insertarán nuevos elementos
        int comienzo = 0; //Funcion auxiliar para ayuda a encontrar la lectura en filas de los datos dentro del arreglo

        for (int p = 0; p < this.rows.length; p++) {

            if (this.rows[p] == i) {
                contador++;

                if (contador == 1) {
                    comienzo = p;
                }
            }
        }

        int encontrarDato = comienzo;

        for (int q = comienzo; q < comienzo + contador; q++) { //Funcion auxiliar que ayuda a leer las columnas del arreglo

            if (encontrarDato < this.columns[q]) {
                encontrarDato = this.columns[q];
            }
        }

        nuevaFila[encontrarDato] = i;  //Se asignan las posiciones en fijas y columnas, incluido el valor a los nuevos arreglos.
        nuevaColumna[encontrarDato] = j;
        nuevoValor[encontrarDato] = value;

        for (int s = 0; s < encontrarDato; s++) {

            nuevaFila[s] = this.rows[s];
            nuevaColumna[s] = this.columns[s]; //Los arreglos nuevos reciben de nuevos sus valores
            nuevoValor[s] = this.values[s];    // para las posiciones anteriores a las del nuevo valor agregado.
        }

        for (int t = encontrarDato + 1; t <= this.rows.length; t++) { //Aquí se realiza el mismo procedimiento que arriba,
                                                                      // cambian en que se rellena el resto del arreglo para
                                                                      // las posiciones posteriores al nuevo valor agregado.
            nuevaFila[t] = this.rows[t - 1];
            nuevaColumna[t] = this.columns[t - 1];
            nuevoValor[t] = this.values[t - 1];
        }

        this.rows = nuevaFila;
        this.columns = nuevaColumna; //Se están asignando los valores de los nuevos arreglos a los arreglos originales.
        this.values = nuevoValor;
    }

    public SparseMatrixCoordinateFormat getSquareMatrix()
    {

        SparseMatrixCoordinateFormat squaredMatrix = new SparseMatrixCoordinateFormat();
        squaredMatrix.setColumns(this.columns);     //Construyendo  la matriz elevada al cuadrado.
        squaredMatrix.setRows(this.rows);

        int[] valor = new int[this.values.length];

        for (int i = 0; i < valor.length; i++) {
            valor[i] = (int) Math.pow(this.values[i], 2);
        }
                                                                         //Esta parte del código es la que se encarga de elevar los valores de la matriz original
                                                                         //al cuadrado y además de asignarlo de igual forma a la matriz.
        int[][] matriz = new int[this.rows.length][this.columns.length];

        for (int i = 0; i < matriz.length; i++) {
            matriz[this.rows[i]][this.columns[i]] = valor[i];
        }

        squaredMatrix.setMatrix(matriz); //Asignando la matriz a la matriz al cuadrado.
        squaredMatrix.setValues(valor); //Asignando los valores a la matriz al cuadrado.

        return squaredMatrix;
    }

    public SparseMatrixCoordinateFormat getTransposedMatrix()
    {

        SparseMatrixCoordinateFormat transposedMatrix = new SparseMatrixCoordinateFormat();
        int[] filaTranspuesta = new int[this.rows.length];
        int[] columnaTranspuesta = new  int[this.columns.length]; //Creando nuevos arreglos para guardar los datos de la matriz transpuesta.
        int[] valores = new int[this.values.length];
        int[][] matrix = new int[this.columns.length][this.rows.length]; //Asignando en forma invertida los tamaños de las filas y las columnas.

        for (int i = 0; i < matrix.length; i++) {
            matrix[this.columns[i]][this.rows[i]] = this.matrix[this.rows[i]][this.columns[i]]; //Este for invierte los valores de las columnas y las filas.

        }
        transposedMatrix.setMatrix(matrix);

        int a = 0;

        for (int i = 0; i < matrix.length; i++) {

            for (int j = 0; j < matrix[0].length; j++) {

                if (matrix[i][j] != 0) {
                    filaTranspuesta[a] = i; //Asignando las filas, columnas y valores en la matriz transpuesta.
                    columnaTranspuesta[a] = j;
                    valores[a] = matrix[i][j];
                    a++;
                }
            }
        }
        transposedMatrix.setColumns(columnaTranspuesta);
        transposedMatrix.setValues(valores);              //Asignando los valores guardados para columna, valores y filas en la matriz transpuesta.
        transposedMatrix.setRows(filaTranspuesta);

        return  transposedMatrix;

    }
}
