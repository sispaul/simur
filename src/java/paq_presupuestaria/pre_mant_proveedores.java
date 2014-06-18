/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_presupuestaria;

import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Combo;
import framework.componentes.Dialogo;
import framework.componentes.Efecto;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.ItemMenu;
import framework.componentes.Panel;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Texto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import paq_presupuestaria.ejb.Programas;
import paq_sistema.aplicacion.Pantalla;
import persistencia.Conexion;


/**
 *
 * @author p-sistemas
 */
public class pre_mant_proveedores extends Pantalla{ 

    //Combo
     private Combo cmb_tipo = new Combo();
     
     //Dialogo
     private Dialogo dia_dialogoc = new Dialogo();
     private Grid grid_dc = new Grid();
     private Dialogo dia_dialogot = new Dialogo();
     private Grid grid_dt = new Grid();
     private Grid gridt = new Grid();

     //para busqueda
     private Texto txt_buscar = new Texto();

    //declaracion de conexion
    private Conexion con_postgres= new Conexion();

    //Tabla
    private Tabla tab_consulta = new Tabla();
    private Tabla tab_proveedor = new Tabla();
    private Tabla tab_empleados = new Tabla();
    private Tabla set_cuenta = new Tabla();
    
    //tablas de seleccion
    private SeleccionTabla set_proveedor = new SeleccionTabla();
    private SeleccionTabla set_empleados = new SeleccionTabla();

    //Auto completar
    private AutoCompletar aut_busca = new AutoCompletar();
    
    //dibujar cuadros de panel
    private Panel pan_opcion = new Panel();
    private Panel pan_opcion1 = new Panel();
    private Efecto efecto1 = new Efecto();  
          
    @EJB
    private Programas programas = (Programas) utilitario.instanciarEJB(Programas.class);
    
