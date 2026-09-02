
package com.mycompany.geriatrico1.modelo;

public class Reporte {
    private String categorias;
    private long cantidad;

    public Reporte(String categorias, long cantidad) {
        this.categorias = categorias;
        this.cantidad = cantidad;
    }
    
    

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }

    public long getCantidad() {
        return cantidad;
    }

    public void setCantidad(long cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Reporte{" + "categorias=" + categorias + ", cantidad=" + cantidad + '}';
    }
    
    
    
}