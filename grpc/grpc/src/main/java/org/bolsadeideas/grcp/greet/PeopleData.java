package org.bolsadeideas.grcp.greet;

import java.util.Arrays;
import java.util.List;

public class PeopleData {

    public static String DIEGO_SERRANO() { return "Diego Serrano"; }
    public static String SANTIAGO_SERRANO() { return "Santiago Serrano"; }
    public static String LURDITAS() { return "Lurditas"; }

    public static String MANOLITO() { return "Manolito"; }
    public static String SERVER_ENEMY() { return "Server Enemy"; }
    public static List<String> ALL() {
        return Arrays.asList(
                DIEGO_SERRANO(),
                SANTIAGO_SERRANO(),
                LURDITAS(),
                MANOLITO()
        );
    }
    public static List<String> ALL_WITH_ENEMY() {
        return Arrays.asList(
                DIEGO_SERRANO(),
                SANTIAGO_SERRANO(),
                SERVER_ENEMY(),
                LURDITAS(),
                MANOLITO()
        );
    }

}
