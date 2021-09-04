package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import vista.Ventana;

/**
 *
 * @author paula
 */
public class controlador implements ActionListener {

    Ventana frmprincipal;
    ArrayList<String> base64;   //Almacena todos los valores que tiene la base 64
    String mensaje;
    DecimalFormat df = new DecimalFormat("#.############");
    String procedimiento = "";
    String procedimientoBase10 = "";

    public controlador() {
        this.frmprincipal = new Ventana();
        this.frmprincipal.getBtnCalcu().addActionListener(this);

    }

    public void iniciar() {
        this.frmprincipal.setTitle("SUMAS DE BASES");
        this.frmprincipal.setLocationRelativeTo(null);
        this.frmprincipal.setVisible(true);
        generarBase64();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        procedimiento = "";
        procedimientoBase10="";
        if (e.getSource().equals(frmprincipal.getBtnCalcu())) {
            String numero = frmprincipal.getTxtfCantNum().getText(); //almacenamos la suma
            int base = Integer.parseInt(frmprincipal.getTxtfBase().getText()); //almacenamos la base.

            String[] valores = numero.trim().split("\\+");// 5Dc+2s+1 [0]=5Dc [1]=2s [2]=1
            int mayor = obtenerMayor(valores); //Obtener cual de lo valores tiene la mayor longitud   [0]=5Dc [1]=2s [2]=1 es mayor
            valores = rellenarNumero(valores, mayor); //método para rellenar los 0 correspondientes dependiendo de la mayor longitud

            String total = suma(valores, mayor, base);

            frmprincipal.getTxtfResul().setText(total);
            frmprincipal.getTxtaProce().setText(procedimiento);
            comprobacion(valores,base);
            frmprincipal.getTxtaCompro().setText(procedimientoBase10);
        }
    }

    public String suma(String[] valores, int mayor, int base) {             //realiza la suma directa
        String total = "";
        int lleva = 0;
        String op;  //auxiliar de mensaje
        for (int i = mayor; i > 0; i--) { //recorre el ciclo de atrás hacia adelante para hacer la suma de derecha a izquierda
            int suma = 0;
            op = "";
            mensaje = ""; 
            for (int j = 0; j < valores.length; j++) {
                op += base64.indexOf(Character.toString(valores[j].charAt(i - 1))) + " + "; //mensaje
                suma += base64.indexOf(Character.toString(valores[j].charAt(i - 1)));    // 5Dc+02s+001 = 1. c+s+1
            }
            suma += lleva;
            mensaje += op + lleva + " = ";
            if (suma >= base) {
                procedimiento += "\n" + mensaje + suma + "\n";
                procedimiento += "Resultado " + suma + " mayor o igual a la base " + base + "\n";
                int resultado = suma / base;
                procedimiento += suma + "/" + base + "= " + resultado + "\t Residuo: " + suma % base + "\n";
                lleva = resultado;
                int residuo = suma % base;
                suma = residuo;
                mensaje = suma + " = " + base64.get(suma) + "\t" + "Lleva: " + lleva + "\n";;
            } else {
                lleva = 0;
                mensaje += base64.get(suma) + "\t" + "Lleva: " + lleva + "\n";

            }
            total += base64.get(suma);
            procedimiento += mensaje + "TOTAL:" + revertirCadena(total) + "\n";
        }
        if (lleva != 0) {
            total += lleva;
            procedimiento += "\nSobrante  +" + lleva + "\n";
        }
        procedimiento += "\nOrganizamos el resultado:\n " + revertirCadena(total);
        System.out.println(procedimiento);
        return revertirCadena(total);
    }

