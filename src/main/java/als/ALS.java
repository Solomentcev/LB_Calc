package als;

import java.io.Serializable;
import java.util.*;
import java.util.List;

public class ALS implements Serializable {
    private Project parentProject;
    private String name;
    private String description;
    private int height;
    private int depth;
    private int width;
    private int upperFrame;
    private int bottomFrame;
    private int depthCell;
    private int countCells;
    private LC lc;
    private PositionLC positionLC;
    private Colors colorDoor;
    private Colors colorBody;

    private List<LB> lbList;
    private Map<LB, Integer> uniqueLB;
    public Map<LB, Integer> getUniqueLB() {
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ALS)) return false;
        ALS als = (ALS) o;
        return getHeight() == als.getHeight() && getDepth() == als.getDepth() && getWidth() == als.getWidth() && getUpperFrame() == als.getUpperFrame() && getBottomFrame() == als.getBottomFrame() && getDepthCell() == als.getDepthCell() && getCountCells() == als.getCountCells()  && Objects.equals(getLc(), als.getLc()) && (getLbList().size()==als.getLbList().size());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getParentProject(), getName(), getHeight(), getDepth(), getWidth(), getUpperFrame(), getBottomFrame(), getDepthCell(), getCountCells(), getLc(), getLbList());
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
  public ALS(Project project){
        parentProject=project;
        height=1940;
        depth=500;
        upperFrame=50;
        bottomFrame=50;
        depthCell=depth-20;
        lc=new LC(height,depth);
        colorBody=Colors.BLUE;
        colorDoor=Colors.White;
        width = width +lc.getWidth();
        lc.setParentALS(this);
        lbList=new ArrayList<>();
        uniqueLB=new HashMap<>();
        positionLC=PositionLC.CENTER;
       OpenDoorDirection openDoorDirection = null;
       if (positionLC == PositionLC.LEFT || positionLC == PositionLC.CENTER) openDoorDirection=OpenDoorDirection.RIGHT;
          else if (positionLC == PositionLC.RIGHT) openDoorDirection=OpenDoorDirection.LEFT;
        LB lb=new LB(5,this, openDoorDirection);
        countCells = countCells + lb.getCountCells();
        lbList.add(lb);
        uniqueLB.put(lb,1);
        System.out.println("СОЗДАНА АКХ:"+getName());
      for (Map.Entry<LB,Integer> lb1:uniqueLB.entrySet()){
          System.out.println(lb1.getKey().getName()+" - "+lb1.getValue()+" шт.");
      }
    }

    public LB addLb(){
        OpenDoorDirection openDoorDirection = null;
        if (positionLC == PositionLC.LEFT || positionLC == PositionLC.CENTER) openDoorDirection=OpenDoorDirection.RIGHT;
        else if (positionLC == PositionLC.RIGHT) openDoorDirection=OpenDoorDirection.LEFT;
        LB lb=new LB(lbList.get(lbList.size()-1).getCountCells(), this,openDoorDirection);
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
        name=getName();
        description=getDescription();
        getUniqueLB();
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

    public Project getParentProject() {
        return parentProject;
    }

    public void setParentProject(Project parentProject) {
        this.parentProject = parentProject;
    }

    public String getName() {
        return name="АКХ на "+ countCells +" ячеек.";
    }

    public String getDescription() {
        description="АКХ на "+ countCells +" ячеек, ВхШхГ,мм: "+height+"x"+ width +"x"+depth+".";
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        try {
            for (LB lb:lbList){
               lb.setHeightLB(height);
            }
            lc.setHeight(height);
            this.height = height;
            System.out.println("ИЗМЕНЕНА Высота АКХ на :"+getHeight());
        } catch (DimensionException e) {
            System.out.println(e.getMessage());
    }
    }
    public int getWidth() {
        width =lc.getWidth();
        for(LB lb:lbList){
            width = width +lb.getWidth();
        }
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getDepth() {
        return depth;
    }
    public void setDepth(int depth) throws DimensionException{
        try {
            lc.setDepth(depth);
            for (LB lb:lbList){
              lb.setDepth(depth);
            }
            this.depth = depth;
            depthCell=depth-20;
            System.out.println("ИЗМЕНЕНА Глубина АКХ на :"+getDepth());
        } catch (DimensionException e) {
            System.out.println(e.getMessage());
    }
    }
    public int getCountCells() {
        countCells=0;
        for(LB lb:lbList){
            countCells=countCells+lb.getCountCells();
        }
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
    public Map<String,String> getInfoAls(){
        Map<String, String> alsInfo=new HashMap<>();
        alsInfo.put("name",name);
        alsInfo.put("count_lb",String.valueOf(lbList.size())+1);
        alsInfo.put("count_cells",String.valueOf(countCells));
        alsInfo.put("height",String.valueOf(height));
        alsInfo.put("weight",String.valueOf(width));
        alsInfo.put("depth",String.valueOf(depth));
        alsInfo.put("upper_frame",String.valueOf(upperFrame));
        alsInfo.put("bottom_frame",String.valueOf(bottomFrame));
        alsInfo.put("depth_cell",String.valueOf(depthCell));
        return alsInfo;
    }
    public List<Map<String,String>> getInfoLbAls(){
        List<Map<String, String>> infoLbAls=new ArrayList<>();
        for(LB lb:lbList){
            infoLbAls.add(lb.getInfoLB());
        }
        return infoLbAls;
    }
    public void updateALS(){
        getWidth();
        getCountCells();
        getName();
        getDescription();
        getUniqueLB();
        System.out.println("ИЗМЕНЕНЫ размеры АКХ");
    }

    public void deleteLB(LB lb) {
        getUniqueLB();
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
        System.out.println("Расположение МУ: "+positionLC);
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
}
