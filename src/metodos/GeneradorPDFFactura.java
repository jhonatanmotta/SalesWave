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

    private String nombreCliente;
    private String nombreUsuario;
    private int idEmpresa;
    private int numVenta;
    private String fechaFactura;
    private String nombreArchivoPDF;
    private JTable listaProductos;
    private double subtotal;
    private double iva;
    private double total;

    public GeneradorPDFFactura() {
    }

    public GeneradorPDFFactura(String nombreCliente, String nombreUsuario, int idEmpresa, int numVenta, String fechaFactura, String nombreArchivoPDF, JTable listaProductos, double subtotal, double iva, double total) {
        this.nombreCliente = nombreCliente;
        this.nombreUsuario = nombreUsuario;
        this.idEmpresa = idEmpresa;
        this.numVenta = numVenta;
        this.fechaFactura = fechaFactura;
        this.nombreArchivoPDF = nombreArchivoPDF;
        this.listaProductos = listaProductos;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
    }

    public void generarFactura() throws FileNotFoundException, BadElementException, IOException {
        try {
            // Nombre del archivo a crear
            nombreArchivoPDF = "Factura_" + numVenta + "_" + nombreCliente + "_" + obtenerNombreArchivo(fechaFactura) + ".pdf";

            FileOutputStream archivo;
            File ruta = new File("src/facturas/" + nombreArchivoPDF);
            archivo = new FileOutputStream(ruta);
            Document archivoPDF = new Document();

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
            
            // Se cierra el archivoPDF
            archivoPDF.close();
            
            archivo.close();
            Desktop.getDesktop().open(ruta);
            // Atrapa las excepciones que se puedan generar
        } catch (DocumentException | IOException e) {
            // Impreme las excepciones en consola
            System.out.println(e);
        }
    }

    public String obtenerNombreArchivo(String fecha) {
        String fechaNueva = "";
        for (int i = 0; i < fecha.length(); i++) {
            if (fecha.charAt(i) == '/') {
                fechaNueva = fecha.replace('/', '_');
            }
        }
        return fechaNueva;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getNumVenta() {
        return numVenta;
    }

    public void setNumVenta(int numVenta) {
        this.numVenta = numVenta;
    }

    public String getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public JTable getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(JTable listaProductos) {
        this.listaProductos = listaProductos;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

}
