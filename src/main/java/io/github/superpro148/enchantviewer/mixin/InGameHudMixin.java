package io.github.superpro148.enchantviewer.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.superpro148.enchantviewer.config.EnchantViewerConfig;
import io.github.superpro148.enchantviewer.config.Side;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Shadow private static Identifier WIDGETS_TEXTURE;

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow protected abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed);

    @Shadow protected abstract void renderHotbar(float tickDelta, MatrixStack matrices);

    @Redirect(
            method = "renderHeldItemTooltip",
            at = @At(
                    value = "FIELD",
                    target = "net/minecraft/client/gui/hud/InGameHud.heldItemTooltipFade : I",
                    ordinal = 0
            )
    )
    private int enchantviewer$removeToolTipFade(InGameHud instance) {
        return 1;
    }

    @Redirect(
            method = "renderHeldItemTooltip",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;heldItemTooltipFade:I",
                    ordinal = 1
            )
    )
    private int enchantviewer$removeToolTipFade2(InGameHud instance) {
        return 10;
    }

    /* @Inject(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/gui/hud/InGameHud.drawTexture (Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    ordinal = 0
            )
    )
    private void enchantviewer$renderArmorBar(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        int xPos = this.scaledWidth - 22;
        int yPos = this.scaledHeight / 2 - 41;
        RenderSystem.setShaderTexture(0, new Identifier("enchantviewer", "textures/gui/armorviewer.png"));
        this.drawTexture(matrices, xPos, yPos, 0, 0, 22, 82);
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
    } */

    /* @Inject(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isEmpty()Z",
                    ordinal = 0
            )
    )
    private void enchantviewer$renderArmorPieces(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        int xPos = this.scaledWidth - 22;
        int yPos = this.scaledHeight / 2 - 41;
        PlayerEntity player = this.getCameraPlayer();
        for (int i = 0; i < 4; i++) {
            this.renderHotbarItem(xPos + 3, yPos + 3 + 20 * i, tickDelta, player, player.getInventory().armor.get(3 - i), 1);
        }
    } */

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V",
                    ordinal = 0
            )
    )
    private void enchantviewer$renderArmorBar(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (EnchantViewerConfig.SHOW_ARMORHUD) this.renderArmorBar(tickDelta, matrices);
    }

    private void renderArmorBar(float tickDelta, MatrixStack matrices) {
        PlayerEntity player = this.getCameraPlayer();
        int xPos = (EnchantViewerConfig.ARMORHUD_SIDE == Side.RIGHT) ? this.scaledWidth - 22 : 0;
        int yPos = (this.scaledHeight - 82) * EnchantViewerConfig.ARMORHUD_HEIGHT / 100;
        RenderSystem.setShaderTexture(0, new Identifier("enchantviewer", "textures/gui/armorviewer.png"));
        this.drawTexture(matrices, xPos, yPos, 0, 0, 22, 82);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        for (int i = 0; i < 4; i++) {
            this.renderHotbarItem(xPos + 3, yPos + 3 + 20 * i, tickDelta, player, player.getInventory().armor.get(3 - i), 1);
        }
        RenderSystem.disableBlend();
    }
}