    public pre_mant_proveedores() {

        //Cadena de conexión
        con_postgres.setUnidad_persistencia(utilitario.getPropiedad("poolPostgres"));
        con_postgres.NOMBRE_MARCA_BASE = "postgres";

        //Consulta de usuario conectado
        tab_consulta.setId("tab_consulta");
        tab_consulta.setSql("select IDE_USUA, NOM_USUA, NICK_USUA from SIS_USUARIO where IDE_USUA="+utilitario.getVariable("IDE_USUA"));
        tab_consulta.setCampoPrimaria("IDE_USUA");
        tab_consulta.setLectura(true);
        tab_consulta.dibujar();

        bar_botones.quitarBotonInsertar();
	bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonGuardar();
	bar_botones.quitarBotonsNavegacion();

        //Creación de Botones; Busqueda/Limpieza
        Boton bot_busca = new Boton();
        bot_busca.setValue("Busqueda Avanzada");
        bot_busca.setExcluirLectura(true);
        bot_busca.setIcon("ui-icon-search");
        bot_busca.setMetodo("abrirBusqueda");
        bar_botones.agregarBoton(bot_busca);
        
        aut_busca.setId("aut_busca");
        aut_busca.setConexion(con_postgres);
        aut_busca.setSize(100);
        bar_botones.agregarComponente(new Etiqueta("Buscador Proveedor:"));
        bar_botones.agregarComponente(aut_busca);
        
        Boton bot_limpiar = new Boton();
        bot_limpiar.setIcon("ui-icon-cancel");
        bot_limpiar.setMetodo("limpiar");
        bar_botones.agregarBoton(bot_limpiar);
        
        cmb_tipo.setId("cmb_tipo");
        List lista2 = new ArrayList();
        Object filat[] = {
            "EMPLEADOS", "EMPLEADOS"
        };
        Object filau[] = {
            "PROVEEDORES", "PROVEEDORES"
        };
        lista2.add(filat);;
        lista2.add(filau);;
        cmb_tipo.setCombo(lista2);
        
        //DIALOGO DE CONFIRMACIÓN DE ACCIÓN -DESCUENTOS  
        dia_dialogoc.setId("dia_dialogoc");
        dia_dialogoc.setTitle("BUSCAR DATOS A CORREGIR"); //titulo
        dia_dialogoc.setWidth("37%"); //siempre en porcentajes  ancho
        dia_dialogoc.setHeight("15%");//siempre porcentaje   alto 
        dia_dialogoc.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogoc.getBot_aceptar().setMetodo("abroBusqueda");
        grid_dc.setColumns(4);
        agregarComponente(dia_dialogoc);

        set_proveedor.setId("set_proveedor");
        set_proveedor.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_proveedor.setSeleccionTabla("SELECT ide_proveedor,ruc,titular,actividad FROM tes_proveedores WHERE ide_proveedor=-1", "ide_proveedor");
        set_proveedor.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_proveedor.getTab_seleccion().setRows(12);
        set_proveedor.setRadio();
        set_proveedor.getBot_aceptar().setMetodo("aceptaProveedor");
        set_proveedor.setHeader("LISTADO DE PROVEEDORES");
        agregarComponente(set_proveedor);

        set_empleados.setId("set_empleados");
        set_empleados.getTab_seleccion().setConexion(con_postgres);//conexion para seleccion con otra base
        set_empleados.setSeleccionTabla("SELECT cod_empleado,cedula_pass,nombres,direccion,indentificacion_empleado FROM srh_empleado WHERE cod_empleado=-1", "cod_empleado");
        set_empleados.getTab_seleccion().setEmptyMessage("No se encontraron resultados");
        set_empleados.getTab_seleccion().setRows(12);
        set_empleados.setRadio();
        set_empleados.getBot_aceptar().setMetodo("aceptaEmpleado");
        set_empleados.setHeader("LISTADO DE EMPLEADOS");
        agregarComponente(set_empleados);
        
        dia_dialogot.setId("dia_dialogot");
        dia_dialogot.setTitle("TIPO DE CUENTA"); //titulo
        dia_dialogot.setWidth("22%"); //siempre en porcentajes  ancho
        dia_dialogot.setHeight("15%");//siempre porcentaje   alto
        dia_dialogot.setResizable(false); //para que no se pueda cambiar el tamaño
        dia_dialogot.getBot_aceptar().setMetodo("aceptoCuenta");
        grid_dt.setColumns(4);
        agregarComponente(dia_dialogot);
             
        set_cuenta.setId("set_cuenta");
        set_cuenta.setConexion(con_postgres);
        set_cuenta.setSql("SELECT ide_tipo_cuenta,tipo_cuenta,cuenta FROM ocebanco_tipo_cuenta");
        set_cuenta.setRows(5);
        set_cuenta.setTipoSeleccion(false);
        set_cuenta.dibujar();

        //Creación de Divisiones/Menú
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setHeader(" MANTENIMIENTO DE PROVEEDORES / EMPLEADOS (TESORERIA) ");
        efecto1.setType("drop");
	efecto1.setSpeed(150);
	efecto1.setPropiedad("mode", "'show'");
	efecto1.setEvent("load");
        pan_opcion.getChildren().add(efecto1);
        agregarComponente(pan_opcion);

    }

    public void abrirBusqueda(){
                limpiar();
                txt_buscar.limpiar();
                grid_dc.getChildren().clear();
                dia_dialogoc.Limpiar();
                grid_dc.getChildren().add(new Etiqueta("SELECIONE  :"));
                grid_dc.getChildren().add(cmb_tipo);
                grid_dc.getChildren().add(new Etiqueta("    INGRESE C.I. O RUC:"));
                grid_dc.getChildren().add(txt_buscar);
                dia_dialogoc.setDialogo(grid_dc);
                dia_dialogoc.dibujar();
    }
    
    public void abroBusqueda() {     
          if(cmb_tipo.getValue().equals("EMPLEADOS")){
                limpiarPanel();
                dia_dialogoc.cerrar();
                set_empleados.dibujar();
                set_empleados.getTab_seleccion().limpiar();
                tab_empleados.limpiar();
                aut_busca.setAutoCompletar("SELECT cod_empleado,cedula_pass,nombres,direccion FROM srh_empleado");
                aut_busca.setSize(100);
                aceptoEmpleado();
             } else if(cmb_tipo.getValue().equals("PROVEEDORES")){
                         limpiarPanel();
                         dia_dialogoc.cerrar();
                         set_proveedor.dibujar();
                         set_proveedor.getTab_seleccion().limpiar();
                         tab_proveedor.limpiar();
                         aut_busca.setAutoCompletar("SELECT ide_proveedor,ruc,titular,actividad,domicilio,telefono1 FROM tes_proveedores");
                         aut_busca.setSize(100);
                         aceptoProveedor();
                  }
    }

