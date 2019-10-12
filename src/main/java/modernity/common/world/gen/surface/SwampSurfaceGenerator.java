/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 2 - 2019
 */

package modernity.common.world.gen.surface;

import modernity.api.util.MovingBlockPos;
import modernity.common.biome.ModernityBiome;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.MDSurfaceGenSettings;
import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.noise.FractalOpenSimplex2D;
import net.rgsw.noise.INoise3D;

import java.util.Random;

public class SwampSurfaceGenerator implements ISurfaceGenerator<MDSurfaceGenSettings> {

    private static final BlockState GRASS = MDBlocks.DARK_GRASS_BLOCK.getDefaultState();
    private static final BlockState DIRT = MDBlocks.DARK_DIRT.getDefaultState();
    private static final BlockState MUD = MDBlocks.MUD.getDefaultState();

    private FractalOpenSimplex2D marshNoise;
    private FractalOpenSimplex2D marshGroupNoise;

    @Override
    public void init( Random rand, MDSurfaceGenSettings settings ) {
        marshNoise = new FractalOpenSimplex2D( rand.nextInt(), 3.26224, 3 );
        marshGroupNoise = new FractalOpenSimplex2D( rand.nextInt(), 31.46233, 3 );
    }

    @Override
    public void buildSurface( IChunk chunk, int cx, int cz, int x, int z, Random rand, ModernityBiome biome, INoise3D surfaceNoise, MovingBlockPos rpos, MDSurfaceGenSettings settings ) {
        int ctrl = 0;
        BlockState secondLayers = null;
        for( int y = 255; y >= 0; y-- ) {
            rpos.setPos( x, y, z );
            if( ctrl >= 0 && ! chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = - 1;
            } else if( ctrl == - 1 && chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = (int) ( 3 + 2 * surfaceNoise.generate( x + cx * 16, y, z + cz * 16 ) );

                boolean underwater = y < settings.getWaterLevel() - 1;
                boolean marsh = false;
                secondLayers = underwater ? MUD : DIRT;
                if( y == settings.getWaterLevel() - 2 ) {
                    double groupNoise = marshGroupNoise.generateMultiplied( cx * 16 + x, cz * 16 + z, 8 ) + 1;
                    if( groupNoise > 0 ) {
                        double noise = marshNoise.generateMultiplied( cx * 16 + x, cz * 16 + z, 8 );
                        if( noise > 0 ) {
                            rpos.moveUp();
                            chunk.setBlockState( rpos, GRASS, false );
                            rpos.moveDown();
                            secondLayers = DIRT;
                            marsh = true;
                        }
                    }
                }
                if( marsh ) {
                    chunk.setBlockState( rpos, secondLayers, false );
                } else {
                    chunk.setBlockState( rpos, underwater ? MUD : GRASS, false );
                }
            } else if( ctrl > 0 ) {
                ctrl--;
                chunk.setBlockState( rpos, secondLayers, false );
            }
        }
    }
}
