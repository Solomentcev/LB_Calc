package als;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;
@XmlRootElement(name = "ALS")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "ALS")
public class ALS implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ALS.class);
    @XmlTransient
    @JsonIgnore
    private Project parentProject;
    private int id;
    @XmlAttribute(name="nameALS")
    private String name;
    private String description;
    private int height;
    private int depth;
    private int width;
    private int upperFrame;
    private int bottomFrame;
    private int depthCell;
    private int countCells;
    @XmlElement(name="LC",type = LC.class)
    private LC lc;
    private PositionLC positionLC;
    private Colors colorDoor;
    private Colors colorBody;
    @XmlElementWrapper(name="LBList")
    @XmlElement(name="LB",type = LB.class)
    private List<LB> lbList=new ArrayList<>();

    @XmlTransient
    @JsonIgnore
    private final Map<LB, Integer> uniqueLB=new HashMap<>();
    public ALS(){}

    public ALS( Project project){
        parentProject=project;
        height=1940;
        depth=500;
        upperFrame=50;
        bottomFrame=50;
        depthCell=depth-20;
        colorBody=Colors.Blue;
        colorDoor=Colors.White;
        lc=new LC(height,this,depth);
        lc.setParentALS(this);

        width = width +lc.getWidth();
        positionLC=PositionLC.CENTER;
        DirectionDoorOpening directionDoorOpening =DirectionDoorOpening.LEFT;
        LB lb=new LB(5,this, directionDoorOpening);
        lb.setParentALS(this);
        width=width+lb.getWidth();
        countCells = countCells + lb.getCountCells();
        lbList.add(lb);
        uniqueLB.put(lb,1);
        updateName();
        logger.info("СОЗДАНА АКХ:"+getName());
    }
    public LB addLb(){
        DirectionDoorOpening directionDoorOpening = null;
        if (positionLC == PositionLC.LEFT || positionLC == PositionLC.CENTER) directionDoorOpening=DirectionDoorOpening.RIGHT;
        else if (positionLC == PositionLC.RIGHT) directionDoorOpening=DirectionDoorOpening.LEFT;
        LB lb=new LB(lbList.get(lbList.size()-1).getCountCells(), this,directionDoorOpening);
        lb.setParentALS(this);
        lbList.add(lb);
        if (positionLC == PositionLC.CENTER) {
            for (int i = 0; i < lbList.size(); i++) {
                if (i<lbList.size()/2) {
                    directionDoorOpening=DirectionDoorOpening.LEFT;
                    lbList.get(i).setDirectionDoorOpening(directionDoorOpening);
                }else directionDoorOpening=DirectionDoorOpening.RIGHT;
                lbList.get(i).setDirectionDoorOpening(directionDoorOpening);
            }
        }

        countCells = countCells + lb.getCountCells();
        width = width + lb.getWidth();
        updateName();
        updateDescription();
        updateUniqueLB();
        if (uniqueLB.containsKey(lb)){
            Integer i=uniqueLB.get(lb);
            i=i+1;
            uniqueLB.put(lb,i);
            logger.info("Добавлен еще один "+lb.getDescription());
        } else {uniqueLB.put(lb,1);
            logger.info("Добавлен уникальный "+lb.getDescription());}
        logger.info("ДОБАВЛЕН в АКХ: "+lb.getName());
        return lb;
    }
    @XmlTransient
    public Map<LB, Integer> getUniqueLB() {
        return uniqueLB;
    }

    public Map<LB, Integer> updateUniqueLB() {
        uniqueLB.clear();
        for(LB lb:lbList){
            if (uniqueLB.containsKey(lb)){
                Integer i=uniqueLB.get(lb);
                i=i+1;
                uniqueLB.put(lb,i);
            } else uniqueLB.put(lb,1);
        }
        return uniqueLB;
    }
    public int getDepthCell() {
        return depthCell;
    }
    public void setDepthCell(int depthCell) {
        this.depthCell = depthCell;
    }
    public void setCountCells(int countCells) {
        this.countCells = countCells;
    }
    public void setLc(LC lc) {
        this.lc = lc;
    }
    public LC getLc() {
        return lc;
    }
    @XmlTransient
    public Project getParentProject() {
        return parentProject;
    }

    public void setParentProject(Project parentProject) {
        this.parentProject = parentProject;
    }

    public String getName() {
        return name;
    }
    public String updateName() {
        return name="АКХ на "+ countCells +" ячеек.";
    }
    public String getDescription() {
        return description;
    }
    public String updateDescription() {
        return description="АКХ на "+ countCells +" ячеек, ВхШхГ,мм: "+height+"x"+ width +"x"+depth+".";
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
        try {
            lc.changeHeight(height);
            for (LB lb:lbList){
                lb.setHeightLB(height);
            }
            this.height = height;
            updateName();
            updateDescription();
            logger.info("ИЗМЕНЕНА Высота АКХ на :"+getHeight());
        } catch (DimensionException e) {
            throw new DimensionException(e.getMessage());
        }

    }
    public int updateWidth() {
        width =lc.getWidth();
        for(LB lb:lbList){
            width = width +lb.getWidth();
        }

        return width;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getDepth() {
        return depth;
    }
    public void setDepth(int depth){
       this.depth=depth;
    }
    public void changeDepth(int depth) throws DimensionException {
            try {
                lc.changeDepth(depth);
                for (LB lb:lbList){
                    lb.setDepth(depth);
                }
                this.depth = depth;
                depthCell=depth-20;
                updateName();
                updateDescription();
                logger.info("ИЗМЕНЕНА Глубина АКХ на :"+getDepth());
            } catch (DimensionException e) {
                throw new DimensionException(e.getMessage());
            }

    }
    public int updateCountCells() {
        countCells=0;
        for(LB lb:lbList){
            countCells=countCells+lb.getCountCells();
        }
        return countCells;
    }
    public int getCountCells() {
        return countCells;
    }
    public List<LB> getLbList() {
        return lbList;
    }

    public void setLbList(List<LB> lbList) {
        this.lbList = lbList;
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
    public void updateALS() {
        updateWidth();
        updateCountCells();
        updateName();
        updateDescription();
        updateUniqueLB();
        logger.info("ИЗМЕНЕНЫ размеры АКХ");
    }

    public void deleteLB(LB lb) {
        updateUniqueLB();
        for (Map.Entry<LB,Integer> lb1:uniqueLB.entrySet()){
            Integer i=lb1.getValue();
            if (lb.equals(lb1.getKey())){
                if (lb1.getValue()==1) {
                    uniqueLB.remove(lb);
                    break;}
                else {
                    i=i-1;
                    uniqueLB.put(lb,i);
                }
            }
        }
        lbList.remove(lb);
        logger.info("УДАЛЕН: "+lb.getName() );
        updateALS();
    }

    public PositionLC getPositionLC() {
        return positionLC;
    }

    public void setPositionLC(PositionLC positionLC) {
        this.positionLC = positionLC;
        for (int i = 0; i < lbList.size() ; i++) {
            if (positionLC==PositionLC.LEFT) {
                lbList.get(i).setDirectionDoorOpening(DirectionDoorOpening.RIGHT);
            } else if ((positionLC==PositionLC.CENTER && i<lbList.size()/2) || positionLC==PositionLC.RIGHT)
                lbList.get(i).setDirectionDoorOpening(DirectionDoorOpening.LEFT);
        }
        updateALS();
    }

    public Colors getColorDoor() {
        return colorDoor;
    }

    public void setColorDoor(Colors colorDoor) {
        this.colorDoor = colorDoor;
        for (LB lb:lbList){
            lb.setColorDoor(colorDoor);
        }
    }

    public Colors getColorBody() {
        return colorBody;
    }

    public void setColorBody(Colors colorBody) {
        this.colorBody = colorBody;
        lc.setColorBody(colorBody);
        for (LB lb:lbList){
            lb.setColorBody(colorBody);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ALS)) return false;
        ALS als = (ALS) o;
        return getHeight() == als.getHeight() && getDepth() == als.getDepth() && getWidth() == als.getWidth() && getUpperFrame() == als.getUpperFrame() && getBottomFrame() == als.getBottomFrame() && getDepthCell() == als.getDepthCell() && getCountCells() == als.getCountCells() && Objects.equals(getLc(), als.getLc()) && getPositionLC() == als.getPositionLC() && getColorDoor() == als.getColorDoor() && getColorBody() == als.getColorBody() && Objects.equals(getUniqueLB(), als.getUniqueLB());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHeight(), getDepth(), getWidth(), getUpperFrame(), getBottomFrame(), getDepthCell(), getCountCells(), getLc(), getPositionLC(), getColorDoor(), getColorBody(), getLbList(), getUniqueLB());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
