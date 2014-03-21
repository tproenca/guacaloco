package guacaloco.model;

public class VirtualMachine extends VMwareEntity {
    
    public VirtualMachine(VMwareEntity parent) {
       super(parent);
       parent.addChildren(this);
    }
}
