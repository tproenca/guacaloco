package guacaloco.actions.vm;

import guacaloco.actions.SnippetAction;
import guacaloco.model.IVMwareEntity;
import guacaloco.model.VirtualMachine;
import guacaloco.utils.TemplateConstants;

import org.apache.velocity.VelocityContext;
import org.eclipse.jface.viewers.StructuredViewer;

public class VMRenameAction extends SnippetAction {
    
    private static final String DESCRIPTION = "Rename";
    private static final String TEMPLATE_NAME = "RenameVirtualMachine.vm";

    public VMRenameAction(StructuredViewer viewer) {
        super(viewer);
    }

    @Override
    public boolean isValid() {
        IVMwareEntity entity = getViewerSelection();
        return (entity instanceof VirtualMachine);
    }

    @Override
    public String getTemplateName() {
        return TEMPLATE_NAME;
    }

    @Override
    public VelocityContext getContext() {
        VelocityContext ctx = getDefaultContext();
        ctx.put("packageName", TemplateConstants.SAMPLES_PKG_NAME);
        return ctx;
    }

    @Override
    protected void init() {
        setText(DESCRIPTION);
    }
}
