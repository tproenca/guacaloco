package guacaloco.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Cluster extends VMwareEntity {

    public Cluster(IVMwareEntity parent) {
        super(parent);
    }

    public void addESXHost(ESXHost host) {
        children.add(host);
    }
    
    public void removeESXHost(ESXHost host) {
        children.remove(host);
    }

    public Collection<ESXHost> getESXHosts() {
        List<ESXHost> esxs = new ArrayList<ESXHost>();
        for(IVMwareEntity child: children) {
            if (child instanceof ESXHost) {
                esxs.add((ESXHost)child);
            }
        }
        return esxs;
    }
}
