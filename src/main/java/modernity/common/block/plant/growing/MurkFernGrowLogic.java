/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 01 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.growing;

import modernity.common.block.MDBlocks;
import modernity.common.block.farmland.IFarmland;
import modernity.common.item.MDItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class MurkFernGrowLogic extends SpreadingGrowLogic {
    public MurkFernGrowLogic() {
        super( MDBlocks.MURK_FERN );
    }

    public boolean growFromFloor( World world, BlockPos pos, BlockState state, Random rand ) {
        if( world.isRemote ) return true;
        return grow( world, pos, rand, true );
    }

    @Override
    protected boolean isFertilizer( ItemStack item ) {
        return item.getItem().isIn( MDItemTags.FERTILIZER );
    }

    @Override
    protected GrowType checkResources( IFarmland logic, Random rand ) {
        if( logic.isDecayed() ) {
            return rand.nextInt( 2 ) == 0 ? GrowType.DIE : GrowType.NONE;
        } else if( logic.isFertile() ) {
            return rand.nextInt( 16 ) < 6 ? GrowType.GROW : GrowType.NONE;
        } else if( logic.isWet() ) {
            return rand.nextInt( 16 ) < 1 ? GrowType.GROW : GrowType.NONE;
        } else {
            return GrowType.NONE;
        }
    }

    @Override
    protected void consumeResources( IFarmland logic, Random rand ) {
        logic.decreaseLevel();
    }

    @Override
    protected boolean grow( World world, BlockPos pos, Random rand, boolean isItem ) {
        boolean isTall = world.getBlockState( pos ).getBlock() == MDBlocks.TALL_MURK_FERN;
        if( isTall ) {
            if( isItem ) return false;
            return super.grow( world, pos, rand, false );
        } else {
            if( isItem || rand.nextInt( 3 ) == 0 ) {
                if( canGrowLarge( world, pos ) ) {
                    MDBlocks.TALL_MURK_FERN.growAt( world, pos );
                    return true;
                }
                return false;
            } else {
                return super.grow( world, pos, rand, false );
            }
        }
    }

    private boolean canGrowLarge( World world, BlockPos pos ) {
        return MDBlocks.TALL_MURK_FERN.isValidPosition( world.getBlockState( pos ), world, pos ) && world.isAirBlock( pos.up() );
    }

    @Override
    protected boolean canPlacePlant( World world, BlockPos pos, Random rand ) {
        return MDBlocks.MURK_FERN.canGenerateAt( world, pos, world.getBlockState( pos ) );
    }

    @Override
    protected void placePlant( World world, BlockPos pos, Random rand ) {
        MDBlocks.MURK_FERN.growAt( world, pos );
    }

    @Override
    protected void killPlant( World world, BlockPos pos, Random rand ) {
        BlockState state = world.getBlockState( pos );
        if( state.getBlock() == MDBlocks.TALL_MURK_FERN ) {
            MDBlocks.TALL_MURK_FERN.kill( world, pos, state );
        } else {
            MDBlocks.MURK_FERN.kill( world, pos, state );
        }
    }
}
