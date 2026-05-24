package com.chen1335.immersiveMechanical.definitions;

import com.chen1335.registrate.MetalDefinition;
import com.chen1335.registrate.MetalTypes;

import static com.chen1335.immersiveMechanical.ImmersiveMechanical.REGISTRATE;

public class IMMetals {
    public static final MetalDefinition CHROME = REGISTRATE.metal("chrome")
            .type(MetalTypes.values())
            .register();

    public static final MetalDefinition NICHROME = REGISTRATE.metal("nichrome")
            .type(MetalTypes.values())
            .register();

    public static void init() {

    }
}
