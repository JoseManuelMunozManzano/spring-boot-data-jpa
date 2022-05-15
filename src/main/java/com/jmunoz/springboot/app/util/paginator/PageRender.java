package com.jmunoz.springboot.app.util.paginator;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

// Clase que calcula desde que elemento hasta cual mostrar, yendo hacia delante o atrás...
// El nombre de la clase puede ser cualquiera
public class PageRender<T> {

    private String url;
    private Page<T> page;

    // Para los cálculos
    private int totalPaginas;
    private int numElementosPorPagina;
    private int paginaActual;

    private List<PageItem> paginas;

    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;
        this.paginas = new ArrayList<>();

        // El size, tal y como se indica en ClienteController, serían 4 elementos por página, pero se podría
        // poner cualquier valor.
        // page.getNumber() parte desde 0, ya que en el controller se indicó defaultValue="0". Se suma 1 para la vista,
        // para que en ella parta desde la página 1.
        numElementosPorPagina = page.getSize();
        totalPaginas = page.getTotalPages();
        paginaActual = page.getNumber() + 1;

        // Desde y hasta para dibujar el paginador en la vista
        int desde, hasta;
        if (totalPaginas <= numElementosPorPagina) {
            desde = 1;
            hasta = totalPaginas;
        } else {
            if (paginaActual <= numElementosPorPagina/2) {
                desde = 1;
                hasta = numElementosPorPagina;
            } else if (paginaActual >= totalPaginas - numElementosPorPagina/2) {
                desde = totalPaginas - numElementosPorPagina + 1;
                hasta = numElementosPorPagina;
            } else {
                desde = paginaActual - numElementosPorPagina/2;
                hasta = numElementosPorPagina;
            }
        }

        for (int i=0; i<hasta; i++) {
            paginas.add(new PageItem(desde + i, paginaActual == desde + i));
        }
    }

    public String getUrl() {
        return url;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public int getPaginaActual() {
        return paginaActual;
    }

    public List<PageItem> getPaginas() {
        return paginas;
    }

    public boolean isFirst() {
        return page.isFirst();
    }

    public boolean isLast() {
        return page.isLast();
    }

    public boolean isHasNext() {
        return page.hasNext();
    }

    public boolean isHasPrevious() {
        return page.hasPrevious();
    }
}
