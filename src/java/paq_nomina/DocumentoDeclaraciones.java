/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Reporte;
import framework.componentes.SeleccionFormatoReporte;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.event.SelectEvent;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;

/**
 *
 * @author p-sistemas
 */
public class DocumentoDeclaraciones extends Pantalla {

    private Conexion conPostgres = new Conexion();
    private AutoCompletar autBusca = new AutoCompletar();
    private Tabla tabEmpleado = new Tabla();
    private Tabla tabTabla = new Tabla();
    private Tabla tabConsulta = new Tabla();
    private Panel panOpcion = new Panel();
    private Dialogo dialogoDE = new Dialogo();
    private Grid gridDe = new Grid();
    private Combo cmbDescripcion = new Combo();
    private Combo cmbPendientes = new Combo();
    private Reporte rep_reporte = new Reporte(); //siempre se debe llamar rep_reporte
    private SeleccionFormatoReporte sef_formato = new SeleccionFormatoReporte();
    private Map p_parametros = new HashMap();

    public DocumentoDeclaraciones() {
        //Para capturar el usuario que se encuntra utilizando la opción
        tabConsulta.setId("tabConsulta");
        tabConsulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA=" + utilitario.getVariable("IDE_USUA"));
        tabConsulta.setCampoPrimaria("IDE_USUA");
        tabConsulta.setLectura(true);
        tabConsulta.dibujar();

        //Elemento principal
        panOpcion.setId("panOpcion");
        panOpcion.setTransient(true);
        panOpcion.setHeader("DECLARACIONES  JURAMENTADAS");
        agregarComponente(panOpcion);

        //cadena de conexión para otra base de datos
        conPostgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        conPostgres.NOMBRE_MARCA_BASE = "postgres";

        autBusca.setId("autBusca");
        autBusca.setConexion(conPostgres);
        autBusca.setAutoCompletar("select cod_empleado,cedula_pass,nombres from srh_empleado order by nombres");
        autBusca.setMetodoChange("buscaEmpleado");
        autBusca.setSize(80);

        bar_botones.agregarComponente(new Etiqueta("Buscar Empleado:"));
        bar_botones.agregarComponente(autBusca);

        Boton botLimpiar = new Boton();
        botLimpiar.setIcon("ui-icon-cancel");
        botLimpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(botLimpiar);

        tabEmpleado.setId("tabEmpleado");
        tabEmpleado.setConexion(conPostgres);
        tabEmpleado.setTabla("srh_empleado", "cod_empleado", 0);
        tabEmpleado.getColumna("sexo ").setVisible(false);
        tabEmpleado.getColumna(" fecha_ingreso ").setVisible(false);
        tabEmpleado.getColumna(" fecha_nacimiento ").setVisible(false);
        tabEmpleado.getColumna(" fotografia ").setVisible(false);
        tabEmpleado.getColumna(" fecha_pasivo ").setVisible(false);
        tabEmpleado.getColumna(" cod_grupo ").setVisible(false);
        tabEmpleado.getColumna(" cod_tipo ").setVisible(false);
        tabEmpleado.getColumna(" cod_sangre ").setVisible(false);
        tabEmpleado.getColumna(" cod_estado_civil ").setVisible(false);
        tabEmpleado.getColumna(" direccion ").setVisible(false);
        tabEmpleado.getColumna(" lugar_de_nacimiento ").setVisible(false);
        tabEmpleado.getColumna(" fono_convencional ").setVisible(false);
        tabEmpleado.getColumna(" fono_celular ").setVisible(false);
        tabEmpleado.getColumna(" e_mail ").setVisible(false);
        tabEmpleado.getColumna(" fono_convencional2 ").setVisible(false);
        tabEmpleado.getColumna(" fono_celular2 ").setVisible(false);
        tabEmpleado.getColumna(" indentificacion_empleado ").setVisible(false);
        tabEmpleado.getColumna(" estado ").setVisible(false);
        tabEmpleado.getColumna(" pathfoto ").setVisible(false);
        tabEmpleado.getColumna(" num_cedula_militar ").setVisible(false);
        tabEmpleado.getColumna(" num_afiliado_iess ").setVisible(false);
        tabEmpleado.getColumna(" fecha_nombramiento ").setVisible(false);
        tabEmpleado.getColumna(" cod_banco ").setVisible(false);
        tabEmpleado.getColumna(" dir_calles ").setVisible(false);
        tabEmpleado.getColumna(" dir_sector ").setVisible(false);
        tabEmpleado.getColumna(" dir_ciudad ").setVisible(false);
        tabEmpleado.getColumna(" dir_barrio ").setVisible(false);
        tabEmpleado.getColumna(" relacion_laboral ").setVisible(false);
        tabEmpleado.getColumna(" cod_direccion ").setVisible(false);
        tabEmpleado.getColumna(" cod_jefatura ").setVisible(false);
        tabEmpleado.getColumna(" cod_area ").setVisible(false);
        tabEmpleado.getColumna(" cod_cuenta ").setVisible(false);
        tabEmpleado.getColumna(" numero_cuenta ").setVisible(false);
        tabEmpleado.getColumna(" sindicalizacion ").setVisible(false);
        tabEmpleado.getColumna(" grado ").setVisible(false);
        tabEmpleado.getColumna(" num_tarjeta ").setVisible(false);
        tabEmpleado.getColumna(" dependencia ").setVisible(false);
        tabEmpleado.getColumna(" lugar_trab ").setVisible(false);
        tabEmpleado.getColumna(" partida_pres ").setVisible(false);
        tabEmpleado.getColumna(" agremiado ").setVisible(false);
        tabEmpleado.getColumna(" remuneracion ").setVisible(false);
        tabEmpleado.getColumna(" ide_cuenta ").setVisible(false);
        tabEmpleado.getColumna(" ide_clasificador ").setVisible(false);
        tabEmpleado.getColumna(" ip_responsable ").setVisible(false);
        tabEmpleado.getColumna(" nom_responsable ").setVisible(false);
        tabEmpleado.getColumna(" fecha_responsable ").setVisible(false);
        tabEmpleado.getColumna(" sueldo_basico ").setVisible(false);
        tabEmpleado.getColumna(" ide_programa ").setVisible(false);
        tabEmpleado.getColumna(" dir_actual ").setVisible(false);
        tabEmpleado.getColumna(" id_denominacion_puesto ").setVisible(false);
        tabEmpleado.getColumna(" partida_indv ").setVisible(false);
        tabEmpleado.getColumna(" cod_biometrico ").setVisible(false);
        tabEmpleado.getColumna(" firma ").setVisible(false);
        tabEmpleado.getColumna(" fecha_decimos").setVisible(false);

        tabEmpleado.getColumna("id_distributivo").setCombo("SELECT id_distributivo, descripcion FROM srh_tdistributivo");
        tabEmpleado.getColumna("cod_cargo").setCombo("SELECT cod_cargo,nombre_cargo FROM srh_cargos");
        tabEmpleado.setTipoFormulario(true);
        tabEmpleado.agregarRelacion(tabTabla);
        tabEmpleado.dibujar();
        PanelTabla patEmp = new PanelTabla();
        patEmp.setPanelTabla(tabEmpleado);

        tabTabla.setId("tabTabla");
        tabTabla.setConexion(conPostgres);
        tabTabla.setTabla("srh_documentos_declaraciones", "doc_codigo", 1);
        tabTabla.getColumna("doc_codigo").setVisible(false);
        List lista = new ArrayList();
        Object fil1[] = {
            "Inicial", "Inicial"
        };
        Object fil2[] = {
            "Final", "Final"
        };
        Object fil3[] = {
            "Intermedio", "Intermedio"
        };
        lista.add(fil1);
        lista.add(fil2);
        lista.add(fil3);
        tabTabla.getColumna("doc_tipo_declaracion").setCombo(lista);
        tabTabla.getColumna("doc_documento").setUpload("logos");
        tabTabla.dibujar();
        PanelTabla patPanel = new PanelTabla();
        patPanel.setPanelTabla(tabTabla);
        Division div = new Division();
        div.setId("div");
        div.dividir2(patEmp, patPanel, "%30", "h");
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);

