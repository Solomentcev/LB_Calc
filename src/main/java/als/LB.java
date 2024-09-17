package als;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LB implements Serializable {
    private ALS parentALS;
    private String name;
    private String description;
    private TypeLb type;
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
    private OpenDoorDirection openDoorDirection;
    public LB(int numCells,ALS als, OpenDoorDirection openDoorDirection) {
        setParentALS(als);
        this.countCells = numCells;
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
        heightCell = (height - upperFrame - bottomFrame - ((numCells - 1) * shelfThick)) / numCells;
        name=getName();
        description=getDescription();
        this.openDoorDirection=openDoorDirection;
        System.out.println("Создан: "+name);
    }
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
        name="Модуль хранения на "+countCells+" ячеек.";
        return name;
    }
    public String getDescription() {
        description="Модуль хранения на "+countCells+" ячеек тип-"+type+" ("+heightCell+"x"+ widthCell +"x"+depthCell+")," +
                " ВхШхГ,мм: "+height+"x"+ width +"x"+depth+".";
        return description;
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
        name=getName();
        description=getDescription();
        parentALS.updateALS();
    }
    public void setHeightCell(double heightCell) {
        this.heightCell = heightCell;
    }
    public void setCountCells(int countCells) throws DimensionException {
        if (countCells>0 & (height-upperFrame-bottomFrame-(countCells -1)*shelfThick)/ countCells>85) {
            this.countCells = countCells;
            System.out.println("Изменено кол-во ячеек в модуле хранения №"+(parentALS.getLbList().indexOf(this)+1)+" на " + countCells);
            heightCell = (height - upperFrame - bottomFrame - (countCells - 1) * shelfThick) / countCells;
            name=getName();
            description=getDescription();
        } else throw new DimensionException("Слишком большое количество ячеек(Высота ячейки меньше допустимой)");
        parentALS.updateALS();
    }
    public void setWidth(int width) throws DimensionException {
        if ( (width>1200))
            throw new DimensionException("Ширина модуля больше допустимой");
        if ( (width-type.getDeltaWidth())<100 )
            throw new DimensionException("Ширина ячейки меньше допустимой");
        this.width = width;
        System.out.println("Изменена ширина модуля на:"+ width + " мм");
        widthCell = width -type.getDeltaWidth();
        System.out.println("Изменена ширина ячеек на:"+ widthCell + " мм");
        name=getName();
        description=getDescription();
        parentALS.updateALS();
    }
    public void setWidthCell(int widthCell) throws DimensionException {
        if ( (widthCell+type.getDeltaWidth()>1200) )
            throw new DimensionException("Ширина ячейки больше допустимой");
        if ( widthCell<100 )
            throw new DimensionException("Ширина ячейки меньше допустимой");
        this.widthCell = widthCell;
        System.out.println("ИЗМЕНЕНА ширина ячеек до:"+ widthCell + " мм");
        width = widthCell+type.getDeltaWidth();
        System.out.println("ИЗМЕНЕНА ширина модуля до:"+ width + " мм");
        name=getName();
        description=getDescription();
        parentALS.updateALS();

    }
    public void setDepthCell(int depthCell) throws DimensionException {
        if ((depthCell+20)<150)
            throw new DimensionException("Глубина модуля хранения меньше допустимой");
        if ((depthCell+20)>900)
            throw new DimensionException("Глубина модуля хранения больше допустимой");
        this.depthCell = depthCell;
        depth = depthCell + 20;
        name=getName();
        description=getDescription();
    }
    public void setDepth(int depth) throws DimensionException {
        if (depth>900)
            throw new DimensionException("Глубина модуля хранения больше допустимой");
        if (depth<150)
            throw new DimensionException("Глубина модуля хранения меньше допустимой");
        this.depth = depth;
        depthCell = depth - 20;
        name=getName();
        description=getDescription();
    }
    public void setType(String type) throws DimensionException {
        if (((height-upperFrame-bottomFrame-(countCells-1)*TypeLb.valueOf(type).getShelfThick())/ countCells)<85 )
            throw new DimensionException("Высота ячейки меньше допустимой");
        if ( width-TypeLb.valueOf(type).getDeltaWidth()<100 )
            throw new DimensionException("Ширина ячейки меньше допустимой");
        this.type = TypeLb.valueOf(type);
        shelfThick= this.type.getShelfThick();
        widthCell=width-this.type.getDeltaWidth();
        System.out.println("ИЗМЕНЕН тип модуля на: "+type);
        heightCell=(height-upperFrame-bottomFrame-(countCells -1)*shelfThick)/ countCells;
        name=getName();
        description=getDescription();
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

    public OpenDoorDirection getOpenDoorDirection() {
        return openDoorDirection;
    }

    public void setOpenDoorDirection(OpenDoorDirection openDoorDirection) {
        this.openDoorDirection = openDoorDirection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LB)) return false;
        LB lb = (LB) o;
        return getHeight() == lb.getHeight() && getWidth() == lb.getWidth() && getDepth() == lb.getDepth() && getUpperFrame() == lb.getUpperFrame() && getBottomFrame() == lb.getBottomFrame() && getShelfThick() == lb.getShelfThick() && getCountCells() == lb.getCountCells() && Double.compare(lb.getHeightCell(), getHeightCell()) == 0 && getWidthCell() == lb.getWidthCell() && getDepthCell() == lb.getDepthCell() && getType() == lb.getType() && getOpenDoorDirection() == lb.getOpenDoorDirection();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getHeight(), getWidth(), getDepth(), getUpperFrame(), getBottomFrame(), getShelfThick(), getCountCells(), getHeightCell(), getWidthCell(), getDepthCell(), getOpenDoorDirection());
    }

    public Map<String,String> getInfoLB(){
        Map<String,String> LBinfo=new HashMap();
        LBinfo.put("name", name);
        LBinfo.put("height",String.valueOf(height));
        LBinfo.put("weight",String.valueOf(width));
        LBinfo.put("depth",String.valueOf(depth));
        LBinfo.put("count_cells",String.valueOf(countCells));
        LBinfo.put("height_cell",String.valueOf(heightCell));
        LBinfo.put("weight_cell",String.valueOf(widthCell));
        LBinfo.put("depth_cell",String.valueOf(depthCell));
        return LBinfo;
    }
}
