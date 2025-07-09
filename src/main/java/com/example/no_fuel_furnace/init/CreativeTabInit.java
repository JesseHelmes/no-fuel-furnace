package com.example.no_fuel_furnace.init;

import com.example.no_fuel_furnace.NoFuelFurnaceMod;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = NoFuelFurnaceMod.MODID, value = Dist.CLIENT)
public class CreativeTabInit {
	@SubscribeEvent
	public static void buildContents(BuildCreativeModeTabContentsEvent event) {
		if(event.getTabKey() != CreativeModeTabs.FUNCTIONAL_BLOCKS) {
			return;
		}

		event.insertAfter(Items.BLAST_FURNACE.getDefaultInstance(),
				ItemInit.NO_FUEL_FURNACE.get().getDefaultInstance(),
				CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}
}
