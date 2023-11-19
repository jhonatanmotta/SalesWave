package metodos;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JTable;

public class GeneradorPDFFactura {

    private String nombreCliente; // Almacena el nombre del cliente asociado a la factura
    private String nombreUsuario; // Almacena el nombre del usuario relacionado con la factura
    private int idEmpresa; // Almacena el ID de la empresa correspondiente a la factura
    private int numVenta; // Almacena el número de venta o factura
    private String fechaFactura; // Almacena la fecha de emisión de la factura
    private String nombreArchivoPDF; // Almacena el nombre del archivo PDF de la factura
    private JTable listaProductos; // Almacena la lista de productos de la factura
    private double subtotal; // Almacena el monto subtotal de la factura
    private double iva; // Almacena el monto del impuesto IVA aplicado a la factura
    private double total; // Almacena el monto total a pagar de la factura

    // Constructor vacio
    public GeneradorPDFFactura() {
    }

    // Constructor de la clase GeneradorPDFFactura para inicializar sus variables con los datos de la factura
    public GeneradorPDFFactura(String nombreCliente, String nombreUsuario, int idEmpresa, int numVenta, String fechaFactura, String nombreArchivoPDF, JTable listaProductos, double subtotal, double iva, double total) {
        this.nombreCliente = nombreCliente; // Nombre del cliente de la factura
        this.nombreUsuario = nombreUsuario; // Nombre del usuario asociado a la factura
        this.idEmpresa = idEmpresa; // ID de la empresa relacionada con la factura
        this.numVenta = numVenta; // Número de la venta/factura
        this.fechaFactura = fechaFactura; // Fecha de la factura
        this.nombreArchivoPDF = nombreArchivoPDF; // Nombre del archivo PDF asociado a la factura
        this.listaProductos = listaProductos; // Lista de productos de la factura (posiblemente una tabla)
        this.subtotal = subtotal; // Monto subtotal de la factura
        this.iva = iva; // Monto del impuesto IVA de la factura
        this.total = total; // Monto total de la factura
    }

    // Metodo para generar una factura
    public void generarFactura() throws FileNotFoundException, BadElementException, IOException {
        try {
            // Nombre del archivo a crear
            nombreArchivoPDF = "Factura_" + numVenta + "_" + nombreCliente + "_" + obtenerFechaArchivo(fechaFactura) + ".pdf";

            // Se instancia un objeto FileOutputStream para manejar la escritura del archivo
            FileOutputStream archivo;

            // Se crea una instancia de File con la ruta y nombre del archivo PDF
            File ruta = new File("src/facturas/" + nombreArchivoPDF);

            // Se establece el archivo de salida utilizando la ruta especificada
            archivo = new FileOutputStream(ruta);

            // Se instancia un objeto Document que representa el archivo PDF a crear
            Document archivoPDF = new Document();

            // Se asocia el archivo PDF con el FileOutputStream para permitir la escritura
            PdfWriter.getInstance(archivoPDF, archivo);

            // Abro el archivo que cree para empezar a trabajar con el
            archivoPDF.open();

            // Crear tabla para colocar el logo y el título en la misma línea
            PdfPTable tablaEncabezado = new PdfPTable(2);
            // Se estable el ancho de la tabla
            tablaEncabezado.setWidthPercentage(100);
            // Se estable el ancho de las columnas de la tabla
            float[] anchoColumnasEncabezado = new float[]{30f, 70f};
            // Se le da el ancho de las columnasa  la tabla
            tablaEncabezado.setWidths(anchoColumnasEncabezado);

            // Imagen usando la ruta
            Image logoSalesWave = Image.getInstance("src/iconos/logo75px.png");
            // Se añade la imagen a la celda
            PdfPCell celdaLogo = new PdfPCell(logoSalesWave);
            // Se quitan los bordes a la celda
            celdaLogo.setBorder(Rectangle.NO_BORDER);
            // Se agrega la celda a la tabla
            tablaEncabezado.addCell(celdaLogo);

            // Celda para el título
            PdfPCell celdaTitulo = new PdfPCell();
            // Se da estilos a la letra para el titulo
            Font estiloTitulo = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, new BaseColor(0xE8, 0x9E, 0x43));
            // Nombre el titulo y se le asigna el estilo
            Paragraph titulo = new Paragraph(("Factura de Venta"), estiloTitulo);
            // Nombre el titulo y se le asigna el estilo
            titulo.setAlignment(Element.ALIGN_CENTER);
            // Se añade el titulo a la celda
            celdaTitulo.addElement(titulo);
            // Se quitan los bordes a la celda
            celdaTitulo.setBorder(Rectangle.NO_BORDER);
            // Alineamiento de la celda
            celdaTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
            // Se agrega la celda a la tabla
            tablaEncabezado.addCell(celdaTitulo);

            // Se agrega la tabla al archivo
            archivoPDF.add(tablaEncabezado);

            // Se da estilos a la letra para los datos del archivo
            Font estiloDatos = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL, new BaseColor(0x12, 0x72, 0xB0));
            // Se crean los nombre de la fecha, cliente, empleado y se le asignan sus valores
            Paragraph datosVenta = new Paragraph(("Factura N: " + numVenta + "\n"
                    + "Fecha: " + fechaFactura + "\n"
                    + "Cliente: " + nombreCliente + "\n"
                    + "Empleado: " + nombreUsuario + "\n"), estiloDatos);
            // Se estable un espacio entre el encabezado y los datos de la venta
            datosVenta.setSpacingBefore(30f);
            // Se añade los datos de la venta al archivo
            archivoPDF.add(datosVenta);

