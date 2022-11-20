package io.github.superpro148.armorhud.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.superpro148.armorhud.config.Side;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.superpro148.armorhud.config.ArmorHUDConfig.*;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow protected abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed);

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V",
                    ordinal = 0
            )
    )
    private void armorhud$injectArmorHUD(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (SHOW_ARMORHUD.getValue()) this.armorhud$renderArmorHUD(tickDelta, matrices);
    }

    private void armorhud$renderArmorHUD(float tickDelta, MatrixStack matrices) {
        PlayerEntity player = this.getCameraPlayer();
        int xPos = (ARMORHUD_SIDE.getValue() == Side.RIGHT) ? this.scaledWidth - 22 : 0;
        int yPos = (this.scaledHeight - 82) * ARMORHUD_HEIGHT.getValue() / 100;
        RenderSystem.setShaderTexture(0, new Identifier("armorhud", "textures/gui/armorhud.png"));
        this.drawTexture(matrices, xPos, yPos, 0, 0, 22, 82);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        for (int i = 0; i < 4; i++) {
            this.renderHotbarItem(xPos + 3, yPos + 3 + 20 * i, tickDelta, player, player.getInventory().armor.get(3 - i), 1);
        }
        RenderSystem.disableBlend();
    }
}
