# ProyectoAEMET
<h1>Hecho por Laura Garrido Y Miguel Zanoto</h1>
<b>Repositorio del proyecto:</b> <br>

![image](https://github.com/MiguelZanottto/ProyectoAEMET/assets/132077920/d651ce4c-8835-4e1b-b317-5e29958ae9c1)

<h2>Carpeta Controllers</h2>
<p>Esta carpeta tiene las clases:</p>
<ul> 
  <li><b>Aemet Controllador principal</b> del cual se encarga de gestionar los datos de Amet del csv.</li>
  <P>Del cual dentro de ella se encarga de:</P>
  <ol>
    <li>Cargar los datos de los archivos CSV.</li>
    
 ![image](https://github.com/MiguelZanottto/ProyectoAEMET/assets/132077920/580e00de-9797-40f3-8bbd-d779a3a1251c)
    <li>Leer los CSV.</li>

  ![image](https://github.com/MiguelZanottto/ProyectoAEMET/assets/132077920/d81b6b58-952b-4912-8f61-59d984760485)
    <li>Convertir un archivo de texto en Formato UTF-8 del cual manejará los carecteres especiales del archivo</li>

![image](https://github.com/MiguelZanottto/ProyectoAEMET/assets/132077920/28d3f86a-9e05-4469-9de4-236d3a50e1c7)
    <li>Convertir una cadena de hora en formato HH:mm en un objecto LocalTime</li>

![image](https://github.com/MiguelZanottto/ProyectoAEMET/assets/132077920/106521a9-6808-44b7-b6de-646ff8a32e35)
    <li>Convertir una cadena de fehca en formato "yyyyMMddd" en un objeto LocalDate</li>

![image](https://github.com/MiguelZanottto/ProyectoAEMET/assets/132077920/ef0d05b4-40ad-4677-b0f1-8a901d77a9e9)
    <li>Crear Api string que resuelvan estas cuestiones:
      <p>¿Dónde se dio la temperatura máxima y mínima total en cada uno de los días?.</p>
      <p>Máxima temperatura agrupado por provincias y día.</p>
      <p>Mínima temperatura agrupado por provincias y día.</p>
      <p>Medía de temperatura agrupado por provincias y día.</p>
      <p>Precipitación máxima por días y dónde se dio.</p>
      <p>Precipitación media por provincias y día.</p>
      <P>Lugares donde ha llovido agrupado por provincias y día.</P>
      <p>Lugar donde más ha llovido.</p>
        <p><b>Datos de las provincia de Madrid</b></p>
        <p>Por cada día:</p>
        <p>Temperatura máxima, mínima y dónde ha sido.</p>
        <p>Temperatura media máxima.</p>
        <p>Temperatura media mínima.</p>
        <p>Precipitación máxima y dónde ha sido.</p>
        <p>Precipitación media.</p>
    </li>
  </ol>
  </li>

<li><b>Export JSON</b> es la clase encargada de exportar los datos en json de Amet.</li>
<p>Del cual tendra:</p>

<ol>Metodo exportar, del cual Utiliza la biblioteca Gson para serializar los objetos y proporciona una opción para formatear el JSON de manera legible.</ol>

![image](https://github.com/MiguelZanottto/ProyectoAEMET/assets/132077920/f129702d-6fb0-4a6a-afe5-fa684336cfe6)

</ul>

<h2>Carpeta Models</h2>
<ul>
<li><B>Aemet  .</B>Esta clase se encarga de crear las propiedades donde se guardara y utilizare las propiedades del fichero csv.</li>

  ![image](https://github.com/MiguelZanottto/ProyectoAEMET/assets/132077920/21c58afb-82a0-4ea7-968f-a7e28c63b99a)
  
</ul>

</ul>

</ul>
<h2>Carpeta  Repositories</h2>
<ul>
<li><B>Aemet Repository  .</B>Esta clase se encarga de definir un repositorio para acceder y gestionar los datos de AEMET.</li>

![image](https://github.com/MiguelZanottto/ProyectoAEMET/assets/132077920/cacd3872-0c42-400e-88fa-bc11a0ad963a)

<li><b>Aemet Repository Impl   .</b> Esta clase se encarga de implementar la interfaz AemetRepositoryy realizar las operaciones de crud.</li>

![image](https://github.com/MiguelZanottto/ProyectoAEMET/assets/132077920/d44a0733-4d86-4b42-b84b-c86845205ecb)

<li><b>Crud  Repository   .</b> Esta clase define un conjunto de operaciones CRUD (Crear, Leer, Actualizar, Eliminar)</li>

![image](https://github.com/MiguelZanottto/ProyectoAEMET/assets/132077920/ff12fe23-56de-4e72-b59d-f1846238516a)

</ul>


<h2>Carpeta Services</h2>
<ul>
<li><B>Data base Manager  .</B>Esta clase se encarga de leer los datos sql. Como se dio en clase la Clase DatabaseManager funciona de la misma manera.</li>
</ul>

<h2>Carpeta utils</h2>
<ul>
<li><B>Local Date Adapter   .</B>Esta clase se encarga de crear un adapatador  del cual no tenga problemas al leer los datos tipo Local Date</li>

![image](https://github.com/MiguelZanottto/ProyectoAEMET/assets/132077920/06915082-d3a7-4f97-9144-3c1d7c1ba282)

<li><B>Local Time Adapter   .</B>Esta clase se encarga de crear un adapatador  del cual no tenga problemas al leer los datos tipo Local Date</li>

![image](https://github.com/MiguelZanottto/ProyectoAEMET/assets/132077920/f4439cc1-fa56-492a-9be6-6d60011d1a24)

</ul>
