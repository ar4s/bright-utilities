package pieselki.bright_utilities.events;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import pieselki.bright_utilities.setup.Registration;

public class ChatEvents {
  @SubscribeEvent
	public void onPrintChatMessage(ClientChatReceivedEvent event) {
    Minecraft instance = Minecraft.getInstance();
    List<String> onlinePlayers = instance.level.players().stream().map((AbstractClientPlayerEntity player) -> player.getName().getString()).collect(Collectors.toList());
    ClientPlayerEntity currentPlayer = instance.player;
    String message = event.getMessage().getString();
    String messageContent = message.replaceFirst("<([^>]+)> ", "");
    String currentPlayerName = currentPlayer.getName().getString();

    // text coloring only for online players
    if(messageContent.contains("@")) {
      String mentionOptions = String.join("|", onlinePlayers);
      event.setMessage(new StringTextComponent(message.replaceAll("(@[".concat(mentionOptions).concat("]+)"), "\2475$1\247f")));
    }

    // play sound if someone mentioned you
    if(!message.startsWith("<".concat(currentPlayerName).concat(">")) && messageContent.contains("@".concat(currentPlayerName))) {
      currentPlayer.playSound(Registration.CHAT_MENTION.get(), 1.0F, 1.0F);
    }
	}
}
