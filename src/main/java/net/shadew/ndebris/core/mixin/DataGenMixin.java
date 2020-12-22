package net.shadew.ndebris.core.mixin;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

import net.shadew.ndebris.data.DataMain;

@Mixin(Main.class)
public class DataGenMixin {
    @Inject(method = "main", at = @At("HEAD"), cancellable = true)
    private static void onDataMain(CallbackInfo info) {
        boolean data = Boolean.parseBoolean(System.getProperty("natures.debris.datagen"));
        if (data) {
            String[] args = {
                "-all",
                "-output", "../src/gen/resources/"
            };

            try {
                DataMain.main(args);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                info.cancel();
            }
        }
    }
}
