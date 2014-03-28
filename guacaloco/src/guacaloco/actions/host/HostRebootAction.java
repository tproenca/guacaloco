package guacaloco.actions.host;

import guacaloco.Activator;
import guacaloco.actions.SnippetAction;
import guacaloco.model.ESXHost;
import guacaloco.model.IVMwareEntity;
import guacaloco.utils.TemplateConstants;

import org.apache.velocity.VelocityContext;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class HostRebootAction extends SnippetAction {

    private static final String ICON = "/icons/host_reboot.png";
    private static final String DESCRIPTION = "Reboot";
    private static final String TEMPLATE_NAME = "HostReboot.vm";

    public HostRebootAction(StructuredViewer viewer) {
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
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
                Activator.PLUGIN_ID, ICON));
    }

    @Override
    public void run() {
        // TODO Remove this method after adding template in the directory
    }
}
