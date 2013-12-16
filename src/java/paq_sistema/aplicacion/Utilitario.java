/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_sistema.aplicacion;

import framework.aplicacion.Framework;
import framework.aplicacion.TablaGenerica;
import framework.componentes.BuscarTabla;
import framework.componentes.FormatoTabla;
import framework.componentes.Grupo;
import framework.componentes.ImportarTabla;
import framework.componentes.Notificacion;
import framework.componentes.Tabla;
import framework.componentes.TerminalTabla;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;
import org.primefaces.util.Constants;

/**
 *
 * @author Diego
 */
public class Utilitario extends Framework {

    public void agregarNotificacionInfo(String titulo, String mensaje) {
        Notificacion not_notificacion = (Notificacion) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:not_notificacion");
        if (not_notificacion != null) {
            not_notificacion.setNotificacion(titulo, mensaje, "");
            RequestContext requestContext = RequestContext.getCurrentInstance();
            addUpdate("not_notificacion");
            requestContext.execute("not_notificacion.show();");
        }
    }

    public void agregarNotificacion(String titulo, String mensaje, String pathImagen) {
        Notificacion not_notificacion = (Notificacion) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:not_notificacion");
        if (not_notificacion != null) {
            not_notificacion.setNotificacion(titulo, mensaje, pathImagen);
            addUpdate("not_notificacion");
            ejecutarJavaScript("not_notificacion.show();");
        }
    }

    public Tabla getTablaisFocus() {
        Tabla tabla = (Tabla) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:" + getVariable("TABLA_FOCO"));
        return tabla;
    }

    public Grupo getMensajes() {
        return (Grupo) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:mensajes");
    }

    public List getListaNiveles() {
        //pARA USAR EN TODAS LAS TABLAS QUE SEAN RECURSIVAS
        List lista = new ArrayList();
        Object fila1[] = {
            "HIJO", "HIJO"
        };
        Object fila2[] = {
            "PADRE", "PADRE"
        };
        lista.add(fila1);
        lista.add(fila2);
        return lista;
    }

    public void addUpdateTabla(Tabla tabla, String columnas, String componentes) {
        if (columnas != null && !columnas.isEmpty()) {
            String[] campos = columnas.split(",");

            for (int i = 0; i < campos.length; i++) {
                String str_update = tabla.getColumna(campos[i]).getId();

                if (tabla.isTipoFormulario() == false) {
                    str_update = str_update.replace("**", tabla.getFilaActual() + "");
                }
                addUpdate(str_update);
            }
        }
        if (componentes != null && !componentes.isEmpty()) {
            String[] compo = componentes.split(",");

            for (int i = 0; i < compo.length; i++) {
                String str_update = compo[i];
                addUpdate(str_update);
            }
        }
    }

