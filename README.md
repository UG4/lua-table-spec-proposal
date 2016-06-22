# Spezifikationssprache für Lua-Tables

## Stand der Dinge

Die Lua-Tables sehen prima aus und ermöglichen es, unter Ausnutzung der Lua-Syntax, auf einfache Weise größere Mengen an strukturierten Daten als Argumente an Methoden zu übergeben, ohne auf externe Dateiformate oder *flache* Argumentlisten zurückgreifen zu müssen.

**Beispiel (Auszug aus `henry.lua`):**

```lua
solver = {
  type                = "newton",
	lineSearch = {            -- ["standard", "none"]
      type            = "standard",
      maxSteps        = 30,   -- maximum number of line search steps
      lambdaStart     = 1,    -- start value for scaling parameter
      lambdaReduce    = 0.5,  -- reduction factor for scaling parameter
      acceptBest      = true, -- check for best solution if true
      checkAll        = false -- check all maxSteps steps if true
    }
}
```

Syntaktisch wird hier eine Trennung zwischen Simulationsparametern und dem eigentlichen Simulationsablauf (Verhalten) erreicht, Stichwort *Separation of Concerns*. Dies erleichtert es Benutzern, Simulationen durchzuführen, deren Ablauf im Wesentlichen, d.h., bis auf durch einfache Parameter gesteuerte Änderungen am Grundablauf, gleich ist.

Der Austausch der Parameter ist um ein Vielfaches leichter, als bei einem gemischten Simulationsablauf, dem flache und verhältnismäßig unstrukturiert Parameter übergeben werden.

Der bisherige Ansatz löst die Trennung syntaktisch, d.h., für das Auge des Skript-Entwicklers, sehr gut. Leider ist die Konvention, nach der die Daten strukturiert werden müssen, nur implizit und darüber hinaus mehrfach gegeben.

Einerseits implementiert die Methode, der die Struktur als Parametersatz übergeben wird, einige Checks, die auf fehlende oder ungültige Parameter hinweist:

** Auszug aus `d3f_util.lua` (L80-L84)**
```lua
if problem.free_surface then
		if type (problem.free_surface) ~= "table" then
			print ("Illegal specification of the free surface")
			exit ()
		end
...
```

Andererseits existiert oft eine Dokumentation, die auf gültige Strukturierung hinweist. In einer Help-Message wird zusätzlich auf die korrekte Verwendung hingewiesen:

** Auszug aus `d4f_util.lua` (L33-L46)**
```lua
function util.d3f.usage()
	print("");
	print("Usage of util.d3f.solve(problem):");
	print("---------------------------------");
	print("");
	print(" (r) = required, (o) = optional");
	print("");
	print("(r) problem.domain             -- a subsection ");
	print("(r) problem.domain.dim         -- World dimension of problem [1,2,3]");
	print("(r) problem.domain.grid        -- (path to) grid file [\"*.ugx\"]");
	print("(r) problem.domain.numRefs     -- number of refinements (integer)");
	print("(r) problem.domain.numPreRefs  -- number of pre-refinements (parallel) (integer)");
end
```

An all diesen Stellen ist die Information darüber, wie die Parameter-Daten spezifiziert werden dürfen, partiell gegeben.

Ein problematische Eigenschaft ist, dass zusätzliche Parameter spezifieziert werden können, ohne dass hier deutlich wird, ob diese benutzt werden.

**Beispiel:**

```lua
my_special_solver = {
	turbo_mode = "on"
}
```

Selbstverständlich ließe sich das in der Implementierung prinzipiell ebenfalls verhindern. Das würde aber die Redundanz und den Implementierungsaufwand noch weiter erhöhen.

## Warum ist eine Spezifikationsschicht nötig?

Eine Spezifikationsschicht ermöglicht es, die Strukturinformation und Validierung maschinenlesbar und redundanzfrei zu definieren.

