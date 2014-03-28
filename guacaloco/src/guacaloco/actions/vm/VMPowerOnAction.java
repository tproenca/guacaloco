package guacaloco.actions.vm;

import guacaloco.Activator;
import guacaloco.actions.SnippetAction;
import guacaloco.model.IVMwareEntity;
import guacaloco.model.VirtualMachine;
import guacaloco.utils.TemplateConstants;

import org.apache.velocity.VelocityContext;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class VMPowerOnAction extends SnippetAction {
    
    private static final String ICON = "/icons/vm_on.png";
    private static final String DESCRIPTION = "Power On";
    private static final String TEMPLATE_NAME = "PowerOnVirtualMachine.vm";

    public VMPowerOnAction(StructuredViewer viewer) {
        super(viewer);
    }

    @Override
    protected void init() {
        setText(DESCRIPTION);
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
                Activator.PLUGIN_ID, ICON));
        
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
}
