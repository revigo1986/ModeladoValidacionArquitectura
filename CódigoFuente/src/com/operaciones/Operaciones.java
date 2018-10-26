package com.operaciones;

import java.io.IOException;

public class Operaciones { 
	public int sumar(int param1, int param2, int param3, int param4) {
		int suma = 0;
		suma = param1 + param2 + param3 + param4;
		return suma;
	}
	
	public int restar(int param1, int param2) {
		int resta = 0;
		resta = param1 - param2;
		return resta;
	}
	
	public int multiplicar(int param1, int param2, int param3, int param4) {
		int producto = 0;
		producto = param1 * param2 * param3 * param4;
		return producto;
	}
	
	public double dividir(int param1, int param2) throws IOException{
		double cociente = 0;
		if(param2 == 0) {
			throw new IOException("No se puede dividir por cero");
		}
		cociente = param1 / param2;
		return cociente;
	}
}