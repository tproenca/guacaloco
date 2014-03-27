package guacaloco.actions.vm;

import guacaloco.Activator;
import guacaloco.actions.SnippetAction;
import guacaloco.core.VmwareManagerConnection;
import guacaloco.model.IVMwareEntity;
import guacaloco.model.VirtualMachine;
import guacaloco.utils.VelocityUtils;

import org.apache.velocity.VelocityContext;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class VMCloneAction extends SnippetAction {

    private static final String ICON = "/icons/vm_clone.png";
    private static final String DESCRIPTION = "Clone...";
    private static final String TEMPLATE_NAME = "vm_clone.vm";

    public VMCloneAction(StructuredViewer viewer) {
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
        VmwareManagerConnection conn = VmwareManagerConnection.getInstance();
        VelocityContext context = VelocityUtils.getVelocityContext();
        context.put("className", "VMPowerOff");
        context.put("serverName", conn.getServerName());
        context.put("userName", conn.getUserName());
        context.put("password", conn.getPassword());
        context.put("vmUID", "XXXX");
        return context;
    }
}