- **Redundanzfreiheit:** Validierung und Usage-Message (siehe oben) sind durch die Spezifikation definiert und müssen nicht manuell angegeben werden.

- **Maschinenlesbar:** Es ist möglich, Code-Generatoren zu entwickeln, die eine Benutzerschnittstelle (Grafisch oder Commandline) erstellen und sämtliche Validierungsschritte automatisieren. Auch Dokumentation lässt sich in gewissem Umfang erstellen.

Dadurch, dass die Spezifikation in expliziter Form vorliegt, ist sie selbst zugleich Dokumentation. Ein Benutzer/Entwickler, der nur die Regeln verstehen möchte, ohne sich durch den Quelltext zu hangeln, kann diese unabhängig davon betrachten.

Von großem Vorteil ist, dass zusätzlich zur syntaktischen Trennung von Parametern und Verhalten, auch eine Trennung in der eigentlichen Implementierung vorgenommen wird. Die Regeln zur Struktur der Modellparameter werden in gewissem Sinn selbst "modelliert". Wie bei Projektbesprechungen immer wieder deutlich wird, ist man sich oft nicht sicher, mit welchen Parametern gerechnet wurde, bzw. ob diese wirklich gültig sind.

## Wie könnte so eine Spezifikation aussehen?

###Welche Sprache?###

Bei der Wahl der Basis für eine Spezifikationssprache gilt es die Vorteile von Sprachen mit guter Compiler-API und mächtigen Werkzeugen zur Compilezeit-Metaprogrammierung gegen die bereits getroffenen Designentscheidungen von UG abzuwägen. Da UG häufig auf Rechnern läuft, bei denen nicht klar ist, ob eine Java-Laufzeitumgebung vorhanden ist, wäre eine Lua-basierte Spezifikationssprache praktisch. Leider müsste hier einiges nachgerüstet werden, um einen mit den in VRL vorhandenen Spezifikationsmöglichkeiten vergleichbaren Funktionsumfang zu erhalten. Allerdings kann Lua relativ leicht interpretiert und weiterverbarbeitet werden. Unter der Voraussetzung, dass die Spezifikation auch von UG selbst verwendet wird, wird wohl die Wahl auf Lua fallen.

###Aufbau###

Die Spezifikation folgt im Wesentlichen der Struktur der Daten. Aus

```lua
problem = {
    valueOne = 2.0,
    valueTwo = 1.5
}
```

wird folgende Spezifikation:

```lua
problem = {

  -- validation for problem.valueOne
  valueOne = {
    -- properties
  },

  -- validation for problem.valueTwo
  valueTwo = {
    -- properties
  }

}
```

Die Spezifikation sieht also tatsächlich so aus wie die Daten selbst, was den Einstieg ins Lesen der Spezifikation enorm erleichtert.

> **ANMERKUNG:** die Kommentare sind komplett optional und werden für die Auswertung selbst nicht benötigt! Die Spezifikation ist eine separate Datei oder ein Codeabschnitt. Die konkrete Darreichungsform für den Validator (Datei, Tableeintrag etc.) wurde noch nicht festgelegt. Das hängt u.U. auch davon ab, ob der entsprechende Validator runtime oder compile-time basiert ist. Möglicherweise wird es beide Varianten geben.

Jeder Parameter kann genau definiert werden. Dabei können der Typ, eine Range und die Validierung angegeben werden. Die Gültigkeit von Parametern kann auch in Abhängigkeit von anderen Parametern angeben werden. Zusätzlich werden Angaben zur Darstellung gemacht.

**Skizze:**

