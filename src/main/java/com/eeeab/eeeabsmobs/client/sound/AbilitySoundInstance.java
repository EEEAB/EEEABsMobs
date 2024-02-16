package com.eeeab.eeeabsmobs.client.sound;

import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.ability.AbilityPeriod;
import com.eeeab.eeeabsmobs.sever.ability.AbilityType;
import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AbilitySoundInstance extends AbstractTickableSoundInstance {
    public final Player user;
    public final Ability<?> ability;

    public AbilitySoundInstance(Player user, SoundEvent soundEvent, AbilityType<Player, ?> ability) {
        super(soundEvent, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.user = user;
        this.x = this.user.getX();
        this.y = this.user.getY();
        this.z = this.user.getZ();
        AbilityCapability.IAbilityCapability capability = AbilityHandler.INSTANCE.getAbilityCapability(this.user);
        if (capability != null && ability != null) {
            this.ability = capability.getAbilitiesMap().get(ability);
        } else {
            this.ability = null;
        }

    }

    @Override
    public void tick() {
        if (this.user.isAlive()) {
            this.x = this.user.getX();
            this.y = this.user.getY();
            this.z = this.user.getZ();
        } else {
            this.stop();
        }
    }

    @Override
    public boolean canPlaySound() {
        return !this.user.isSilent() && !this.user.isRemoved() && this.ability != null && this.ability.isUsing() && AbilityPeriod.AbilityPeriodType.STARTUP != ability.getSection().periodType;
    }
}
