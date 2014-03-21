package guacaloco.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Cluster extends VMwareEntity {

    public Cluster(VMwareEntity parent) {
        super(parent);
        parent.addChildren(this);
    }

    public void addESXHost(ESXHost host) {
        addChildren(host);
    }
    
    public void removeESXHost(ESXHost host) {
        removeChildren(host);
    }

    public Collection<ESXHost> getESXHosts() {
        List<ESXHost> esxs = new ArrayList<ESXHost>();
        for(IVMwareEntity child: getChildren()) {
            if (child instanceof ESXHost) {
                esxs.add((ESXHost)child);
            }
        }
        return esxs;
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