```lua
-- finally, the full specification
problem = {

  -- validation for problem.valueOne
  valueOne = {
    -- data type (Integer, Double, String, Boolean, Function, Array of I, D, S, B, F,  Table with individual type per column)
    -- TODO which data types do we need? Are the listed ones enough?
    type     = "Double",
    -- default value
    default  = 123.4,
    -- style (only relevant for ui)
    style    = "default",
    -- tooltip (relevant for ui and potentially commandline, e.g., --help)
    tooltip  = "...",

    -- supported value range
    range = {
      min = 1,
      max = 10
    },

    -- validation
    validation = {
      -- validation can depend on other values
      dependsOn = {},
      -- validation function
      eval      = function(v,range) return v >= range.min and v <= range.max end
    },

    --value visibility (only relevant for ui)
    visibility = {
      -- just like validation, visibility can depend on other values
      dependsOn = {},
      -- function that controls visibility
      -- (in this case the value is always visible)
      eval      = function() return true end
    },
  },

  -- validation for valueTwo
  valueTwo = {
     -- data type (Integer, Double, String, Boolean, Function, Array of I, D, S, B, F,  Table with individual type per column)
    type     = "Double",
    -- default value
    default  = 1.5,
    -- style requests selection, e.g., drop-down (only relevant for ui)
    style    = "selection",
    -- tooltip (relevant for ui and potentially commandline, e.g., --help)
    tooltip  = "...",

    -- supported value range:
    -- in addition to min, max, ranges can also be explicitly specified
    -- which is useful for drop-down
    -- TODO think about necessary range specifications (multiple intervals etc.)
    range    = {
      values = {0.5,1.0,1.5,2.0,2.5,3.0}
    },

    -- validation
    validation = {
      -- whether this value is valid, depends on valueOne
      dependsOn = {"problem.valueOne"},
      -- validation function:
      -- arguments are: function(this value, range of this value, other value)
      -- TODO check whether thatis flexible enough
      eval      = function(v, range, other1) return v < other1 end
    },

    --value visibility (only relevant for ui)
    visibility = {
      -- just like validation, visibility can depend on other values
      dependsOn = {},
      -- function that controls visibility
      -- (in this case the value is always visible)
      eval      = function() return true end
    }
  }
}
```

Wow, was soll denn das?!? Das ist ja super lang! Ja, das stimmt ;) Aber es lässt sich bei geschickter Definition von Default-Werten an sehr vielen Stellen Code sparen. Außerdem muss man diese Spezifikation nur einmal schreiben. Man könnte auch Profile mit Defaults einführen, was den Schreibaufwand weiter verkürzt.

###Variable Typen & Parametersätze###

Um Variablen abzubilden, die mehrere Typen erlauben, können pro Parameter mehrere Alternativen angegeben werden, die dann, im Falle einer statisch generierten API, auf mehrere Methodensignaturen abgebildet werden.

Angenommen, es sollen folgende Beispiele mit einer Spezifikation validiert werden:

**Beispiel 1**
```lua
problem = {
	valueOne = 3,
    valueTwo = "abc"
}
```

und

**Beispiel 2**
```lua
problem = {
	valueOne = {
    	subParam1 = 3.4,
        subParam2 = "def"
    },
    valueTwo = "abc"
}
```

Dann sähe die zugehörige Spezifikation so aus:

```lua
problem = {
  -- validation for problem.valueOne
  valueOne = {
    -- option 1
    {
        type = "Integer"
    },
    -- option 2
    {
        subParam1 = {
        	type = "Double"
        },
        subParam2 = {
        	type = "String"
        }
    }
  },
  -- validation for problem.valueTwo
  valueTwo = {
    type = "String"
  }
}
```

### Wie lässt sich Schreibaufwand einsparen? ###

Wie schon angesprochen, kann die Spezifikation sehr lang werden und verliert damit den Dokumentationscharakter mit steigender Komplexität. Daher hier einige simple Vorschläge zur Komplexitätsreduktion:

Wiederkehrende Muster lassen sich recht einfach durch wiederverwendbare Funktionen zur Generierung der Spezifikation zusammenfassen. Möchte man beispielsweise nahezu identische Parameterangaben machen, kann man das folgendermaßen verkürzt ausdrücken:

