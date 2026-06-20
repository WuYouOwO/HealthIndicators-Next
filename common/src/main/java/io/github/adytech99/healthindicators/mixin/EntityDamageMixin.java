package io.github.adytech99.healthindicators.mixin;

import io.github.adytech99.healthindicators.DamageDirectionIndicatorRenderer;
import io.github.adytech99.healthindicators.HealthIndicatorsCommon;
import io.github.adytech99.healthindicators.RenderTracker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class EntityDamageMixin {
    // MC 26.2 split the old single damage method into hurtServer(ServerLevel, DamageSource, float)
    // and the client-side hurtClient(DamageSource). This is a client-only mod (reads client.player,
    // renders a HUD), so we hook the client path.
    @Inject(method = "hurtClient(Lnet/minecraft/world/damagesource/DamageSource;)Z", at = @At("TAIL"))
    private void onEntityDamage(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        RenderTracker.onDamage(damageSource, ((LivingEntity) (Object) this));

        if (damageSource.getEntity() instanceof LivingEntity attacker && (Object) this == HealthIndicatorsCommon.client.player) {
            DamageDirectionIndicatorRenderer.markDamageToPlayer(attacker);
        }
    }
}
