package guacaloco.actions;

import guacaloco.core.utils.JdtUtils;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
                String snippetTemplateName = getTemplateName();

                try {
                    // Create package
                    String packageName = (String) ctx.get("packageName");
                    IPackageFragment pkg = JdtUtils.createPackage(packageName);

                    List<String> templateFileNames = new ArrayList<String>(
                            getRequiredTemplateFileNames());
                    templateFileNames.add(snippetTemplateName);

                    monitor.beginTask("Generating code...",
                            templateFileNames.size());
                    for (String templateName : templateFileNames) {
                        String fileName = templateName.replaceAll(".vm",
                                ".java");
                        monitor.setTaskName("Creating " + fileName);
                        StringWriter code = new StringWriter();
                        Velocity.mergeTemplate(templateName, ENCODING, ctx,
                                code);
                        ICompilationUnit cu = JdtUtils.createJavaClass(pkg,
                                code.toString(), fileName);
                        if (templateName.equals(snippetTemplateName)) {
                            snippetClass = cu;
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

        // VelocityContext ctx = getContext();
        // String snippetTemplateName = getTemplateName();
        //
        // try {
        // // Create package
        // String packageName = (String)ctx.get("packageName");
        // IPackageFragment pkg =
        // AddSampleIntoEclipseProject.createPackage(packageName);
        //
        // List<String> templateFileNames = new
        // ArrayList<String>(getRequiredTemplateFileNames());
        // templateFileNames.add(snippetTemplateName);
        //
        //
        // for (String templateName : templateFileNames) {
        // String fileName = templateName.replaceAll(".vm", ".java");
        // StringWriter code = new StringWriter();
        // Velocity.mergeTemplate(templateName, ENCODING, ctx, code);
        // ICompilationUnit cu =
        // AddSampleIntoEclipseProject.createJavaClass(pkg, code.toString(),
        // fileName);
        // if (templateName.equals(snippetTemplateName)) {
        // snippetClass = cu;
        // }
        // }
        //
        // JavaUI.openInEditor(snippetClass);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
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

    public abstract String getTemplateName();

    public abstract VelocityContext getContext();
}
