package guacaloco.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ESXHost extends VMwareEntity {
    
    public ESXHost(VMwareEntity parent) {
        super(parent);
        parent.addChildren(this);
    }
    
    public void addVM(VirtualMachine vm) {
        addChildren(vm);
    }
    
    public void removeVM(VirtualMachine vm) {
        removeChildren(vm);
    }

    public Collection<VirtualMachine> getVMs() {
        List<VirtualMachine> vms = new ArrayList<VirtualMachine>();
        for(IVMwareEntity child: getChildren()) {
            if (child instanceof VirtualMachine) {
                vms.add((VirtualMachine)child);
            }
        }
        return vms;
    }
}
