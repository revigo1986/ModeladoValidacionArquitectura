package com.componentes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FacturaInterna {

    private int idFactura;
    private double saldoAPagar;
    private String descripcion;

    public FacturaInterna() {
    }

    public FacturaInterna(double saldoAPagar) {
        this.saldoAPagar = saldoAPagar;
    }

    public FacturaInterna(int idFactura, double saldoAPagar, String descripcion) {
        this.idFactura = idFactura;
        this.saldoAPagar = saldoAPagar;
        this.descripcion = descripcion;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public double getSaldoAPagar() {
        return saldoAPagar;
    }

    public void setSaldoAPagar(double saldoAPagar) {
        this.saldoAPagar = saldoAPagar;
    }

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
