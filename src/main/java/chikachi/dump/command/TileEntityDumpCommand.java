package chikachi.dump.command;

import chikachi.dump.ChikachiEntityDump;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
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
import java.util.Comparator;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TileEntityDumpCommand extends CommandBase {
    @Override
    public String getName() {
        return "tileentitydump";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/tileentitydump [dimId]";
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
            List<TileEntity> tileEntities = worldServer.loadedTileEntityList;
            int tileEntitiesCount = tileEntities.size();
            if (tileEntitiesCount > 0) {
                tileEntities.sort(Comparator.comparing(tileEntityA -> tileEntityA.getClass().getSimpleName()));

                FileWriter writer = new FileWriter(new File(ChikachiEntityDump.instance.dumpLocation, String.format("tileentities.%d.txt", dim)));
                for (TileEntity tileEntity : tileEntities) {
                    BlockPos pos = tileEntity.getPos();
                    writer.write(String.format("%s[x=%d, y=%d, z=%d]\r\n", tileEntity.toString(), pos.getX(), pos.getY(), pos.getZ()));
                }
                writer.close();

                sender.sendMessage(new TextComponentString(String.format("Dumped %d tile entities for dimension %d", tileEntitiesCount, dim)));
            } else {
                sender.sendMessage(new TextComponentString(String.format("No tile entities in dimension %d", dim)).setStyle(new Style().setColor(TextFormatting.RED)));
            }

            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        sender.sendMessage(new TextComponentString("Error dumping tile entities").setStyle(new Style().setColor(TextFormatting.RED)));
    }
}
