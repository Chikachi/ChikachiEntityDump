package chikachi.dump;

import chikachi.dump.command.EntityDumpCommand;
import chikachi.dump.command.TileEntityDumpCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class ChikachiEntityDump {
    private static final Logger logger = LogManager.getLogger(Constants.MODNAME);
    @Mod.Instance(value = Constants.MODID)
    public static ChikachiEntityDump instance;
    public File dumpLocation;

    public static void Log(String message) {
        Log(message, false);
    }

    public static void Log(String message, boolean warning) {
        logger.log(warning ? Level.WARN : Level.INFO, "[" + Constants.VERSION + "] " + message);
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        this.dumpLocation = new File(event.getModConfigurationDirectory().getParentFile(), "dump");
        if (!this.dumpLocation.exists()) {
            //noinspection ResultOfMethodCallIgnored
            this.dumpLocation.mkdir();
        }
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new EntityDumpCommand());
        event.registerServerCommand(new TileEntityDumpCommand());
    }
}
