package guacaloco.actions;

import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.jface.viewers.StructuredViewer;

public abstract class SnippetAction extends VMwareEntityAction {

    public SnippetAction(StructuredViewer viewer) {
        super(viewer);
    }

    public SnippetAction(StructuredViewer viewer, int style) {
        super(viewer, style);
    }

    @Override
    public void run() {
        StringWriter w = new StringWriter();
        Velocity.mergeTemplate(getTemplateName(), "ISO-8859-1", getContext(), w);
    }
    
    public abstract String getTemplateName();
    
    public abstract VelocityContext getContext();
}
