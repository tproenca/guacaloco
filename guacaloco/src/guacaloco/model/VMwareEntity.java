package guacaloco.model;

import java.util.ArrayList;
import java.util.List;

public abstract class VMwareEntity implements IVMwareEntity {

    private IVMwareEntity parent;
    protected List<IVMwareEntity> children = new ArrayList<IVMwareEntity>();
    
    public VMwareEntity(IVMwareEntity parent) {
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
}
