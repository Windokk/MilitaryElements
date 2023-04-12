package com.windokkstudio.militaryelements.items.armors;

import com.windokkstudio.militaryelements.MilitaryElements;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

public abstract class Vest_Goggles extends ArmorItem {
    public Vest_Goggles(EquipmentSlot slot, Item.Properties properties) {
        super(new ArmorMaterial() {
            @Override
            public int getDurabilityForSlot(EquipmentSlot slot) {
                return new int[]{13, 15, 16, 11}[slot.getIndex()] * 25;
            }

            @Override
            public int getDefenseForSlot(EquipmentSlot slot) {
                return new int[]{0, 0, 10, 2}[slot.getIndex()];
            }

            @Override
            public int getEnchantmentValue() {
                return 9;
            }

            @Override
            public SoundEvent getEquipSound() {
                return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(""));
            }

            @Override
            public Ingredient getRepairIngredient() {
                return Ingredient.EMPTY;
            }

            @Override
            public String getName() {
                return "vestandgoggles";
            }

            @Override
            public float getToughness() {
                return 2f;
            }

            @Override
            public float getKnockbackResistance() {
                return 0f;
            }
        }, slot, properties);
    }

    public static class Helmet extends Vest_Goggles {
        public Helmet() {
            super(EquipmentSlot.HEAD, new Item.Properties().tab(MilitaryElements.TAB));
        }

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "militaryelements:textures/models/armor/bulletproof_vest__layer_1.png";
        }

        @Override
        public void onArmorTick(ItemStack itemstack, Level world, Player entity) {
            entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 4, 1, (false), (false)));
        }
    }

    public static class Chestplate extends Vest_Goggles {
        public Chestplate() {
            super(EquipmentSlot.CHEST, new Item.Properties().tab(MilitaryElements.TAB));
        }

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "militaryelements:textures/models/armor/bulletproof_vest__layer_1.png";
        }
    }
}