    public void aceptoEmpleado(){
      if (txt_buscar.getValue() != null && txt_buscar.getValue().toString().isEmpty() == false) {
             set_empleados.getTab_seleccion().setSql("SELECT cod_empleado,cedula_pass,nombres,direccion,indentificacion_empleado\n" +
                                                     "FROM srh_empleado WHERE cedula_pass LIKE '"+txt_buscar.getValue()+"' ORDER BY nombres");
             set_empleados.getTab_seleccion().ejecutarSql();
                      } else {
                            utilitario.agregarMensajeInfo("NO EXISTEN DATOS", "");
                            }
    }
    
    public void aceptoProveedor(){
      if (txt_buscar.getValue() != null && txt_buscar.getValue().toString().isEmpty() == false) {
             set_proveedor.getTab_seleccion().setSql("SELECT ide_proveedor,ruc,titular,actividad,domicilio,telefono1\n" +
                                                     "FROM tes_proveedores WHERE ruc LIKE '"+txt_buscar.getValue()+"' ORDER BY titular");
             set_proveedor.getTab_seleccion().ejecutarSql();
                      } else {
                            utilitario.agregarMensajeInfo("NO EXISTEN DATOS", "");
                            }
    }
    
  public void aceptaProveedor() {
     if (set_proveedor.getValorSeleccionado() != null) {
            aut_busca.setValor(set_proveedor.getValorSeleccionado());
            set_proveedor.cerrar();
            dibujarProveedor();
            utilitario.addUpdate("aut_busca,pan_opcion");
        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una empresa", "");
        }
}

    public void aceptaEmpleado() {
     if (set_empleados.getValorSeleccionado() != null) {
            aut_busca.setValor(set_empleados.getValorSeleccionado());
            set_empleados.cerrar();
            dibujarEmpleado();
            utilitario.addUpdate("aut_busca,pan_opcion");

        } else {
            utilitario.agregarMensajeInfo("Debe seleccionar una empresa", "");
        }
}
    
