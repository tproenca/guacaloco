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

}
