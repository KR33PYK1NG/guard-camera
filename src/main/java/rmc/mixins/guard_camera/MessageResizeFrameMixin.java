package rmc.mixins.guard_camera;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import de.maxhenkel.camera.net.MessageResizeFrame;
import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

@Mixin(value = MessageResizeFrame.class)
public abstract class MessageResizeFrameMixin {

    @Inject(method = "Lde/maxhenkel/camera/net/MessageResizeFrame;executeServerSide(Lnet/minecraftforge/fml/network/NetworkEvent$Context;)V",
            remap = false,
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT,
            at = @At(value = "INVOKE",
                     target = "Lde/maxhenkel/camera/entities/ImageEntity;resize(Lde/maxhenkel/camera/net/MessageResizeFrame$Direction;Z)V"))
    private void executeServerSideMixin(NetworkEvent.Context context, CallbackInfo mixin, ServerWorld world, Entity entity) {
        PlayerInteractEntityEvent event = new PlayerInteractEntityEvent(
            context.getSender().getBukkitEntity(),
            entity.getBukkitEntity()
        );
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            mixin.cancel();
        }
    }

}