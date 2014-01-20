/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Texto;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_holamundo extends Pantalla{
    
    private AutoCompletar aut_marcas=new AutoCompletar();

    public pre_holamundo() {
        Etiqueta eti_etiqueta=new Etiqueta();
        eti_etiqueta.setValue("HOLA MUNDO");  
        eti_etiqueta.setStyle("font-size:18px;color:red"); //pone un estilo css
        agregarComponente(eti_etiqueta);
        
        Boton bot_mensaje=new Boton();
        bot_mensaje.setValue("Ver mensaje");
        //bot_mensaje.setOnclick("alert('HOLA');");//Ejecuta javascript
        bot_mensaje.setMetodo("verMensaje"); // EJECUTA UN METODO 
        
        //agregarComponente(bot_mensaje);
        
        bar_botones.agregarBoton(bot_mensaje);
        
        
        Grid grid=new Grid();/// me forma una matriz
        grid.setColumns(2);
        //Creo una etiqueta
        grid.getChildren().add(new Etiqueta("NOMBRE ")); //agrega a la grid la etiqueta
        //Crear cuadro de texto
        Texto tex_nombre=new Texto();        
        grid.getChildren().add(tex_nombre);// agrega el etxto a la grid
        
        //CREAR COMBO
        Combo com_combo=new Combo();
        com_combo.setCombo("select ide_marca,marca from trans_marcas");
        
        grid.getChildren().add(new Etiqueta("MARCAS"));
        grid.getChildren().add(com_combo);
        
        //CREAR AUTOCOMPLETAR 
        aut_marcas.setId("aut_marcas"); //ID del autocompletar
        aut_marcas.setAutoCompletar("select ide_marca,marca from trans_marcas");
        
        grid.getChildren().add(new Etiqueta("MARCAS AUTOCOMPLETAR"));
        grid.getChildren().add(aut_marcas);
        
        agregarComponente(grid);
        
        
        
        
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

    public AutoCompletar getAut_marcas() {
        return aut_marcas;
    }

    public void setAut_marcas(AutoCompletar aut_marcas) {
        this.aut_marcas = aut_marcas;
    }
    
    
    
}
