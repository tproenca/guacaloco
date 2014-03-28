package guacaloco.actions.vm;

import org.apache.velocity.VelocityContext;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import guacaloco.Activator;
import guacaloco.actions.SnippetAction;
import guacaloco.model.IVMwareEntity;
import guacaloco.model.VirtualMachine;
import guacaloco.utils.TemplateConstants;

public class VMMigrateAction extends SnippetAction {
    
    private static final String ICON = "/icons/vm_migrate.png";
    private static final String DESCRIPTION = "Migrate...";
    private static final String TEMPLATE_NAME = "MigrateVirtualMachine.vm";

    public VMMigrateAction(StructuredViewer viewer) {
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
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
                Activator.PLUGIN_ID, ICON));
    }

}
