/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_transporte_otro;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_sistema.aplicacion.Pantalla;
import paq_transporte_otros.ejb.AbastecimientoCombustible;
import persistencia.Conexion;

/**
 *
 * @author KEJA
 */
public class pre_detalle_mantenimiento extends Pantalla{

    //Conexion a base
    private Conexion con_postgres= new Conexion();
    private Conexion con_sql = new Conexion();
    
    //dibujar tablas
    private Tabla tab_consulta = new Tabla();
    private Tabla tab_cabecera = new Tabla();
    private Tabla tab_detalle = new Tabla();
    private Tabla set_articulos = new Tabla();
    
    //DECLARACION OBJETO TEXTO
    private Texto tdescripcion = new Texto();
    private Texto tvalor = new Texto();
    private Texto tcodigo = new Texto();

    //combo de seleccion
    private Combo cmb_unidad = new Combo();
    private Combo cmb_manten = new Combo();
    private Combo cmb_tipo = new Combo();
    
    //Dialogo de Ingreso de tablas
    private Dialogo dia_dialogo = new Dialogo();
    private Grid grid_o = new Grid();
    private Grid grid = new Grid();
    
    //buscar solicitud
    private AutoCompletar aut_busca = new AutoCompletar();
    
    @EJB
    private AbastecimientoCombustible aCombustible = (AbastecimientoCombustible) utilitario.instanciarEJB(AbastecimientoCombustible.class);
    
    public pre_detalle_mantenimiento() {
        //Mostrar el usuario 
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();
        
        //cadena de conexión para base de datos en postgres/produccion2014
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";
        //cadena de conexión para base de datos en sql/manauto
        con_sql.setUnidad_persistencia(utilitario.getPropiedad("poolSqlmanAuto"));
        con_sql.NOMBRE_MARCA_BASE = "sqlserver";
        
        cmb_unidad.setId("cmb_unidad");
        List lista = new ArrayList();
        Object fila1[] = {
            "UNIDADES", "UNIDADES"
        };
        Object fila2[] = {
            "GALONES", "GALONES"
        };
        Object fila3[] = {
            "OTROS", "OTROS"
        };
        lista.add(fila1);;
        lista.add(fila2);;
        lista.add(fila3);;
        cmb_unidad.setCombo(lista); 

        cmb_manten.setId("cmb_manten");
        List lista1 = new ArrayList();
        Object fila[] = {
            "SERVICIO", "SERVICIO"
        };
        Object filau[] = {
            "FISICO", "FISICO"
        };
        Object filat[] = {
            "OTROS", "OTROS"
        };
        lista1.add(fila);;
        lista1.add(filat);;
        lista1.add(filau);;
        cmb_manten.setCombo(lista1);
        
        cmb_tipo.setId("cmb_tipo");
        List lista2 = new ArrayList();
        Object filaa[] = {
            "INTERNO", "INTERNO"
        };
        Object filab[] = {
            "EXTERNO", "EXTERNO"
        };
        Object filac[] = {
            "OTROS", "OTROS"
        };
        lista2.add(fila);;
        lista2.add(filat);;
        lista2.add(filau);;
        cmb_tipo.setCombo(lista2);
        
        Boton bot_busca = new Boton();
        bot_busca.setValue("Articulos ");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("ing_articulos");
        bar_botones.agregarBoton(bot_busca);
        //para poder busca por apelllido el garante
        Grid gri_marcas = new Grid();
        gri_marcas.setColumns(6);
        gri_marcas.getChildren().add(new Etiqueta("Codigo: "));
        gri_marcas.getChildren().add(tcodigo);
        gri_marcas.getChildren().add(new Etiqueta("Descripción: "));
        gri_marcas.getChildren().add(tdescripcion);
        gri_marcas.getChildren().add(new Etiqueta("Tipo Producto: "));
        gri_marcas.getChildren().add(cmb_manten);
        gri_marcas.getChildren().add(new Etiqueta("Unidad: "));
        gri_marcas.getChildren().add(cmb_unidad);
        gri_marcas.getChildren().add(new Etiqueta("Valor: "));
        gri_marcas.getChildren().add(tvalor);
        gri_marcas.getChildren().add(new Etiqueta("Tipo Mantenimiento: "));
        gri_marcas.getChildren().add(cmb_tipo);
        Boton bot_marcas = new Boton();
        bot_marcas.setValue("Guardar");
        bot_marcas.setIcon("ui-icon-disk");
        bot_marcas.setMetodo("insArticulos");
        bar_botones.agregarBoton(bot_marcas);
        Boton bot_marcaxs = new Boton();
        bot_marcaxs.setValue("Eliminar");
        bot_marcaxs.setIcon("ui-icon-closethick");
        bot_marcaxs.setMetodo("endArticulos");
        bar_botones.agregarBoton(bot_marcaxs);
        gri_marcas.getChildren().add(bot_marcas);
        gri_marcas.getChildren().add(bot_marcaxs);
        dia_dialogo.setId("dia_dialogo");
        dia_dialogo.setTitle("IINGRESO DE MARCA"); //titulo
        dia_dialogo.setWidth("60%"); //siempre en porcentajes  ancho
        dia_dialogo.setHeight("70%");//siempre porcentaje   alto
        dia_dialogo.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogo.getGri_cuerpo().setHeader(gri_marcas);
        dia_dialogo.getBot_aceptar().setMetodo("acepta_marca");
        grid_o.setColumns(4);
        agregarComponente(dia_dialogo);
        
        set_articulos.setId("set_articulos");
        set_articulos.setConexion(con_sql);
        set_articulos.setSql("SELECT MAR_SECUENCIAL,MAR_CODIGO,MAR_DESCRIPCION,MAR_TIPOPRODUCTO,MAR_UNIDAD,MAR_VALOR,MAR_PERTENECE FROM MVARTICULOS order by MAR_CODIGO");
        set_articulos.getColumna("MAR_DESCRIPCION").setFiltro(true);
        set_articulos.setTipoSeleccion(false);
        set_articulos.setRows(10);
        set_articulos.dibujar();
        
        tab_cabecera.setId("tab_cabecera");
        tab_cabecera.setConexion(con_sql);
        tab_cabecera.setTabla("mvcabmanteni", "mca_secuencial", 2);
        tab_cabecera.getColumna("").setVisible(true);
        tab_cabecera.setTipoFormulario(true);
        tab_cabecera.getGrid().setColumns(4);
        tab_cabecera.agregarRelacion(tab_detalle);
        tab_cabecera.dibujar();
        PanelTabla ptc = new PanelTabla();
        ptc.setPanelTabla(tab_cabecera);
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setConexion(con_sql);
        tab_detalle.setTabla("mvdetmateni", "mde_codigo", 3);
        tab_detalle.setTipoFormulario(true);
        tab_detalle.getGrid().setColumns(4);
        tab_detalle.dibujar();
        PanelTabla ptd = new PanelTabla();
        ptd.setPanelTabla(tab_detalle);
        
        Division div = new Division();
        div.dividir2(ptc, ptd,"%", "H");
        agregarComponente(div);
    }

