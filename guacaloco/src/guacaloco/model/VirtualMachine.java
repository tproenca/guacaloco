package guacaloco.model;

public class VirtualMachine extends VMwareEntity {
    
    public VirtualMachine(IVMwareEntity parent) {
       super(parent);
       ((VMwareEntity)parent).children.add(this);
    }
}
