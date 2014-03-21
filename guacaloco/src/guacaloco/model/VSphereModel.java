package guacaloco.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class VSphereModel extends VMwareEntity{

    private static VSphereModel instance;
    
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
        for(IVMwareEntity child: getChildren()) {
            if (child instanceof Cluster) {
                vcs.add((VirtualCenter)child);
            }
        }
        return vcs;
    }

}
