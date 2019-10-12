/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.common.entity;

import com.google.common.reflect.TypeToken;
import modernity.client.render.entity.RenderFallBlock;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( "modernity" )
public final class MDEntityTypes {
    private static final RegistryHandler<EntityType<?>> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final EntityType<EntityFallBlock> FALL_BLOCK = register( "fall_block", EntityType.Builder.create( EntityFallBlock::new, EntityClassification.MISC ).setTrackingRange( 10 ).setUpdateInterval( 20 ).setShouldReceiveVelocityUpdates( true ).size( 0.98F, 0.98F ) );

    private MDEntityTypes() {
    }

    @SuppressWarnings( "unchecked" )
    public static <T extends Entity> EntityType<T> register( String id, EntityType.Builder<?> builder, String... aliases ) {
        EntityType<T> entry = (EntityType<T>) builder.build( "modernity:" + id );
        ENTRIES.register( id, entry, aliases );
        return entry;
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<EntityType<?>> token = new TypeToken<EntityType<?>>( EntityType.class ) {
        };
        handler.addHandler( (Class<EntityType<?>>) token.getRawType(), ENTRIES );
    }

    @OnlyIn( Dist.CLIENT )
    public static void initEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler( EntityFallBlock.class, new RenderFallBlock.Factory() );
    }
}
