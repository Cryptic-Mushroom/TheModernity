package modernity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import modernity.api.event.ModernityProxyReadyEvent;
import modernity.common.util.ProxyCommon;
import modernity.common.util.RegistryHandler;

@Mod( "modernity" )
public class Modernity {
    private static final Logger LOGGER = LogManager.getLogger();

    public static ProxyCommon proxy;

    public Modernity() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::commonSetup );
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::clientSetup );
        FMLJavaModLoadingContext.get().getModEventBus().addListener( this::serverSetup );

        FMLJavaModLoadingContext.get().getModEventBus().register( new RegistryHandler() );
    }

    // MAYBE: Is this method necessary?
    private void commonSetup( FMLCommonSetupEvent event ) {
    }

    private void clientSetup( FMLClientSetupEvent event ) {
        try {
            Class cls = Class.forName( "modernity.client.proxy.ProxyClient" );
            proxy = (ProxyCommon) cls.newInstance();
        } catch( ClassNotFoundException | IllegalAccessException | InstantiationException e ) {
            throw new IllegalStateException( "Unable to instantiate ProxyClient", e );
        }
        initProxy( Dist.CLIENT );
    }

    private void serverSetup( FMLDedicatedServerSetupEvent event ) {
        // MAYBE: ProxyServer?
        proxy = new ProxyCommon();
        initProxy( Dist.DEDICATED_SERVER );
    }

    private void initProxy( Dist dist ) {
        proxy.registerListeners();
        MinecraftForge.EVENT_BUS.register( proxy );
        MinecraftForge.EVENT_BUS.post( new ModernityProxyReadyEvent( dist, proxy ) );
    }
}
