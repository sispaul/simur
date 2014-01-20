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
        bot_mensaje.setOnclick("alert('HOLA');");
        
        agregarComponente(bot_mensaje);
        
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
