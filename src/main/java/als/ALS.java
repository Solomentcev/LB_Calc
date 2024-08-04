package als;

import java.io.Serializable;
import java.util.*;

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
    private List<LB> lbList;

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
        width = width +lc.getWidth();
        lc.setParentALS(this);
        lbList=new ArrayList<>();
        LB lb=new LB(5,this);
        countCells = countCells + lb.getCountCells();
        lbList.add(lb);
        System.out.println("СОЗДАНА АКХ:"+getName());
    }

    public LB addLb(){
        LB lb=new LB(lbList.get(lbList.size()-1).getCountCells(), this) ;
        lbList.add(lb);
        countCells = countCells + lb.getCountCells();
        width = width + lb.getWidth();
        System.out.println("ДОБАВЛЕН в АКХ: "+lb.getName());
        name=getName();
        description=getDescription();
        System.out.println(name);
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
        System.out.println("ИЗМЕНЕНЫ размеры АКХ");
    }

    public void deleteLB(LB lb) {
        System.out.printf("УДАЛЕН: "+lb.getName() );
        lbList.remove(lb);
        updateALS();
    }
}
