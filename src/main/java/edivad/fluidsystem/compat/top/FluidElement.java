package edivad.fluidsystem.compat.top;

import edivad.fluidsystem.tools.utils.FluidUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;

public class FluidElement extends TOPElement
{
    public static int ID;

    @Nonnull
    protected final FluidStack fluid;
    protected final int capacity;
    protected final int colorLiquid;

    protected FluidElement(@Nonnull FluidStack fluid, int capacity, int colorLiquid)
    {
        super(0xFF000000, 0xFFFFFF);
        this.fluid = fluid;
        this.capacity = capacity;
        this.colorLiquid = colorLiquid;
    }

    public FluidElement(@Nonnull FluidStack fluid, int capacity, TileEntity tile)
    {
        this(fluid, capacity, FluidUtils.getLiquidColorWithBiome(fluid, tile));
    }

    public FluidElement(PacketBuffer buf)
    {
        this(buf.readFluidStack(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(PacketBuffer buf)
    {
        buf.writeFluidStack(fluid);
        buf.writeInt(capacity);
        buf.writeInt(colorLiquid);
    }

    @Override
    public int getScaledLevel(int level)
    {
        if(capacity == 0 || fluid.getAmount() == Integer.MAX_VALUE)
        {
            return level;
        }
        Long fluidAmount = (long) fluid.getAmount();
        long result = fluidAmount * level / capacity;
        return (int) result;
    }

    @Override
    public TextureAtlasSprite getIcon()
    {
        return fluid.isEmpty() ? null : FluidUtils.getFluidTexture(fluid);
    }

    @Override
    public ITextComponent getText()
    {
        String liquidText = fluid.isEmpty() ? "Empty" : fluid.getDisplayName().getString();
        DecimalFormat f = new DecimalFormat("#,##0");
        int amount = fluid.getAmount();
        return new StringTextComponent(String.format("%s: %smB", liquidText, f.format(amount)));
    }

    @Override
    protected boolean applyRenderColor()
    {
        FluidUtils.color(colorLiquid);
        return true;
    }

    @Override
    public int getID()
    {
        return ID;
    }
}
