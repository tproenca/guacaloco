package guacaloco.views;

import guacaloco.actions.VMwareEntityAction;
import guacaloco.actions.host.HostEnterMaintenanceModeAction;
import guacaloco.actions.host.HostEnterStandbyModeAction;
import guacaloco.actions.host.HostNewVirtualMachineAction;
import guacaloco.actions.host.HostPowerOnAction;
import guacaloco.actions.host.HostRebootAction;
import guacaloco.actions.host.HostRemoveAction;
import guacaloco.actions.host.HostShutdownAction;
import guacaloco.actions.vm.VMAddPermissionAction;
import guacaloco.actions.vm.VMCloneAction;
import guacaloco.actions.vm.VMMigrateAction;
import guacaloco.actions.vm.VMPowerAction;
import guacaloco.actions.vm.VMRenameAction;
import guacaloco.actions.vm.VMTakeSnapshotAction;
import guacaloco.core.DataAccessService;
import guacaloco.core.VmwareManagerConnection;
import guacaloco.core.VsphereToolkitException;
import guacaloco.model.VSphereModel;
import guacaloco.wizards.AddVirtualCenterWizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
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

    private List<VMwareEntityAction> actions = new ArrayList<VMwareEntityAction>();
    private UIJob job;

    @Override
    public void dispose() {
        super.dispose();
        job.cancel();
        job = null;
    }

    @Override
    public void createPartControl(Composite parent) {
        viewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(new VirtualCenterContentProvider());
        viewer.setLabelProvider(new VirtualCenterLabelProvider());

        makeActions();
        contributeToActionBars();
        hookContextMenu();
        populate();
        scheduleJob();
    }

    protected synchronized void init() {
        try {
            VmwareManagerConnection conn = VmwareManagerConnection
                    .getInstance();
            if (conn.isConnected()) {
                VSphereModel model = VSphereModel.getInstance();
                model.clear();
                DataAccessService dataAccessService = new DataAccessService();
                dataAccessService.populateModel();
            }

        } catch (VsphereToolkitException ex) {
            MessageDialog.openError(viewer.getControl().getShell(), "Error",
                    ex.getMessage());
        }
    }

    private synchronized void populate() {
        VSphereModel model = VSphereModel.getInstance();
        // System.out.println("populate");
        if (!model.isEmpty()) {
            if (viewer.getInput() == null) {
                viewer.setInput(model);
                // System.out.println("input");
            } else {
                viewer.refresh();
             // System.out.println("refresh");
            }
            expandAllAction.setEnabled(true);
        }
    }

    private void scheduleJob() {
        if (job == null) {
            job = new UIJob("Fetching data from Virtual Center...") {
                @Override
                public IStatus runInUIThread(IProgressMonitor monitor) {
                    try {
                        if (monitor.isCanceled()) {
                            return Status.CANCEL_STATUS;
                        }
                        init();
                        populate();
                        return Status.OK_STATUS;
                    } finally {
                        schedule(8000);
                    }
                }
            };
            job.setSystem(true);
            job.schedule();
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
                    // System.out.println("Ok pressed");
                     init();
                     populate();
                } else {
                    // System.out.println("Cancel pressed");
                }
            }
        };
        createConnectionAction.setToolTipText("Create New Connection");

        createConnectionAction.setImageDescriptor(AbstractUIPlugin
                .imageDescriptorFromPlugin("org.eclipse.ui",
                        "$nl$/icons/full/obj16/add_obj.gif"));

        actions.add(new VMPowerAction(viewer));
        actions.add(new VMTakeSnapshotAction(viewer));
        actions.add(new VMMigrateAction(viewer));
        actions.add(new VMCloneAction(viewer));
        actions.add(new VMAddPermissionAction(viewer));
        actions.add(new VMRenameAction(viewer));
        
        actions.add(new HostNewVirtualMachineAction(viewer));
        actions.add(new HostEnterMaintenanceModeAction(viewer));
        actions.add(new HostShutdownAction(viewer));
        actions.add(new HostEnterStandbyModeAction(viewer));
        actions.add(new HostRebootAction(viewer));
        actions.add(new HostPowerOnAction(viewer));  
        actions.add(new HostRemoveAction(viewer));    
    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                GuacalocoView.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
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

    private void fillContextMenu(IMenuManager manager) {
        for (VMwareEntityAction action : actions) {
            if (action.isValid()) {
                manager.add(action);
            }
        }
    }
}
