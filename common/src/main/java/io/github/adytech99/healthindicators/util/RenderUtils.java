package io.github.adytech99.healthindicators.util;

import io.github.adytech99.healthindicators.config.ModConfig;
import io.github.adytech99.healthindicators.enums.ArmorTypeEnum;
import io.github.adytech99.healthindicators.enums.HeartTypeEnum;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.LivingEntity;
import org.joml.Matrix4f;

public class RenderUtils {
    public static void drawHeart(Matrix4f model, VertexConsumer vertexConsumer, float x, HeartTypeEnum type, LivingEntity livingEntity) {
        drawHeart(model, vertexConsumer, x, type, livingEntity, 1.0F);
    }

    public static void drawHeart(Matrix4f model, VertexConsumer vertexConsumer, float x, HeartTypeEnum type, LivingEntity livingEntity, float opacity) {
        float minU = 0F;
        float maxU = 1F;
        float minV = 0F;
        float maxV = 1F;

        float heartSize = 9F;
        
        // fix z index
        float z = (type == HeartTypeEnum.EMPTY) ? -0.01F : 0.01F;

        vertexConsumer.vertex(model, x, 0F - heartSize, z).texture(minU, maxV).light(15728880).color(1.0F, 1.0F, 1.0F, opacity);
        vertexConsumer.vertex(model, x - heartSize, 0F - heartSize, z).texture(maxU, maxV).light(15728880).color(1.0F, 1.0F, 1.0F, opacity);
        vertexConsumer.vertex(model, x - heartSize, 0F, z).texture(maxU, minV).light(15728880).color(1.0F, 1.0F, 1.0F, opacity);
        vertexConsumer.vertex(model, x, 0F, z).texture(minU, minV).light(15728880).color(1.0F, 1.0F, 1.0F, opacity);
    }

    public static void drawArmor(Matrix4f model, VertexConsumer vertexConsumer, float x, ArmorTypeEnum type) {
        drawArmor(model, vertexConsumer, x, type, 1.0F);
    }

    public static void drawArmor(Matrix4f model, VertexConsumer vertexConsumer, float x, ArmorTypeEnum type, float opacity) {
        float minU = 0F;
        float maxU = 1F;
        float minV = 0F;
        float maxV = 1F;

        float armorSize = 9F;
        
        // again fix z index
        float z = (type == ArmorTypeEnum.EMPTY) ? -0.01F : 0.01F;

        vertexConsumer.vertex(model, x, 0F - armorSize, z).texture(minU, maxV).light(15728880).color(1.0F, 1.0F, 1.0F, opacity);
        vertexConsumer.vertex(model, x - armorSize, 0F - armorSize, z).texture(maxU, maxV).light(15728880).color(1.0F, 1.0F, 1.0F, opacity);
        vertexConsumer.vertex(model, x - armorSize, 0F, z).texture(maxU, minV).light(15728880).color(1.0F, 1.0F, 1.0F, opacity);
        vertexConsumer.vertex(model, x, 0F, z).texture(minU, minV).light(15728880).color(1.0F, 1.0F, 1.0F, opacity);
    }

    public static String getHealthText(LivingEntity livingEntity) {
        int decimalPlaces = ModConfig.HANDLER.instance().decimal_places;
        float health = livingEntity.getHealth();
        float maxHealth = livingEntity.getMaxHealth();
        float absorption = livingEntity.getAbsorptionAmount();

        String healthStr = formatToDecimalPlaces(health + absorption, decimalPlaces);
        String maxHealthStr = formatToDecimalPlaces(maxHealth, decimalPlaces);

        if (ModConfig.HANDLER.instance().percentage_based_health) {
            float percentage = ((health + absorption) / maxHealth) * 100;
            String percentageStr = formatToDecimalPlaces(percentage, decimalPlaces);
            return percentageStr + " %";
        } else {
            return healthStr + " / " + maxHealthStr;
        }
    }

    private static String formatToDecimalPlaces(float value, int decimalPlaces) {
        if (decimalPlaces == 0) {
            return String.format("%.0f", value);
        } else {
            String decimalPlaces2 = String.valueOf(decimalPlaces);
            return String.format("%." + decimalPlaces2 + "f", value);
        }
    }
}
