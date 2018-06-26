package chikachi.dump.command;

import chikachi.dump.ChikachiEntityDump;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.lang3.math.NumberUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EntityDumpCommand extends CommandBase {
    @Override
    public String getName() {
        return "entitydump";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/entitydump [dimId]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        int dim = 0;
        if (args.length > 0 && NumberUtils.isCreatable(args[0])) {
            dim = Integer.parseInt(args[0]);
        }

        WorldServer worldServer = DimensionManager.getWorld(dim);
        if (worldServer == null) {
            sender.sendMessage(new TextComponentString("Dimension not found").setStyle(new Style().setColor(TextFormatting.RED)));
            return;
        }


        try {
            List<Entity> entities = new ArrayList<>(worldServer.loadedEntityList);
            int entityCount = entities.size();
            if (entityCount > 0) {
                entities.sort(Comparator.comparing(entityA -> entityA.getClass().getSimpleName()));

                FileWriter writer = new FileWriter(new File(ChikachiEntityDump.instance.dumpLocation, String.format("entities.%d.txt", dim)));
                for (Entity entity : entities) {
                    writer.write(entity.toString() + "\r\n");
                }
                writer.close();

                sender.sendMessage(new TextComponentString(String.format("Dumped %d entities for dimension %d", entityCount, dim)));
            } else {
                sender.sendMessage(new TextComponentString(String.format("No entities in dimension %d", dim)).setStyle(new Style().setColor(TextFormatting.RED)));
            }

            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        sender.sendMessage(new TextComponentString("Error dumping entities").setStyle(new Style().setColor(TextFormatting.RED)));
    }
}
