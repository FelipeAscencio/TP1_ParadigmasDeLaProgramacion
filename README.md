# Trabajo Práctico 1: Robots.

---

## Profesores
### Titulares: Diego Corsi y Diego Essaya.
### Corrector: Cristian Ciarallo.

## Alumnos
### Álvarez Julián: Padrón 110825.
### Ascencio Felipe Santino: Padrón 110675.

---

# Informe

## Introducción

Se implementó tanto la capa "lógica" (modelo), como la parte "gráfica" (vista + controladores) del juego "Robots", con las reglas brindadas por la cátedra.

El juego fue desarrollado utilizando "JAVA FX", "MAVEN" y todo el código fue trabajado en "INTELLIJ" (La versión de Java utilizada fue JAVA 21).

## ¿Cómo jugar?

Es tan simple como abrir el proyecto en nuestro "IntellIJ" de confianza, dirigirnos a la clase "APP" y ejecutar el programa.

Todas las instrucciones de uso, tutoriales, menús intuitivos y guías necesarias son brindadas dentro de los distintos escenarios del juego.

## Carpeta "doc"

En la carpeta "doc" se encuentra toda la documentación pertinenete respectiva a el trabajo práctico.

Listado del material:
- Modelos de todas las flechas de dirección utilizadas en la "GUI".
- Modelos "sprites" de los robots, la explosión y el jugador.
- Diagrama UML de clases (.png y .puml).
- Diagrama UML de secuencia (.png y .puml).

## Carpeta "src"

En la carpeta "src" se encuentran todos los archivos pertinentes a la implementación del juego.

Para analizar de forma mas clara vamos a dividir estos archivos en 2 secciones.

### Capa lógica de abstracción (modelo)

En esta capa se encuentra toda la implementación "lógica" o de "funcionamiento" del juego.

En todas las clases utilizadas, no existe ningún método o atributo relacionado a "JAVA FX", ya que como trabajamos con las capas de abstracción solicitadas, la parte "gráfica" del juego si conoce la existencia de la parte "lógica", pero no al revés.

Estas clases son totalmente reutilizables para otros juegos, o variaciones del mismo, con distinta "GUI".

Archivos relacionados:
- Direccion.java
- Enemigo.java
- Explosion.java
- Juego.java
- Jugador.java
- Robot1.java
- Robot2.java
- Tablero.java

### Capa gráfica de abstracción (vista + controladores)

En esta capa se encuentra toda la implementación "gráfica" o "visual" del juego.

Esta capa cuenta principalmente de la clase "App.java", que permite la ejecución del programa.

Y cuenta con 3 archivos ".fxml", con sus respectivas clases asociadas (controladores), para la implementación de cada escenario.

Nos ayudamos del patrón de arquitectura "MVC" para dividir los conceptos de "modelo" (antes explicado), vista y controlador. Fusionando estos últimos 2 en esta capa de abstracción, para ayudarnos a manejar de forma mas eficiente los archivos ".fxml" y conseguir mejores resultados en la implementación tanto del apartado de funcionamiento como en el visual.

Archivos relacionados:
- App.java
- ControladorMapa.java
- ControladorMenu.java
- ControladorTutorial.java
- mapa.fxml
- menu.fxml
- tutorial.fxml

---

## Interfaz Gráfica (GUI)

La interfaz gráfica desarrollada está pensada para ser lo mas intuitiva y simple posible, sin perder ese toque artístico que le da una personalidad propia a la implementación del juego.

---

## Detalles de implementación

### Polimorfismo

La implementación tiene, como se solicita en el enunciado, los comportamientos polimorficos relacionados a los robots y las celdas incendiadas.

Las 3 clases heredan desde la superclase "Enemigo", utilizan métodos heredados con resultados distintos (definición de polimorfismo), y también tienen sus comportamientos y atributos particulares que los caracterízan.

### Jugabilidad

La implementación de "ROBOTS" brinda al usuario una gran gamma de herramientas para jugar.

Por lo que, el juego puede ser disfrutado en su totalidad tanto con el "mouse" como con el "teclado" (Si tienes dudas de los comandos siempre puedes consultar el "Tutorial").

### Tamaño de mapa

El mapa tiene una implementación que le permite al usuario en el juego modificar la cantidad de filas y columnas a elección, entre unos valores estipulados.

Estos valores se determinaron en base a la resolución promedio de un monitor convencional, viendo que la jugabilidad no sea afectada y se puedan apreciar claramente todos los elementos del juego.

Sin embargo, el "modelo" del juego no está limitado a valores específicos de "Filas" y "Columnas", por lo que con otra interfaz gráfica, o mínimas modificaciones de la actual, se podrían aumentar o reducir la cantidad de "Filas" y "Columnas" tanto como sea deseado.

### Sistema de puntaje

El sistema de puntaje del juego es simple. Por cada explosión el jugador gana "1" punto y el puntaje es acumulable entre niveles, es decir, que al avanzar de nivel los puntos obtenidos previamente siguen contando. ¿Cuánto será el máximo que podrás alcanzar?

### Dificultad

El juego comienza con "5" robots en el nivel "1" y luego, con el avance de los niveles, se van aumentando de 2 en 2.

### Tipos de robots

El tipo de los robots que se generan es totalmente aleatorio, lo que hace que cada nueva partida sea un desafío distinto para probar tus habilidades.

### Animaciones

La cátedra brindó una "tira de sprites" con la que se trabajó para la representación de los personajes y la "animación" de los mismos.

La "animación" que se implementó hace que entre los turnos, los sprites de todos los robots vivos, y el jugador, cambien entre unas 4 posibilidades de forma automática.

Por último, el jugador cuenta con un "sprite" específico de "Game Over", con el que indicarle de forma mas intuitiva (junto con los mensajes provistos por la "GUI") el final de la partida al usuario, para que pueda reiniciarla.

### Finalización del programa

Como se solicitó por enunciado, la única forma de finalizar el programa es mediante a la "X" de la "GUI".

Por lo que se pueden jugar la cantidad de partidas que se desee sin tener que reiniciar la aplicación.
