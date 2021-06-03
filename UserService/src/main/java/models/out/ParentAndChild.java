package models.out;

public class ParentAndChild {
    int parentId;
    int childId;

    public ParentAndChild(int parentId, int childId) {
        this.parentId = parentId;
        this.childId = childId;
    }

    public int getParentId() {
        return parentId;
    }

    public int getChildId() {
        return childId;
    }

    @Override
    public String toString() {
        return "ParentAndChild{" +
                "parentId=" + parentId +
                ", childId=" + childId +
                '}';
    }
}
