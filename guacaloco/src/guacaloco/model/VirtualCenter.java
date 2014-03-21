package guacaloco.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VirtualCenter extends VMwareEntity {

    public VirtualCenter(VMwareEntity parent) {
        super(parent);
        parent.addChildren(this);
    }

    public void addDataCenter(DataCenter datacenter) {
        addChildren(datacenter);
    }
    
    public void removeDataCenter(DataCenter datacenter) {
        removeChildren(datacenter);
    }

    public Collection<DataCenter> getDataCenter() {
        List<DataCenter> datacenters = new ArrayList<DataCenter>();
        for(IVMwareEntity child: getChildren()) {
            if (child instanceof DataCenter) {
                datacenters.add((DataCenter)child);
            }
        }
        return datacenters;
    }

}
