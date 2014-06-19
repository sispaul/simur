/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Texto;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author KEJA
 */
public class pre_solicitud_anticipos extends Pantalla{

    //texto de ingreso
    private Texto txt_comentario = new Texto();
    private Texto txt_ci = new Texto();
    
    //dialogos
    private Dialogo dia_autoriza = new Dialogo();
    private Grid grid_au = new Grid();
    private Grid grida = new Grid();
    
    public pre_solicitud_anticipos() {
    //Botones para autorizar o quitar autorizacion
        Boton bt=new Boton();
        bt.setValue("OPCIONES:  ");
        bt.setMetodo("eventos");
        bar_botones.agregarBoton(bt);
        
        //Dialogo de autorizacion
        dia_autoriza.setId("dia_dialogoe");
        dia_autoriza.setTitle("ESCOGER LO QUE SE DESEA HACER"); //titulo
        dia_autoriza.setWidth("25%"); //siempre en porcentajes  ancho
        dia_autoriza.setHeight("25%");//siempre porcentaje   alto 
        dia_autoriza.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_autoriza.getBot_aceptar().setDisabled(true);
        grid_au.setColumns(4);
        agregarComponente(dia_autoriza);
        
    }

    public void eventos(){
        Boton bn=new Boton();
        bn.setValue("NUEVA SOLICITUD");
        bn.setMetodo("eventos");
        
        Boton bb=new Boton();
        bb.setValue("BUSCAR ESTADO SOLICITUD");
        bb.setMetodo("busca");
        
        dia_autoriza.Limpiar();
        dia_autoriza.setDialogo(grida);
        txt_comentario.setSize(10);
        grida.getChildren().add(new Etiqueta("COD EMPLEADO:"));
        grida.getChildren().add(txt_comentario);
        txt_ci.setSize(15);
        grida.getChildren().add(new Etiqueta("C.I. EMPLEADO:"));
        grida.getChildren().add(txt_ci);
        grida.getChildren().add(bn);
        grida.getChildren().add(bb);
        dia_autoriza.setDialogo(grid_au);
        dia_autoriza.dibujar();
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
