/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 27 - 2019
 */

package modernity.common.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import modernity.api.util.IBlockProvider;
import modernity.api.util.MovingBlockPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;

public class ClusterBushFeature extends Feature<ClusterBushFeature.Config> {

    public ClusterBushFeature() {
        super( dynamic -> new Config( 1, 1, null ) );
    }

    @Override
    public boolean place( IWorld world, ChunkGenerator<? extends GenerationSettings> chunkGen, Random rand, BlockPos pos, Config config ) {
        int placed = 0;

        MovingBlockPos rpos = new MovingBlockPos();
        {
            for( int i = 0; i < config.iterations; i++ ) {
                int r2 = config.radius * 2 + 1;
                int rx = rand.nextInt( r2 ) + pos.getX();
                int ry = rand.nextInt( r2 ) + pos.getY();
                int rz = rand.nextInt( r2 ) + pos.getZ();

                rpos.setPos( rx, ry, rz );

                if( config.provider.provide( world, rpos, rand ) ) {
                    placed++;
                }
            }
        }

        return placed > 0;
    }

    public static class Config implements IFeatureConfig {
        public final int iterations;
        public final int radius;
        public final IBlockProvider provider;

        public Config( int iterations, int radius, IBlockProvider provider ) {
            this.iterations = iterations;
            this.radius = radius;
            this.provider = provider;
        }

        @Override
        public <T> Dynamic<T> serialize( DynamicOps<T> ops ) {
            return new Dynamic<>( ops );
        }
    }
}