    public boolean validarRUC(String str_ruc) {
        boolean boo_correcto = false;
        try {
            if (str_ruc.length() == 13) {
                int[] int_digitos = new int[10];
                int int_coeficiente = 10;
                String str_valida = str_ruc.substring(10, 13);

                if (str_valida.equals("001")) {
                    for (int i = 0; i < int_digitos.length; i++) {
                        int_digitos[i] = Integer.parseInt(str_ruc.charAt(i) + "");
                    }
                    int int_digito_verifica = int_digitos[9];
                    int[] int_multiplica = {2, 1, 2, 1, 2, 1, 2, 1, 2};
                    if (int_digitos[2] == 9) {
                        int[] int_multiplica9 = {4, 3, 2, 7, 6, 5, 4, 3, 2};
                        int_multiplica = int_multiplica9;
                        int_coeficiente = 11;
                    }
                    if (int_digitos[2] == 6) {
                        int[] aint_multiplica6 = {3, 2, 7, 6, 5, 4, 3, 2};
                        int_digito_verifica = int_digitos[8];
                        int_multiplica = aint_multiplica6;
                        int_coeficiente = 11;
                    }
                    int int_suma = 0;
                    for (int i = 0; i < (int_digitos.length - 1); i++) {
                        try {
                            if (int_coeficiente == 10) {
                                int mul = int_digitos[i] * int_multiplica[i];
                                if (mul > 9) {
                                    String aux = mul + "";
                                    mul = Integer.parseInt(aux.charAt(0) + "") + Integer.parseInt(aux.charAt(1) + "");
                                }
                                int_suma += mul;
                            } else {
                                int_suma += (int_digitos[i] * int_multiplica[i]);
                            }
                        } catch (Exception ex) {
                        }
                    }
                    int int_valida = 0;
                    if (int_coeficiente == 10) {
                        if (int_suma % 10 == 0) {
                            int_valida = 0;
                        } else {
                            int_valida = 10 - (int_suma % 10);
                        }
                    } else {
                        if (int_suma % 11 == 0) {
                            int_valida = 0;
                        } else {
                            int_valida = 11 - (int_suma % 11);
                        }
                    }

                    if (int_valida == 0) {
                        int_digito_verifica = 0;
                    }
                    if (int_valida == int_digito_verifica) {
                        boo_correcto = true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return boo_correcto;
    }

    public boolean validarCedula(String str_cedula) {
        boolean boo_correcto = false;
        try {
            if (str_cedula.length() == 10) {

                if (!str_cedula.equals("2222222222")) {
                    int lint_suma = 0;

                    for (int i = 0; i < 9; i++) {
                        int lstr_digito = Integer.parseInt(str_cedula.charAt(i) + "");
                        if (i % 2 == 0) {
                            lstr_digito = lstr_digito * 2;
                            if (lstr_digito > 9) {
                                String lstr_aux = lstr_digito + "";
                                lstr_digito = Integer.parseInt(lstr_aux.charAt(0) + "") + Integer.parseInt(lstr_aux.charAt(1) + "");
                            }
                        }
                        lint_suma += lstr_digito;
                    }
                    if (str_cedula.charAt(9) != '0') {
                        String lstr_aux = lint_suma + "";
                        int lint_superior = (Integer.parseInt(lstr_aux.charAt(0) + "") + 1) * 10;
                        int lint_ultimo_real = lint_superior - lint_suma;
                        int lint_ultimo_digito = Integer.parseInt(str_cedula.charAt(9) + "");
                        if (lint_ultimo_digito == lint_ultimo_real) {
                            boo_correcto = true;
                        }
                    } else {
                        //Para cedulas que terminan en 0
                        if (lint_suma % 10 == 0) {
                            boo_correcto = true;
                        }
                    }

                } else {
                    boo_correcto = false;
                }

            }
        } catch (Exception ex) {
        }
        return boo_correcto;
    }

    public int getDiferenciasDeFechas(Date fechaInicial, Date fechaFinal) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
        String fechaInicioString = formatoFecha.format(fechaInicial);
        try {
            fechaInicial = formatoFecha.parse(fechaInicioString);
        } catch (ParseException ex) {
        }

        String fechaFinalString = formatoFecha.format(fechaFinal);
        try {
            fechaFinal = formatoFecha.parse(fechaFinalString);
        } catch (ParseException ex) {
        }
        long fechaInicialMs = fechaInicial.getTime();
        long fechaFinalMs = fechaFinal.getTime();
        long diferencia = fechaFinalMs - fechaInicialMs;
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
        return ((int) dias);
    }

    public Date sumarDiasFecha(Date fch, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, dias);
        return new Date(cal.getTimeInMillis());
    }

    public boolean isEmailValido(String email) {
        Pattern pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
        Matcher mat = pat.matcher(email);
        if (mat.find()) {
            return true;
        } else {
            return false;
        }
    }

    public String getFormatoNumero(Object numero, int numero_decimales) {
        String lstr_formato = "#";
        for (int i = 0; i < numero_decimales; i++) {
            if (i == 0) {
                lstr_formato += ".";
            }
            lstr_formato += "#";
        }
        DecimalFormat formatoNumero;
        DecimalFormatSymbols idfs_simbolos = new DecimalFormatSymbols();
        idfs_simbolos.setDecimalSeparator('.');
        formatoNumero = new DecimalFormat(lstr_formato, idfs_simbolos);
        try {
            double ldob_valor = Double.parseDouble(numero.toString());
            return formatoNumero.format(ldob_valor);
        } catch (Exception ex) {
            return null;
        }
    }

    public Date getDate() {
        return new Date();
    }

    public void mandarMensaje() {
        PushContext pushContext = PushContextFactory.getDefault().getPushContext();
        pushContext.push("/notifications", new FacesMessage("aaaa", "eee"));
    }

    public BuscarTabla getBuscaTabla() {
        return (BuscarTabla) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:bus_buscar");
    }

    public ImportarTabla getImportarTabla() {
        return (ImportarTabla) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:imt_importar");
    }

    public FormatoTabla getFormatoTabla() {
        return (FormatoTabla) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:fot_formato");
    }

    public TerminalTabla getTerminal() {
        return (TerminalTabla) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:term_tabla");
    }

    public double evaluarExpresion(String expresion) {
        //Resuleve el valor de una expresion Ejemplo: 5+3-3
        double resultado = 0;
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object operacion;
        try {
            expresion = expresion.replace("[", "(");
            expresion = expresion.replace("]", ")");
            operacion = engine.eval(expresion);
            resultado = Double.parseDouble(operacion.toString());
        } catch (ScriptException e) {
            System.out.println("ERROR al evaluarExpresion( " + expresion + " )  :" + e.toString());
        }
        return resultado;
    }

    public void resetarPantalla() {
        RequestContext.getCurrentInstance().reset("formulario:dibuja");
    }

    public String getLetrasNumero(Object numero) {
        String letras = getFormatoNumero(numero);
        if (letras != null) {
            try {
                letras = recursivoNumeroLetras(Integer.parseInt(letras.substring(0, letras.lastIndexOf(".")))) + " CON" + recursivoNumeroLetras(Integer.parseInt(letras.substring((letras.lastIndexOf(".") + 1), letras.length())));
                letras = letras.toUpperCase();
                letras = letras.trim();
            } catch (Exception e) {
            }
        }
        return letras;
    }

    public String getLetrasDolarNumero(Object numero) {
        String letras = getFormatoNumero(numero);
        if (letras != null) {
            try {
                String centavos = (Integer.parseInt(letras.substring((letras.lastIndexOf(".") + 1), letras.length()))) + "";
                if (centavos.trim().length() == 1) {
                    centavos = "0" + centavos;
                }
                letras = recursivoNumeroLetras(Integer.parseInt(letras.substring(0, letras.lastIndexOf(".")))) + " CON " + centavos + "/100 ";
                letras = letras.toUpperCase();
                letras = letras.trim();
            } catch (Exception e) {
            }
        }
        return letras;
    }

    private String recursivoNumeroLetras(int numero) {
        String cadena = new String();
        // Aqui identifico si lleva millones
        if ((numero / 1000000) > 0) {
            if ((numero / 1000000) == 1) {
                cadena = " Un Millon" + recursivoNumeroLetras(numero % 1000000);
            } else {
                cadena = recursivoNumeroLetras(numero / 1000000) + " Millones" + recursivoNumeroLetras(numero % 1000000);
            }
        } else {
            // Aqui identifico si lleva Miles
            if ((numero / 1000) > 0) {

                if ((numero / 1000) == 1) {
                    cadena = " Mil" + recursivoNumeroLetras(numero % 1000);
                } else {
                    cadena = recursivoNumeroLetras(numero / 1000) + " Mil" + recursivoNumeroLetras(numero % 1000);
                }
            } else {
                // Aqui identifico si lleva cientos
                if ((numero / 100) > 0) {
                    if ((numero / 100) == 1) {
                        if ((numero % 100) == 0) {
                            cadena = " Cien";
                        } else {
                            cadena = " Ciento" + recursivoNumeroLetras(numero % 100);
                        }
                    } else {
                        if ((numero / 100) == 5) {
                            cadena = " Quinientos" + recursivoNumeroLetras(numero % 100);
                        } else {
                            if ((numero / 100) == 9) {
                                cadena = " Novecientos" + recursivoNumeroLetras(numero % 100);
                            } else {
                                cadena = recursivoNumeroLetras(numero / 100) + "cientos" + recursivoNumeroLetras(numero % 100);
                            }
                        }
                    }
                } // Aqui se identifican las Decenas
                else {
                    if ((numero / 10) > 0) {
                        switch (numero / 10) {
                            case 1:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Diez";
                                        break;
                                    case 1:
                                        cadena = " Once";
                                        break;
                                    case 2:
                                        cadena = " Doce";
                                        break;
                                    case 3:
                                        cadena = " Trece";
                                        break;
                                    case 4:
                                        cadena = " Catorce";
                                        break;
                                    case 5:
                                        cadena = " Quince";
                                        break;
                                    default:
                                        cadena = " Diez y " + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 2:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Veinte";
                                        break;
                                    default:
                                        cadena = " Veinti" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 3:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Treinta";
                                        break;
                                    default:
                                        cadena = " Treinta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 4:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Cuarenta";
                                        break;
                                    default:
                                        cadena = " Cuarenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 5:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Cincuenta";
                                        break;
                                    default:
                                        cadena = " Cincuenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 6:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Sesenta";
                                        break;
                                    default:
                                        cadena = " Sesenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 7:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Setenta";
                                        break;
                                    default:
                                        cadena = " Setenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 8:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Ochenta";
                                        break;
                                    default:
                                        cadena = " Ochenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 9:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Noventa";
                                        break;
                                    default:
                                        cadena = " Noventa y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                        }
                    } else {
                        switch (numero) {
                            case 1:
                                cadena = " Uno";
                                break;
                            case 2:
                                cadena = " Dos";
                                break;
                            case 3:
                                cadena = " Tres";
                                break;
                            case 4:
                                cadena = " Cuatro";
                                break;
                            case 5:
                                cadena = " Cinco";
                                break;
                            case 6:
                                cadena = " Seis";
                                break;
                            case 7:
                                cadena = " Siete";
                                break;
                            case 8:
                                cadena = " Ocho";
                                break;
                            case 9:
                                cadena = " Nueve";
                                break;
                            case 0:
                                //      cadena = " Cero";
                                break;
                        }
                    }
                }
            }
        }
        return cadena;
    }

    public void crearArchivo(String path) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        StreamedContent content;
        InputStream stream = null;
        try {
            if (path.startsWith("/")) {
                stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(path);
            } else {
                stream = new FileInputStream(path);
            }
        } catch (Exception e) {
            crearError("No se puede generar el archivo path: " + path, "crearArchivo()", e);
        }
        if (stream == null) {
            return;
        }
        content = new DefaultStreamedContent(stream);
        if (content == null) {
            return;
        }
        ExternalContext externalContext = facesContext.getExternalContext();
        String contentDispositionValue = "attachment";
        try {
            externalContext.setResponseContentType(content.getContentType());
            externalContext.setResponseHeader("Content-Disposition", contentDispositionValue + ";filename=\"" + path.substring(path.lastIndexOf("/") + 1) + "\"");
            externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", new HashMap<String, Object>());
            byte[] buffer = new byte[2048];
            int length;
            InputStream inputStream = content.getStream();
            OutputStream outputStream = externalContext.getResponseOutputStream();
            while ((length = (inputStream.read(buffer))) != -1) {
                outputStream.write(buffer, 0, length);
            }
            externalContext.setResponseStatus(200);
            externalContext.responseFlushBuffer();
            content.getStream().close();
            facesContext.getApplication().getStateManager().saveView(facesContext);
            facesContext.responseComplete();
        } catch (Exception e) {
            crearError("No se puede descargar :  path: " + path, "crearArchivo()", e);
        }
    }

    public String generarComillaSimple(String cadena) {
        String str_cadena = "";
        String[] vec = cadena.split(",");
        for (int i = 0; i < vec.length; i++) {
            if (!str_cadena.isEmpty()) {
                str_cadena += ",";
            }
            str_cadena += "'" + vec[i] + "'";
        }
        return str_cadena;
    }

    public String getCampoEmpresa(String campo) {
        String valor = null;
        TablaGenerica tab_empresa = consultar("SELECT IDE_EMPR," + campo + " FROM SIS_EMPRESA WHERE IDE_EMPR=" + getVariable("IDE_EMPR"));
        if (tab_empresa.getTotalFilas() > 0) {
            valor = tab_empresa.getValor(0, campo);
        }
        return valor;
    }

    public Object instanciarEJB(Class<?> ejb) {
        //Para cuando se necesite instanciar el EJB
        try {
            Context c = new InitialContext();
            return c.lookup("java:global/" + getNombreProyecto() + "/" + ejb.getSimpleName());
        } catch (Exception e) {
            System.out.println("FALLO AL INSTANCIAR EL EJB " + ejb.getSimpleName() + " :" + e.getMessage());
        }
        return null;
    }

    public String getNombreProyecto() {
        ExternalContext iecx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) iecx.getRequest();
        String contexto = request.getContextPath() + "";
        contexto = contexto.replace("/", "");
        contexto = contexto.trim();
        return contexto;
    }

    public String getIdSession() {
        String str_id = null;
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
            str_id = session.getId();
        } catch (Exception e) {
        }

        return str_id;
    }

