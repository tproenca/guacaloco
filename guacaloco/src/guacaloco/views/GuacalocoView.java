package guacaloco.views;

import guacaloco.core.DataAccessService;
import guacaloco.core.VmwareManagerConnection;
import guacaloco.core.VsphereToolkitException;
import guacaloco.model.VSphereModel;
import guacaloco.wizards.AddVirtualCenterWizard;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;

public class GuacalocoView extends ViewPart {

    private TreeViewer viewer;

    private Action colapseAllAction;
    private Action expandAllAction;
    private Action createConnectionAction;

    @Override
    public void createPartControl(Composite parent) {
        viewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(new VirtualCenterContentProvider());
        viewer.setLabelProvider(new VirtualCenterLabelProvider());

        makeActions();
        contributeToActionBars();
        populate();
    }

    protected void init() {
        try {
            VmwareManagerConnection conn = VmwareManagerConnection
                    .getInstance();
            if (conn.isConnected()) {
                DataAccessService dataAccessService = new DataAccessService();
                dataAccessService.populateModel();
            }

        } catch (VsphereToolkitException ex) {
            MessageDialog.openError(viewer.getControl().getShell(), "Error",
                    ex.getMessage());
        }
    }

    private void populate() {
        VSphereModel model = VSphereModel.getInstance();

        if (!model.isEmpty()) {
            viewer.setInput(model);
            expandAllAction.setEnabled(true);
        }
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    private void makeActions() {
        colapseAllAction = new Action() {
            @Override
            public void run() {
                viewer.collapseAll();
                colapseAllAction.setEnabled(false);
                expandAllAction.setEnabled(true);
            }
        };
        colapseAllAction.setToolTipText("Collapse All");
        colapseAllAction.setImageDescriptor(PlatformUI.getWorkbench()
                .getSharedImages()
                .getImageDescriptor(ISharedImages.IMG_ELCL_COLLAPSEALL));
        colapseAllAction.setEnabled(false);

        expandAllAction = new Action() {
            @Override
            public void run() {
                viewer.expandAll();
                colapseAllAction.setEnabled(true);
                expandAllAction.setEnabled(false);
            }
        };
        expandAllAction.setToolTipText("Expand All");
        expandAllAction.setImageDescriptor(AbstractUIPlugin
                .imageDescriptorFromPlugin("org.eclipse.ui.cheatsheets",
                        "$nl$/icons/elcl16/expandall.gif"));
        expandAllAction.setEnabled(false);

        createConnectionAction = new Action() {
            @Override
            public void run() {
                WizardDialog wizardDialog = new WizardDialog(viewer
                        .getControl().getShell(), new AddVirtualCenterWizard());
                if (wizardDialog.open() == Window.OK) {
                    System.out.println("Ok pressed");
                    UIJob job = new UIJob(
                            "Fetching data from Virtual Center...") {
                        @Override
                        public IStatus runInUIThread(IProgressMonitor monitor) {
                            init();
                            populate();
                            return Status.OK_STATUS;
                        }
                    };
                    job.schedule();
                } else {
                    System.out.println("Cancel pressed");
                }
            }
        };
        createConnectionAction.setToolTipText("Create New Connection");
        createConnectionAction.setImageDescriptor(AbstractUIPlugin
                .imageDescriptorFromPlugin("org.eclipse.ui",
                        "$nl$/icons/full/obj16/add_obj.gif"));
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(colapseAllAction);
        manager.add(expandAllAction);
        manager.add(createConnectionAction);
    }
}
