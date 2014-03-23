package guacaloco;

import guacaloco.utils.ISharedImages;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "guacaloco"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;

    /**
     * The constructor
     */
    public Activator() {
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    @Override
    protected void initializeImageRegistry(ImageRegistry reg) {
        super.initializeImageRegistry(reg);
        IPath path;
        ImageDescriptor desc;
        Bundle bundle = Platform.getBundle(PLUGIN_ID);

        path = new Path("icons/cluster.png");
        desc = ImageDescriptor.createFromURL(FileLocator.find(bundle, path,
                null));
        reg.put(ISharedImages.IMG_VC_CLUSTER, desc);

        path = new Path("icons/datacenter.png");
        desc = ImageDescriptor.createFromURL(FileLocator.find(bundle, path,
                null));
        reg.put(ISharedImages.IMG_VC_DATACENTER, desc);

        path = new Path("icons/host.png");
        desc = ImageDescriptor.createFromURL(FileLocator.find(bundle, path,
                null));
        reg.put(ISharedImages.IMG_VC_ESXHOST, desc);

        path = new Path("icons/vcenter.png");
        desc = ImageDescriptor.createFromURL(FileLocator.find(bundle, path,
                null));
        reg.put(ISharedImages.IMG_VC_VCENTER, desc);

        path = new Path("icons/vm.png");
        desc = ImageDescriptor.createFromURL(FileLocator.find(bundle, path,
                null));
        reg.put(ISharedImages.IMG_VC_VM, desc);

        path = new Path("icons/vm_on.png");
        desc = ImageDescriptor.createFromURL(FileLocator.find(bundle, path,
                null));
        reg.put(ISharedImages.IMG_VC_VM_ON, desc);

        path = new Path("icons/vm_suspend.png");
        desc = ImageDescriptor.createFromURL(FileLocator.find(bundle, path,
                null));
        reg.put(ISharedImages.IMG_VC_VM_SUSPEND, desc);
    }

}
