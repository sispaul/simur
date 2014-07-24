/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Tabulador;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import paq_nomina.ejb.SolicAnticipos;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class pre_solicitud_anticipo extends Pantalla{

    ///REPORTES
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();
    
    //Conexion a base
    private Conexion con_postgres= new Conexion();
    
    //tablas
    private Tabla tab_anticipo = new Tabla();
    private Tabla tab_garante = new Tabla();
    private Tabla tab_parametros = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla tab_consulta = new Tabla();
    
    //PARA ASIGNACION DE MES
    String selec_mes = new String();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    @EJB
    private SolicAnticipos iAnticipos = (SolicAnticipos) utilitario.instanciarEJB(SolicAnticipos.class);
    
    public pre_solicitud_anticipo() {
         
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);
        
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setAutoCompletar("SELECT  \n" +
                                    "ide_anticipo,  \n" +
                                    "ide_empleado_solicitante,  \n" +
                                    "ci_solicitante,  \n" +
                                    "solicitante,  \n" +
                                    "ide_estado_anticipo  \n" +
                                    "FROM srh_anticipo  \n" +
                                    "where ide_estado_anticipo = (SELECT ide_estado_tipo  \n" +
                                    "FROM srh_estado_anticipo   where estado LIKE 'INGRESADO')OR\n" +
                                    "ide_estado_anticipo = (SELECT ide_estado_tipo  \n" +
                                    "FROM srh_estado_anticipo   where estado LIKE 'AUTORIZADO')OR\n" +
                                    "ide_estado_anticipo = (SELECT ide_estado_tipo  \n" +
                                    "FROM srh_estado_anticipo   where estado LIKE 'COBRADO')\n" +
                                    "order by fecha_anticipo");
        aut_busca.setMetodoChange("buscarPersona");
        aut_busca.setSize(100);
        
        bar_botones.agregarComponente(new Etiqueta("Buscar Personas:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular Soliciud");
        bot_anular.setIcon("ui-icon-close");
        bot_anular.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_anular);
        
        tab_anticipo.setId("tab_anticipo");
        tab_anticipo.setConexion(con_postgres);
        tab_anticipo.setTabla("srh_solicitud_anticipo", "ide_solicitud_anticipo", 1);
     
        tab_anticipo.getColumna("id_distributivo").setCombo("SELECT id_distributivo, descripcion FROM srh_tdistributivo");
        tab_anticipo.getColumna("ide_estado_anticipo").setCombo("SELECT ide_estado_tipo,estado FROM srh_estado_anticipo");
        tab_anticipo.getColumna("cod_banco").setCombo("SELECT ban_codigo,ban_nombre FROM ocebanco");
        tab_anticipo.getColumna("cod_cuenta").setCombo("SELECT cod_cuenta,nombre FROM ocecuentas");
        tab_anticipo.getColumna("cod_cargo").setCombo("SELECT cod_cargo,nombre_cargo FROM srh_cargos");
        tab_anticipo.getColumna("cod_tipo").setCombo("SELECT cod_tipo,tipo FROM srh_tipo_empleado");
        tab_anticipo.getColumna("cod_grupo").setCombo("SELECT cod_grupo,nombre FROM srh_grupo_ocupacional");
        tab_anticipo.setTipoFormulario(true);
        tab_anticipo.agregarRelacion(tab_garante);
        tab_anticipo.getGrid().setColumns(4);
        tab_anticipo.dibujar();
        PanelTabla tpa = new PanelTabla();
        tpa.setPanelTabla(tab_anticipo);
        
        tab_garante.setId("tab_garante");
        tab_garante.setIdCompleto("tab_tabulador:tab_garante");
        tab_garante.setConexion(con_postgres);
        tab_garante.setTabla("srh_garante_anticipo", "ide_garante_anticipo", 2);
        tab_garante.getColumna("ci_garante").setMetodoChange("llenarGarante");
        tab_garante.setTipoFormulario(true);
        tab_garante.getGrid().setColumns(4);
        tab_garante.dibujar();
        PanelTabla tpd = new PanelTabla();
        tpd.setPanelTabla(tab_garante);
        
        Tabulador tab_tabulador = new Tabulador();
        tab_tabulador.setId("tab_tabulador");
        tab_tabulador.agregarTab("DATOS GARANTE", tpd);
//        tab_tabulador.agregarTab("DATOS PARA ANTICIPO", pat_panel3);
            
        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(tpa, tab_tabulador, "40%", "H");
        agregarComponente(div_division);
        
    }
    
        //LLENAR DATOS DE SOLICTANTE Y GARANTE     /*POR IDENTIFICACION*/
    public void llenarDatosE(){//SOLICITANTE
       TablaGenerica tab_dato = iAnticipos.Datos(tab_consulta.getValor("NICK_USUA"));
       if (!tab_dato.isEmpty()) {
                    TablaGenerica tab_dato1 = iAnticipos.empleados(tab_dato.getValor("NOM_USUA")+"");//empleados
                    if (!tab_dato1.isEmpty()) {
                        tab_anticipo.setValor("ide_empleado_solicitante", tab_dato1.getValor("COD_EMPLEADO"));
                        tab_anticipo.setValor("ci_solicitante", tab_dato1.getValor("cedula_pass"));
                        tab_anticipo.setValor("solicitante", tab_dato1.getValor("nombres"));
                        tab_anticipo.setValor("rmu", tab_dato1.getValor("ru"));
                        tab_anticipo.setValor("rmu_liquido_anterior", tab_dato1.getValor("liquido_recibir"));
                        tab_anticipo.setValor("id_distributivo", tab_dato1.getValor("id_distributivo_roles"));
                        tab_anticipo.setValor("cod_cargo", tab_dato1.getValor("cod_cargo"));
                        tab_anticipo.setValor("cod_grupo", tab_dato1.getValor("cod_grupo"));
                        tab_anticipo.setValor("cod_tipo", tab_dato1.getValor("cod_tipo"));
                        tab_anticipo.setValor("cod_banco", tab_dato1.getValor("cod_banco"));
                        tab_anticipo.setValor("cod_cuenta", tab_dato1.getValor("cod_cuenta"));
                        tab_anticipo.setValor("numero_cuenta", tab_dato1.getValor("numero_cuenta"));
                        tab_anticipo.setValor("ide_estado_anticipo", "1");
                        utilitario.addUpdate("tab_anticipo");
                    }else {
                       TablaGenerica tab_dato2 = iAnticipos.trabajadores(tab_dato.getValor("NOM_USUA")+"");//trabajadores
                            if (!tab_dato2.isEmpty()) {
                                tab_anticipo.setValor("ide_empleado_solicitante", tab_dato2.getValor("COD_EMPLEADO"));
                                tab_anticipo.setValor("ci_solicitante", tab_dato2.getValor("cedula_pass"));
                                tab_anticipo.setValor("solicitante", tab_dato2.getValor("nombres"));
                                tab_anticipo.setValor("rmu", tab_dato2.getValor("su"));
                                tab_anticipo.setValor("rmu_liquido_anterior", tab_dato2.getValor("liquido_recibir"));
                                tab_anticipo.setValor("id_distributivo", tab_dato2.getValor("id_distributivo_roles"));
                                tab_anticipo.setValor("cod_cargo", tab_dato2.getValor("cod_cargo"));
                                tab_anticipo.setValor("cod_grupo", tab_dato2.getValor("cod_grupo"));
                                tab_anticipo.setValor("cod_tipo", tab_dato2.getValor("cod_tipo"));
                                tab_anticipo.setValor("cod_banco", tab_dato2.getValor("cod_banco"));
                                tab_anticipo.setValor("cod_cuenta", tab_dato2.getValor("cod_cuenta"));
                                tab_anticipo.setValor("numero_cuenta", tab_dato2.getValor("numero_cuenta"));
                                tab_anticipo.setValor("ide_estado_anticipo", "1");
                                utilitario.addUpdate("tab_anticipo");
                            }else {
                                utilitario.agregarMensajeInfo("No existen Datos", "");
                                }
                      }
                           } else {
                        utilitario.agregarMensajeError("El Número de Cédula no es válido", "");
                    }  
                              tab_garante.insertar(); 
    }
    

    public void llenarGarante(){//GARANTE
        TablaGenerica tab_dato = iAnticipos.VerifGaranteid(tab_garante.getValor("ci_garante"));
       if (!tab_dato.isEmpty()) {
            utilitario.agregarMensajeInfo("Garante No Disponible", "");
       }else {
                   if (utilitario.validarCedula(tab_garante.getValor("ci_garante"))) {
                        TablaGenerica tab_dato1 = iAnticipos.Garantemple(tab_garante.getValor("ci_garante"));
                           if (!tab_dato1.isEmpty()) {
                                   tab_garante.setValor("garante", tab_dato1.getValor("nombres"));
                                   tab_garante.setValor("ide_empleado_garante", tab_dato1.getValor("COD_EMPLEADO"));
                                   tab_garante.setValor("cod_tipo", tab_dato1.getValor("COD_EMPLEADO"));
                                   tab_garante.setValor("id_distributivo", tab_dato1.getValor("COD_EMPLEADO"));
                                   utilitario.addUpdate("tab_garante");
                                }else {
                                      utilitario.agregarMensajeInfo("Garante No Disponible", "");
                                      }    
                    } else {
                            utilitario.agregarMensajeError("El Número de Cédula no es válido", "");
                            }
              }
    }
    
    @Override
    public void insertar() {
        tab_anticipo.insertar();
                llenarDatosE();
    }

    @Override
    public void guardar() {
        
    }

    @Override
    public void eliminar() {
        
    }

    public Tabla getTab_anticipo() {
        return tab_anticipo;
    }

    public void setTab_anticipo(Tabla tab_anticipo) {
        this.tab_anticipo = tab_anticipo;
    }

    public Tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(Tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public Tabla getTab_garante() {
        return tab_garante;
    }

    public void setTab_garante(Tabla tab_garante) {
        this.tab_garante = tab_garante;
    }

    public Conexion getCon_postgres() {
        return con_postgres;
    }

    public void setCon_postgres(Conexion con_postgres) {
        this.con_postgres = con_postgres;
    }
    
}