    public void comprobacion(String valores[], int base) {
        long sumab10 = 0;
        long valor=0;
        long residuo;
        String cadena="",aux="";
        
        procedimientoBase10="Convirtiendo a base 10\n";
     
        for (int i = 0; i < valores.length; i++) { //suma en base 10
            long entero= base10(valores[i], base);
            sumab10 += entero;
            procedimientoBase10+=valores[i]+" = "+entero+"\n\n";
            aux+=entero;
            if(i<valores.length-1)
            {
                aux+=" + ";
            }
        }
        procedimientoBase10+=aux+" = "+sumab10+"\n\n";
        valor=sumab10;
        procedimientoBase10+="Convirtiendo "+sumab10+" a base "+base+"\n";
        while (valor != 0) {
            procedimientoBase10 += valor + " / " + base + " = ";
            residuo = valor % base;                                                     //Obtiene el residuo
            valor = valor / base;
            cadena += base64.get((int) residuo);                                            // Obtiene la letra o número del residuo
            procedimientoBase10 += valor + " Residuo: " + residuo + " Acumulado: " + cadena + "\n";
        }
        cadena = revertirCadena(cadena);                                          // Revertimos la cadena
        procedimientoBase10+="TOTAL: "+cadena+"\n";
    }

    public long base10(String valor, int base) {
        procedimientoBase10 += "\nConvirtiendo " + valor + " a base 10\n";
        long acum = 0;
        int num;
        int i = 0; // elevado a la i....
        for (int j = valor.length() - 1; j >= 0; j--) {                        //Proceso para pasar cualquier número a base 10
            num = base64.indexOf(Character.toString(valor.charAt(j)));
            acum += (Math.pow(base, i) * num);
            procedimientoBase10 += "(" + base + "^" + i + ") x " + num + " = " + acum + "\n";
            i++;
        }
        return acum;
    }

    public String[] rellenarNumero(String arreglo[], int mayor) { //[0]=5Dc [1]=2s [2]=1 ,  3
        for (int i = 0; i < arreglo.length; i++) {
            System.out.println(arreglo[i]);
            int resultado = mayor - (arreglo[i].length()); // 1. 3 - 3 =0 2. 3-2 =1 3. 3-1 = 2 
            if (resultado > 0) {
                String nuevo = "";
                for (int j = 0; j < resultado; j++) {
                    nuevo += "0"; 
                }
                nuevo += arreglo[i]; // [1]=02s [2]=001
                arreglo[i] = nuevo;
            }
        }
        return arreglo;
    }

    public int obtenerMayor(String[] arreglo) {
        int mayor = 0;
        for (int i = 0; i < arreglo.length; i++) {
            if (arreglo[i].length() > arreglo[mayor].length()) {
                mayor = i;
            }
        }
        return arreglo[mayor].length();         //retorna el tamaño de la mayor suma 
    }

    public void generarBase64() {                                   //generamos la base 64
        int i = 0; // iterador para la posición del valor
        base64 = new ArrayList<String>();
        //generar los números de 0 a 9
        for (i = 0; i < 10; i++) {
            base64.add(i, String.valueOf(i));
        }
        //generar los valores del A al Z
        for (char caracter = 'A'; caracter <= 'Z'; caracter++) {
            if (i == 24) {
                base64.add(i, Character.toString('Ñ'));
                caracter--;
            } else {
                base64.add(i, Character.toString(caracter));
            }
            i++;
        }
        //genera los valores de a a z
        for (char caracter = 'a'; caracter <= 'z'; caracter++) {
            if (i == 51) {
                base64.add(i, Character.toString('ñ'));
                caracter--;
            } else {
                base64.add(i, Character.toString(caracter));
            }
            i++;
        }
    }

    public String revertirCadena(String cadena) {
        String nueva_cadena = "";
        for (int i = cadena.length() - 1; i >= 0; i--) {
            nueva_cadena += cadena.charAt(i);
        }
        return nueva_cadena;
    }

    public String obtenerEntero(String entero) //Cambiamos las letras por su posición en la base 64
    {
        String dato = "";
        for (int i = 0; i < entero.length(); i++) {
            dato += base64.indexOf(Character.toString(entero.charAt(i)));
        }
        return dato;
    }

}
