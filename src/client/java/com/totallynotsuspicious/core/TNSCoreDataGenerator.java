package com.totallynotsuspicious.core;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class TNSCoreDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(TNSRecipeGenerator::new);
		pack.addProvider(TNSBiomeGenerator::new);
		pack.addProvider(TNSBiomeTagGenerator::new);
		pack.addProvider(TNSEntityTypeTagGenerator::new);
	}
}
