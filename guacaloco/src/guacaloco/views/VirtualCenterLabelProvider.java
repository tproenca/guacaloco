package guacaloco.views;

import guacaloco.Activator;
import guacaloco.model.Cluster;
import guacaloco.model.DataCenter;
import guacaloco.model.ESXHost;
import guacaloco.model.IVMwareEntity;
import guacaloco.model.VirtualCenter;
import guacaloco.model.VirtualMachine;
import guacaloco.utils.ISharedImages;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class VirtualCenterLabelProvider extends LabelProvider {

    @Override
    public Image getImage(Object element) {
        String imageKey = null;
        if (element instanceof Cluster) {
            imageKey = ISharedImages.IMG_VC_CLUSTER;
        } else if (element instanceof DataCenter) {
            imageKey = ISharedImages.IMG_VC_DATACENTER;
        } else if (element instanceof ESXHost) {
            imageKey = ISharedImages.IMG_VC_ESXHOST;
        } else if (element instanceof VirtualCenter) {
            imageKey = ISharedImages.IMG_VC_VCENTER;
        } else if (element instanceof VirtualMachine) {
            VirtualMachine vm = (VirtualMachine) element;
            switch (vm.getPowerState()) {
            case POWERED_ON:
                imageKey = ISharedImages.IMG_VC_VM_ON;
                break;
            case SUSPENDED:
                imageKey = ISharedImages.IMG_VC_VM_SUSPEND;
                break;
            default:
                imageKey = ISharedImages.IMG_VC_VM;
                break;
            }
        }
        return Activator.getDefault().getImageRegistry().get(imageKey);
    }

    @Override
    public String getText(Object element) {
        if (element instanceof IVMwareEntity) {
            return ((IVMwareEntity)element).getName();
        }
        return super.getText(element);
    }

}
