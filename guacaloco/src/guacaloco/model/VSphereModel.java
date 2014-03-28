package guacaloco.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VSphereModel extends VMwareEntity {

    private static VSphereModel instance = null;

    private VSphereModel() {
        super(null);
    }

    public static VSphereModel getInstance() {
        if (instance == null) {
            instance = new VSphereModel();
        }
        return instance;
    }

    public boolean isEmpty() {
        return getChildren().length == 0;
    }

    public void addVC(VirtualCenter vc) {
        addChildren(vc);
    }

    public void removeVC(VirtualCenter vc) {
        removeChildren(vc);
        vc.setParent(null);
    }

    public Collection<VirtualCenter> getVCs() {
        List<VirtualCenter> vcs = new ArrayList<VirtualCenter>();
        for (IVMwareEntity child : getChildren()) {
            if (child instanceof VirtualCenter) {
                vcs.add((VirtualCenter) child);
            }
        }
        return vcs;
    }

    // FIXME: PERFORMANCE We need to change the model, here we have a complexity of [O de 4]
    public List<ESXHost> getEsxHosts() {
        List<ESXHost> esxHostList = new ArrayList<ESXHost>();
        for (IVMwareEntity virtualCenter : getChildren()) {
            for (IVMwareEntity datacenter : virtualCenter.getChildren()) {
                if (datacenter instanceof DataCenter) {
                    for (IVMwareEntity computeResource : datacenter.getChildren()) {
                        if (computeResource instanceof Cluster) {
                            for (IVMwareEntity clusterChild : computeResource.getChildren()) {
                                if (clusterChild instanceof ESXHost) {
                                    ESXHost esxHost = (ESXHost) clusterChild;
                                    esxHostList.add(esxHost);
                                }
                            }
                        } else if (computeResource instanceof ESXHost) {
                            ESXHost esxHost = (ESXHost) computeResource;
                            esxHostList.add(esxHost);
                        }
                    }
                }
            }
        }
        return esxHostList;
    }

    public void clear() {
        children.clear();
    }
}
