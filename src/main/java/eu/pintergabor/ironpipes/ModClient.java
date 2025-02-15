package eu.pintergabor.ironpipes;

import net.fabricmc.api.ClientModInitializer;
import eu.pintergabor.ironpipes.networking.ModClientNetworking;

public class ModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModClientNetworking.registerPacketReceivers();
	}
}
