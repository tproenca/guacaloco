package guacaloco.actions.host;

import guacaloco.actions.SnippetAction;
import guacaloco.model.ESXHost;
import guacaloco.model.IVMwareEntity;
import guacaloco.utils.TemplateConstants;

import org.apache.velocity.VelocityContext;
import org.eclipse.jface.viewers.StructuredViewer;

public class HostRemoveAction extends SnippetAction {

    private static final String DESCRIPTION = "Remove";
    private static final String TEMPLATE_NAME = "HostRemove.vm";

    public HostRemoveAction(StructuredViewer viewer) {
        super(viewer);
    }

    @Override
    public boolean isValid() {
        IVMwareEntity entity = getViewerSelection();
        return (entity instanceof ESXHost);
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

    @Override
    public void run() {
        // TODO Remove this method after adding template in the directory
    }
}

