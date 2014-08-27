/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Etiqueta;
import framework.componentes.Imagen;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_listado_rechazado extends Pantalla{

    //declaracion de conexion
    private Conexion con_postgres= new Conexion();
    
    //declaracion de tablas
    private Tabla tab_consulta =  new Tabla();
    private Tabla tab_comprobante = new Tabla();
    
    //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();//cabecera
    
    //Auto completar
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //para busqueda
    private Texto txt_buscar = new Texto();
    
    public pre_listado_rechazado() {
        
        //Mostrar el usuario 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        Imagen quinde = new Imagen();
        quinde.setValue("imagenes/logo_financiero.png");
        agregarComponente(quinde);
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        
        //Creación de Botones; Busqueda,Limpieza
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);
        
        //Auto complentar en el formulario
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT ide_listado,fecha_listado,ci_envia,responsable_envia,devolucion,estado,usuario_ingre_envia\n" +
                                   "FROM tes_comprobante_pago_listado");
        aut_busca.setSize(100);
        bar_botones.agregarComponente(new Etiqueta("Busca Listado:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        //Creación de Divisiones
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader(" LISTA PARA RECHAZADOS ");
        agregarComponente(pan_opcion);
        
        //tabla detalle
        tab_comprobante.setId("tab_comprobante");
        tab_comprobante.setConexion(con_postgres);
        tab_comprobante.setSql("SELECT  \n" +
                                " d.ide_detalle_listado,  \n" +
                                " d.ide_listado,  \n" +
                                " d.item,  \n" +
                                " d.comprobante,  \n" +
                                " d.cedula_pass_beneficiario,  \n" +
                                " d.nombre_beneficiario,  \n" +
                                " d.valor,  \n" +
                                " d.usuario_ingre_envia,  \n" +
                                " d.usuario_actua_envia,  \n" +
                                " d.ip_ingre_envia,  \n" +
                                " d.ip_actua_envia,  \n" +
                                " d.numero_cuenta,  \n" +
                                " d.ban_nombre,  \n" +
                                " d.tipo_cuenta,  \n" +
                                " d.usuario_actua_pagado,  \n" +
                                " d.ip_actua_pagado,  \n" +
                                " d.usuario_actua_devolucion,  \n" +
                                " d.ip_actua_devolucion\n" +
                                " FROM  \n" +
                                " tes_detalle_comprobante_pago_listado AS d  \n" +
                                " where ide_estado_listado = (SELECT ide_estado_listado FROM tes_estado_listado where estado like 'PAGADO')");
        tab_comprobante.getColumna("ide_detalle_listado").setVisible(false);
        tab_comprobante.getColumna("item").setVisible(false);
        tab_comprobante.getColumna("ide_listado").setVisible(false);
        tab_comprobante.getColumna("USUARIO_ACTUA_ENVIA").setVisible(false);
        tab_comprobante.getColumna("IP_ACTUA_ENVIA").setVisible(false);
        tab_comprobante.getColumna("IP_INGRE_ENVIA").setVisible(false);
        tab_comprobante.getColumna("USUARIO_ACTUA_DEVOLUCION").setVisible(false);
        tab_comprobante.getColumna("IP_ACTUA_DEVOLUCION").setVisible(false);
        tab_comprobante.getColumna("USUARIO_INGRE_ENVIA").setVisible(false);
        tab_comprobante.getColumna("USUARIO_ACTUA_PAGADO").setVisible(false);
        tab_comprobante.getColumna("IP_ACTUA_PAGADO").setVisible(false);
        
        tab_comprobante.setRows(5);
        tab_comprobante.dibujar();
        PanelTabla tdd = new PanelTabla();
        tdd.setPanelTabla(tab_comprobante);
        pan_opcion.getChildren().add(tdd);
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

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_comprobante() {
        return tab_comprobante;
    }

    public void setTab_comprobante(Tabla tab_comprobante) {
        this.tab_comprobante = tab_comprobante;
    }
    
}
