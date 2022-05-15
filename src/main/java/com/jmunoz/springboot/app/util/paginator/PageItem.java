package com.jmunoz.springboot.app.util.paginator;

// Representa cada una de las páginas. Tiene el número de página y un atributo para indicar si es o no
// página actual.
public class PageItem {

    private int numero;
    private boolean actual;

    public PageItem(int numero, boolean actual) {
        this.numero = numero;
        this.actual = actual;
    }

    public int getNumero() {
        return numero;
    }

    public boolean isActual() {
        return actual;
    }
}
