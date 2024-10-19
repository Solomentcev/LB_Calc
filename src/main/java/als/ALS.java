package als;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;
@XmlRootElement(name = "ALS")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "ALS")
public class ALS implements Serializable {
    @XmlTransient
    @JsonIgnore
    private Project parentProject;
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
        colorBody=Colors.BLUE;
        colorDoor=Colors.White;
        lc=new LC(height,this,depth);
        lc.setParentALS(this);

        width = width +lc.getWidth();
        positionLC=PositionLC.CENTER;
        OpenDoorDirection openDoorDirection =OpenDoorDirection.LEFT;
        LB lb=new LB(5,this, openDoorDirection);
        lb.setParentALS(this);
        width=width+lb.getWidth();
        countCells = countCells + lb.getCountCells();
        lbList.add(lb);
        uniqueLB.put(lb,1);
        updateName();
        System.out.println("СОЗДАНА АКХ:"+getName());
    }
    public LB addLb(){
        OpenDoorDirection openDoorDirection = null;
        if (positionLC == PositionLC.LEFT || positionLC == PositionLC.CENTER) openDoorDirection=OpenDoorDirection.RIGHT;
        else if (positionLC == PositionLC.RIGHT) openDoorDirection=OpenDoorDirection.LEFT;
        LB lb=new LB(lbList.get(lbList.size()-1).getCountCells(), this,openDoorDirection);
        lb.setParentALS(this);
        lbList.add(lb);
        if (positionLC == PositionLC.CENTER) {
            for (int i = 0; i < lbList.size(); i++) {
                if (i<lbList.size()/2) {
                    openDoorDirection=OpenDoorDirection.LEFT;
                    lbList.get(i).setOpenDoorDirection(openDoorDirection);
                }else openDoorDirection=OpenDoorDirection.RIGHT;
                lbList.get(i).setOpenDoorDirection(openDoorDirection);
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
            System.out.println("Добавлен еще один "+lb.getDescription());
        } else {uniqueLB.put(lb,1);
            System.out.println("Добавлен уникальный "+lb.getDescription());}
        System.out.println("ДОБАВЛЕН в АКХ: "+lb.getName());
        for (Map.Entry<LB,Integer> lb1:uniqueLB.entrySet()){
            System.out.println(lb1.getKey().getDescription()+" - "+lb1.getValue()+" шт.");
        }
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
    public void changeHeight(){
        try {
            this.height = height;
            for (LB lb:lbList){
                lb.setHeightLB(height);
            }
            lc.changeHeight();
            updateName();
            updateDescription();
            System.out.println("ИЗМЕНЕНА Высота АКХ на :"+getHeight());
        } catch (DimensionException e) {
            System.out.println(e.getMessage());
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
    public void updateDepth(){
            try {
                lc.setDepth(depth);
                for (LB lb:lbList){
                    lb.setDepth(depth);
                }
                this.depth = depth;
                depthCell=depth-20;
                updateName();
                updateDescription();
                System.out.println("ИЗМЕНЕНА Глубина АКХ на :"+getDepth());
            } catch (DimensionException e) {
                System.out.println(e.getMessage());
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
        changeHeight();
        updateWidth();
        updateDepth();
        updateCountCells();
        updateName();
        updateDescription();
        updateUniqueLB();
        System.out.println("ИЗМЕНЕНЫ размеры АКХ");
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

        System.out.printf("УДАЛЕН: "+lb.getName() );
        for (Map.Entry<LB,Integer> lb1:uniqueLB.entrySet()){
            System.out.println(lb1.getKey().getName()+" - "+lb1.getValue()+" шт.");
        }
        updateALS();
    }

    public PositionLC getPositionLC() {
        return positionLC;
    }

    public void setPositionLC(PositionLC positionLC) {
        this.positionLC = positionLC;
        for (int i = 0; i < lbList.size() ; i++) {
            if (positionLC==PositionLC.LEFT) {
                lbList.get(i).setOpenDoorDirection(OpenDoorDirection.RIGHT);
            } else if ((positionLC==PositionLC.CENTER && i<lbList.size()/2) || positionLC==PositionLC.RIGHT)
                lbList.get(i).setOpenDoorDirection(OpenDoorDirection.LEFT);
        }
        updateALS();
        System.out.println("Расположение МУ: "+positionLC);
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
        return getHeight() == als.getHeight() && getDepth() == als.getDepth() && getWidth() == als.getWidth() && getUpperFrame() == als.getUpperFrame() && getBottomFrame() == als.getBottomFrame() && getDepthCell() == als.getDepthCell() && getCountCells() == als.getCountCells() && Objects.equals(getLc(), als.getLc()) && getPositionLC() == als.getPositionLC() && getColorDoor() == als.getColorDoor() && getColorBody() == als.getColorBody() && Objects.equals(getLbList(), als.getLbList()) && Objects.equals(getUniqueLB(), als.getUniqueLB());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHeight(), getDepth(), getWidth(), getUpperFrame(), getBottomFrame(), getDepthCell(), getCountCells(), getLc(), getPositionLC(), getColorDoor(), getColorBody(), getLbList(), getUniqueLB());
    }
}
