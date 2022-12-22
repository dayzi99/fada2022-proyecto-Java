package proyecto;

import javax.naming.OperationNotSupportedException;
import lombok.Getter;
import lombok.Setter;
import java.util.LinkedList; //Librería importada para crear listas enlazadas
import java.io.FileNotFoundException;

public class SparseMatrixCSR {
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
        //Load data
        loader.loadFile(inputFile);
        matrix = loader.getMatrix();

        //Creamos nuevos LinkedList para los atributos valor y columna ya que no sabemos el tamaña que tendrán.
        //Filas se obtiene de una manera diferente
        LinkedList<Integer> valor = new LinkedList<Integer>();
        LinkedList<Integer> columna = new LinkedList<Integer>();
        this.rows = new int[this.matrix.length + 1]; //Se le suma uno por definición de representación CSR que tiene en cuenta al último número como la cantidad total de NZE.

        boolean filaInicial = false; //Para saber cuando inicia una fila

        int m = 0;

        for (int i = 0; i < matrix.length; i++) {

            for (int j = 0; j < matrix[0].length; j++) {

                if (j == 0) {
                    filaInicial = true;     //Comprobamos que la fila siempre inicie en 0
                }

                if (matrix[i][j] != 0) {
                    valor.add(matrix[i][j]);
                    columna.add(j);

                    if (filaInicial) {
                        rows[m] = valor.size() - 1;
                        m++;
                        filaInicial = false;
                    }
                } else if (j == matrix[0].length - 1 && filaInicial) {
                    rows[m] = valor.size();
                    m++;
                }
            }
        }

        rows[m] = valor.size();

        //Se llenan los arreglos con los datos de los LinkedList

        this.values = new int[valor.size()];
        this.columns = new int[columna.size()];

        int i = 0;

        for (int val: valor) {
            this.values[i] = val;   //Se recorre el linkedlist valor y se le asignan los elementos en el arreglo values
            i++;
        }

        i = 0;

        for (int col: columna) {
            this.columns[i] = col;  //Se recorre el linkedlist columns y se le asignan los elementos en el arreglo values
            i++;
        }
    }

    public int getElement(int i, int j)
    {
        //Se recorre el arreglo values hasta encontrar los datos que coincidan
        // con las posiciones dadas y retorna el valor en esa posición

        for (int d = this.rows[i]; d < this.rows[i + 1]; d++) {

            if (this.columns[d] == j) {
                return this.values[d];
            }
        }

        return 0;
    }

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
        int[] valorFila = new int[max(this.columns) + 1];
        int[] index = new int[this.rows[i + 1 ] - this.rows[i]];
        int[] valor = new int[this.rows[i + 1] - this.rows[i]];
        int n = 0;

        for (int z = this.rows[i]; z < this.rows[i + 1]; z++) {
            index[n] = this.columns[z];
            valor[n] = this.values[z];
            n++;
        }

        for (n = 0; n < index.length; n++) {
            valorFila[index[n]] = valor[n];
        }

        return valorFila;
    }

    public int[] getColumn(int j)
    {
        int[] valorColumna = new int[this.rows.length - 1];

        for (int i = 0; i < valorColumna.length; i++) {

            for (int z = this.rows[i]; z < this.rows[i + 1]; z++) {

                if (this.columns[z] == j) {
                    valorColumna[i] = this.values[z];
                }
            }
        }

        return valorColumna;
    }

    public void setValue(int i, int j, int value) throws OperationNotSupportedException
    {
        throw new OperationNotSupportedException();
    }

    public SparseMatrixCSR getSquareMatrix()
    {
        SparseMatrixCSR squaredMatrix = new SparseMatrixCSR();
        squaredMatrix.setColumns(this.columns);
        squaredMatrix.setRows(this.rows);

        int[] valor = new int[this.values.length];

        for (int i = 0; i < valor.length; i++) {
            valor[i] = (int) Math.pow(this.values[i], 2);
        }
        squaredMatrix.setValues(valor);

        return squaredMatrix;
    }

    public SparseMatrixCSR getTransposedMatrix() throws OperationNotSupportedException
    {
        SparseMatrixCSR squaredMatrix = new SparseMatrixCSR();
        throw new OperationNotSupportedException();
    }

}
