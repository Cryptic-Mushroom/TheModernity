/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome.layer;

import modernity.common.generator.biome.core.IRegionRNG;

public class SmoothingLayer implements ICastleTransformerLayer {
    public static final SmoothingLayer INSTANCE = new SmoothingLayer();

    protected SmoothingLayer() {
    }

    @Override
    public int generate( IRegionRNG rng, int center, int negX, int posX, int negZ, int posZ ) {
        boolean x = negX == posX;
        boolean z = negZ == posZ;
        if( x == z ) {
            if( x ) { // Both axes have equal values
                return rng.pickRandom( negX, negZ );
            } else { // All sides are different
                return center;
            }
        } else { // One axis has equal values
            return x ? negX : negZ;
        }
    }
}
