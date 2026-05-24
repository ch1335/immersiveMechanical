package com.chen1335.immersiveMechanical.data.tag;

import com.chen1335.immersiveMechanical.API.tags.IMBlockTags;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;


public class IMBlockTagsProvider {
    public static void init(RegistrateTagsProvider.IntrinsicImpl<Block> blockIntrinsic) {
        blockIntrinsic.addTag(Tags.Blocks.ORES)
                .addTag(IMBlockTags.ORES_CHROME);

    }
}
