package com.chen1335.registrate.devData;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IEDamageTypeTagsProvider extends TagsProvider<DamageType> implements RegistrateProvider {
    private final AbstractRegistrate<?> parent;

    public IEDamageTypeTagsProvider(AbstractRegistrate<?> parent, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, parent.getModid(), existingFileHelper);
        this.parent = parent;
    }


    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        parent.genData(IEProviderTypes.DAMAGE_TYPE_TAG, this);
    }

    @Override
    public TagAppender<DamageType> tag(TagKey<DamageType> tag) {
        return super.tag(tag);
    }

    @Override
    public LogicalSide getSide() {
        return LogicalSide.SERVER;
    }
}
