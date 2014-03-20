package guacaloco.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VirtualCenter extends VMwareEntity {

    public VirtualCenter(IVMwareEntity parent) {
        super(parent);
    }
    
    public void addDataCenter(DataCenter datacenter) {
        children.add(datacenter);
    }
    
    public void removeDataCenter(DataCenter datacenter) {
        children.remove(datacenter);
    }

    public Collection<DataCenter> getDataCenter() {
        List<DataCenter> datacenters = new ArrayList<DataCenter>();
        for(IVMwareEntity child: children) {
            if (child instanceof DataCenter) {
                datacenters.add((DataCenter)child);
            }
        }
        return datacenters;
    }

}
