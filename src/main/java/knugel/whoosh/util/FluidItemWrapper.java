package knugel.whoosh.util;

import cofh.core.util.helpers.FluidHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidItemWrapper implements ICapabilityProvider {

    final ItemStack stack;
    final IFluidItem container;
    final boolean canFill;
    final boolean canDrain;

    public FluidItemWrapper(ItemStack stackIn, IFluidItem containerIn) {

        stack = stackIn;
        container = containerIn;
        canFill = true;
        canDrain = true;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing from) {

        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, final EnumFacing from) {

        if (!hasCapability(capability, from)) {
            return null;
        }
        return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(new IFluidHandlerItem() {

            @Override
            public IFluidTankProperties[] getTankProperties() {

                return new IFluidTankProperties[] { new FluidTankProperties(container.getFluid(stack), container.getTankCapacity(stack), canFill, canDrain) };
            }

            @Override
            public int fill(FluidStack resource, boolean doFill) {

                return container.fill(stack, resource, doFill);
            }

            @Nullable
            @Override
            public FluidStack drain(FluidStack resource, boolean doDrain) {

                if (FluidHelper.isFluidEqual(resource, container.getFluid(stack))) {
                    return container.drain(stack, resource.amount, doDrain);
                }
                return null;
            }

            @Nullable
            @Override
            public FluidStack drain(int maxDrain, boolean doDrain) {

                return container.drain(stack, maxDrain, doDrain);
            }

            @Nonnull
            @Override
            public ItemStack getContainer() {

                return stack;
            }

        });
    }

}