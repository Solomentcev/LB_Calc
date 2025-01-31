package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Objects;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LB")
@JsonRootName(value = "LB")
public class LB implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(LB.class);
    @XmlTransient
    @JsonIgnore
    private ALS parentALS;
    private int id;
    @XmlAttribute(name="nameLB")
    private String name;
    private String description;
    private TypeLb type=TypeLb.TYPE1;
    private int height;
    private int width;
    private int depth;
    private int upperFrame;
    private int bottomFrame;
    private int shelfThick;
    private int countCells;
    private double heightCell;
    private int widthCell;
    private int depthCell;
    private DirectionDoorOpening directionDoorOpening;
    private Colors colorDoor;
    private Colors colorBody;
    public LB(){}

    public LB(int numCells, ALS als,DirectionDoorOpening directionDoorOpening) {
        setParentALS(als);
        countCells = numCells;
        height=als.getHeight();
        depth=als.getDepth();
        upperFrame=als.getUpperFrame();
        bottomFrame=als.getBottomFrame();
        this.countCells =numCells;
        width =420;
        type=TypeLb.TYPE1;
        shelfThick=type.getShelfThick();
        widthCell = width -type.getDeltaWidth();
        depthCell=depth-20;
        heightCell = (height - upperFrame - bottomFrame - ((numCells - 1) * shelfThick)) / countCells;
        this.directionDoorOpening = directionDoorOpening;
        colorBody=als.getColorBody();
        colorDoor=als.getColorDoor();
        updateName();
        updateDescription();
        logger.info("Создан: "+name);
    }
    @XmlTransient
    public ALS getParentALS() {
        return parentALS;
    }
    public void setParentALS(ALS parentALS) {
        this.parentALS = parentALS;
    }
    public int getHeight() {
        return height;
    }
    public int getDepth() {
        return depth;
    }

    public int getUpperFrame() {
        return upperFrame;
    }
    public void setUpperFrame(int upperFrame) {
        this.upperFrame = upperFrame;
    }
    public int getBottomFrame() {
        return bottomFrame;
    }
    public void setBottomFrame(int bottomFrame) {
        this.bottomFrame = bottomFrame;
    }
    public int getHeightLB() {
        return height;
    }
    public String getName() {
        return name;
    }
    public String updateName() {
        return name="Модуль хранения на "+countCells+" ячеек.";
    }
    public String getDescription() {
        return description;
    }
    public String updateDescription() {
        return description="Модуль хранения на "+countCells+" ячеек тип-"+type+" ("+heightCell+"x"+ widthCell +"x"+depthCell+")," +
                " ВхШхГ,мм: "+height+"x"+ width +"x"+depth+".";
    }
    public void setHeightLB(int height) throws DimensionException{
        if (height>2300){
            throw new DimensionException("Высота модуля больше допустимой");
        }
        if (((height-upperFrame-bottomFrame-(countCells-1)*shelfThick)/ countCells)<85 ){
            throw new DimensionException("Высота ячейки меньше допустимой");
        }
        this.height = height;
        heightCell=(height-upperFrame-bottomFrame-(countCells -1)*shelfThick)/ countCells;
        updateName();
        updateDescription();
    }
    public void setHeightCell(double heightCell) {
        this.heightCell = heightCell;
    }
    public void setCountCells(int countCells){
       this.countCells=countCells;
    }
    public void changeCountCells(int countCells) throws DimensionException {
        if (countCells>0 & (height-upperFrame-bottomFrame-(countCells -1)*shelfThick)/ countCells>85) {
            this.countCells = countCells;
            logger.info("Изменено кол-во ячеек в модуле хранения №"+(parentALS.getLbList().indexOf(this)+1)+" на " + countCells);
            heightCell = (height - upperFrame - bottomFrame - (countCells - 1) * shelfThick) / countCells;
            updateName();
            updateDescription();
        } else throw new DimensionException("Слишком большое количество ячеек(Высота ячейки меньше допустимой)");
    }
    public void setWidth(int width) throws DimensionException {
        if ( (width>1200))
            throw new DimensionException("Ширина модуля больше допустимой");
        if ( (width-type.getDeltaWidth())<100 )
            throw new DimensionException("Ширина ячейки меньше допустимой");
        this.width = width;
        logger.info("Изменена ширина модуля на:"+ width + " мм");
        widthCell = width -type.getDeltaWidth();
        logger.info("Изменена ширина ячеек на:"+ widthCell + " мм");
        updateName();
        updateDescription();
       // parentALS.updateALS();
    }
    public void setWidthCell(int widthCell) throws DimensionException {
        if ( (widthCell+type.getDeltaWidth()>1200) )
            throw new DimensionException("Ширина ячейки больше допустимой");
        if ( widthCell<100 )
            throw new DimensionException("Ширина ячейки меньше допустимой");
        this.widthCell = widthCell;
        logger.info("ИЗМЕНЕНА ширина ячеек до:"+ widthCell + " мм");
        width = widthCell+type.getDeltaWidth();
        logger.info("ИЗМЕНЕНА ширина модуля до:"+ width + " мм");
        updateName();
        updateDescription();

    }
    public void setDepthCell(int depthCell) throws DimensionException {
        if ((depthCell+20)<150)
            throw new DimensionException("Глубина модуля хранения меньше допустимой");
        if ((depthCell+20)>900)
            throw new DimensionException("Глубина модуля хранения больше допустимой");
        this.depthCell = depthCell;
        depth = depthCell + 20;
        updateName();
        updateDescription();
    }
    public void setDepth(int depth) throws DimensionException {
        if (depth>900)
            throw new DimensionException("Глубина модуля хранения больше допустимой");
        if (depth<150)
            throw new DimensionException("Глубина модуля хранения меньше допустимой");
        this.depth = depth;
        depthCell = depth - 20;
        updateName();
        updateDescription();
    }
    public void setType(String type){
       this.type= TypeLb.valueOf(type);
    }
    public void changeType(String type) throws DimensionException {
        if (((height-upperFrame-bottomFrame-(countCells-1)*TypeLb.valueOf(type).getShelfThick())/ countCells)<85 )
            throw new DimensionException("Высота ячейки меньше допустимой");
        if ( width-TypeLb.valueOf(type).getDeltaWidth()<100 )
            throw new DimensionException("Ширина ячейки меньше допустимой");
        this.type = TypeLb.valueOf(type);
        shelfThick= this.type.getShelfThick();
        widthCell=width-this.type.getDeltaWidth();
        logger.info("ИЗМЕНЕН тип модуля на: "+type);
        heightCell=(height-upperFrame-bottomFrame-(countCells -1)*shelfThick)/ countCells;
        updateName();
        updateDescription();
    }

    public int getShelfThick() {
        return shelfThick;
    }
    public int getCountCells() {
        return this.countCells;
    }
    public double getHeightCell() {
        return heightCell;
    }
    public int getWidthCell() {
        return widthCell;
    }
    public int getWidth() {
        return width;
    }
    public int getDepthCell() {
        return depthCell;
    }
    public TypeLb getType() {
        return type;
    }

    public DirectionDoorOpening getDirectionDoorOpening() {
        return directionDoorOpening;
    }

    public void setDirectionDoorOpening(DirectionDoorOpening directionDoorOpening) {
        this.directionDoorOpening = directionDoorOpening;
    }
    public Colors getColorDoor() {
        return colorDoor;
    }

    public void setColorDoor(Colors colorDoor) {
        this.colorDoor = colorDoor;
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
        if (!(o instanceof LB)) return false;
        LB lb = (LB) o;
        return getHeight() == lb.getHeight() && getWidth() == lb.getWidth() && getDepth() == lb.getDepth() && getUpperFrame() == lb.getUpperFrame() && getBottomFrame() == lb.getBottomFrame() && getShelfThick() == lb.getShelfThick() && getCountCells() == lb.getCountCells() && Double.compare(lb.getHeightCell(), getHeightCell()) == 0 && getWidthCell() == lb.getWidthCell() && getDepthCell() == lb.getDepthCell() && getType() == lb.getType() && getDirectionDoorOpening() == lb.getDirectionDoorOpening();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getHeight(), getWidth(), getDepth(), getUpperFrame(), getBottomFrame(), getShelfThick(), getCountCells(), getHeightCell(), getWidthCell(), getDepthCell(), getDirectionDoorOpening());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
