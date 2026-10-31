# Paradigmas de Programación — Trabajo Práctico N° 2

**Objetivo:** Aplicar los conceptos, principios y características del paradigma de la programación orientada a objetos (Clases, objetos, métodos, interfaz, herencia, polimorfismo, colecciones, etc.).  
**Fecha de entrega:** 17 de Noviembre / 20 de Noviembre de 2026 (según comisión).

---

## Objetivo General
Implementar los conceptos abordados del paradigma de la Programación Orientada a Objetos, como solución a un problema real, partiendo del análisis de la problemática, el diseño de la solución (Diagrama de Clases) y posteriormente la construcción del producto final (Software).

---

## Consigna

### Temática
**El Torneo de los Inmortales: la elección de los Dos Santos Supremos**

### Objetivos del Trabajo
- Modelar un sistema de combate por parejas entre cultivadores de distintas escuelas.
- Implementar técnicas de combate y equipamiento que modifican las capacidades de los combatientes.
- Gestionar las parejas, los cruces y las rondas de un torneo eliminatorio, utilizando distintos tipos de colección y aplicando los patrones de diseño que correspondan.
- Usar herencia y polimorfismo para permitir la diversidad de combatientes (*Cultivador de la Espada*, *Monje del Cuerpo Celestial*, *Hechicero del Vacío*) y que cada escuela los prepare según su filosofía.

### Descripción General
Parejas de combatientes se enfrentan en rondas eliminatorias hasta que queda una sola en pie; sus integrantes son proclamados los *Dos Santos Supremos*. Cada combatiente pertenece a una escuela de cultivación, que define su preparación inicial. Se espera que el sistema esté diseñado de manera flexible para que se puedan agregar nuevos tipos de combatientes, técnicas, escuelas y equipamientos fácilmente.

---

### 1. Combatientes
- **Combatiente (abstracta):** identificador único e inmutable, nombre, vida y energía (actuales y máximas), equipamiento y técnicas disponibles. Dos combatientes son el mismo si tienen el mismo identificador, sin importar el nombre.
- **Tipos iniciales:**
  - **Cultivador de la Espada:** potencia el daño que provoca.
  - **Monje del Cuerpo Celestial:** reduce el daño que recibe.
  - **Hechicero del Vacío:** gasta menos energía al ejecutar técnicas.
- **Polimorfismo:** La diferencia entre tipos debe surgir del polimorfismo, no de preguntar el tipo con `if` o `instanceof`: una misma técnica provoca distinto daño según quién la ejecuta, sin que la técnica conozca el tipo del combatiente. Se deben poder agregar tipos nuevos sin modificar el torneo ni las técnicas existentes.

---

### 2. Técnicas
Una técnica es únicamente su definición: nombre, costo de energía y efecto. Los usos se registran en cada combatiente, porque una misma técnica puede estar en varios. Agregar una técnica nueva no debe modificar las existentes.
- **Ejemplos:**
  - *Corte de los Mil Amaneceres:* daña al adversario.
  - *Muro del Cuerpo Dorado:* reduce el daño que recibe el ejecutor durante el resto del combate.
  - *Dominio de la Nada:* daña al adversario y le quita energía.
  - *Rugido de la Bestia Primordial:* ataque poderoso.
  - *Meditación del Inmortal:* recupera vida.
- Los valores concretos (daño, costo) los define el grupo y deben quedar documentados y justificados.

---

### 3. Escuelas y Creación de Combatientes
Las escuelas preparan combatientes de cualquier tipo según su filosofía, asignándoles el equipamiento y las técnicas iniciales:
- **Escuela de la Espada Celestial:** ataques físicos y armas.
- **Palacio del Vacío:** técnicas especiales y dominio de la energía.
- **Monasterio del Cuerpo Dorado:** resistencia, defensa y recuperación.

Una escuela es un objeto con nombre, técnicas y equipamiento iniciales. Agregar una escuela es crear otro objeto así, sin escribir código nuevo.  
La creación de combatientes está centralizada: pedir un *Hechicero del Vacío* preparado por el *Palacio del Vacío* no requiere conocer su clase concreta, sus atributos iniciales ni cómo se arma su preparación.

---

### 4. Equipamiento
Modifica las capacidades del combatiente mientras lo lleva.  
**Ejemplos:** *Espada celestial* (más daño), *Armadura dorada* (menos daño recibido), *Anillo espiritual* (más energía máxima), *Sello del Fénix* (más vida máxima).
- Un combatiente puede llevar hasta tres equipamientos, sin dos de la misma categoría (arma, armadura, anillo, sello). Los efectos se acumulan.
- Se puede equipar y quitar antes del torneo y entre combates. Se debe manejar correctamente cuando un cambio intenta romper una regla.
- Equipar un objeto que aumenta un máximo no aumenta el valor actual. Al quitarlo, el valor actual se recorta al nuevo máximo si lo supera.
- El combatiente conoce los objetos que lleva y puede consultarlos.

