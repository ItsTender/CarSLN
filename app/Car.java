public class Car
{
    private String Manufacturer;
    private String Model;
    private String Engine;
    private Int BHP;

    public Car() {
    }

    public Car(String manufacturer, String model, String engine, Int BHP) {
        Manufacturer = manufacturer;
        Model = model;
        Engine = engine;
        this.BHP = BHP;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getEngine() {
        return Engine;
    }

    public void setEngine(String engine) {
        Engine = engine;
    }

    public Int getBHP() {
        return BHP;
    }

    public void setBHP(Int BHP) {
        this.BHP = BHP;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Car{" +
                "Manufacturer='" + Manufacturer + '\'' +
                ", Model='" + Model + '\'' +
                ", Engine='" + Engine + '\'' +
                ", BHP=" + BHP +
                '}';
    }
}
