package guacaloco.templates.commands;

import guacaloco.core.DataAccessService;
import guacaloco.core.VmwareManagerConnection;
import guacaloco.core.VsphereToolkitException;
import guacaloco.core.utils.VMwareConnection;
import guacaloco.templates.commands.utils.TaskUtils;

import com.vmware.vim25.LocalizedMethodFault;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.TaskInfoState;
import com.vmware.vim25.VimPortType;

public class PowerOffVirtualMachine {

    private String serverName = DataAccessService.SERVER_NAME;
    private String userName = DataAccessService.USER_NAME;
    private String password = DataAccessService.PASSWORD;
    private String vmUuid = "52b55af4-7eaa-87f2-8b90-12e4290ed300";

    public static void main(String[] args) throws VsphereToolkitException {
        new PowerOffVirtualMachine().execute();
    }

    public void execute() throws VsphereToolkitException {
        // connect to vCenter
        VmwareManagerConnection managerConnection = VmwareManagerConnection.getInstance();
        VMwareConnection connection = managerConnection.init(serverName, userName, password);
        VimPortType vimPort = connection.getVimPort();
        try {
            ManagedObjectReference vmMOR = findVirtualMachine();
            ManagedObjectReference taskMOR = vimPort.powerOffVMTask(vmMOR);

            Object[] objectList = TaskUtils.wait(vimPort, taskMOR, new String[] {
                    "info.state", "info.error", "info.result" }, new String[] { "state" },
                    new Object[][] { new Object[] { TaskInfoState.SUCCESS, TaskInfoState.ERROR } });

            for (Object object : objectList) {
                if (object instanceof TaskInfoState) {
                    // 3. Check the task completion state (success or error)
                    TaskInfoState state = (TaskInfoState) object;
                    if (TaskInfoState.SUCCESS.equals(state)) {
                        System.out.println("Success in power off virtual machine");
                    } else if (TaskInfoState.ERROR.equals(state)) {
                        System.err.println("Unable to power off virtual machine");
                    }
                } else if (object instanceof LocalizedMethodFault) {
                    LocalizedMethodFault fault = (LocalizedMethodFault) object;
                    String errorMsg = fault.getLocalizedMessage();
                    throw new VsphereToolkitException(errorMsg);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new VsphereToolkitException("Unable to find the virtual machine", e.getCause());
        }
    }

    private ManagedObjectReference findVirtualMachine() throws RuntimeFaultFaultMsg {
        VMwareConnection connection = VmwareManagerConnection.getInstance().getConnection();
        VimPortType vimPort = connection.getVimPort();
        ServiceContent serviceContent = connection.getServiceContent();
        ManagedObjectReference vmMOR = vimPort.findByUuid(serviceContent.getSearchIndex(), null,
                vmUuid, true, true);
        return vmMOR;
    }

}