            // Se añadel al archivo un salto de linea
            archivoPDF.add(Chunk.NEWLINE);

            // Se da estilos a la letra de la cabecera de la tabla
            Font estiloCabeceraTabla = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);
            // Crear tabla para añadir los productos de la venta
            PdfPTable tablaProductos = new PdfPTable(4);
            // Se estable el ancho de la tabla
            tablaProductos.setWidthPercentage(100);
            // Se estable el ancho de las columnas de la tabla
            float[] anchoColumnasProducto = new float[]{40f, 20f, 20f, 20f};
            // Se le da el ancho de las columnasa  la tabla
            tablaProductos.setWidths(anchoColumnasProducto);
            // Se estable un espacio entre los datos de la venta y la tabla productos
            tablaProductos.setSpacingBefore(30f);

            // Titulo de la cabecera de la tabla y asignacion del estilo
            PdfPCell columnaDescripcion = new PdfPCell(new Phrase("Descripcion", estiloCabeceraTabla));
            PdfPCell columnaCantidad = new PdfPCell(new Phrase("Cantidad", estiloCabeceraTabla));
            PdfPCell columnaPrecio = new PdfPCell(new Phrase("Precio", estiloCabeceraTabla));
            PdfPCell columnaTotal = new PdfPCell(new Phrase("Total(+Iva)", estiloCabeceraTabla));

            // Se le quita los bordes a las celdas de la cabecera de la tabla de productos
            columnaDescripcion.setBorder(Rectangle.NO_BORDER);
            columnaCantidad.setBorder(Rectangle.NO_BORDER);
            columnaPrecio.setBorder(Rectangle.NO_BORDER);
            columnaTotal.setBorder(Rectangle.NO_BORDER);

            // Se estable el color de fondo de las celdas de la tabla de productos
            columnaDescripcion.setBackgroundColor(new BaseColor(0xE8, 0x9E, 0x43));
            columnaCantidad.setBackgroundColor(new BaseColor(0xE8, 0x9E, 0x43));
            columnaPrecio.setBackgroundColor(new BaseColor(0xE8, 0x9E, 0x43));
            columnaTotal.setBackgroundColor(new BaseColor(0xE8, 0x9E, 0x43));
            // Se estable la altura de las celdas de la tabla de productos
            columnaDescripcion.setFixedHeight(25f);
            columnaCantidad.setFixedHeight(25f);
            columnaPrecio.setFixedHeight(25f);
            columnaTotal.setFixedHeight(25f);

            // Se añaden los celdas de la cabecera a la tabla productos
            tablaProductos.addCell(columnaDescripcion);
            tablaProductos.addCell(columnaCantidad);
            tablaProductos.addCell(columnaPrecio);
            tablaProductos.addCell(columnaTotal);

            // Color del border inferior del cuerpo de la tabla
            BaseColor colorBorde = new BaseColor(0xE8, 0x9E, 0x43);
            // Ciclo for que recorre el contenido de la tabla
            for (int i = 0; i < listaProductos.getRowCount(); i++) {

                // se le asignan a las variables, los valores de la tabla de venta en los puntos especificados
                String producto = listaProductos.getValueAt(i, 1).toString();
                String cantidad = listaProductos.getValueAt(i, 2).toString();
                String precio = listaProductos.getValueAt(i, 3).toString();
                String total = listaProductos.getValueAt(i, 6).toString();

                // Se crean las celdas con el contenido respectivo, usando las variables anteriores
                PdfPCell productoValor = new PdfPCell(new Phrase(producto, estiloDatos));
                PdfPCell cantidadValor = new PdfPCell(new Phrase(cantidad, estiloDatos));
                PdfPCell precioValor = new PdfPCell(new Phrase("$ " + precio, estiloDatos));
                PdfPCell totalValor = new PdfPCell(new Phrase("$ " + total, estiloDatos));

                // Se le quitan los bordes a las celdas del cuerpo de la tabla productos
                productoValor.setBorder(Rectangle.NO_BORDER);
                cantidadValor.setBorder(Rectangle.NO_BORDER);
                precioValor.setBorder(Rectangle.NO_BORDER);
                totalValor.setBorder(Rectangle.NO_BORDER);

                // Se le crea un borde inferior a las celdas del cuerpo de la tabla productos
                productoValor.setBorder(Rectangle.BOTTOM);
                cantidadValor.setBorder(Rectangle.BOTTOM);
                precioValor.setBorder(Rectangle.BOTTOM);
                totalValor.setBorder(Rectangle.BOTTOM);

                // Se le da un color al borde inferior
                productoValor.setBorderColor(colorBorde);
                cantidadValor.setBorderColor(colorBorde);
                precioValor.setBorderColor(colorBorde);
                totalValor.setBorderColor(colorBorde);

                // Se estable la altura de las celdas del cuerpo de la tabla productos
                productoValor.setFixedHeight(25f);
                cantidadValor.setFixedHeight(25f);
                precioValor.setFixedHeight(25f);
                totalValor.setFixedHeight(25f);

                // Se añaden los celdas del cuerpo de la tabla productos
                tablaProductos.addCell(productoValor);
                tablaProductos.addCell(cantidadValor);
                tablaProductos.addCell(precioValor);
                tablaProductos.addCell(totalValor);
            }
            // Se agrega la tabla productos al archivo
            archivoPDF.add(tablaProductos);

            // Crear tabla para añadir los totales de la venta
            PdfPTable tablaTotales = new PdfPTable(4);
            // Se estable el ancho de la tabla
            tablaTotales.setWidthPercentage(100);
            // Se estable el ancho de las columnas de la tabla (el mismo ancho de columnas de la tabla anterior)
            tablaTotales.setWidths(anchoColumnasProducto);
            // Se estable un espacio entre la tabla productos y la tabla de totales
            tablaTotales.setSpacingBefore(30f);
            // Se le quitan los bordes a la tabla de totales
            tablaTotales.getDefaultCell().setBorder(0);

            // Se crean las celdas de la tabla totales tanto el titulo como su respestivo valor
            PdfPCell columnaSubtotal = new PdfPCell(new Phrase("Subtotal: ", estiloDatos));
            PdfPCell columnaValorSubtotal = new PdfPCell(new Phrase("$ " + subtotal, estiloDatos));
            PdfPCell columnaImpuestos = new PdfPCell(new Phrase("Impuestos: ", estiloDatos));
            PdfPCell columnaValorImpuestos = new PdfPCell(new Phrase("$ " + iva, estiloDatos));
            PdfPCell columnaTotalGeneral = new PdfPCell(new Phrase("Total: ", estiloDatos));
            PdfPCell columnaValorTotal = new PdfPCell(new Phrase("$ " + total, estiloDatos));

            // Se eliminan los bordes a las celdas creadas anteriormente
            columnaSubtotal.setBorder(Rectangle.NO_BORDER);
            columnaValorSubtotal.setBorder(Rectangle.NO_BORDER);
            columnaImpuestos.setBorder(Rectangle.NO_BORDER);
            columnaValorImpuestos.setBorder(Rectangle.NO_BORDER);
            columnaTotalGeneral.setBorder(Rectangle.NO_BORDER);
            columnaValorTotal.setBorder(Rectangle.NO_BORDER);

            // Se añade el patron de celda vacia - vacia - contenido - contenido
            // dejando en blanco las dos primeras filas para alinear la informacion a la derecha
            tablaTotales.addCell("");
            tablaTotales.addCell("");
            tablaTotales.addCell(columnaSubtotal);
            tablaTotales.addCell(columnaValorSubtotal);
            tablaTotales.addCell("");
            tablaTotales.addCell("");
            tablaTotales.addCell(columnaImpuestos);
            tablaTotales.addCell(columnaValorImpuestos);
            tablaTotales.addCell("");
            tablaTotales.addCell("");
            tablaTotales.addCell(columnaTotalGeneral);
            tablaTotales.addCell(columnaValorTotal);

            // Se agrega la tabla de totales al archivo
            archivoPDF.add(tablaTotales);

            // Se cierra el documento PDF para finalizar su escritura y liberar recursos
            archivoPDF.close();

            // Se cierra el flujo de salida del archivo PDF
            archivo.close();

            // Se utiliza la clase Desktop para abrir el archivo PDF recién creado en la aplicación predeterminada para archivos PDF
            Desktop.getDesktop().open(ruta);
            // Atrapa las excepciones que se puedan generar
        } catch (DocumentException | IOException e) {
            // Impreme las excepciones en consola
            System.out.println(e);
        }
    }

    // Método para obtener un nombre de archivo a partir de una fecha
    public String obtenerFechaArchivo(String fecha) {
        String fechaNueva = ""; // Variable para almacenar la nueva fecha
        // Recorre la cadena de fecha
        for (int i = 0; i < fecha.length(); i++) {
            if (fecha.charAt(i) == '/') { // Si encuentra un '/', reemplaza todos los '/' con '_'
                fechaNueva = fecha.replace('/', '_'); // Reemplaza '/' por '_'
            }
        }
        return fechaNueva; // Devuelve la nueva fecha
    }

    // Devuelve el nombre del cliente
    public String getNombreCliente() {
        return nombreCliente;
    }

    // Establece el nombre del cliente
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    // Devuelve el nombre del usuario
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    // Establece el nombre del usuario
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    // Devuelve el ID de la empresa
    public int getIdEmpresa() {
        return idEmpresa;
    }

    // Establece el ID de la empresa
    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    // Devuelve el número de venta
    public int getNumVenta() {
        return numVenta;
    }

    // Establece el número de venta
    public void setNumVenta(int numVenta) {
        this.numVenta = numVenta;
    }

    // Devuelve la fecha de la factura
    public String getFechaFactura() {
        return fechaFactura;
    }

    // Establece la fecha de la factura
    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    // Devuelve la tabla de lista de productos
    public JTable getListaProductos() {
        return listaProductos;
    }

    // Establece la tabla de lista de productos
    public void setListaProductos(JTable listaProductos) {
        this.listaProductos = listaProductos;
    }

    // Devuelve el subtotal
    public double getSubtotal() {
        return subtotal;
    }

    // Establece el subtotal
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    // Devuelve el IVA
    public double getIva() {
        return iva;
    }

    // Establece el IVA
    public void setIva(double iva) {
        this.iva = iva;
    }

    // Devuelve el total
    public double getTotal() {
        return total;
    }

    // Establece el total
    public void setTotal(double total) {
        this.total = total;
    }

}
