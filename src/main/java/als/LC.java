package als;

import java.io.Serializable;
import java.util.*;

public class LC implements Serializable {
    private ALS parentALS;
    private String name;
    private String description;
    private int height;
    private int width;
    private int depth;
    private DisplayLC display;
    private BarReader barReader;
    private Payment payment;
    private boolean printer;
    private boolean rfidReader;
    public ALS getParentALS() {
        return parentALS;
    }

    public void setParentALS(ALS parentALS) {
        this.parentALS = parentALS;
    }
    public LC(int height, int depth) {
        this.height=height;
        this.depth=depth;
        name = getName();
        description=getDescription();
        width=300;
        display=(DisplayLC.LC10);
        barReader=BarReader.NONE;
        payment=Payment.NONE;
        printer=false;
        rfidReader=false;
        System.out.println("СОЗДАН: " + name);
    }

    public String getName() {
        name = "Модуль управления.";
        return name;
    }

    public String getDescription() {
        description = "Модуль управления, ВхШхГ,мм: " + height + "x" + width + "x" + depth + ".";
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) throws DimensionException {
        if (height> 2300 )
            throw new DimensionException("Высота модуля управления больше допустимой");
        if (height< 500 )
            throw new DimensionException("Высота модуля управления меньше допустимой");
        this.height = height;
        System.out.println("Изменена высота модуля управления на:"+ height + " мм");
        name = getName();
        description=getDescription();
        parentALS.updateALS();
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) throws DimensionException {
        if (depth<350)
            throw new DimensionException("Глубина модуля управления меньше допустимой");
        if (depth>900)
            throw new DimensionException("Глубина модуля управления больше допустимой");
        this.depth = depth;
        System.out.println("Изменена глубина модуля управления на:"+ depth + " мм");
        name = getName();
        description=getDescription();
        parentALS.updateALS();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) throws DimensionException {
        if (width<300)
            throw new DimensionException("Ширина модуля управления меньше допустимой");
        if (width>600)
            throw new DimensionException("Ширина модуля управления больше допустимой");
        this.width = width;
        System.out.println("Изменена ширина модуля управления на:"+ width + " мм");
        name = getName();
        description=getDescription();
        parentALS.updateALS();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DisplayLC getDisplay() {
        return display;
    }

    public void setDisplay(DisplayLC display) throws DimensionException {
        this.display = display;
        switch (display){
            case NONE, LC10 -> setWidth(300);
            case LC17 -> setWidth(450);
            case LC19 -> setWidth(500);
        }
        System.out.println("Дисплей: "+display);

    }

    public BarReader getBarReader() {
        return barReader;
    }

    public void setBarReader(BarReader barReader) {
        this.barReader = barReader;
        System.out.println("Сканер:"+barReader);
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
        System.out.println("Оплата: "+payment);

    }

    public boolean isPrinter() {
        return printer;
    }

    public void setPrinter(boolean printer) {
        this.printer = printer;
        System.out.println("Принтер: "+printer);
    }

    public boolean isRfidReader() {
        return rfidReader;
    }

    public void setRfidReader(boolean rfidReader) {
        this.rfidReader = rfidReader;
        System.out.println("Cчитыватель: "+ rfidReader);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LC)) return false;
        LC lc = (LC) o;
        return getHeight() == lc.getHeight() && getWidth() == lc.getWidth() && getDepth() == lc.getDepth() && isPrinter() == lc.isPrinter() && isRfidReader() == lc.isRfidReader() && getDisplay() == lc.getDisplay() && getBarReader() == lc.getBarReader() && getPayment() == lc.getPayment();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHeight(), getWidth(), getDepth(), getDisplay(), getBarReader(), getPayment(), isPrinter(), isRfidReader());
    }

    public Map<String,String> getInfoLC(){
        Map<String, String> LCInfo=new HashMap<>();
        LCInfo.put("name",name);
        LCInfo.put("height",String.valueOf(height));
        LCInfo.put("weight",String.valueOf(width));
        LCInfo.put("depth", String.valueOf(depth));
        return LCInfo;
    }
}
