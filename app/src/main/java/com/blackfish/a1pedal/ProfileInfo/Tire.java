package com.blackfish.a1pedal.ProfileInfo;

public class Tire {

    private static Tire dataObject = null;

    private Tire() {
        // left blank intentionally
    }

    public static Tire getInstance() {
        if (dataObject == null)
            dataObject = new Tire();
        return dataObject;
    }

    private String diametr;
    private String height;
    private String runflat;
    private String throns;
    private String type;
    private String index = null;
    private String width;


    // Getter Methods

    public String getDiametr() {
        return diametr;
    }

    public String getHeight() {
        return height;
    }

    public String getRunflat() {
        return runflat;
    }

    public String getThrons() {
        return throns;
    }

    public String getType() {
        return type;
    }

    public String getIndex() {
        return index;
    }

    public String getWidth() {
        return width;
    }

    // Setter Methods

    public void setDiametr(String diametr) {
        this.diametr = diametr;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setRunflat(String runflat) {
        this.runflat = runflat;
    }

    public void setThrons(String throns) {
        this.throns = throns;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
