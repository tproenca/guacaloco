package guacaloco.model;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class VMwareEntity implements IVMwareEntity {

    private IVMwareEntity parent;
    protected Set<IVMwareEntity> children = new LinkedHashSet<IVMwareEntity>();
    private String name;
    
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
}
