La estructura de los tutoriales es la siguiente.

1.- PREPARACION DE OPENGL
Este primer ejemplo muestra la preparación de OpenGL. Realmente no contiene código propio de OpenGL. Casi todo el código se corresponde con el sistema de ventanas. 
El código tiene dos métodos de interés. Uno de ellos es initGL que se responsabiliza de crear la ventana, crear el contexto de OpenGl y poner un estado básico. Este estado se irá complicando en los ejercicios posteriores, pero en este primer ejercicio unicamente  se fija el color de borrado. (glClear)
Os animamos a estudiar el código y aprender a distinguir el código que se corresponde con OpenGL con el que es el soporte de ventanas propio de LWGL.

1.- DIBUJANDO CON OPENGL ANTIGUO - MONOCOLOR
Este ejemplo muestra el dibujo de un triangulo utilizando la estructura de OpenGL antiguo. 
El código tiene una estructura muy similar al ejemplo anterior. La principal diferencia está en el método de bucle (loop). Se observa la especificación del color del vértice y el dibujo del triangulo. La principal desventaja de esta forma de dibujar es que se inyectan datos a OpenGL en cada dibujo.
Os animamos a estudiar el código y aprender a cambiar los colores o las formas del dibujo. OpenGL tiene muchas otras primitivas (líneas, puntos, cuadros, etc.). Intentad probar esas posibilidades.

2.- DIBUJANDO CON OPENGL ANTIGUO - COLORES
Este ejemplo sigue utilizando OpenGL antiguo para el dibujo de un triangulo. 
La principal diferencia con el anterior es que en este ejemplo ponemos diferentes colores a cada vértice. Observad que unicamente se definen los colores de los vértices y OpenGL interpola el color en el interior del triángulo. El código se diferencia muy poco del anterior. Era una de las ventajas del OpenGL antiguo.

3.- DIBUJANDO CON OPENGL MODERNO - MONOCOLOR
Este ejemplo muestra el dibujo de un triángulo utilizando la estructura de OpenGL moderno. 
Aunque el dibujo es muy similar a los anteriores, el código tiene diferencias profundas y fundamentales. El primer cambio  que se puede observar es que el método initGL ha ganado en tamaño. Ahora preparar la maquinaría de OpenGL implica preparar los shaders de vértices y de fragmentos.
Otra diferencia es que el dibujo del triángulo se realiza rellenando un buffer y conectándolo con los shaders.

4.- DIBUJANDO CON OPENGL MODERNO - COLORES, UNICO BUFFER
Este ejemplo sigue utilizando OpenGL moderno para el dibujo de un triángulo con colores.
Cada vértice tiene ahora especificado un color. Para poner colores a los vértices tenemos que jugar con los shaders y buffers. En este ejemplo definimos en el shader de vértices una nueva variable para recibir el color. También modificamos la construcción del buffer. ¿Puedes localizar esos cambios?
Observa cómo los cambios afectan a los elementos del código. Se modifican las definiciones de ambos Shaders, la construcción de los buffers y la sentencias que los conectan con los shaders.

5.- DIBUJANDO CON OPENGL MODERNO - COLORES, VARIOS BUFFERS
De nuevo volvemos a dibujar el mismo triángulo, pero esta vez vamos a cambiar la forma de crear y conectar los buffers. En lugar de tener un único buffer con los datos de los vértices y de los colores, se van a crear dos buffers. Uno con los datos de los vértices y otro con los datos de los colores. Observad que el código de initGL no cambia. Unicamente cambiamos el proceso de definición y uso de los buffers. Es interesante el cambio que se ha producido en las llamadas a glVertexAttribPointer. ¿Podéis determinar las razones?
Nota. Observad que hemos cambiado la paleta de colores para distinguirlo del ejemplo anterior.

6.- USANDO VARIABLES UNIFORMS. MOVIENDO UN TRIANGULO
Este ejercicio introduce un nuevo tipo de variable en los shaders. Hasta ahora hemos utilizado “attribute” o “varying”. En este ejercicio se introduce el tipo “uniform” explicado en clase. Este tipo de variable toma un valor fijo (por eso su nombre) para todo el proceso de dibujo.
En este ejemplo se utiliza una variable uniform para mover el triangulo. El ejemplo utiliza un contador (de hecho, el seno del contador) para fijar la variable uniform y mover el triangulo.
Es interesante observar el cambio producido en los shaders y en el código Java que conecta la variable del shader con el contador.

