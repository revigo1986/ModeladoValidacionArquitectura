package com.componentes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Factura {

    private int idFactura;
    private double valorFactura;
    private String descripcion;

    public Factura() {
    }

    public Factura(double valorFactura) {
        this.valorFactura = valorFactura;
    }

    public Factura(int idFactura, double valorFactura, String descripcion) {
        this.idFactura = idFactura;
        this.valorFactura = valorFactura;
        this.descripcion = descripcion;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public double getValorFactura() {
        return valorFactura;
    }

    public void setValorFactura(double valorFactura) {
        this.valorFactura = valorFactura;
    }

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
