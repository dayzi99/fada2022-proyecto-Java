package proyecto;

import javax.naming.OperationNotSupportedException;
import lombok.Getter;
import java.util.LinkedList;
import java.io.FileNotFoundException;

public class SparseMatrixCSC {
    private LoadFile loader = LoadFile.getInstance();
    private int[][] matrix;
    @Getter
    private int[] rows;
    @Getter
    private int[] columns;
    @Getter
    private int[] values;

    public void createRepresentation(String inputFile) throws FileNotFoundException {
        //Load data
        loader.loadFile(inputFile);
        matrix = loader.getMatrix();

        //Creamos nuevos LinkedList para los atributos valor y columna ya que no sabemos el tamaña que tendrán.
        //Columnas se obtiene de una manera diferente
        LinkedList<Integer> valor = new LinkedList<Integer>();
        LinkedList<Integer> fila = new LinkedList<Integer>();
        this.columns = new int[this.matrix[0].length + 1];
        int contador = 0;

        //La matriz se recorre de forma inversa, primero las columnas y luego las filas.

        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[j][i] != 0) {
                    valor.add(matrix[j][i]);
                    fila.add(j);
                    contador++;
                }
            }
            if (i == 0) {
                this.columns[1] = contador;
                contador = 0;
            } else {
                this.columns[i + 1] = this.columns[i] + contador;
                contador = 0;
            }
        }

        this.rows = new int[fila.size()];
        this.values = new int[valor.size()];

        int i;
        i = 0;
        for (int val: valor) {
            this.values[i] = val;
            i++;
        }

        i = 0;
        for (int row: fila) {
            this.rows[i] = row;
            i++;
        }
    }

    public int getElement(int i, int j)
    {
        for (int z = this.columns[j]; z < this.columns[j + 1]; z++) {
            if (this.rows[z] == i) {
                return this.values[z];
            }
        }
        return 0;
    }

    public int[] getRow(int i)
    {
        int[] valorFila = new int[this.columns.length - 1];
        for (int j = 0; j < valorFila.length; j++) {
            for (int z = this.columns[j]; z < this.columns[j + 1]; z++) {
                if (this.rows[z] == i) {
                    valorFila[j] = this.values[z];
                }
            }
        }
        return valorFila;
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
    public int[] getColumn(int j)
    {
        int[] valorColumna = new int[max(this.rows) + 1];

        for (int i = 0; i < this.columns.length; i++) {
            if (i == j) {
                for (int c = this.columns[i]; c < this.columns[i + 1]; c++) {
                    valorColumna[this.rows[c]] = this.values[c];

                }
            }
        }
        return valorColumna;
    }

    public void setValue(int i, int j, int value) throws OperationNotSupportedException
    {
        throw new OperationNotSupportedException();
    }

    public SparseMatrixCSC getSquareMatrix() throws OperationNotSupportedException
    {
        SparseMatrixCSC squaredMatrix = new SparseMatrixCSC();
        squaredMatrix.setColumns(this.columns);
        squaredMatrix.setRows(this.rows);
        int[] values = new int[this.values.length];
        for (int i = 0; i<values.length; i++) {
            values[i] = (int) Math.pow(this.values[i], 2);
        }
        squaredMatrix.setValues(values);
        return squaredMatrix;
    }

    public SparseMatrixCSC getTransposedMatrix() throws OperationNotSupportedException
    {
        SparseMatrixCSC squaredMatrix = new SparseMatrixCSC();
        throw new OperationNotSupportedException();
    }
}
