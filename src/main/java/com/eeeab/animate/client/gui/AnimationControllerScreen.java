package com.eeeab.animate.client.gui;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.animate.server.message.PlayAnimationMessage;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.animate.server.inventory.AnimationControllerMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class AnimationControllerScreen extends AbstractContainerScreen<AnimationControllerMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EEEABMobs.MOD_ID, "textures/screens/animation_controller.png");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+");
    private final Entity animationEntity;
    private final Component chatComponent;
    EditBox animationIndexEdit;
    Button playButton;

    public AnimationControllerScreen(AnimationControllerMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.imageWidth = 80;
        this.imageHeight = 83;
        this.chatComponent = text;
        this.animationEntity = container.animationEntity;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        animationIndexEdit.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }


    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            return close();
        }
        if (animationIndexEdit.isFocused()) return animationIndexEdit.keyPressed(key, b, c);
        return super.keyPressed(key, b, c);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        animationIndexEdit.tick();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.chatComponent, this.imageWidth / 2 - this.font.width(this.chatComponent) / 2, 6, 4210752, false);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void init() {
        super.init();
        animationIndexEdit = new EditBox(this.font, this.leftPos + 9, this.topPos + 32, 61, 18, Component.empty()) {
            @Override
            public void moveCursorTo(int pos) {
                super.moveCursorTo(pos);
                doSuggestionMsg();
            }

            @Override
            public void insertText(@NotNull String text) {
                super.insertText(text);
                doSuggestionMsg();
            }

            private void doSuggestionMsg() {
                int length = -1;
                if (animationEntity instanceof EMAnimatedEntity animatedEntity && animatedEntity.getAnimations() != null) {
                    length = animatedEntity.getAnimations().length;
                }
                setSuggestion(getValue().isEmpty() ? Component.literal(length > 0 ? "index[0-" + length + ")" : "index[0]").getString() : null);
            }
        };
        animationIndexEdit.setMaxLength(4);
        this.addWidget(this.animationIndexEdit);
        playButton = Button.builder(Component.literal("play"), e -> {
            if (animationEntity instanceof EMAnimatedEntity animatedEntity) {
                String value = animationIndexEdit.getValue();
                Animation[] animations = animatedEntity.getAnimations();
                if (animations != null && isInteger(value)) {
                    int index = Integer.parseInt(value);
                    if (index >= 0 && index < animations.length) {
                        Animation animation = animations[index];
                        if (animation != null && animatedEntity.getNoAnimation() == animatedEntity.getAnimation()) {
                            EEEABMobs.NETWORK.sendToServer(new PlayAnimationMessage(animationEntity.getId(), ArrayUtils.indexOf(animations, animation)));
                            close();
                        }
                    }
                }
                e.playDownSound(Minecraft.getInstance().getSoundManager());
            }
        }).bounds(this.leftPos + 17, this.topPos + 57, 46, 20).build();
        this.addRenderableWidget(playButton);
    }

    private boolean close() {
        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return false;
    }

    private static boolean isInteger(String str) {
        return str != null && NUMBER_PATTERN.matcher(str).matches();
    }
}
