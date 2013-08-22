package org.fbi.linking.server.posprize.helper;

import org.fbi.linking.bootstrap.PosPrizeServerActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Properties;

/**
 * 项目配置文件管理，配置修改后可马上生效.
 * User: zhanrui
 */
public class ProjectConfigManager {
    public static final Logger logger = LoggerFactory.getLogger(ProjectConfigManager.class);

    private static final String PROP_FILE_NAME = "prjcfg.properties";
    private File propFile = null;
    private long fileLastModifiedTime = 0;
    private Properties props = null;
    private static ProjectConfigManager manager = new ProjectConfigManager();

    private ProjectConfigManager() {
        //URL url = ProjectConfigManager.class.getClassLoader().getResource(PROP_FILE_NAME);
        BundleContext bundleContext = PosPrizeServerActivator.getBundleContext();
        URL url = bundleContext.getBundle().getEntry(PROP_FILE_NAME);

        props = new Properties();
        try {
            props.load(url.openConnection().getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


/*
        URL url = getClass().getResource(PROP_FILE_NAME);
        if (url == null) {
            logger.error("配置文件不存在!");
            throw new RuntimeException("配置文件不存在!");
        }
        propFile = new File(url.getFile());
        fileLastModifiedTime = propFile.lastModified();
        props = new Properties();
        try {
            props.load(new FileInputStream(propFile));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
*/
    }

    public static ProjectConfigManager getInstance() {
        return manager;
    }

    final public Object getProperty(String name) {
/*
        long newTime = propFile.lastModified();

        if (newTime == 0) {
            if (fileLastModifiedTime == 0) {
                System.err.println(PROP_FILE_NAME + " 文件不存在.");
            } else {
                System.err.println(PROP_FILE_NAME + " 文件已被删除!");
            }
            return null;
        } else if (newTime > fileLastModifiedTime) {
            props.clear();
            try {
                props.load(new FileInputStream(propFile));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        fileLastModifiedTime = newTime;
*/
        return props.getProperty(name);
    }
}
