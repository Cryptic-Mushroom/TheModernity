/*
 * Copyright (c) 2019 RGSW.
 * This file belongs to a separate library, made for the Modernity.
 * Licensed under the Apache Licence v2.0. Do not redistribute.
 *
 * Date: 7 - 5 - 2019
 */

package net.rgsw.noise;

/**
 * Fractal-Perlin noise generator for 2D space. This generator uses a specified amount of {@link Perlin2D}-instances as
 * octaves.
 */
public class InverseFractalPerlin2D extends Noise2D {

    private final Perlin2D[] noiseOctaves;

    /**
     * Constructs a Fractal-Perlin noise generator.
     *
     * @param seed    The seed, may be any {@code int}.
     * @param octaves The amount of octaves.
     */
    public InverseFractalPerlin2D( int seed, int octaves ) {
        super( seed );

        if( octaves < 1 ) {
            throw new IllegalArgumentException( "There should be at least one octave." );
        }

        this.noiseOctaves = new Perlin2D[ octaves ];

        for( int i = 0; i < octaves; i++ ) {
            this.noiseOctaves[ i ] = new Perlin2D( seed );
        }
    }

    /**
     * Constructs a Fractal-Perlin noise generator.
     *
     * @param seed    The seed, may be any {@code int}.
     * @param scale   The coordinate scaling along every axis.
     * @param octaves The amount of octaves.
     */
    public InverseFractalPerlin2D( int seed, double scale, int octaves ) {
        super( seed, scale );

        if( octaves < 1 ) {
            throw new IllegalArgumentException( "There should be at least one octave." );
        }

        this.noiseOctaves = new Perlin2D[ octaves ];

        for( int i = 0; i < octaves; i++ ) {
            this.noiseOctaves[ i ] = new Perlin2D( seed );
        }
    }

    /**
     * Constructs a Fractal-Perlin noise generator.
     *
     * @param seed    The seed, may be any {@code int}.
     * @param scaleX  The coordinate scaling along X axis.
     * @param scaleY  The coordinate scaling along Y axis.
     * @param octaves The amount of octaves.
     */
    public InverseFractalPerlin2D( int seed, double scaleX, double scaleY, int octaves ) {
        super( seed, scaleX, scaleY );

        if( octaves < 1 ) {
            throw new IllegalArgumentException( "There should be at least one octave." );
        }

        this.noiseOctaves = new Perlin2D[ octaves ];

        for( int i = 0; i < octaves; i++ ) {
            this.noiseOctaves[ i ] = new Perlin2D( seed );
        }
    }

    @Override
    public double generate( double x, double y ) {
        x /= this.scaleX;
        y /= this.scaleY;

        double t = 0;
        double d = 1;
        double n = 0;

        for( Perlin2D noise : this.noiseOctaves ) {
            t += 1 * d;
            n += noise.generate( x / d, y / d ) * d;
            d *= 2;
        }
        return n / t;
    }

    @Override
    public void setSeed( int seed ) {
        this.seed = seed;
        for( Perlin2D perlin : this.noiseOctaves ) {
            perlin.setSeed( this.seed );
        }
    }
}
