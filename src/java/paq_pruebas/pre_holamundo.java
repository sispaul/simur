/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

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
