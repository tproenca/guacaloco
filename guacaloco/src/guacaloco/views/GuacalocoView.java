package guacaloco.views;

import guacaloco.core.DataAccessService;
import guacaloco.core.VmwareManagerConnection;
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
        try {
            // create and initialize singleton connection with server
            VmwareManagerConnection managerConnection = VmwareManagerConnection.getInstance();
            managerConnection.init(DataAccessService.SERVER_NAME, DataAccessService.USER_NAME,
                    DataAccessService.PASSWORD);
            DataAccessService dataAccessService = new DataAccessService();

            dataAccessService.populateModel();
            System.out.println();
        } catch (VsphereToolkitException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

}
