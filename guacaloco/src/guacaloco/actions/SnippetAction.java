package guacaloco.actions;

import guacaloco.core.VmwareManagerConnection;
import guacaloco.core.utils.JdtUtils;
import guacaloco.model.VirtualMachine;
import guacaloco.utils.TemplateConstants;
import guacaloco.utils.VelocityUtils;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.progress.UIJob;

public abstract class SnippetAction extends VMwareEntityAction {

    private static final String ENCODING = "ISO-8859-1";

    ICompilationUnit snippetClass = null;

    public SnippetAction(StructuredViewer viewer) {
        super(viewer);
    }

    public SnippetAction(StructuredViewer viewer, int style) {
        super(viewer, style);
    }

    @Override
    public void run() {
        // FIXME (tproenca): Create a Job class that adds the snippetClass
        UIJob job = new UIJob("Generating code...") {
            public IStatus runInUIThread(IProgressMonitor monitor) {
                VelocityContext ctx = getContext();
                VelocityContext defaultCtx = getDefaultContext();
                String snippetTemplateName = getTemplateName();
                List<String> templateFileNames = new ArrayList<String>(
                        getRequiredTemplateFileNames());
                templateFileNames.add(snippetTemplateName);

                try {
                    // Create packages
                    String packageName = (String) ctx.get("packageName");
                    IPackageFragment samplesPkg = JdtUtils.createPackage(packageName);
                    IPackageFragment utilsPkg = JdtUtils.createPackage(TemplateConstants.UTIL_PKG_NAME);

                    int ticks = (int)(templateFileNames.size()/2);
                    monitor.beginTask("Preparing workspace...",
                            templateFileNames.size() + ticks);
                    
                    // Importing libraries
                    JdtUtils.importLibrariesIntoProject(new SubProgressMonitor(monitor, ticks));
                    
                    // Generating code
                    for (String templateName : templateFileNames) {
                        String fileName = templateName.replaceAll(".vm",
                                ".java");
                        monitor.setTaskName("Generating " + fileName);
                        StringWriter code = new StringWriter();
                        
                        if (templateName.equals(snippetTemplateName)) {
                            Velocity.mergeTemplate(templateName, ENCODING, ctx,
                                    code);
                            snippetClass = JdtUtils.createJavaClass(samplesPkg,
                                    code.toString(), fileName);;
                        } else {
                            Velocity.mergeTemplate(templateName, ENCODING, defaultCtx,
                                    code);
                            JdtUtils.createJavaClass(utilsPkg,
                                    code.toString(), fileName);
                        }
                        monitor.worked(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    monitor.done();
                }

                return Status.OK_STATUS;
            }
        };
        job.addJobChangeListener(new JobChangeAdapter() {
            public void done(IJobChangeEvent event) {
                if (event.getResult().isOK()) {
                    try {
                        JavaUI.openInEditor(snippetClass);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        job.setUser(true);
        job.schedule();
    }

    private Collection<String> getRequiredTemplateFileNames() {
        List<String> result = new ArrayList<String>();
        result.add("FakeTrustManager.vm");
        result.add("ObjectUtils.vm");
        result.add("TaskUtils.vm");
        result.add("VMwareConnection.vm");
        result.add("VMwareManagerConnection.vm");
        result.add("VsphereToolkitException.vm");
        return result;
    }
    
    protected VelocityContext getDefaultContext() {
        VmwareManagerConnection conn = VmwareManagerConnection.getInstance();
        VirtualMachine vm = (VirtualMachine) getViewerSelection();
        VelocityContext context = VelocityUtils.getVelocityContext();
        String className = getTemplateName().replaceAll(".vm","");
        context.put("packageName", TemplateConstants.UTIL_PKG_NAME);
        context.put("className", className);
        context.put("serverName", conn.getServerName());
        context.put("userName", conn.getUserName());
        context.put("password", conn.getPassword());
        context.put("vmUUID", vm.getInstanceUuid());
        return context;
    }

    public abstract String getTemplateName();

    public abstract VelocityContext getContext();
}
