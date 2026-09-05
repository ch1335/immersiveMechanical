package com.chen1335.immersiveMechanical.common.items.misc;

import com.chen1335.immersiveMechanical.API.objects.IMDataComponents;
import com.chen1335.immersiveMechanical.common.entities.Landmine;
import com.chen1335.immersiveMechanical.common.items.dataComponents.Disguise;
import com.chen1335.immersiveMechanical.definitions.IMEntityTypes;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LandmineItem extends Item {
    public LandmineItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Landmine landmine = IMEntityTypes.LANDMINE.get().create(level);
        if (landmine == null) {
            return InteractionResult.FAIL;
        }
        ItemStack itemInHand = context.getItemInHand();
        landmine.setDisguise(context.getItemInHand().getOrDefault(IMDataComponents.DISGUISE, Disguise.EMPTY).blockState());
        Vec3 hit = context.getClickLocation();
        Direction face = context.getClickedFace();
        double halfWidth = landmine.getBbWidth() / 2.0;
        double halfHeight = landmine.getBbHeight() / 2.0;
        // 实体坐标位于底面中心，将包围盒朝命中面的外侧偏移至刚好贴合。
        landmine.setPos(
                hit.x + face.getStepX() * halfWidth,
                hit.y - halfHeight + face.getStepY() * halfHeight,
                hit.z + face.getStepZ() * halfWidth);

        AABB bounds = landmine.getBoundingBox();
        if (!level.noBlockCollision(landmine, bounds)
                || !level.getEntitiesOfClass(Landmine.class,
                bounds, entity -> !entity.isRemoved()).isEmpty()) {
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide) {
            if (!level.addFreshEntity(landmine)) {
                return InteractionResult.FAIL;
            }
            level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, landmine.position());
            context.getItemInHand().consume(1, context.getPlayer());
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

}
