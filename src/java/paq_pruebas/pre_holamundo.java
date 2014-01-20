/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Hora;
import framework.componentes.Texto;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_holamundo extends Pantalla {

    private AutoCompletar aut_marcas = new AutoCompletar();
    private Texto tex_nombre = new Texto();
    private Combo com_combo = new Combo();

    public pre_holamundo() {
        Etiqueta eti_etiqueta = new Etiqueta();
        eti_etiqueta.setValue("HOLA MUNDO");
        eti_etiqueta.setStyle("font-size:18px;color:red"); //pone un estilo css
        agregarComponente(eti_etiqueta);

        Boton bot_mensaje = new Boton();
        bot_mensaje.setValue("Ver mensaje");
        //bot_mensaje.setOnclick("alert('HOLA');");//Ejecuta javascript
        bot_mensaje.setMetodo("verMensaje"); // EJECUTA UN METODO 

        //agregarComponente(bot_mensaje);

        bar_botones.agregarBoton(bot_mensaje);


        Grid grid = new Grid();/// me forma una matriz
        grid.setColumns(2);
        //Creo una etiqueta
        grid.getChildren().add(new Etiqueta("NOMBRE ")); //agrega a la grid la etiqueta
        //Crear cuadro de texto
        tex_nombre.setId("tex_nombre");
        grid.getChildren().add(tex_nombre);// agrega el etxto a la grid

        //CREAR COMBO
        
        com_combo.setMetodo("seleccionoCombo");
        com_combo.setCombo("select ide_marca,marca from trans_marcas");

        grid.getChildren().add(new Etiqueta("MARCAS"));
        grid.getChildren().add(com_combo);

        //CREAR AUTOCOMPLETAR 
        aut_marcas.setId("aut_marcas"); //ID del autocompletar
        aut_marcas.setAutoCompletar("select ide_marca,marca from trans_marcas");

        grid.getChildren().add(new Etiqueta("MARCAS AUTOCOMPLETAR"));
        grid.getChildren().add(aut_marcas);


        //CALENDARIO
        Calendario cal_calendario = new Calendario();
        grid.getChildren().add(new Etiqueta("FECHA"));
        grid.getChildren().add(cal_calendario);
        
         //HORA
        Hora hor_hora = new Hora();
        grid.getChildren().add(new Etiqueta("HORA"));
        grid.getChildren().add(hor_hora);
        
        
        
        

        agregarComponente(grid);




    }
    
    /**
     * Se ejecuta cada ves q seleccione un elemento del combo
     */
    public void seleccionoCombo(){
        tex_nombre.setValue(com_combo.getValue());
        utilitario.addUpdate("tex_nombre"); //UTILIZA AJAX SOLO PARA ACTUALIZAR EL CAMPO DE TEXTO tex_nombre
    }

    public void verMensaje() {
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