---

### 5. Parejas y Torneo
- Cada pareja tiene identificador único, asignado al crearse; el de los combatientes lo asigna el creador, en orden de creación. Un combatiente no puede inscribirse dos veces ni una pareja registrarse dos veces; el intento debe rechazarse con una excepción.
- El torneo se debe jugar únicamente con 4 u 8 parejas.
- Los cruces de la primera ronda se arman por orden de inscripción (primera contra segunda, tercera contra cuarta, etc.). En las siguientes, por el orden de los cruces de la ronda anterior.
- Una pareja pierde cuando sus dos integrantes quedan fuera de combate. El torneo termina cuando queda una sola pareja.
- Las colecciones se eligen según la necesidad (conservar el orden de rondas y cruces, evitar duplicados, registrar cuántas veces cada combatiente usó cada técnica, ordenar la tabla de posiciones).

---

### 6. Reglas del Combate

- **Asaltos y turnos:**
  - El combate se desarrolla en asaltos. En cada asalto, cada pareja juega su turno completo: cada integrante en pie realiza una acción. Empieza la pareja de menor identificador.
  - Dentro de la pareja actúa primero el de menor identificador. Si en cualquier momento una pareja queda sin integrantes en pie, el combate termina.
- **Acción:**
  - La acción es un ataque básico o una técnica. El ataque básico no cuesta energía, no tiene límite de usos y no cuenta para la regla del compañero. La técnica requiere energía suficiente y usos restantes.
  - **Criterio por defecto (en este orden):**
    1. Una técnica de recuperación si le falta vida.
    2. Una técnica defensiva si no tiene una activa.
    3. La técnica ofensiva con más usos restantes.
    4. Si ninguna es posible (por energía, usos o regla del compañero), se realiza un ataque básico.
    5. En caso de empate, la primera en el orden de la escuela.  
    *(El grupo puede definir otro criterio, siempre que sea determinista y esté documentado).*
- **Objetivo:**
  - Para ataques y técnicas ofensivas: el rival en pie con menos vida actual; en empate, el de menor identificador.
  - Las técnicas defensivas y de recuperación se aplican al ejecutor.
- **Vida y energía:**
  - Al llegar a cero de vida, el combatiente queda fuera de combate: no actúa ni puede ser objetivo en ese combate.
  - Al comenzar cada asalto, cada combatiente en pie recupera una cantidad fija de energía (definida por el grupo), sin superar su máximo.
  - Al comenzar cada combate, todos los integrantes (incluidos los caídos en combates anteriores) recuperan vida y energía al máximo.
  - Valores iniciales definidos por el grupo (ejemplo: 100 de vida y 40 de energía).
- **Técnicas (Restricciones):**
  - **Regla del compañero:** cuando un integrante usa una técnica en un combate, su compañero no puede usarla mientras el primero conserve usos de ella y siga en pie; si los agota o queda fuera de combate, el compañero puede usarla. La restricción termina con el combate: en el siguiente, cualquiera de los dos puede volver a usarla.
  - **Límite total:** Cada combatiente puede usar cada técnica cinco veces en todo el torneo; el registro no se reinicia entre combates. Sin usos no se puede ejecutar aunque haya energía.
- **Duración:**
  - Límite de asaltos por combate fijado por el grupo. Al alcanzarlo gana la pareja con más vida total (suma de vida actual de sus integrantes; caídos suman cero); en empate, la de menor identificador.
- **Reproducibilidad:**
  - El combate es determinista: ante el mismo estado inicial produce el mismo resultado.

---

### 7. Acontecimientos y Presentación
El combate y el torneo comunican acontecimientos: inicio de combate, acción realizada (ataque básico o técnica), eliminación, resultado del combate (ronda, ganadora y vida total de cada pareja al terminar) y proclamación. Cada acontecimiento guarda los datos del momento en que ocurrió. Deben poder sumarse nuevos interesados sin modificar la lógica del torneo.

**Componentes obligatorios:**
- **Heraldo Imperial:** Presenta los acontecimientos por pantalla. Es la **única** clase del sistema que escribe en pantalla, incluido lo que muestra el main.
- **Cronista de la Secta:** Registra los acontecimientos y permite consultar el historial. Con los resultados registrados arma la tabla de posiciones: las parejas ordenadas por la última ronda del torneo que jugaron, en empate por la vida total con la que terminaron su último combate y luego por identificador. Al terminar el torneo, el main le pide la tabla al Cronista y el Heraldo la presenta.

