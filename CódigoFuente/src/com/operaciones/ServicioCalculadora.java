package com.operaciones;  

import java.io.IOException;

import javax.ws.rs.GET; 
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces; 
import javax.ws.rs.core.MediaType;  
@Path("/Operaciones") 

public class ServicioCalculadora {  
   Operaciones operaciones = new Operaciones();  
   @GET
   @Path("/sum/{param1}/{param2}/{param3}/{param4}") 
   @Produces(MediaType.TEXT_PLAIN) 
   public int getSuma(@PathParam("param1") int param1,
		   @PathParam("param2") int param2,
		   @PathParam("param3") int param3,
		   @PathParam("param4") int param4){ 
      return operaciones.sumar(param1, param2, param3, param4); 
   }  
   
   @GET
   @Path("/res/{param1}/{param2}") 
   @Produces(MediaType.TEXT_PLAIN) 
   public int getResta(@PathParam("param1") int param1,
		   @PathParam("param2") int param2) {
      return operaciones.restar(param1, param2); 
   }
   
   @GET
   @Path("/mul/{param1}/{param2}/{param3}/{param4}")
   @Produces(MediaType.TEXT_PLAIN)
   public int getMultiplicacion(@PathParam("param1") int param1,
		   @PathParam("param2") int param2,
		   @PathParam("param3") int param3,
		   @PathParam("param4") int param4){
	   return operaciones.multiplicar(param1, param2, param3, param4);
   }
   
   @GET
   @Path("/div/{param1}/{param2}")
   @Produces(MediaType.TEXT_PLAIN)
   public double getDivision(@PathParam("param1") int param1, @PathParam("param2") int param2)
		   throws IOException{
	   return operaciones.dividir(param1, param2);
   }
}