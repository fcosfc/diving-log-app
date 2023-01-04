# APP móvil con Registro de Inmersiones de Buceo

Proyecto asignatura *Dispositivos Móviles*. \
Alumno [Francisco Saucedo](https://www.linkedin.com/in/franciscosaucedo/). \
[Máster Universitario en Ingeniería Informática](https://www.upo.es/postgrado/Master-Oficial-Ingenieria-Informatica/). \
[Universidad Pablo de Olavide](https://www.upo.es).

## Descripción general

APP móvil para sistema operativo Android desarrollada con lenguaje de programación Java, según las directrices de la asignatura. 

El caso de uso se corresponde con el de una aplicación para que un submarinistra registre datos de sus inmersiones. 

La aplicación evolucionará desde los requisitos establecidos en la asignatura para el Ejercicio Evaluable hasta los del Trabajo Final. Cada versión se identificará mediante una etiqueta.

## Ejercicio evaluable 

* Etiqueta: v1.0
* Funcionamiento: al entrar en la APP se carga una lista de inmersiones de ejemplo, si se pulsa sobre una de ellas se puede ver el detalle. También se pueden guardar nuevas inmersiones mediante el menú de la APP. 
* Matriz de cumplimiento de requisitos:

| Requisito | Cumplimiento en APP                                                               |
| --------- |-----------------------------------------------------------------------------------|
| La App debe constar de, al menos, 2 Activities | Tiene tres actividades: MainActivity, DiveDetail y EditDive                       |
| Entre dichas actividades se debe pasar información | Se pasa el detalle de inmersión entre MainActivity y DiveDetail                   |
| La App debe constar con un servicio de persistencia | Se usa SQLite para almacenar los detalles de las inmersiones                      |
| En la App debe hacer uso de al menos un par de tipos de notificaciones | Notificación en barra de estado en primer uso. Toast cuando se guarda un registro |
