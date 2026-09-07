package com.chen1335.immersiveMechanical.client.models.model;

import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import com.chen1335.immersiveMechanical.client.models.callbacks.FlyWheelCallBacks;
import com.chen1335.immersiveMechanical.common.blocks.multiblocks.logic.modularFlywheel.flywheel.FlyWheelLogic;
import com.chen1335.immersiveMechanical.definitions.IMMultiblocks;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;


public class FlyWheelParticleModel extends BakedModelWrapper<BakedModel> {
    public FlyWheelParticleModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public ModelData getModelData(
            BlockAndTintGetter level,
            BlockPos pos,
            BlockState state,
            ModelData modelData
    ) {
        ModelData data = super.getModelData(level, pos, state, modelData);

        if (level.getBlockEntity(pos) instanceof IMultiblockBE<?> be
                && be.getHelper().getState() instanceof FlyWheelLogic.State flywheel) {
            return data.derive()
                    .with(FlyWheelCallBacks.getProperty(), flywheel.getMaterial())
                    .build();
        }

        return data;
    }

    @Override
    public TextureAtlasSprite getParticleIcon(ModelData data) {
        Block material = data.get(FlyWheelCallBacks.getProperty());

        if (material == null || material == IMMultiblocks.FLYWHEEL.getBlock()) {
            return getParticleIcon();
        }

        TextureAtlasSprite replacement =
                FlyWheelCallBacks.INSTANCE.getParticleReplacement(material);

        return replacement != null ? replacement : getParticleIcon();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        TextureAtlasSprite replacement =
                FlyWheelCallBacks.INSTANCE.getParticleReplacement(Blocks.IRON_BLOCK);

        return replacement != null ? replacement : super.getParticleIcon();
    }
}

