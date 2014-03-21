package guacaloco.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataCenter extends VMwareEntity {

    public DataCenter(VMwareEntity parent) {
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
    
    public void addCluster(Cluster cluster) {
        addChildren(cluster);
    }
    
    public void removeCluster(Cluster cluster) {
        removeChildren(cluster);
    }

    public Collection<Cluster> getClusters() {
        List<Cluster> clusters = new ArrayList<Cluster>();
        for(IVMwareEntity child: getChildren()) {
            if (child instanceof Cluster) {
                clusters.add((Cluster)child);
            }
        }
        return clusters;
    }
}
