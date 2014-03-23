package guacaloco.views;

import guacaloco.core.DataAccessService;
import guacaloco.core.VsphereToolkitException;
import guacaloco.model.VSphereModel;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class GuacalocoView extends ViewPart {

    private TreeViewer viewer;

    @Override
    public void createPartControl(Composite parent) {
        init();
        viewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(new VirtualCenterContentProvider());
        viewer.setLabelProvider(new VirtualCenterLabelProvider());
        viewer.setInput(VSphereModel.getInstance());
    }

    protected void init() {
        DataAccessService dataAccessService = new DataAccessService();
        try {
            dataAccessService.populateModel();
            System.out.println();
        } catch (VsphereToolkitException e) {
            e.printStackTrace();
        }

//        VSphereModel model = VSphereModel.getInstance();
//        if (model.isEmpty()) {
//            VirtualCenter vc = new VirtualCenter(model);
//            vc.setName("proma-2s-dhcp2-47");
//
//            DataCenter datacenter = new DataCenter(vc);
//            datacenter.setName("Datacenter2");
//
//            Cluster cluster = new Cluster(datacenter);
//            cluster.setName("My Cluster");
//
//            ESXHost host1 = new ESXHost(cluster);
//            host1.setName("cert052.eng.vmware.com");
//
//            for (int i = 1; i < 5; i++) {
//                VirtualMachine vm = new VirtualMachine(host1);
//                vm.setName("w2k3-32-sdk-" + i);
//            }
//
//            ESXHost host2 = new ESXHost(datacenter);
//            host2.setName("10.112.15.201");
//
//            for (int i = 1; i < 4; i++) {
//                VirtualMachine vm = new VirtualMachine(host2);
//                vm.setName("ubuntu-32-sdk-" + i);
//            }
//
//            ESXHost host3 = new ESXHost(datacenter);
//            host3.setName("peng011.eng.vmware.com");
//
//            for (int i = 1; i < 3; i++) {
//                VirtualMachine vm = new VirtualMachine(host3);
//                vm.setName("win8-32-sdk-" + i);
//            }
//        }
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

}
