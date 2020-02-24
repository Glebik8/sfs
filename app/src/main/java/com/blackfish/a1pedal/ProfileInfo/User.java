package com.blackfish.a1pedal.ProfileInfo;

public class User {

    private static User dataObject = null;

    private User() {
        // left blank intentionally
    }

    public static User getInstance() {
        if (dataObject == null)
            dataObject = new User();
        return dataObject;
    }

    private String pk;
    private String email;
    private String fio;
    private String photo;
    private String type;
    private String name;
    private String gps ;
    private String work;
    private String street ;
    private String timeFrom ;
    private String timeTo ;
    private String weekends ;
    private String site ;
    private String phone;
    private String brand;
    private String gearbox;
    private String miles;
    private String model;
    private String motory_type;
    private String motor_volume;
    private String vin;
    private String number;
    private String privod;
    private String year;
    Tire TireObject;
    Discs DiscsObject;


    // Getter Methods

    public String getPk() {
        return pk;
    }

    public String getEmail() {
        return email;
    }

    public String getFio() {
        return fio;
    }

    public String getPhoto() {
        return photo;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getGps() {
        return gps;
    }

    public String getWork() {
        return work;
    }

    public String getStreet() {
        return street;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public String getWeekends() {
        return weekends;
    }

    public String getSite() {
        return site;
    }

    public String getPhone() {
        return phone;
    }

    public String getBrand() {
        return brand;
    }

    public String getGearbox() {
        return gearbox;
    }

    public String getMiles() {
        return miles;
    }

    public String getModel() {
        return model;
    }

    public String getMotory_type() {
        return motory_type;
    }

    public String getMotor_volume() {
        return motor_volume;
    }

    public String getVin() {
        return vin;
    }

    public String getNumber() {
        return number;
    }

    public String getPrivod() {
        return privod;
    }

    public String getYear() {
        return year;
    }

    public Tire getTire() {
        return TireObject;
    }

    public Discs getDiscs() {
        return DiscsObject;
    }

    // Setter Methods

    public void setPk(String pk) {
        this.pk = pk;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public void setWeekends(String weekends) {
        this.weekends = weekends;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setMotory_type(String motory_type) {
        this.motory_type = motory_type;
    }

    public void setMotor_volume(String motor_volume) {
        this.motor_volume = motor_volume;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPrivod(String privod) {
        this.privod = privod;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setTire(Tire tireObject) {
        this.TireObject = tireObject;
    }

    public void setDiscs(Discs discsObject) {
        this.DiscsObject = discsObject;
    }
}
