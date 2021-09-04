
package modelo;

/**
 *
 * @author paula
 */
public class Numeros {
    private int posicion;
    private String valor;

    public Numeros()
    {
        
    }

    public Numeros(int posicion, String valor) {
        this.posicion = posicion;
        this.valor = valor;
    }
    
    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
