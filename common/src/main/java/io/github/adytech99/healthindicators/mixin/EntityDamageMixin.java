package io.github.adytech99.healthindicators.mixin;

import io.github.adytech99.healthindicators.DamageDirectionIndicatorRenderer;
import io.github.adytech99.healthindicators.HealthIndicatorsCommon;
import io.github.adytech99.healthindicators.RenderTracker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, remap = false)
public class EntityDamageMixin {
    // MC 26.2 (1.21.2+) split the old single hurt(DamageSource, float) method into the
    // server-side hurtServer(ServerLevel, DamageSource, float) and a client-side counterpart.
    // hurtServer is the verified, stable method where damage is actually applied (confirmed in
    // NeoForge 26.2.x sources). remap=false is required: this is a no-remap/unobfuscated build,
    // so the mixin must match the official name verbatim without searge/notch remapping.
    @Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("TAIL"))
    private void onEntityDamage(ServerLevel level, DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        RenderTracker.onDamage(damageSource, ((LivingEntity) (Object) this));

        if (damageSource.getEntity() instanceof LivingEntity attacker && (Object) this == HealthIndicatorsCommon.client.player) {
            DamageDirectionIndicatorRenderer.markDamageToPlayer(attacker);
        }
    }
}
