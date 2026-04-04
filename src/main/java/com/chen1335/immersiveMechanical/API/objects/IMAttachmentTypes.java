package com.chen1335.immersiveMechanical.API.objects;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.attachmentDatas.IMBEAttachmentData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class IMAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ImmersiveMechanical.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<IMBEAttachmentData>> BE_ATTACHMENT = ATTACHMENT_TYPES.register("be_attachment", () -> AttachmentType.serializable(IMBEAttachmentData::new).build());
}
