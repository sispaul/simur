/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_nomina;

import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
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
        tabTabla.getColumna("doc_documento").setUpload("logos");
        tabTabla.getColumna("doc_documento").setImagen("", "");
        tabTabla.dibujar();
        PanelTabla patPanel = new PanelTabla();
        patPanel.setPanelTabla(tabTabla);
        Division div = new Division();
        div.setId("div");
        div.dividir2(patEmp, patPanel, "%30", "h");
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        panOpcion.getChildren().add(gru);
    }

    public void buscaEmpleado(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
//        limpia();
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
        tabTabla.insertar();
    }

    @Override
    public void guardar() {
        tabTabla.guardar();
        conPostgres.guardarPantalla();
    }

    @Override
    public void eliminar() {
        tabTabla.eliminar();
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
}
