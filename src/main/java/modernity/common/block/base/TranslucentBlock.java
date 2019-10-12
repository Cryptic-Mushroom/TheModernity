/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 12 - 2019
 */

package modernity.common.block.base;

import modernity.common.particle.MDParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class TranslucentBlock extends Block {

    public TranslucentBlock( Block.Properties properties ) {
        super( properties );
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isSideInvisible( BlockState state, BlockState adjacentBlockState, Direction side ) {
        return adjacentBlockState.getBlock() == this || super.isSideInvisible( state, adjacentBlockState, side );
    }

    public static class Salt extends TranslucentBlock {

        public Salt( Block.Properties properties ) {
            super( properties );
        }

        @Override
        @OnlyIn( Dist.CLIENT )
        public void animateTick( BlockState state, World world, BlockPos pos, Random rand ) {
            if( rand.nextInt( 5 ) == 0 ) {
                if( world.getBlockState( pos.down() ).getMaterial().blocksMovement() ) return;

                double x = rand.nextDouble() + pos.getX();
                double y = - 0.05 + pos.getY();
                double z = rand.nextDouble() + pos.getZ();

                world.addParticle( MDParticleTypes.SALT, x, y, z, 0, 0, 0 );
            }
        }
    }
}