Man definiert sich zunächt einen Satz Methoden, die mit geeigneter Parameterisierung versehen werden. Diese liefern die entsprechenden Teilspezifikationen zurück.


**Wiederkehrende Muster 1**
```lua
-- recurrent pattern
function myNumber(defaultV, ttip, mn, mx)
  return {
    type     = "Double",
    default  = defaultV,
    style    = "default",
    tooltip  = ttip,

    -- supported value range
    range = {
      min = mn,
      max = mx
    },

    -- validation
    validation = {
      dependsOn = {},
      eval      = function(v,range) return v >= range.min and v <= range.max end
    },

    --value visibility (only relevant for ui)
    visibility = {
      dependsOn = {},
      eval      = function() return true end
    }
  }
end
```

Jetzt können in der Spezifikation selbst diese Methoden eingesetzt werden, was sehr viel Code einspart:

**Wiederkehrende Muster 2**
```lua
-- the actual specification
problem = {
	valueOne = myNumber(3.3 , "...",   1.4, 20.0),
    valueTwo = myNumber(8.8,  "...", -10.0, 10.0)
}
```

Man kann sich jetzt überlegen, ob diese Methoden in einen eigenen Namespace ausgelagert werden sollen. Für einige Muster, wie sie in tieferliegenden Util-Skripten vorkommen, ist das sicherlich sinnvoll. Voraussetzung für eine sinnvolle Nutzung ist natürlich, dass diese Hilfsmethoden sauber dokumentiert werden.

Wichtig hierbei ist, dass am Ende, also nach der Auswertung der Spezifikation inklusive der Hilfsmethoden eine *flache* Spezifikation herauskommt, die ohne besonders komplexe Extra-Regeln auskommt (siehe Listing "**Skizze**"). Dadurch wird die anschließende Code-Generierung erheblich vereinfacht, da bei zu vielen impliziten Sonderregeln diese eine hohe Redundanz und Komplexität in jedem der möglichen Codegeneratoren hervorruft.

###Beispielprojekt###

Im Ordner `VRL-LuaValidation` befindet sich ein Java-Projekt, mit dessen Hilfe der Spezifikationsentwurf entwickelt wurde. Die vorgeschlagene Spezifikationssprache lässt sich damit einlesen und steht als rudimentäre Datenstruktur zur Weiterverarbeitung bereit. Die Code-Generierung selbst ist nicht implementiert.

#### Voraussetzungen: ####

**Benötigte Software**

- Linux, Windows oder OS X
- JDK >= 1.8
- Internetverbindung (weitere Abhängigkeiten werden automatisch heruntergeladen)

**Optionale Software**

- IDE mit Gradle-Plugin (NetBeans, Eclipse, IntelliJ, Sublime)

#### Kompilieren und Ausführen ####

Entwerder direkt über eine IDE ausführen. Oder per Commandline ins Verzeichnis navigieren und folgendes ausführen:

**Unix Shell**
```bash
sh ./gradlew run
```

**CMD (Windows)**
```bash
gradlew.bat run
```

**Power Shell (Windows)**
```bash
.\gradlew run
```

> **Anmerkung:** wurde unter Windows sowohl ein Browser-JRE, als auch ein Entwickler-JDK installiert, ist es hilfreich, die Umgebungsvariable `JAVA_HOME` zu definieren und auf das Basisverzeichnis des zu verwendenden JDKs zu setzen.

## TODO ##

Hier können Ergänzungen, wie z.B. Vorschläge zur verkürzten Schreibweise, angehängt werden.

### Verkürzungsregeln ###

Für sehr häufug verwendete Muster wären dennoch einfache vordefinierte Verkürzungsregeln denkbar, sofern sie sich leicht implementieren lassen.

**TODO: Ideen dazu bitte hier anhängen!**

### Wie definiert man eigene Typen? ###

**TODO: Ideen dazu bitte hier anhängen!**

### Weitere TODOs und Anmerkungen ###


