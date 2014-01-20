/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.Boton;
import framework.componentes.Etiqueta;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_holamundo extends Pantalla{

    public pre_holamundo() {
        Etiqueta eti_etiqueta=new Etiqueta();
        eti_etiqueta.setValue("HOLA MUNDO");        
        agregarComponente(eti_etiqueta);
        
        Boton bot_mensaje=new Boton();
        bot_mensaje.setValue("Ver mensaje");
        //bot_mensaje.setOnclick("alert('HOLA');");//Ejecuta javascript
        bot_mensaje.setMetodo("verMensaje"); // EJECUTA UN METODO 
        
        //agregarComponente(bot_mensaje);
        
        bar_botones.agregarBoton(bot_mensaje);
        
    }
    
       
    public void verMensaje(){
        System.out.println("ENTRO AL METODO");
    }
    

    @Override
    public void insertar() {
        
    }

    @Override
    public void guardar() {
        
    }

    @Override
    public void eliminar() {
       
    }
    
}
