/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_varios;

import framework.componentes.Division;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_acceso_sistemas extends Pantalla {

    //Conexion a base
    private Conexion con_postgres = new Conexion();
    //Tablas
    private Tabla tab_sistemas = new Tabla();
    private Tabla tab_modulos = new Tabla();
    private Tabla tab_perfiles = new Tabla();
    private Tabla tab_consulta = new Tabla();

    public pre_acceso_sistemas() {

        //Para capturar el usuario que se encuntra utilizando la opción
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();

        //cadena de conexión para otra base de datos
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        tab_sistemas.setId("tab_sistemas");
        tab_sistemas.setConexion(con_postgres);
        tab_sistemas.setTabla("sca_sistemas", "id_sistema", 1);
        tab_sistemas.agregarRelacion(tab_modulos);
        tab_sistemas.dibujar();
        PanelTabla tps = new PanelTabla();
        tps.setMensajeWarn("SISTEMAS DISPONIBLES");
        tps.setPanelTabla(tab_sistemas);

        tab_modulos.setId("tab_modulos");
        tab_modulos.setConexion(con_postgres);
        tab_modulos.setTabla("sca_modulos", "id_modulo", 2);
        tab_modulos.agregarRelacion(tab_perfiles);
        tab_modulos.dibujar();
        PanelTabla tpm = new PanelTabla();
        tpm.setMensajeWarn("MODULOS DEL SISTEMA");
        tpm.setPanelTabla(tab_modulos);

        tab_perfiles.setId("tab_perfiles");
        tab_perfiles.setConexion(con_postgres);
        tab_perfiles.setTabla("sca_perfiles", "id_perfil", 3);
        tab_perfiles.dibujar();
        PanelTabla tpp = new PanelTabla();
        tpp.setMensajeWarn("PERFILES DE LOS MODULOS DEL SISTEMA");
        tpp.setPanelTabla(tab_perfiles);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir3(tps, tpm, tpp, "30%", "35%", "H");
        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_sistemas.guardar()) {
            if (tab_modulos.guardar()) {
                if (tab_perfiles.guardar()) {
                    con_postgres.guardarPantalla();
                }
            }
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }

    public Tabla getTab_sistemas() {
        return tab_sistemas;
    }

    public void setTab_sistemas(Tabla tab_sistemas) {
        this.tab_sistemas = tab_sistemas;
    }

    public Tabla getTab_modulos() {
        return tab_modulos;
    }

    public void setTab_modulos(Tabla tab_modulos) {
        this.tab_modulos = tab_modulos;
    }

    public Tabla getTab_perfiles() {
        return tab_perfiles;
    }

    public void setTab_perfiles(Tabla tab_perfiles) {
        this.tab_perfiles = tab_perfiles;
    }
}