        dialogoDE.setId("dialogoDE");
        dialogoDE.setTitle("PARAMETROS DE REPORTE"); //titulo
        dialogoDE.setWidth("35%"); //siempre en porcentajes  ancho
        dialogoDE.setHeight("20%");//siempre porcentaje   alto 
        dialogoDE.setResizable(false); //para que no se pueda cambiar el tamaño
        dialogoDE.getBot_aceptar().setMetodo("aceptoDescuentos");
        gridDe.setColumns(4);
        agregarComponente(dialogoDE);

        bar_botones.agregarReporte(); //1 para aparesca el boton de reportes 
        agregarComponente(rep_reporte); //2 agregar el listado de reportes
        sef_formato.setId("sef_formato");
        sef_formato.setConexion(conPostgres);
        agregarComponente(sef_formato);
    }

    public void buscaEmpleado(SelectEvent evt) {
        autBusca.limpiar();
        autBusca.onSelect(evt);
        utilitario.addUpdate("autBusca");
        tabEmpleado.setFilaActual(autBusca.getValor());
        utilitario.addUpdate("tabEmpleado");
    }

    public void limpiar() {
        autBusca.limpiar();
        utilitario.addUpdate("autBusca");
    }

    @Override
    public void insertar() {
        utilitario.getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tabTabla.guardar()) {
            conPostgres.guardarPantalla();
        }
    }

    @Override
    public void eliminar() {
        utilitario.getTablaisFocus().eliminar();
    }

    @Override
    public void abrirListaReportes() {
        rep_reporte.dibujar();

    }

    //llamado para seleccionar el reporte
    @Override
    public void aceptarReporte() {
        rep_reporte.cerrar();
        switch (rep_reporte.getNombre()) {
            case "DECLARACIONES JURAMENTADAS":
                dialogoDE.Limpiar();
                Grid griComp = new Grid();
                griComp.setColumns(2);
                griComp.getChildren().add(new Etiqueta("DISTRIBUTIVO: "));
                cmbDescripcion.setId("cmbDescripcion");
                cmbDescripcion.setConexion(conPostgres);
                cmbDescripcion.setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo ORDER BY id_distributivo");
                griComp.getChildren().add(cmbDescripcion);
                griComp.getChildren().add(new Etiqueta("Parametro: "));
                List lista = new ArrayList();
                Object fila1[] = {
                    "1", "Entregados"
                };
                Object fila2[] = {
                    "2", "Pendientes"
                };
                lista.add(fila1);
                lista.add(fila2);
                cmbPendientes.setCombo(lista);
                griComp.getChildren().add(cmbPendientes);
                gridDe.getChildren().add(griComp);
                dialogoDE.setDialogo(gridDe);
                dialogoDE.dibujar();
                break;
        }
    }

    // dibujo de reporte y envio de parametros
    public void aceptoDescuentos() {
        switch (rep_reporte.getNombre()) {
            case "DECLARACIONES JURAMENTADAS":
                String cadena = "";
                if (cmbPendientes.getValue().equals("1")) {
                    cadena = "not null";
                } else {
                    cadena = "null";
                }
                p_parametros.put("cadena", cadena + "");
                p_parametros.put("distributivo", Integer.parseInt(cmbDescripcion.getValue() + ""));
                p_parametros.put("nom_resp", tabConsulta.getValor("NICK_USUA") + "");
                rep_reporte.cerrar();
                sef_formato.setSeleccionFormatoReporte(p_parametros, rep_reporte.getPath());
                sef_formato.dibujar();
                break;
        }
    }

    public Conexion getConPostgres() {
        return conPostgres;
    }

    public void setConPostgres(Conexion conPostgres) {
        this.conPostgres = conPostgres;
    }

    public Tabla getTabTabla() {
        return tabTabla;
    }

    public void setTabTabla(Tabla tabTabla) {
        this.tabTabla = tabTabla;
    }

    public Tabla getTabEmpleado() {
        return tabEmpleado;
    }

    public void setTabEmpleado(Tabla tabEmpleado) {
        this.tabEmpleado = tabEmpleado;
    }

    public AutoCompletar getAutBusca() {
        return autBusca;
    }

    public void setAutBusca(AutoCompletar autBusca) {
        this.autBusca = autBusca;
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

    public Map getP_parametros() {
        return p_parametros;
    }

    public void setP_parametros(Map p_parametros) {
        this.p_parametros = p_parametros;
    }
}