       public void dibujarProveedor(){
       if (aut_busca.getValue() != null) {
       limpiarPanel();
       tab_proveedor.setId("tab_proveedor");
       tab_proveedor.setConexion(con_postgres);
       tab_proveedor.setTabla("tes_proveedores", "ide_proveedor", 1);
               /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_proveedor.setCondicion("ide_proveedor=-1");
        } else {
            tab_proveedor.setCondicion("ide_proveedor=" + aut_busca.getValor());
        }
        tab_proveedor.getColumna("BAN_CODIGO").setCombo("SELECT ban_codigo,ban_nombre FROM ocebanco");
        tab_proveedor.getColumna("ide_tipo_proveedor").setCombo("SELECT ide_tipo_proveedor,des_tipo_proveedor FROM tes_tipo_proveedor");
        tab_proveedor.getColumna("usuario_actua").setVisible(false);
        tab_proveedor.getColumna("ip_actua").setVisible(false);
        tab_proveedor.getColumna("fecha_actua").setVisible(false);
//        tab_proveedor.getColumna("domicilio").setLectura(true);
//        tab_proveedor.getColumna("poblacion").setLectura(true);
//        tab_proveedor.getColumna("email").setLectura(true);
//        tab_proveedor.getColumna("observaciones").setLectura(true);
//        tab_proveedor.getColumna("calificacion").setLectura(true);
//        tab_proveedor.getColumna("fecha_calificacion").setLectura(true);
//        tab_proveedor.getColumna("tipo").setLectura(true);
//        tab_proveedor.getColumna("tipo_contribuyente").setLectura(true);
        tab_proveedor.setTipoFormulario(true);
        tab_proveedor.getGrid().setColumns(4);
        tab_proveedor.dibujar();
        PanelTabla tpp=new PanelTabla();
        tpp.getMenuTabla().getItem_actualizar().setRendered(false);//nucontextual().setrendered(false);
        tpp.getMenuTabla().getItem_buscar().setRendered(false);//nucontextual().setrendered(false);
        ItemMenu itm_actualizar = new ItemMenu();
        itm_actualizar.setValue("Guardar");
        itm_actualizar.setIcon("ui-icon-disk");
        itm_actualizar.setMetodo("actuaMetodo");
         
        ItemMenu itm_cuenta = new ItemMenu();
        itm_cuenta.setValue("Tipo Cuenta");
        itm_cuenta.setIcon("ui-icon-bookmark");
        itm_cuenta.setMetodo("buscarCuenta");
        
        tpp.getMenuTabla().getChildren().add(itm_actualizar);
        tpp.getMenuTabla().getChildren().add(itm_cuenta);
        tpp.setPanelTabla(tab_proveedor);
        
        pan_opcion1.setHeader("DATOS DE PROVEEDOR");
        pan_opcion1.getChildren().add(tpp);
        pan_opcion.getChildren().add(pan_opcion1);
        
       } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Persona en el autocompletar");
            limpiar();
        }
   }

       public void dibujarEmpleado(){
       if (aut_busca.getValue() != null) {
       limpiarPanel();
       tab_empleados.setId("tab_empleados");
       tab_empleados.setConexion(con_postgres);
       tab_empleados.setTabla("srh_empleado", "cod_empleado", 2);
               /*Filtro estatico para los datos a mostrar*/
        if (aut_busca.getValue() == null) {
            tab_empleados.setCondicion("cod_empleado=-1");
        } else {
            tab_empleados.setCondicion("cod_empleado=" + aut_busca.getValor());
        }
        tab_empleados.getColumna("estado").setCombo("SELECT cod_estado,nombre_estado FROM srh_estado_empleado");
        tab_empleados.getColumna("COD_GRUPO").setCombo("SELECT cod_grupo,nombre FROM srh_grupo_ocupacional");
        tab_empleados.getColumna("id_denominacion_puesto").setCombo("SELECT id_denominacion_puesto,denominacion_puesto FROM srh_denominacion_puesto");
        tab_empleados.getColumna("sexo").setCombo("SELECT cod_sexo,tipo FROM srh_sexo");
        tab_empleados.getColumna("COD_TIPO").setCombo("SELECT cod_tipo,tipo FROM srh_tipo_empleado");
        tab_empleados.getColumna("COD_CARGO").setCombo("SELECT cod_cargo,nombre_cargo FROM srh_cargos");
        tab_empleados.getColumna("id_distributivo").setCombo("SELECT id_distributivo,descripcion FROM srh_tdistributivo");
        tab_empleados.getColumna("COD_ESTADO_CIVIL").setCombo("SELECT cod_estado_civil,nombre FROM srh_estado_civil");
        tab_empleados.getColumna("COD_SANGRE").setCombo("SELECT cod_sangre,tipo FROM srh_sangre");
        tab_empleados.getColumna("COD_BANCO").setCombo("SELECT ban_codigo,ban_nombre FROM ocebanco");
        tab_empleados.getColumna("COD_CUENTA").setCombo("SELECT ide_tipo_cuenta,tipo_cuenta FROM ocebanco_tipo_cuenta");
//        tab_empleados.getColumna("COD_DIRECCION").setCombo("SELECT ide_tipo_cuenta,tipo_cuenta FROM ocebanco_tipo_cuenta");
//        tab_empleados.getColumna("COD_AREA ").setCombo("SELECT ide_tipo_cuenta,tipo_cuenta FROM ocebanco_tipo_cuenta");
        
        tab_empleados.getColumna("pathfoto").setVisible(false);
        tab_empleados.getColumna("dependencia").setVisible(false);
        tab_empleados.getColumna("num_tarjeta").setVisible(false);
        tab_empleados.getColumna("fotografia").setVisible(false);
        tab_empleados.getColumna("lugar_trab").setVisible(false);
        
        tab_empleados.getColumna("ip_responsable").setVisible(false);
        tab_empleados.getColumna("nom_responsable").setVisible(false);
        tab_empleados.getColumna("fecha_responsable").setVisible(false);
        
        tab_empleados.setTipoFormulario(true);
        tab_empleados.getGrid().setColumns(6);
        tab_empleados.dibujar();
        PanelTabla tpe=new PanelTabla();
        tpe.getMenuTabla().getItem_actualizar().setRendered(false);//nucontextual().setrendered(false);
        tpe.getMenuTabla().getItem_buscar().setRendered(false);//nucontextual().setrendered(false);
        ItemMenu itm_actualizar = new ItemMenu();
        itm_actualizar.setValue("Guardar");
        itm_actualizar.setIcon("ui-icon-disk");
        itm_actualizar.setMetodo("actuaEmple");
               
        tpe.getMenuTabla().getChildren().add(itm_actualizar);
        tpe.setPanelTabla(tab_empleados);
        
        pan_opcion1.setHeader("DATOS DE EMPLEADOS");
        pan_opcion1.getChildren().add(tpe);
        pan_opcion.getChildren().add(pan_opcion1);
        
       } else {
            utilitario.agregarMensajeInfo("No se puede abrir la opción", "Seleccione una Persona en el autocompletar");
            limpiar();
        }    
       }
 
 private void limpiarPanel() {
        //borra el contenido de la división central
        pan_opcion.getChildren().clear();
        pan_opcion1.getChildren().clear();
}
public void limpiar() {
        aut_busca.limpiar();
        utilitario.addUpdate("aut_busca");
}   
  
    public void actuaMetodo (){
        programas.actuProveedor(Integer.parseInt(tab_proveedor.getValor("ide_proveedor")), tab_proveedor.getValor("titular"), Integer.parseInt(tab_proveedor.getValor("ban_codigo")), 
                tab_proveedor.getValor("numero_cuenta"), tab_proveedor.getValor("tipo_cuenta"), tab_proveedor.getValor("actividad "), 
                tab_proveedor.getValor("telefono1"), Integer.parseInt(tab_proveedor.getValor("ide_tipo_proveedor")), tab_proveedor.getValor("telefono2"), 
                tab_proveedor.getValor("ruc"), tab_proveedor.getValor("codigo_banco"), tab_consulta.getValor("NICK_USUA")+"");
        utilitario.agregarMensaje("DATOS GUARDADOS CORRECTAMENTE", "");
    }

    public void buscarCuenta(){
        dia_dialogot.Limpiar();
        dia_dialogot.setDialogo(gridt);
        grid_dt.getChildren().add(set_cuenta);
        dia_dialogot.setDialogo(grid_dt);
        set_cuenta.dibujar();
        dia_dialogot.dibujar();
 }
    
    public void aceptoCuenta(){
     if (set_cuenta.getValorSeleccionado()!= null) {
          TablaGenerica tab_dato = programas.periodo(Integer.parseInt(set_cuenta.getValorSeleccionado()));
                if (!tab_dato.isEmpty()) {
                     tab_proveedor.setValor("tipo_cuenta", tab_dato.getValor("tipo_cuenta"));
                      utilitario.addUpdate("tab_proveedor");
                      dia_dialogot.cerrar();
                       } else {
                               utilitario.agregarMensajeInfo("No Existen Coincidencias", "");
                               }
       }else {
       utilitario.agregarMensajeInfo("No se a seleccionado ningun registro ", "");
       }
 }

    public void actuaEmple(){
        programas.actuEmpleado(Integer.parseInt(tab_empleados.getValor("cod_banco")), tab_empleados.getValor("numero_cuenta"),tab_consulta.getValor("NICK_USUA"),
                Integer.parseInt(tab_empleados.getValor("cod_empleado")),Integer.parseInt(tab_empleados.getValor("cod_cuenta")));
        utilitario.agregarMensaje("DATOS GUARDADOS CORRECTAMENTE", "");
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

    public SeleccionTabla getSet_proveedor() {
        return set_proveedor;
    }

    public void setSet_proveedor(SeleccionTabla set_proveedor) {
        this.set_proveedor = set_proveedor;
    }

    public SeleccionTabla getSet_empleados() {
        return set_empleados;
    }

    public void setSet_empleados(SeleccionTabla set_empleados) {
        this.set_empleados = set_empleados;
    }

    public AutoCompletar getAut_busca() {
        return aut_busca;
    }

    public void setAut_busca(AutoCompletar aut_busca) {
        this.aut_busca = aut_busca;
    }

    public Tabla getTab_proveedor() {
        return tab_proveedor;
    }

    public void setTab_proveedor(Tabla tab_proveedor) {
        this.tab_proveedor = tab_proveedor;
    }

    public Tabla getTab_empleados() {
        return tab_empleados;
    }

    public void setTab_empleados(Tabla tab_empleados) {
        this.tab_empleados = tab_empleados;
    }

    public Tabla getSet_cuenta() {
        return set_cuenta;
    }

    public void setSet_cuenta(Tabla set_cuenta) {
        this.set_cuenta = set_cuenta;
    }
    
}
