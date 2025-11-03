📚 Biblioteca Mágica Alrededor del Mundo  
**Proyecto del curso Laboratorio de Estructura de Datos**  
**Universidad de San Carlos de Guatemala – Centro Universitario de Occidente (CUNOC)**  
**Facultad de Ingeniería – Ingeniería en Ciencias y Sistemas**  
**Estudiante:** MadeLayne Ana María Pérez Pérez  
**Carnet:** 202130171  
**Año:** 2025  

---

## 🌟 Descripción del Proyecto

**Biblioteca Mágica Alrededor del Mundo** es una aplicación desarrollada en **Java** que simula una red interconectada de bibliotecas encantadas.  
Cada biblioteca administra su propio catálogo de libros, pudiendo realizar operaciones de búsqueda, transferencia y despacho de ejemplares a través de conexiones mágicas simuladas mediante **grafos ponderados**.

El proyecto fue implementado completamente **desde cero**, aplicando estructuras de datos avanzadas como **Árbol AVL**, **Árbol B**, **Árbol B+**, **Tabla Hash**, **Colas**, **Pilas**, **Listas** y **Grafos**, integradas con una interfaz gráfica **Java Swing** y visualización dinámica mediante **Graphviz**.

---

## 🧩 Funcionalidades Principales

- 📖 **Gestión de libros:** agregar, buscar, eliminar y listar.
- 🏛️ **Administración de bibliotecas:** creación, modificación y conexión entre sedes.
- 🔍 **Búsquedas eficientes:** por título (AVL), ISBN (Hash), género (B+), año (B).
- 🔄 **Transferencias entre bibliotecas:** cálculo de rutas óptimas según tiempo o costo.
- 🧠 **Visualización de estructuras:** árboles, colas, grafos y tablas hash generadas con Graphviz.
- ⚙️ **Simulación de procesos mágicos:** control de ingreso, traspaso y despacho de libros.
- ⏪ **Rollback y devoluciones:** gestión mediante pilas LIFO.
- 📂 **Carga masiva:** lectura y validación de archivos CSV.

---

## 🧱 Tecnologías Utilizadas

| Herramienta | Descripción |
|--------------|-------------|
| **Java SE 21** | Lenguaje de programación principal |
| **NetBeans 19** | IDE utilizado para el desarrollo |
| **Graphviz 14.0.1** | Generación de gráficos de estructuras (.dot → .png) |
| **CSV UTF-8** | Formato de entrada de datos |
| **Swing / JFrame** | Interfaz gráfica del usuario |
| **Windows 10/11** | Sistema operativo de desarrollo y ejecución |

---

## 🧩 Estructuras de Datos Implementadas

| Estructura | Uso Principal | Complejidad |
|-------------|----------------|-------------|
| **Lista Enlazada** | Colección de libros por biblioteca | O(n) |
| **Pila** | Control de devoluciones / rollback | O(1) |
| **Cola** | Simulación de envío y recepción | O(1) |
| **Árbol AVL** | Orden por título | O(log n) |
| **Árbol B** | Búsqueda por año | O(log n) |
| **Árbol B+** | Agrupación por género | O(log n) |
| **Tabla Hash** | Búsqueda por ISBN | O(1) promedio |
| **Grafo Ponderado** | Rutas entre bibliotecas | O(V²) |

---

## 🧰 Requisitos de Instalación

### 1️⃣ Instalar Java JDK
Descargar desde [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/)

Verificar instalación:
```bash
java -version
javac -version
nstalar Apache NetBeans

Descargar desde https://netbeans.apache.org/download/

Abrir el proyecto:

Archivo → Abrir Proyecto → BibliotecaMagica

3️⃣ Instalar Graphviz

Descargar desde https://graphviz.org/download/

Agregar al PATH:

C:\Program Files\Graphviz\bin


Verificar instalación:

dot -version

⚙️ Compilación y Ejecución
💻 Desde NetBeans

Abrir el proyecto en NetBeans.

Compilar con F11.

Ejecutar con Shift + F6.

Cargar los archivos CSV:

bibliotecas.csv

conexiones.csv

libros.csv

🧠 Desde Consola
javac -d build src/com/mycompany/bibliotecamagica/Main.java
java -cp build com.mycompany.bibliotecamagica.Main

🗂️ Estructura del Proyecto
/src/com/mycompany/bibliotecamagica/
├── Arboles/
│   ├── ArbolAVL.java
│   ├── ArbolB.java
│   └── ArbolBMas.java
├── EstructurasBasicas/
│   ├── Cola.java
│   ├── Pila.java
│   ├── Lista.java
│   └── HashTableISBN.java
├── Grafo/
│   ├── Grafo.java
│   └── NodoGrafo.java
├── Vistas/
│   ├── VistaGeneralFrame.java
│   └── PanelBibliotecas.java
├── Controladores/
│   ├── ControladorBiblioteca.java
│   ├── ControladorTransferencias.java
│   └── CargadorCSV.java
└── Main.java

🧩 Ejemplo de Ejecución

Menú Principal:

Archivo → Cargar CSV

Gestión → Agregar / Buscar / Eliminar Libro

Simulación → Transferir Libro

Visualización → Mostrar Árboles y Grafos

Ejemplo de transferencia:

Origen: Sede Mágica Norte
Destino: Centro de Distribución
Criterio: Ruta más rápida


Resultado:
El sistema calcula la ruta óptima en el grafo, mueve el libro a las colas correspondientes y actualiza el estado a “Disponible”.

📊 Visualización Gráfica

Los gráficos se generan automáticamente con Graphviz:

arbolAVL.png

arbolB.png

arbolBmas.png

hashTable.png

grafoBibliotecas.png

colasDespacho.png

📁 Carpeta de salida:

/BibliotecaMagica/export/

🔒 Manejo de Errores
Error	Solución
Archivo CSV no encontrado	Verificar ruta y nombre
ISBN duplicado	Revisar registros antes de insertar
dot no reconocido	Añadir Graphviz al PATH
Ruta no encontrada	Revisar conexiones en el grafo
--Autora

Nombre: MadeLayne Ana María Pérez Pérez
Carnet: 202130171
Carrera: Ingeniería en Ciencias y Sistemas
Universidad: Universidad de San Carlos de Guatemala – Centro Universitario de Occidente (CUNOC), Quetzaltenango.
Colaboración: Proyecto desarrollado íntegramente por la estudiante.

Licencia

Este proyecto fue desarrollado con fines académicos para el curso Laboratorio de Estructura de Datos,
como demostración del dominio de estructuras de datos avanzadas, algoritmos y programación orientada a objetos en Java.

Prohibida su reproducción o distribución sin autorización de la autora.