---

### 8. Pruebas Unitarias (JUnit 5)
Batería de pruebas unitarias para técnicas, combatientes, escuelas, equipamiento, parejas, combate y torneo. Cada regla debe tener al menos una prueba que la demuestre:
- Una misma técnica daña diferente según el tipo del ejecutor.
- No se ejecuta una técnica sin energía o sin usos restantes.
- Los usos se conservan entre combates; vida y energía se recuperan.
- El compañero no puede usar una técnica mientras el primero conserve usos de ella.
- Equipar un cuarto objeto o una categoría repetida se rechaza sin cambios.
- Al quitar un equipamiento que sube la vida máxima, la vida actual se recorta.
- Un caído no actúa ni es elegido como objetivo.
- Elección de objetivo y desempate al límite de asaltos.
- Inscripciones duplicadas y cantidad de parejas inválida se rechazan.
- La tabla de posiciones queda ordenada por última ronda jugada y vida total final.
- Dos torneos armados igual producen el mismo campeón y el mismo historial.

*Las pruebas verifican a través del estado de los objetos o del historial del Cronista, nunca leyendo la salida por pantalla.*

---

### 9. Bonus (Al menos uno obligatorio)
- **Efectos temporales:** un efecto que permanece activo una cantidad de turnos del combatiente afectado (veneno, regeneración). Definir cuándo se aplica, cuánto dura y qué sucede al terminar.
- **Combatiente reactivo:** un combatiente cambia su comportamiento ante un acontecimiento; por ejemplo, potencia su próximo ataque cuando cae su compañero. Debe activarse y terminar según reglas definidas.

---

## Interfaz

Se evaluará mediante:
1. Pruebas unitarias con JUnit de funcionalidades y reglas.
2. Un método `main` ejecutable.

### Ejemplo de main orientativo
```java
public class TorneoDeLosInmortales {
    public static void main(String[] args) {
        // Escuela = configuración: técnicas y equipamiento iniciales.
        Escuela escuelaDeLaEspada = new Escuela(
            "Escuela de la Espada Celestial",
            List.of(new CorteDeLosMilAmaneceres()),
            List.of(new EspadaCelestial())
        );

        Escuela palacioDelVacio = new Escuela(
            "Palacio del Vacío",
            List.of(new DominioDeLaNada(), new MeditacionDelInmortal()),
            List.of(new AnilloEspiritual())
        );

        // Creación centralizada de combatientes
        CreadorCombatientes creador = new CreadorCombatientes();
        Combatiente aurelio = creador.crear("Cultivador de la Espada", "Aurelio", escuelaDeLaEspada);
        Combatiente nebula  = creador.crear("Hechicero del Vacío", "Nébula", palacioDelVacio);
        Combatiente kael    = creador.crear("Monje del Cuerpo Celestial", "Kael", palacioDelVacio);
        Combatiente darian  = creador.crear("Cultivador de la Espada", "Darian", escuelaDeLaEspada);

        Torneo torneo = new Torneo();
        // Registrar al Heraldo y al Cronista como interesados

        Pareja primera = new Pareja(1, aurelio, nebula);
        torneo.inscribir(primera);
        // Crear los demás combatientes e inscribir las otras tres parejas

        torneo.iniciar();
        // El Heraldo anuncia a los Dos Santos Supremos; después, el main
        // le pide la tabla al Cronista y el Heraldo la presenta.
    }
}
```

---

## Entrega
- Proyecto (código y pruebas).
- Diagrama de clases.
- Informe: carátula, índice, introducción, desarrollo, conclusiones y referencias (normas APA).
- Defensa oral grupal.

---

## Anexo: Glosario
- **Escuela:** Configuración que define técnicas y equipamiento iniciales de un combatiente; no determina su tipo.
- **Técnica:** Acción con efecto propio y costo de energía, con usos limitados por torneo.
- **Ataque básico:** Acción sin costo de energía ni límite de usos; no es una técnica.
- **Pareja:** Unidad que participa en el torneo, formada por dos combatientes.
- **Ronda:** Etapa del torneo donde se juegan cruces y las parejas ganadoras avanzan.
- **Cruce:** Enfrentamiento entre dos parejas dentro de una ronda (se resuelve con un combate).
- **Combate:** Desarrollo de un cruce, dividido en asaltos; termina cuando una pareja no tiene integrantes en pie o al límite de asaltos.
- **Asalto:** Unidad del combate en la que cada pareja juega su turno completo.
- **Turno:** Parte del asalto en la que los integrantes en pie realizan una acción.
- **Acontecimiento:** Hecho del combate o torneo que se comunica a los observadores/interesados (Heraldo, Cronista).
