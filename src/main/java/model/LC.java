package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LC")
@JsonRootName(value = "LC")
public class LC implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(LC.class);
    @XmlTransient
    @JsonIgnore
    private ALS parentALS=new ALS();
    private int id;
    @XmlAttribute(name="nameLC")
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
    private Colors colorBody;
    public LC(){
    }
    @XmlTransient
    public ALS getParentALS() {
        return parentALS;
    }
    public void setParentALS(ALS parentALS) {
        this.parentALS = parentALS;
    }
    public LC( int height, ALS als, int depth) {
        setParentALS(als);
        this.height=height;
        this.depth=depth;
        display=(DisplayLC.LC10);
        width=display.getWidth();
        barReader=BarReader.NONE;
        payment=Payment.NONE;
        printer=false;
        rfidReader=false;
        colorBody=als.getColorBody();
        updateName();
        updateDescription();
        logger.info("СОЗДАН: " + name);
    }

    public String getName() {
        return name;
    }
    public String updateName() {
        return name="Модуль управления "+display;
    }

    public String getDescription() {
        return description;
    }
    public String updateDescription() {
        return description="Модуль управления, ВхШхГ,мм: " + height + "x" + width + "x" + depth + ".";
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
       this.height=height;
    }
    public void changeHeight(int height) throws DimensionException {
        if (height> 2300 )
            throw new DimensionException("Высота модуля управления больше допустимой");
        if (height< 500 )
            throw new DimensionException("Высота модуля управления меньше допустимой");
        this.height= height;
        logger.info("Изменена высота модуля управления на:"+ height + " мм");
        updateName();
        updateDescription();
    }


    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth)  {
      this.depth=depth;
    }
    public void changeDepth(int depth) throws DimensionException {
        if (depth<350)
            throw new DimensionException("Глубина модуля управления меньше допустимой");
        if (depth>900)
            throw new DimensionException("Глубина модуля управления больше допустимой");
        this.depth = depth;
        logger.info("Изменена глубина модуля управления на:"+ depth + " мм");
        updateName();
        updateDescription();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) throws DimensionException {
       this.width=width;
    }
    public void changeWidth(int width) throws DimensionException {
        if (width<300)
            throw new DimensionException("Ширина модуля управления меньше допустимой");
        if (width>600)
            throw new DimensionException("Ширина модуля управления больше допустимой");
        this.width = width;
        logger.info("Изменена ширина модуля управления на:"+ width + " мм");
        updateName();
        updateDescription();
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
        updateName();
        updateDescription();
    }

    public BarReader getBarReader() {
        return barReader;
    }

    public void setBarReader(BarReader barReader) {
        this.barReader = barReader;
        updateName();
        updateDescription();
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
        updateName();
        updateDescription();
    }

    public boolean isPrinter() {
        return printer;
    }

    public void setPrinter(boolean printer) {
        this.printer = printer;
        updateName();
        updateDescription();
    }

    public boolean isRfidReader() {
        return rfidReader;
    }

    public void setRfidReader(boolean rfidReader) {
        this.rfidReader = rfidReader;
        updateName();
        updateDescription();
    }

    public Colors getColorBody() {
        return colorBody;
    }

    public void setColorBody(Colors colorBody) {
        this.colorBody = colorBody;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
