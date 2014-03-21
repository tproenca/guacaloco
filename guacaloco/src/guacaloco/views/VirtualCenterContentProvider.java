package guacaloco.views;

import guacaloco.model.IVMwareEntity;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class VirtualCenterContentProvider implements IStructuredContentProvider, ITreeContentProvider{

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // TODO Auto-generated method stub
    }

    @Override
    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    @Override
    public Object[] getChildren(Object parent) {
        if (parent instanceof IVMwareEntity) {
            return ((IVMwareEntity) parent).getChildren();
        }
        return new Object[0];
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof IVMwareEntity) {
            return ((IVMwareEntity) element).getParent();
        }
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof IVMwareEntity)
            return ((IVMwareEntity) element).getChildren().length > 0;
        return false;
    }

}
