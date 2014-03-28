package guacaloco.utils;

import guacaloco.Activator;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

public class VelocityUtils {

    private static boolean initialized = false;

    public static VelocityContext getVelocityContext() {
        if (!initialized) {
            init();
        }
        return new VelocityContext();
    }

    public static void init() {
        String path = "templates/";
        try {
            URL url = Platform.getBundle(Activator.PLUGIN_ID).getEntry(path);
            File templateDir = new File(FileLocator.toFileURL(url).toURI());
            Properties prop = new Properties();
            prop.put("file.resource.loader.path", templateDir.getAbsolutePath());
            Velocity.init(prop);
            initialized = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
