package guacaloco.core;

import guacaloco.core.utils.ObjectUtils;
import guacaloco.core.utils.VMwareConnection;
import guacaloco.model.Cluster;
import guacaloco.model.DataCenter;
import guacaloco.model.ESXHost;
import guacaloco.model.VSphereModel;
import guacaloco.model.VirtualCenter;
import guacaloco.model.VirtualMachine;

import java.util.ArrayList;
import java.util.List;

import com.vmware.vim25.ArrayOfManagedObjectReference;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;

public class DataAccessService {

    public static final String SERVER_NAME = "10.20.77.44";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "vmware";

    public static void main(String[] args) throws Exception {
        // create and initialize singleton connection with server
        VmwareManagerConnection managerConnection = VmwareManagerConnection.getInstance();
        managerConnection.init(DataAccessService.SERVER_NAME, DataAccessService.USER_NAME,
                DataAccessService.PASSWORD);

        DataAccessService dataAccessService = new DataAccessService();
        dataAccessService.populateModel();

        System.out.println();
    }

    public void populateModel() throws VsphereToolkitException {
        VSphereModel model = VSphereModel.getInstance();
        // using Breadth-First Traversal method to populate the vSphere model
        // create virtual center
        VirtualCenter vc = createVirtualCenter();
        // create datacenters
        List<DataCenter> datacenterList = createDatacenters(vc);
        List<Cluster> clusterList = new ArrayList<Cluster>();
        // create clusters
        for (DataCenter dataCenter : datacenterList) {
            clusterList = createClusters(dataCenter);
        }
        // create hosts
        createHosts(clusterList);
        // create virtual machines
        createVirtualMachines(model.getEsxHosts());
    }

    private VirtualCenter createVirtualCenter() {
        VSphereModel model = VSphereModel.getInstance();
        VirtualCenter virtualCenter = new VirtualCenter(model);
        String serverName = VmwareManagerConnection.getInstance().getServerName();
        virtualCenter.setName(serverName);
        return virtualCenter;
    }

    private List<DataCenter> createDatacenters(VirtualCenter virtualCenter)
            throws VsphereToolkitException {
        List<DataCenter> datacenterList = new ArrayList<DataCenter>();
        List<ObjectContent> datacenterOCList = null;
        try {
            VMwareConnection connection = VmwareManagerConnection.getInstance().getConnection();
            datacenterOCList = connection.findAllObjects("Datacenter", "hostFolder", "name");
        } catch (Exception e) {
            throw new VsphereToolkitException(e.getMessage(), e.getCause());
        }
        for (ObjectContent datacenterOC : datacenterOCList) {
            String datacenterName = (String) ObjectUtils.getPropertyObject(datacenterOC, "name");
            DataCenter datacenter = new DataCenter(virtualCenter);
            datacenter.setName(datacenterName);
            datacenter.setObjectContent(datacenterOC);
            datacenterList.add(datacenter);
        }
        return datacenterList;
    }

    private List<Cluster> createClusters(DataCenter dataCenter) {
        List<Cluster> clusterList = new ArrayList<Cluster>();
        VMwareConnection connection = VmwareManagerConnection.getInstance().getConnection();
        ObjectContent datacenterOC = dataCenter.getObjectContent();
        ManagedObjectReference hostFolderMOR = (ManagedObjectReference) ObjectUtils
                .getPropertyObject(datacenterOC, "hostFolder");
        ObjectContent hostFolderOC = connection.findObject(hostFolderMOR, "childEntity");
        ArrayOfManagedObjectReference computeResourceMORArray = (ArrayOfManagedObjectReference) ObjectUtils
                .getPropertyObject(hostFolderOC, "childEntity");
        List<ManagedObjectReference> computeResourceMORList = computeResourceMORArray
                .getManagedObjectReference();
        // for each compute resource create a cluster or host
        for (ManagedObjectReference computeResourceMOR : computeResourceMORList) {
            if ("ClusterComputeResource".equals(computeResourceMOR.getType())) {
                ObjectContent clusterOC = connection.findObject(computeResourceMOR, "name", "host");
                String clusterName = (String) ObjectUtils.getPropertyObject(clusterOC, "name");
                Cluster cluster = new Cluster(dataCenter);
                cluster.setName(clusterName);
                cluster.setObjectContent(clusterOC);
                clusterList.add(cluster);
            } else if ("HostSystem".equals(computeResourceMOR.getType())) {
                // it is possible to have some hosts that are not under any cluster
                // so we have to create those hosts under the datacenter
                ObjectContent hostOC = connection.findObject(computeResourceMOR, "name", "vm");
                String hostName = (String) ObjectUtils.getPropertyObject(hostOC, "name");
                ESXHost esxHost = new ESXHost(dataCenter);
                esxHost.setName(hostName);
                esxHost.setObjectContent(hostOC);
            }
        }
        return clusterList;
    }

    private List<ESXHost> createHosts(List<Cluster> clusterList) {
        List<ESXHost> esxHostList = new ArrayList<ESXHost>();
        VMwareConnection connection = VmwareManagerConnection.getInstance().getConnection();
        for (Cluster cluster : clusterList) {
            ObjectContent clusterOC = cluster.getObjectContent();
            ArrayOfManagedObjectReference hostMORArray = (ArrayOfManagedObjectReference) ObjectUtils
                    .getPropertyObject(clusterOC, "host");
            List<ManagedObjectReference> hostMORList = hostMORArray.getManagedObjectReference();
            for (ManagedObjectReference hostMOR : hostMORList) {
                if ("HostSystem".equals(hostMOR.getType())) {
                    ObjectContent hostOC = connection.findObject(hostMOR, "name", "vm");
                    String hostName = (String) ObjectUtils.getPropertyObject(hostOC, "name");
                    ESXHost esxHost = new ESXHost(cluster);
                    esxHost.setName(hostName);
                    esxHost.setObjectContent(hostOC);
                    esxHostList.add(esxHost);
                }
            }
        }
        return esxHostList;
    }

    private List<VirtualMachine> createVirtualMachines(List<ESXHost> esxHostList) {
        List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
        VMwareConnection connection = VmwareManagerConnection.getInstance().getConnection();
        for (ESXHost esxHost : esxHostList) {
            ObjectContent esxHostOC = esxHost.getObjectContent();
            ArrayOfManagedObjectReference vmMORArray = (ArrayOfManagedObjectReference) ObjectUtils
                    .getPropertyObject(esxHostOC, "vm");
            List<ManagedObjectReference> vmMORList = vmMORArray.getManagedObjectReference();
            for (ManagedObjectReference vmMOR : vmMORList) {
                if ("VirtualMachine".equals(vmMOR.getType())) {
                    ObjectContent vmOC = connection.findObject(vmMOR, "name", "summary",
                            "summary.runtime.powerState");
                    String vmName = (String) ObjectUtils.getPropertyObject(vmOC, "name");
                    VirtualMachine vm = new VirtualMachine(esxHost);
                    vm.setName(vmName);
                    vm.setObjectContent(vmOC);
                    vmList.add(vm);
                }
            }
        }
        return vmList;
    }

}
