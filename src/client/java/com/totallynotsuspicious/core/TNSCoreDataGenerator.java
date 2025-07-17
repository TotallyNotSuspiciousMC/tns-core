package com.totallynotsuspicious.core;

import com.totallynotsuspicious.core.tag.TNSBannerPatternTagGenerator;
import com.totallynotsuspicious.core.tag.TNSBiomeTagGenerator;
import com.totallynotsuspicious.core.tag.TNSEntityTypeTagGenerator;
import com.totallynotsuspicious.core.value.TNSBannerPatternGenerator;
import com.totallynotsuspicious.core.value.TNSBiomeGenerator;
import com.totallynotsuspicious.core.value.TNSRecipeGenerator;
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
		pack.addProvider(TNSBannerPatternGenerator::new);
		pack.addProvider(TNSBannerPatternTagGenerator::new);
	}
}
