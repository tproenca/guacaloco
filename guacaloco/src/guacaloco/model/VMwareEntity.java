package guacaloco.model;

import java.util.LinkedHashSet;
import java.util.Set;

import com.vmware.vim25.ObjectContent;

public abstract class VMwareEntity implements IVMwareEntity {

    private IVMwareEntity parent;
    protected Set<IVMwareEntity> children = new LinkedHashSet<IVMwareEntity>();
    private String name;

    private ObjectContent objectContent;

    public VMwareEntity(VMwareEntity parent) {
        this(parent, "");
    }

    public VMwareEntity(VMwareEntity parent, String name) {
        setParent(parent);
        setName(name);
    }

    @Override
    public IVMwareEntity getParent() {
        return parent;
    }

    protected void setParent(IVMwareEntity parent) {
        this.parent = parent;
    }

    @Override
    public IVMwareEntity[] getChildren() {
        return children.toArray(new IVMwareEntity[0]);
    }

    protected void addChildren(VMwareEntity child) {
        children.add(child);
        child.setParent(this);
    }

    protected void removeChildren(VMwareEntity child) {
        children.remove(child);
        child.setParent(null);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectContent getObjectContent() {
        return objectContent;
    }

    public void setObjectContent(ObjectContent objectContent) {
        this.objectContent = objectContent;
    }

}
