package me.codexadrian.cobaltarmaments.tools;

import me.codexadrian.cobaltarmaments.CobaltArmaments;
import me.codexadrian.cobaltarmaments.CobaltTool;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CobaltAxe extends AxeItem implements CobaltTool {

    public CobaltAxe(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return CobaltTool.super.use(world, user, hand);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return attemptEnergyDrain(stack, 2);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (miner instanceof PlayerEntity player && CobaltArmaments.getIfEmpowered(stack)) {
            ArrayList<BlockPos> cachedPositions = new ArrayList<>();
            cachedPositions.add(pos);
            int index = 0;
            int limit = 32;
            while (index < limit) {
                ArrayList<BlockPos> newCachedPositions = new ArrayList<>();
                for (BlockPos logPos : cachedPositions) {
                    ArrayList<BlockPos> newPositions = this.getLogsAroundPosition(newCachedPositions, logPos, world, limit);
                    newCachedPositions.addAll(newPositions);
                }
                for (BlockPos logPos : newCachedPositions) {
                    world.breakBlock(logPos, !player.isCreative());
                    index++;
                    if(index > limit) break;
                }
                if(newCachedPositions.size() == 0) break;
                cachedPositions = newCachedPositions;
            }
        }
        return attemptEnergyDrain(stack, 1);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        CobaltTool.super.appendTooltip(stack, world, tooltip, context);
    }


    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return CobaltTool.super.isBarVisible(stack);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return CobaltTool.super.getBarWidth(stack);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return CobaltTool.super.getBarColor(stack);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }

    public ArrayList<BlockPos> getLogsAroundPosition(ArrayList<BlockPos> existingList, BlockPos pos, World world, int limit) {
        ArrayList<BlockPos> foundBlocks = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockState currentBlock = world.getBlockState(pos.add(x, y, z));
                        if (currentBlock.isIn(BlockTags.LOGS) && !existingList.contains(pos.add(x, y, z)) && !foundBlocks.contains(pos.add(x,y,z)) && i < limit) {
                            foundBlocks.add(pos.add(x, y, z));
                            i++;
                        }
                    }
                }
            }
        }
        return foundBlocks;
    }
}
