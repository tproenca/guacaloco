package guacaloco.model;

import java.util.LinkedHashSet;
import java.util.Set;

import com.vmware.vim25.ObjectContent;

public abstract class VMwareEntity implements IVMwareEntity {

    private IVMwareEntity parent;
    protected Set<IVMwareEntity> children = new LinkedHashSet<IVMwareEntity>();
    private String name;
    private ObjectContent objectContent;

    public VMwareEntity(IVMwareEntity parent) {
        this.parent = parent;
    }

    public VMwareEntity(IVMwareEntity parent, IVMwareEntity child) {
        this.parent = parent;
    }

    @Override
    public IVMwareEntity getParent() {
        return parent;
    }

    @Override
    public IVMwareEntity[] getChildren() {
        return children.toArray(new IVMwareEntity[0]);
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
