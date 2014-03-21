package guacaloco.model;

import java.util.LinkedHashSet;
import java.util.Set;


public class VSphereModel implements IVMwareEntity{

    private static VSphereModel instance;
    protected Set<IVMwareEntity> children = new LinkedHashSet<IVMwareEntity>();
    
    private VSphereModel() {
        
    }

    public static VSphereModel getInstance() {
        if (instance == null) {
            instance = new VSphereModel();
        }
        return instance;
    }

    @Override
    public IVMwareEntity getParent() {
        return null;
    }

    @Override
    public IVMwareEntity[] getChildren() {
        return children.toArray(new IVMwareEntity[0]);
    }

    @Override
    public String getName() {
        return null;
    }
    
    public boolean isEmpty() {
        return children.size() == 0;
    }

}
