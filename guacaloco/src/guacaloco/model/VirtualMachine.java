package guacaloco.model;

import com.vmware.vim25.VirtualMachinePowerState;

public class VirtualMachine extends VMwareEntity {

    private String instanceUuid;
    private VirtualMachinePowerState powerState;

    public VirtualMachine(VMwareEntity parent) {
        super(parent);
        parent.addChildren(this);
    }

    public String getInstanceUuid() {
        return instanceUuid;
    }

    public void setInstanceUuid(String instanceUuid) {
        this.instanceUuid = instanceUuid;
    }

    public VirtualMachinePowerState getPowerState() {
        return powerState;
    }

    public void setPowerState(VirtualMachinePowerState powerState) {
        this.powerState = powerState;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((instanceUuid == null) ? 0 : instanceUuid.hashCode());
        result = prime * result
                + ((powerState == null) ? 0 : powerState.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        VirtualMachine other = (VirtualMachine) obj;
        if (instanceUuid == null) {
            if (other.instanceUuid != null)
                return false;
        } else if (!instanceUuid.equals(other.instanceUuid))
            return false;
        if (powerState != other.powerState)
            return false;
        return true;
    }
}
