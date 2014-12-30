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
import framework.componentes.Grupo;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.event.SelectEvent;
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
    
    //Contiene todos los elementos de la plantilla
    private Panel pan_opcion = new Panel();
    
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
        
        //Elemento principal
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader("DETALLE SE MANTENIMIENTO");
        agregarComponente(pan_opcion);
        
        //Auto busqueda para, verificar solicitud
//        aut_busca.setId("aut_busca");
//        aut_busca.setConexion(con_sql);
//        aut_busca.setAutoCompletar("SELECT c.MCA_SECUENCIAL,c.MCA_FECHAINMAN,v.MVE_PLACA,m.MVMARCA_DESCRIPCION,c.MCA_PROVEEDOR,c.MCA_TIPO_MANTENIMIENTO\n" +
//                "FROM dbo.MVCABMANTENI c \n" +
//                "INNER JOIN dbo.MVVEHICULO v ON c.MVE_SECUENCIAL = v.MVE_SECUENCIAL\n" +
//                "INNER JOIN dbo.MVMARCA m ON v.MVE_MARCA = m.MVMARCA_ID\n" +
//                "where c.MCA_ESTADO_TRAMITE = 'solicitud'");
//        aut_busca.setMetodoChange("filtrarSolicitud");
//        aut_busca.setSize(70);
//        bar_botones.agregarComponente(new Etiqueta("Buscar Solicitud Por Placa:"));
//        bar_botones.agregarComponente(aut_busca);        
        
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
        dia_dialogo.setWidth("55%"); //siempre en porcentajes  ancho
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
       dibujarDetalle();
    }

    public void dibujarDetalle(){
        tab_cabecera.setId("tab_cabecera");
        tab_cabecera.setConexion(con_sql);
        tab_cabecera.setSql("SELECT MCA_SECUENCIAL,MSC_SECUENCIAL,MVE_SECUENCIAL,MCA_FECHAINMAN,MCA_RESPONSABLE,MCA_PROVEEDOR,MCA_AUTORIZADO,MCA_DETALLE,\n" +
                "MCA_OBSERVACION,MCA_TIPO_MANTENIMIENTO\n" +
                "FROM MVCABMANTENI where MCA_ESTADO_TRAMITE = 'solicitud' order by MSC_SECUENCIAL");
//        tab_cabecera.setTabla("mvcabmanteni", "mca_secuencial", 1);
//                /*Filtro estatico para los datos a mostrar*/
////        if (aut_busca.getValue() == null) {
////            tab_cabecera.setCondicion("mca_secuencial=-1");
////        } else {
////            tab_cabecera.setCondicion("mca_secuencial=" + aut_busca.getValor());
////        }
        tab_cabecera.getColumna("MVE_SECUENCIAL").setCombo("SELECT v.MVE_SECUENCIAL,+'Placa: '+v.MVE_PLACA+' Marca: '+m.MVMARCA_DESCRIPCION+' Tipo: '+t.MVTIPO_DESCRIPCION+' Color: '+v.MVE_COLOR+' Ano: '+v.MVE_ANO\n" +
                "FROM MVVEHICULO v\n" +
                "INNER JOIN MVMARCA m ON v.MVE_MARCA = m.MVMARCA_ID\n" +
                "INNER JOIN MVTIPO t ON t.MVMARCA_ID = m.MVMARCA_ID");
//        tab_cabecera.getColumna("MCA_SECUENCIAL").setVisible(false);
//        tab_cabecera.getColumna("MCA_FECHA_BORRADO").setVisible(false);
//        tab_cabecera.getColumna("MCA_LOGINACTUALI").setVisible(false);
//        tab_cabecera.getColumna("MCA_LOGININGRESO").setVisible(false);
//        tab_cabecera.getColumna("MCA_FECHAINGRESO").setVisible(false);
//        tab_cabecera.getColumna("MCA_LOGINBORRADO").setVisible(false);
//        tab_cabecera.getColumna("MCA_LOGININGRESO ").setVisible(false);
//        tab_cabecera.getColumna("MCA_LOGINACTUALI").setVisible(false);
//        tab_cabecera.getColumna("MCA_FECHAINGRESO").setVisible(false);
//        tab_cabecera.getColumna("MCA_FECHAACTUALI").setVisible(false);
//        tab_cabecera.getColumna("MCA_LOGINBORRADO").setVisible(false);
//        tab_cabecera.getColumna("MCA_FECHASAMAN").setVisible(false);
//        tab_cabecera.getColumna("MCA_MONTO").setVisible(false);
//        tab_cabecera.getColumna("MCA_KMACTUAL").setVisible(false);
//        tab_cabecera.getColumna("MCA_KMANTERIOR").setVisible(false);
//        tab_cabecera.getColumna("MCA_TIPOSOL").setVisible(false);
//        tab_cabecera.getColumna("MCA_TIPOMEDICION").setVisible(false);
//        tab_cabecera.getColumna("MCA_ACOTACIONES").setVisible(false);
//        tab_cabecera.getColumna("MCA_ESTADO_REGISTRO").setVisible(false);
//        tab_cabecera.getColumna("MCA_FORMAPAGO").setVisible(false);
//        tab_cabecera.getColumna("MCA_ESTADO_TRAMITE").setVisible(false);
//        tab_cabecera.getColumna("MSC_SECUENCIAL").setVisible(false);
//        tab_cabecera.getColumna("MCA_FECHASOLI").setVisible(false);
//        tab_cabecera.getColumna("MCA_ESTADO_TRAMITE").setVisible(false);
        tab_cabecera.setTipoFormulario(true);
        tab_cabecera.getGrid().setColumns(4);
        tab_cabecera.agregarRelacion(tab_detalle);
        tab_cabecera.dibujar();
        PanelTabla ptc = new PanelTabla();
        ptc.setPanelTabla(tab_cabecera);
        
        tab_detalle.setId("tab_detalle");
        tab_detalle.setConexion(con_sql);
        tab_detalle.setTabla("mvdetmateni", "mde_codigo", 2);
        tab_detalle.getColumna("MAR_SECUENCIAL").setCombo("SELECT MAR_SECUENCIAL,MAR_CODIGO,MAR_DESCRIPCION,MAR_VALOR FROM dbo.MVARTICULOS order by MAR_CODIGO");
        tab_detalle.getColumna("MAR_SECUENCIAL").setAutoCompletar();
        tab_detalle.getColumna("MDE_CANTIDAD").setMetodoChange("valor");
        tab_detalle.getColumna("MDE_FECHACOMP").setValorDefecto(utilitario.getFechaActual());
        tab_detalle.getColumna("MDE_COMPROBANTE").setVisible(false);
        tab_detalle.getColumna("MDE_CANTIDAD_SOL").setVisible(false);
        tab_detalle.getColumna("MDE_TOTAL").setVisible(false);
        tab_detalle.setRows(10);
        tab_detalle.dibujar();
        PanelTabla ptd = new PanelTabla();
        ptd.setPanelTabla(tab_detalle);
        
        Division div = new Division();
        div.dividir2(ptc, ptd,"40%", "H");
        Grupo gru = new Grupo();
        gru.getChildren().add(div);
        pan_opcion.getChildren().add(gru);
    }
    
    public void filtrarSolicitud(SelectEvent evt) {
        //Filtra el cliente seleccionado en el autocompletar
        limpiar();
        aut_busca.onSelect(evt);
        dibujarDetalle();
    }
    
        //limpia y borrar el contenido de la pantalla
    private void limpiarPanel() {
        //borra el contenido de la división central central
        pan_opcion.getChildren().clear();
    }

    public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
        limpiarPanel();
        utilitario.addUpdate("pan_opcion");
    }
    
    public void valor(){
        System.err.println("Ing");
        TablaGenerica tab_dato =aCombustible.getValor(tab_detalle.getValor("MAR_SECUENCIAL"));
         if (!tab_dato.isEmpty()) {
             Double valor=0.0;
             valor = Double.valueOf(tab_detalle.getValor("MDE_CANTIDAD"))*Double.valueOf(tab_dato.getValor("MAR_VALOR"));
             tab_detalle.setValor("MDE_VALOR", String.valueOf(valor));
             utilitario.addUpdate("tab_detalle");
             total();
         }
    }
    
    public void total(){
        Double valor=0.0;
        for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
            tab_detalle.getValor(i, "MDE_VALOR");
            valor= Double.valueOf(tab_detalle.getSumaColumna("MDE_VALOR"));
            System.err.println(valor);
        }
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
        tab_detalle.getColumna("mar_secuencial").setCombo("SELECT MAR_SECUENCIAL,MAR_CODIGO,MAR_DESCRIPCION,MAR_VALOR FROM dbo.MVARTICULOS order by MAR_CODIGO");
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
        if (tab_detalle.isFocus()) {
            tab_detalle.insertar();
        }
    }

    @Override
    public void guardar() {
        if(tab_detalle.getValor("mde_codigo")!=null){
            if (tab_detalle.guardar()) {
                con_sql.guardarPantalla();
            }
        }else{
            if (tab_detalle.guardar()) {
                con_sql.guardarPantalla();
            }
        }
    }

    @Override
    public void eliminar() {
        tab_detalle.eliminar();
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
