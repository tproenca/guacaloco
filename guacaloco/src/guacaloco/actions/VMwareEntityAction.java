package guacaloco.actions;

import guacaloco.model.IVMwareEntity;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

public abstract class VMwareEntityAction extends Action implements IVMwareEntityAction {

    private StructuredViewer viewer;

    public VMwareEntityAction(StructuredViewer viewer) {
        this(viewer, -1);
    }
    
    public VMwareEntityAction(StructuredViewer viewer, int style) {
        super(null, style);
        this.viewer = viewer;
        init();
    }

    protected abstract void init();
    
    public IVMwareEntity getViewerSelection() {
        IVMwareEntity result = null;
        IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
        Object obj = selection.getFirstElement();
        if (obj instanceof IVMwareEntity) {
            result = (IVMwareEntity)obj;
        }
        return result;
    }
    
    
}
