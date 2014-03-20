package guacaloco.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataCenter extends VMwareEntity {

    public DataCenter(IVMwareEntity parent) {
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
    
    public void addCluster(Cluster cluster) {
        children.add(cluster);
    }
    
    public void removeCluster(Cluster cluster) {
        children.remove(cluster);
    }

    public Collection<Cluster> getCluster() {
        List<Cluster> clusters = new ArrayList<Cluster>();
        for(IVMwareEntity child: children) {
            if (child instanceof Cluster) {
                clusters.add((Cluster)child);
            }
        }
        return clusters;
    }
}
