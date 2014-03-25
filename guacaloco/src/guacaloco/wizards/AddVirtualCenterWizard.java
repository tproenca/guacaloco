package guacaloco.wizards;

import guacaloco.Activator;
import guacaloco.core.VmwareManagerConnection;
import guacaloco.core.VsphereToolkitException;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.osgi.service.prefs.Preferences;

public class AddVirtualCenterWizard extends Wizard {
    private CredentialsWizardPage page;

    public AddVirtualCenterWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    @Override
    public void addPages() {
        Preferences pref = ConfigurationScope.INSTANCE
                .getNode(Activator.PLUGIN_ID);
        Preferences cred = pref.node("credentials");

        page = new CredentialsWizardPage(cred.get("server", ""), cred.get(
                "userid", ""), cred.get("passwd", ""));
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        final String server = page.getServer();
        final String userId = page.getUserId();
        final String password = page.getPassword();
        IRunnableWithProgress op = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor)
                    throws InvocationTargetException {
                try {
                    doFinish(server, userId, password, monitor);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error",
                    realException.getMessage());
            return false;
        }
        return true;
    }

    private void doFinish(String server, String userId, String password,
            IProgressMonitor monitor) throws CoreException {
        monitor.beginTask("Attempting to connect to Virtual Center... ",
                IProgressMonitor.UNKNOWN);

        try {
            VmwareManagerConnection conn = VmwareManagerConnection
                    .getInstance();
            conn.init(server, userId, password);
        } catch (VsphereToolkitException ex) {
            IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.OK,
                    ex.getMessage(), null);
            throw new CoreException(status);
        }

        monitor.setTaskName("Connected!");
        
        Preferences pref = ConfigurationScope.INSTANCE
                .getNode(Activator.PLUGIN_ID);
        Preferences cred = pref.node("credentials");
        cred.put("server", server);
        cred.put("userid", userId);
        cred.put("passwd", password);
    }
}
