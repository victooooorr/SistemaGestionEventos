package controll;

import modelo.eventos.Evento;
import modelo.usuarios.Cliente;
import modelo.ventas.Venta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GestorVentas {

    private static GestorVentas instancia;
    private final List<Venta> ventas = new ArrayList<>();

    private GestorVentas() {}

    public static GestorVentas getInstancia() {
        if (instancia == null) instancia = new GestorVentas();
        return instancia;
    }

    public void agregarVenta(Venta venta) {
        this.ventas.add(venta);
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    /**
     * 🔥 MÉTODO MÁGICO: Lee los archivos .txt y reconstruye las ventas antiguas.
     */
    public void cargarVentasDesdeDisco() {
        File carpeta = new File("tickets");
        if (!carpeta.exists() || carpeta.listFiles() == null) return;

        System.out.println("📂 Cargando historial de ventas desde disco...");
        
        // Evitamos duplicados limpiando la lista o comprobando IDs (aquí limpiamos para recargar limpio)
        ventas.clear(); 

        for (File archivo : carpeta.listFiles()) {
            if (archivo.getName().endsWith(".txt")) {
                try {
                    parsearTicket(archivo);
                } catch (Exception e) {
                    System.err.println("Error leyendo ticket " + archivo.getName() + ": " + e.getMessage());
                }
            }
        }
        System.out.println("✅ Historial recuperado: " + ventas.size() + " ventas cargadas.");
    }

    private void parsearTicket(File archivo) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea;
        
        String nombreCliente = "Desconocido";
        String correoCliente = "Desconocido";
        String codigoEvento = "";
        int cantidad = 0;
        double precioTotal = 0.0;

        // Leemos el archivo línea a línea buscando las palabras clave
        while ((linea = br.readLine()) != null) {
            if (linea.startsWith("Nombre: ")) nombreCliente = linea.substring(8);
            if (linea.startsWith("Correo: ")) correoCliente = linea.substring(8);
            if (linea.startsWith("Código: ")) codigoEvento = linea.substring(8);
            if (linea.startsWith("Cantidad: ")) cantidad = Integer.parseInt(linea.substring(10));
            if (linea.startsWith("Precio total: ")) {
                // Quitamos el símbolo de euro y espacios
                String precioStr = linea.substring(14).replace(" €", "").trim();
                precioTotal = Double.parseDouble(precioStr.replace(",", ".")); // Aseguramos formato decimal
            }
        }
        br.close();

        // 1. Recuperar Evento (Si el evento ya no existe, ignoramos la venta)
        Evento evento = CatalogoEventos.getInstancia().buscarEvento(codigoEvento);
        if (evento == null) return; 

        // 2. Recuperar Cliente (Si no está en memoria, creamos uno temporal para visualizar)
        // Intentamos buscarlo en el sistema
        Cliente cliente = null;
        try {
            // Buscamos si el usuario ya está registrado en el sistema
            cliente = (Cliente) GestorUsuarios.getInstancia().login(correoCliente, ""); // Truco sucio, mejor buscar directo
        } catch (Exception e) {
            // Si no existe (porque reiniciamos), creamos un cliente "fantasma" solo para mostrar datos
            cliente = new Cliente("00000000X", nombreCliente, correoCliente, "temp", "000000000", LocalDate.now());
        }

        // 3. Reconstruir Venta
        Venta ventaRecuperada = new Venta(UUID.randomUUID().toString(), cliente, evento, cantidad, precioTotal);
        this.ventas.add(ventaRecuperada);
    }
}
