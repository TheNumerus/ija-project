/*
soubor: project.map.Stop.java
autoři: Petr Volf (xvolfp00) a David Rubý (xrubyd00)
popis: obsahuje jméno zastávky
        je součástí třídy Node.java
 */

package project.map;

import java.util.Objects;

/**
 * Data class for stops
 */
public class Stop {
    /**
     * Stop name
     */
    public String name;

    /**
     * Constructor for stop
     * @param name stop name
     */
    public Stop(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stop stop = (Stop) o;
        return Objects.equals(name, stop.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