7.- USANDO VARIABLES UNIFORMS. JUGANDO CON COLORES
Este ejercicio es muy similar al anterior pero ahora jugamos con la variable uniform para determinar el color del triángulo.
El código Java es prácticamente el mismo. La diferencia radica en el uso que le damos a la variable uniform en el shader.
¿Podéis encontrar los cambios?
Con estos ejemplos podemos entender las posibilidades que nos ofrece el OpenGL moderno. Hacer esto en el OpenGl antiguo hubiera sido posible, pero a costa de una pérdida en el  rendimiento si el modelo fuera complejo.

8.- USANDO VARIABLES UNIFORMS. DIBUJANDO VARIOS TRIANGULOS
Este ejercicio juega de nuevo con la misma idea pero ahora aprovechamos la variable uniform para mover dos triángulos de forma independiente.
Este ejemplo es interesante ya que reutilizamos el mismo código de shaders y el mismo modelo (los mismos buffers). 
La única diferencia es que se activa un valor de la variable uniform antes de dibujar el triángulo. Jugando con ese parámetro conseguimos dibujar dos triángulos en movimiento.

9.- USANDO MATRICES PARA MOVER UN TRIANGULO
Este ejercicio es similar a los ejercicios anteriores, pero en lugar de mover el objeto utilizando un escalar (un float) vamos a utilizar una matriz. El uso de vectores y matrices en OpenGL es fundamental ya que nos permite mover, escalar o rotar los objetos en el espacio. 
En este primer ejemplo, empezamos introduciendo una matriz de modelo que nos permita mover el triángulo de una forma un poco más interesante. Haciendo círculos. :-)

10.- USANDO MATRICES PARA ESCALAR UN TRIANGULO
La misma matriz del ejemplo anterior la podemos utilizar para escalar el triángulo. Realmente este ejemplo es casi idéntico al anterior. Unicamente cambiamos la forma de construir la matriz. ¿Podéis localizar las diferencias entre este ejemplo y el anterior?

11.- USANDO MATRICES PARA ROTAR UN TRIANGULO
Y finalmente, jugamos con la misma matriz para rotar el triángulo. Seguimos reutilizando los mismos shaders y modelos que en los ejemplos anteriores. Basta con cambiar una solo línea. ¿Podéis localizar las diferencias?

12.- COMBINANDO LA MATRIZ DE MODELO PARA DIBUJAR TRIANGULOS
En este ejemplo jugamos con dos triángulos que cambian de forma independiente. Pero a pesar de que el resultado parece complejo realmente estamos usando el mismo modelo y los mismos shaders para el dibujo. La única diferencia es que cada triangulo construye la matriz de modelo de una forma diferente. 

13. MATRIZ DE PROYECCION ORTOGRAFICA
Hasta ahora estamos representando triángulos planos, pero para dibujar en tres dimensiones debemos introducir una nueva matriz. La matriz de proyección. En nuestras prácticas vamos a utilizar dos tipos de proyección: ortográfica y en perspectiva.
En esta primera práctica, vamos a representar un triángulos en perspectiva ortográfica. Apenas se diferencia de lo que ya teníamos, pero hemos introducido una nueva matriz en nuestros shaders.

14. MATRIZ DE PROYECCION PERSPECTIVA
El siguiente paso es introducir una matriz de perspectiva. Ahora podemos ver que el triángulo se estaba realmente moviendo en el eje Z. Según el triángulo se aleja de nosotros se ve más pequeño. Antes, con una matriz de tipo ortográfica eso no sucedía. ¿Sabrías explicar la razón de ese comportamiento?

15. DIBUJANDO UN CUBO
El siguiente paso es introducir un modelo algo más complejo. Vamos a representar un cubo utilizando el código del ejemplo anterior. En este ejemplo solo cambiamos el modelo. El resto sigue siendo muy similar.
En este ejemplo hemos guardado las coordenadas del cubo en un array para facilitar la lectura. Debido al número de puntos, es más cómodo guardarlo como indica el ejemplo. Con los colores sucede algo similar.
Notad también que hemos introducido una llamada al método “glEnable( GL_DEPTH_TEST);” activando de esta forma el control de partes vistas y partes ocultas.

16. DIBUJANDO CUBOS EN MOVIMIENTO
El último ejercicio de esta serie consiste en dibujar varios cubos en movimiento. De nuevo, no tendremos que modificar los shaders ni los modelos: solo tocaremos el método que los dibuja.
Hasta el momento no hemos introducido iluminación por lo que jugamos con los colores de las caras para tener sensación de profundidad.
Intentad hacer algo similar jugando con los parámetros de  escala, posición y rotación.
