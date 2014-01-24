/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_pruebas;

import com.google.common.collect.HashBiMap;
import framework.componentes.Boton;
import framework.componentes.Dialogo;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Reporte;
import framework.componentes.SeleccionCalendario;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.SeleccionTabla;
import framework.componentes.Texto;
import java.util.HashMap;
import java.util.Map;
import paq_sistema.aplicacion.Pantalla;

/**
 *
 * @author Diego
 */
public class pre_componentes extends Pantalla {

    private Dialogo dia_dialogo = new Dialogo();
    private SeleccionTabla set_tabla = new SeleccionTabla();
    private SeleccionTabla set_modelos = new SeleccionTabla();
    private SeleccionCalendario sec_rango = new SeleccionCalendario();
    //Buscar 
    private Texto tex_busca = new Texto();
    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    public pre_componentes() {
        bar_botones.limpiar(); /// deja en blanco la barra de botones

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

        Grid gri_busca = new Grid();
        gri_busca.setColumns(3);
        gri_busca.getChildren().add(tex_busca);

        Boton bot_busca = new Boton();
        bot_busca.setValue("Buscar");
        bot_busca.setMetodo("buscarMarca");
        gri_busca.getChildren().add(bot_busca);

        Boton bot_ver = new Boton();
        bot_ver.setValue("Ver Todos");
        bot_ver.setMetodo("verTodos");
        gri_busca.getChildren().add(bot_ver);


        set_tabla.getGri_cuerpo().setHeader(gri_busca);

        set_tabla.setTitle("SELECCIONE MARCAS");
        set_tabla.setSeleccionTabla("SELECT ide_marca,marca from trans_marcas", "ide_marca");
         set_tabla.getTab_seleccion().getColumna("marca").setFiltro(true);
        set_tabla.getBot_aceptar().setMetodo("aceptarReporte");



        agregarComponente(set_tabla);

        Boton bot1 = new Boton();
        bot1.setValue("ABRIR SELECCION TABLA");
        bot1.setIcon("ui-icon-document"); //pone icono de jquery temeroller
        bot1.setMetodo("abrirSeleccionTabla");
        bar_botones.agregarBoton(bot1);

////configurar seleccion unica
        set_modelos.setId("set_modelos");
        set_modelos.setTitle("SELECCIONE UN MODELO");
        set_modelos.setSeleccionTabla("SELECT ide_modelo,des_modelo,marca from trans_modelos modelo INNER JOIN trans_marcas marca on modelo.ide_marca=marca.ide_marca\n"
                + "order by marca,des_modelo", "ide_modelo");
        set_modelos.setRadio(); /// para que permita seleccionar un solo registro
        set_modelos.getBot_aceptar().setMetodo("aceptoSeleccionTablaUnica");

        agregarComponente(set_modelos);

        Boton bot2 = new Boton();
        bot2.setValue("ABRIR SELECCION TABLA UNICA");
        bot2.setMetodo("abrirSeleccionTablaUnica");
        bot2.setIcon("ui-guardar"); //pone el icono de guardar que esta creado en estilo.css
        bar_botones.agregarBoton(bot2);

        /////configurar el seleccion calendario
        sec_rango.setId("sec_rango");
        sec_rango.setTitle("RNAGO DE FECHAS");
        sec_rango.setMultiple(false); //mostrar una fecha
        sec_rango.getBot_aceptar().setMetodo("aceptoSeleccionCalendario");
        agregarComponente(sec_rango);


        Boton bot3 = new Boton();
        bot3.setValue("ABRIR SELECCION CALENDARIO");
        bot3.setMetodo("abrirSeleccionCalendario");
        bar_botones.agregarBoton(bot3);


        /**
         * CONFIGURACIÓN DE ONJETO REPORTE
         */
        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 

        agregarComponente(rep_reporte); //2 agregar el listado de reportes

        sef_formato.setId("sef_formato");
        agregarComponente(sef_formato);


    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    @Override
    public void aceptarReporte() {
        if (rep_reporte.getNombre().equals("Reporte Prueba")) {

            if (rep_reporte.isVisible()) {
                rep_reporte.cerrar();
                set_tabla.dibujar();
            } else if (set_tabla.isVisible()) {
                //los parametros de este reporte
                p_parametros = new HashMap();
                p_parametros.put("p_titulo", "PARAMTRO TITULO DEL REPORTE");
                p_parametros.put("p_marcas", set_tabla.getSeleccionados());
                set_tabla.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
            }
        }

    }

    public void verTodos() {
        set_tabla.getTab_seleccion().setSql("SELECT ide_marca,marca from trans_marcas");
        set_tabla.getTab_seleccion().ejecutarSql();
    }

    public void buscarMarca() {
        if (tex_busca.getValue() != null && tex_busca.getValue().toString().isEmpty() == false) {
            set_tabla.getTab_seleccion().setSql("SELECT ide_marca,marca from trans_marcas where marca like '%" + tex_busca.getValue() + "%'");
            set_tabla.getTab_seleccion().ejecutarSql();
        } else {
            utilitario.agregarMensaje("Debe ingresar un valor en el texto", "");
        }

    }

    public void aceptoSeleccionCalendario() {
        if (sec_rango.isFechasValidas()) {
            utilitario.agregarMensaje("FECHA 1 EN STRING", sec_rango.getFecha1String());
            utilitario.agregarMensaje("FECHA 1 EN DATE", sec_rango.getFecha1() + "");
            sec_rango.cerrar();

        } else {
            utilitario.agregarMensajeInfo("El rango de fachas no es válido", "");
        }
    }

    public void abrirSeleccionCalendario() {
        sec_rango.dibujar();
    }

    /**
     * se ejecuta cuando da click en el boton aceptar del componente
     * selecciontabla set_modelos
     */
    public void aceptoSeleccionTablaUnica() {
        //getValorSeleccionado() YA que es de tipo radio y solo se selecciona un registro
        if (set_modelos.getValorSeleccionado() != null && set_modelos.getValorSeleccionado().isEmpty() == false) {
            utilitario.agregarMensaje("seleccionado", set_modelos.getValorSeleccionado());
            set_modelos.cerrar();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar un registro", "");
        }
    }

    /**
     * Dibuja el componente selecciontabla set_modelos
     */
    public void abrirSeleccionTablaUnica() {
        set_modelos.dibujar();
    }

    public void aceptoSeleccionTabla() {
        if (set_tabla.getSeleccionados() != null && set_tabla.getSeleccionados().isEmpty() == false) {
            utilitario.agregarMensaje("SELECCIONADOS", set_tabla.getSeleccionados());
            set_tabla.cerrar();
        } else {
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

    public SeleccionTabla getSet_modelos() {
        return set_modelos;
    }

    public void setSet_modelos(SeleccionTabla set_modelos) {
        this.set_modelos = set_modelos;
    }

    public SeleccionCalendario getSec_rango() {
        return sec_rango;
    }

    public void setSec_rango(SeleccionCalendario sec_rango) {
        this.sec_rango = sec_rango;
    }

    public Reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(Reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public SeleccionFormatoReporte getSef_formato() {
        return sef_formato;
    }

    public void setSef_formato(SeleccionFormatoReporte sef_formato) {
        this.sef_formato = sef_formato;
    }
}
