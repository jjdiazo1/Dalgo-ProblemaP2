# Proyecto CompoundProblemSolver

Este proyecto resuelve problemas compuestos utilizando métodos de análisis de grafos y el algoritmo de Floyd-Warshall para encontrar caminos cortos entre elementos fundamentales.

## Compilación y Ejecución

Para compilar y ejecutar este programa, siga estas instrucciones:

1. Abrir una Consola:  
   Abra una consola o terminal para ejecutar comandos.

2. Ubicarse en la Carpeta del Proyecto:  
   Use el comando `cd` para ir a la carpeta donde se encuentra el proyecto:  
   cd C:\Usuarios\juan\carpeta1\carpeta2\...\CompoundProblemSolver

3. Compilar el Código Java:  
   Cree una carpeta para los archivos compilados y use `javac` para compilar el código fuente:  
   mkdir classes  
   javac -d classes src/*.java

4. Ejecutar el Programa:  
   Ejecute el programa, indicando la carpeta de las clases compiladas:  
   java -cp classes CompoundProblemSolver

Redirección de Entrada/Salida

Para pruebas más grandes, puede redirigir la entrada y salida estándar usando archivos:

1. Entrada desde Archivo:  
   Use `<` para redirigir la entrada estándar desde un archivo:  
   java -cp classes CompoundProblemSolver < data\CompoundProblemSolver.in

2. Salida hacia Archivo:  
   Use `>` para redirigir la salida estándar hacia un archivo:  
   java -cp classes CompoundProblemSolver > data\CompoundProblemSolver.out

## Eclipse IDE

Para compilar y ejecutar el programa desde Eclipse:

1. Ubicarse en la Carpeta de Compilación de Eclipse:  
   Use `cd` para ir a la carpeta donde Eclipse guarda los archivos compilados:  
   cd C:\Usuarios\juan\carpeta1\carpeta2\...\CompoundProblemSolver\bin

2. Ejecutar el Programa con el Compilador de Eclipse:  
   Ejecute el programa, indicando dónde están los archivos compilados:  
   java -cp "C:\Usuarios\juan\carpeta1\carpeta2\...\CompoundProblemSolver\bin" CompoundProblemSolver

3. Redirección desde y hacia Archivos:  
   Para redirigir la entrada y salida estándar desde cualquier directorio:  
   java -cp "C:\Usuarios\jorge\carpeta3\carpeta4\...\entrada.in" CompoundProblemSolver  
   > C:\Usuarios\juan\carpeta10\carpeta11\...\salida.out
