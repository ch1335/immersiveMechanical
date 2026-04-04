package com.chen1335.immersiveMechanical.compat.jade;

import com.chen1335.immersiveMechanical.common.blocks.multiblocks.PartBlocks.IMCoilPartBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class IMWailaPlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockIcon(new CoilIconProvider(), IMCoilPartBlock.class);
    }

}
