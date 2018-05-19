package edu.gcsc.vrl.luaparser;

public class ValProperty {
    /*
    * In dieser Klasse werden die Daten strukturiert gespeichert. Diese Klasse wird benötigt,
    * damit man alle Daten aufeinmal an die CellFactory übergeben kann -> es wird immer ein
    * gewrapptes Objekt von ValProperty an die CellFactory übergeben, welches dann alle
    * Daten enthält, mit denen das UI erstellt werden kann.
    * */
    private String valName = "";
    private String type = "";
    private String defaultVal = "";
    private String style = "";
    private String tooltip = "";
    private double range_min;
    private double range_max;
    private double[] values;
    private boolean visibility;

    // Konstruktoren
    public ValProperty(String valueName, String atype){
        this.valName = valueName;
        this.type = atype;
    }

    public ValProperty(String valueName){
        this.valName = valueName;
    }

    // GETTER - Methoden
    public String getValName() { return this.valName; }

    public String getType() { return this.type; }

    public String getDefaultVal() { return this.defaultVal; }

    public String getStyle() { return this.style; }

    public String getTooltip() { return this.tooltip; }

    public double getRange_min() { return this.range_min; }

    public double getRange_max() { return this.range_max; }

    public double[] getValues() { return this.values; }

    public boolean isVisible() { return this.visibility; }

    // SETTER - Methoden
    public void setType(String type) { this.type = type; }

    public void setDefaultVal(String defaultVal) { this.defaultVal = defaultVal; }

    public void setStyle(String style) { this.style = style; }

    public void setTooltip(String tooltip) { this.tooltip = tooltip; }

    public void setRange_min(double range_min) { this.range_min = range_min; }

    public void setRange_max(double range_max) { this.range_max = range_max; }

    public void setValues(double[] values) { this.values = values; }

    public void setVisibility(boolean visibility) { this.visibility = visibility; }
}