    public void ing_articulos(){
        dia_dialogo.Limpiar();
        dia_dialogo.setDialogo(grid);
        grid_o.getChildren().add(set_articulos);
        dia_dialogo.setDialogo(grid_o);
        set_articulos.dibujar();
        dia_dialogo.dibujar();
    }
    
    public void insArticulos(){
        if(tdescripcion.getValue()!= null && tdescripcion.toString().isEmpty()==false){
            TablaGenerica tab_dato =aCombustible.get_DuplicaDat(tdescripcion.getValue()+"");
            if (!tab_dato.isEmpty()) {
                utilitario.agregarMensaje("Articulo ya se Encuentra Registrado", "");
            }else{
                Integer numero = Integer.parseInt(aCombustible.ParameMax());
                Integer cantidad=0;
                cantidad=numero +1;
                aCombustible.getParame(String.valueOf(cantidad), tcodigo.getValue()+"", tdescripcion.getValue()+"", cmb_tipo.getValue()+"", cmb_unidad.getValue()+"", 
                        Double.valueOf(tvalor.getValue()+""), cmb_manten.getValue()+"", utilitario.getVariable("NICK"));
                tdescripcion.limpiar();
                utilitario.agregarMensaje("Registro Guardado", "");
                set_articulos.actualizar();
                cargarArticulo();
            }
        }
    }
    
    public void cargarArticulo(){
        tab_detalle.getColumna("mar_secuencial").setCombo("SELECT LIS_NOMBRE,LIS_NOMBRE AS MARCA FROM MVLISTA where TAB_CODIGO = 'MARCA' and LIS_ESTADO = 1");
        utilitario.addUpdateTabla(tab_detalle,"mar_secuencial","");//actualiza solo componentes
    }
    
    public void endArticulos(){
        if (set_articulos.getValorSeleccionado() != null && set_articulos.getValorSeleccionado().isEmpty() == false) {
                aCombustible.deleteArticulo(set_articulos.getValorSeleccionado());
                utilitario.agregarMensaje("Registro eliminado", "");
                set_articulos.actualizar();
                cargarArticulo();
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar al menos un registro", "");
        }
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

    public Conexion getCon_sql() {
        return con_sql;
    }

    public void setCon_sql(Conexion con_sql) {
        this.con_sql = con_sql;
    }

    public Tabla getTab_cabecera() {
        return tab_cabecera;
    }

    public void setTab_cabecera(Tabla tab_cabecera) {
        this.tab_cabecera = tab_cabecera;
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

    public Tabla getSet_articulos() {
        return set_articulos;
    }

    public void setSet_articulos(Tabla set_articulos) {
        this.set_articulos = set_articulos;
    }
    
}
