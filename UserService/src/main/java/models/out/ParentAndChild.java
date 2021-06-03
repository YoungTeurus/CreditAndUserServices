package models.out;

public class ParentAndChild {
    long parentId;
    long childId;

    public ParentAndChild(int parentId, int childId) {
        this.parentId = parentId;
        this.childId = childId;
    }

    public long getParentId() {
        return parentId;
    }

    public long getChildId() {
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
