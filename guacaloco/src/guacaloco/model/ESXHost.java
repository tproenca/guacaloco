package guacaloco.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ESXHost extends VMwareEntity {
    
    public ESXHost(IVMwareEntity parent) {
        super(parent);
    }
    
    public void addVM(VirtualMachine vm) {
        children.add(vm);
    }
    
    public void removeVM(VirtualMachine vm) {
        children.remove(vm);
    }

    public Collection<VirtualMachine> getVMs() {
        List<VirtualMachine> vms = new ArrayList<VirtualMachine>();
        for(IVMwareEntity child: children) {
            if (child instanceof VirtualMachine) {
                vms.add((VirtualMachine)child);
            }
        }
        return vms;
    }
}
