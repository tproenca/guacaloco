package guacaloco.wizards;

import guacaloco.Activator;
import guacaloco.utils.ISharedImages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CredentialsWizardPage extends WizardPage {

    private String server;
    private String userId;
    private String password;

    private Text txtServer;
    private Text txtUserId;
    private Text txtPassword;

    protected CredentialsWizardPage(String currentServer, String currentUserId,
            String currentPassword) {
        super("Credentials", "Add New Virtual Center Connection", Activator
                .getDefault().getImageRegistry()
                .getDescriptor(ISharedImages.IMG_VC_VCENTER_WIZARD));
        setDescription("Enter the location of the Virtual Center and user credentials.");
        server = currentServer;
        userId = currentUserId;
        password = currentPassword;
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);

        container.setLayout(new GridLayout(2, false));

        Label lblServer = new Label(container, SWT.NONE);
        lblServer.setText("Server:");

        txtServer = new Text(container, SWT.BORDER);
        txtServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));
        txtServer.setText(server);
        txtServer.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });

        Label lblUserId = new Label(container, SWT.NONE);
        lblUserId.setText("User ID:");

        txtUserId = new Text(container, SWT.BORDER);
        txtUserId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));
        txtUserId.setText(userId);
        txtUserId.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });

        Label lblPassword = new Label(container, SWT.NONE);
        lblPassword.setText("Password:");

        txtPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
        txtPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));
        txtPassword.setText(password);
        txtPassword.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });
        setPageComplete(!server.isEmpty()&&!userId.isEmpty()&&!password.isEmpty());
        // dialogChanged();
    }

    /**
     * Ensures that both text fields are set.
     */
    private void dialogChanged() {
        if (txtServer.getText().trim().length() == 0) {
            updateStatus("Server address must be specified.");
            return;
        }

        if (txtUserId.getText().trim().length() == 0) {
            updateStatus("User ID must be specified.");
            return;
        }

        if (txtPassword.getText().trim().length() == 0) {
            updateStatus("User password must be specified.");
            return;
        }
        updateStatus(null);
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public String getServer() {
        return txtServer.getText();
    }

    public String getUserId() {
        return txtUserId.getText();
    }

    public String getPassword() {
        return txtPassword.getText();
    }
}
