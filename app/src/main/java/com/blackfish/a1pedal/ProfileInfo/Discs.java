package com.blackfish.a1pedal.ProfileInfo;

public class Discs {
    private static Discs dataObject = null;

    private Discs() {
        // left blank intentionally
    }

    public static Discs getInstance() {
        if (dataObject == null)
            dataObject = new Discs();
        return dataObject;
    }


    private String dia;
    private String diametr;
    private String psd1;
    private String psd2;
    private String type;
    private String vilet;
    private String width;


    // Getter Methods

    public String getDia() {
        return dia;
    }

    public String getDiametr() {
        return diametr;
    }

    public String getPsd1() {
        return psd1;
    }

    public String getPsd2() {
        return psd2;
    }

    public String getType() {
        return type;
    }

    public String getVilet() {
        return vilet;
    }

    public String getWidth() {
        return width;
    }

    // Setter Methods

    public void setDia(String dia) {
        this.dia = dia;
    }

    public void setDiametr(String diametr) {
        this.diametr = diametr;
    }

    public void setPsd1(String psd1) {
        this.psd1 = psd1;
    }

    public void setPsd2(String psd2) {
        this.psd2 = psd2;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVilet(String vilet) {
        this.vilet = vilet;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
