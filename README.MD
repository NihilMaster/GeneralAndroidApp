# General App

## Introducción

Esta aplicación agrupa una serie de funcionalidades que he ido implementando durante mi proceso de aprendizaje de Android. Cada función está diseñada para resolver problemas cotidianos que como usuario de Android iré requiriendo.

---

## Funcionalidades

### Memory Fragment

Esta funcionalidad incluye una serie de juegos de memoria diseñados para mejorar la retención de información. (#F1)

#### Pestaña 1 - Recordar el número después de un día

##### Descripción
El usuario debe recordar un número generado aleatoriamente después de un día. La aplicación notifica al usuario cuando es momento de recordar el número y verifica si la respuesta es correcta.

##### Tecnologías utilizadas
- **SharedPreferences v1.1**: Para almacenar el número aleatorio, el estado del juego y otros datos persistentes.
- **ViewModel v1.2**: Para manejar la lógica de la interfaz de usuario y mantener los datos durante los cambios de configuración.
- **LiveData v1.2**: Para observar cambios en los datos y actualizar la interfaz de usuario automáticamente.
- **AlarmManager v1.3**: Para programar la notificación que se activa después de un día.
- **BroadcastReceiver v1.3**: Para manejar la notificación y actualizar el estado del juego.
- **ActivityResultLauncher v1.35**: Para gestionar los resultados de las actividades lanzadas.

#### Pestaña 2 - Recordar el número de forma continua

##### Descripción
El usuario debe recordar un número generado aleatoriamente de forma continua, sin un período de espera. La aplicación verifica la respuesta inmediatamente.

##### Tecnologías utilizadas
- (Próximamente)

#### Pestaña 3 - Recordar la frase

##### Descripción
El usuario debe recordar una frase generada aleatoriamente después de un período de tiempo determinado.

##### Tecnologías utilizadas
- (Próximamente)

#### SharedPreferences

La aplicación utiliza `SharedPreferences` para almacenar los siguientes datos:

- **MF_t1_random_number**: Número aleatorio a recordar (String).
- **MF_t1_random_grade**: Número de cifras del número aleatorio (String).
- **MF_t1_state**: Estado actual del juego (String).
- **MF_t1_strike**: Número de intentos fallidos (String).
- **MF_t1_max_score**: Máximo puntaje alcanzado (String).

---

### Cognitive Fragment

#### Descripción
Esta funcionalidad está en desarrollo y se enfocará en ejercicios cognitivos para mejorar habilidades mentales. (#F2)

##### Tecnologías utilizadas
- (Próximamente)

---

## Estructura del Proyecto

El proyecto está organizado de la siguiente manera:

```
general/
├── ui/             // Contiene los fragmentos y actividades relacionados con la interfaz de usuario.
│   ├── memory/     // Aloja los archivos relacionados con los juegos de memoria.
│   └── cognitive/  // Aloja los archivos relacionados con los ejercicios cognitivos. 
├── receivers/      // Contiene los `BroadcastReceiver` para manejar notificaciones y eventos del sistema.
└── utils/          // Contiene utilidades comunes, como el manejo de `SharedPreferences`.
```