    public int getEdad(String fecha) {
        Calendar fechaNacimiento = Calendar.getInstance();
        Calendar fechaActual = Calendar.getInstance();
        fechaNacimiento.setTime(getFecha(fecha));
        int anios = fechaActual.get(Calendar.YEAR) - fechaNacimiento.get(Calendar.YEAR);
        int mes = fechaActual.get(Calendar.MONTH) - fechaNacimiento.get(Calendar.MONTH);
        int dia = fechaActual.get(Calendar.DATE) - fechaNacimiento.get(Calendar.DATE);
        if (mes < 0 || (mes == 0 && dia < 0)) {
            anios--;
        }
        return anios;
    }

    public String getNombreMes(int numero) {
        String meses[] = {"", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
        return meses[numero];
    }

    /**
     * Crea un archivo comprimido de varios archivo
     *
     * @param archivos arreglo de File con el numero de archivos
     * @param nombrearchivo nombre del archivo comprimido
     */
    public void crearArchivoZIP(File[] archivos, String nombrearchivo) throws Exception {
        //http://www.devtroce.com/2010/06/25/comprimir-y-descomprir-archivos-zip-con-java/
        int BUFFER_SIZE = 1024;
        // objetos en memoria
        FileInputStream fis = null;
        ZipOutputStream zipos = null;
        if (nombrearchivo.indexOf(".zip") < 0) {
            nombrearchivo.concat(".zip");
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpServletResponse resp = (HttpServletResponse) ec.getResponse();
        resp.addHeader("Content-Disposition", "attachment; filename=\"" + nombrearchivo + "\"");

        // buffer
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            // fichero comprimido
            zipos = new ZipOutputStream(resp.getOutputStream());
            for (File pFile : archivos) {
                // fichero a comprimir
                fis = new FileInputStream(pFile);
                ZipEntry zipEntry = new ZipEntry(pFile.getName());
                zipos.putNextEntry(zipEntry);
                int len = 0;
                // zippear
                while ((len = fis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    zipos.write(buffer, 0, len);
                }
            }
            // volcar la memoria al disco
            zipos.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            // cerramos los files
            zipos.close();
            fis.close();
            fc.getApplication().getStateManager().saveView(fc);
            fc.responseComplete();
        }
    }

}
