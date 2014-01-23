/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.SeleccionTabla;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_componentes extends Pantalla {

    private Dialogo dia_dialogo = new Dialogo();
    private SeleccionTabla set_tabla = new SeleccionTabla();

    public pre_componentes() {
        //Configurando el dialogo
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("DIALOGO DE PRUEBA"); //titulo
        dia_dialogo.setWidth("50%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("40%");//siempre porcentaje   alto

        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.setModal(false); //para que no bloque la pantalla

        agregarComponente(dia_dialogo);

        Etiqueta eti = new Etiqueta();
        eti.setValue("MENSAJE DE PRUEBA");
        eti.setStyle("font-size:30px;color:blue");
        //Agrega la etiqueta al dialogo
        dia_dialogo.setDialogo(eti);

        //boton aceptar
        dia_dialogo.getBot_aceptar().setMetodo("aceptoDialogo");


        Boton bot = new Boton();
        bot.setValue("ABRIR DIALOGO");
        bot.setMetodo("abrirDialogo");

        bar_botones.agregarBoton(bot);

        ///configurar el seleccion tabla 

        set_tabla.setId("set_tabla");
        set_tabla.setTitle("SELECCIONE MARCAS");
        set_tabla.setSeleccionTabla("SELECT ide_marca,marca from trans_marcas", "ide_marca");
        set_tabla.getBot_aceptar().setMetodo("aceptoSeleccionTabla");

        agregarComponente(set_tabla);

        Boton bot1 = new Boton();
        bot1.setValue("ABRIR SELECCION TABLA");
        bot1.setMetodo("abrirSeleccionTabla");

        bar_botones.agregarBoton(bot1);



    }

    public void aceptoSeleccionTabla(){
        if(set_tabla.getSeleccionados()!=null){
            utilitario.agregarMensaje("SELECCIONADOS", set_tabla.getSeleccionados());
            set_tabla.cerrar();
        }
        else{
            utilitario.agregarMensajeInfo("Debe seleccionar almenos un registro", "");
        }
    }
    
    
    public void abrirSeleccionTabla() {
        set_tabla.dibujar();
    }

    public void aceptoDialogo() {
        utilitario.agregarMensaje("ACEPTO EL DIALOGO", "");
        //cierra el dialog
        dia_dialogo.cerrar();
    }

    public void abrirDialogo() {
        dia_dialogo.dibujar();
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

    public Dialogo getDia_dialogo() {
        return dia_dialogo;
    }

    public void setDia_dialogo(Dialogo dia_dialogo) {
        this.dia_dialogo = dia_dialogo;
    }

    public SeleccionTabla getSet_tabla() {
        return set_tabla;
    }

    public void setSet_tabla(SeleccionTabla set_tabla) {
        this.set_tabla = set_tabla;
    }
}
