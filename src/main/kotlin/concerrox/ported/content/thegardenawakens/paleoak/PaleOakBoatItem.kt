package concerrox.ported.content.thegardenawakens.paleoak

import net.minecraft.server.level.ServerLevel
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.Boat
import net.minecraft.world.entity.vehicle.ChestBoat
import net.minecraft.world.item.BoatItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.HitResult

class PaleOakBoatItem(private val hasChest: Boolean, properties: Properties) : BoatItem(hasChest, Boat.Type.OAK, properties) {

    companion object {
        private val ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable)
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack?> {
        val stack = player.getItemInHand(hand)
        val hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY)
        if (hitResult.type == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack)
        } else {
            val vec3 = player.getViewVector(1.0f)
            val list = level.getEntities(
                player,
                player.boundingBox.expandTowards(vec3.scale(5.0)).inflate(1.0),
                ENTITY_PREDICATE
            )
            if (!list.isEmpty()) {
                val vec31 = player.eyePosition

                for (entity in list) {
                    val aabb = entity.boundingBox.inflate(entity.pickRadius.toDouble())
                    if (aabb.contains(vec31)) {
                        return InteractionResultHolder.pass(stack)
                    }
                }
            }

            if (hitResult.type == HitResult.Type.BLOCK) {
                val boat = getBoat(level, hitResult, stack, player)
                boat.variant = Boat.Type.OAK
                boat.yRot = player.yRot
                if (!level.noCollision(boat, boat.boundingBox)) {
                    return InteractionResultHolder.fail(stack)
                } else {
                    if (!level.isClientSide) {
                        level.addFreshEntity(boat)
                        level.gameEvent(player, GameEvent.ENTITY_PLACE, hitResult.getLocation())
                        stack.consume(1, player)
                    }

                    player.awardStat(Stats.ITEM_USED.get(this))
                    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide())
                }
            } else {
                return InteractionResultHolder.pass(stack)
            }
        }
    }

    private fun getBoat(level: Level, hitResult: HitResult, stack: ItemStack, player: Player): Boat {
        val vec3 = hitResult.getLocation()
        val boat =
            (if (hasChest) PaleOakChestBoat(level, vec3.x, vec3.y, vec3.z) else PaleOakBoat(level, vec3.x, vec3.y, vec3.z))
        if (level is ServerLevel) {
            EntityType.createDefaultStackConfig<Entity?>(level, stack, player).accept(boat)
        }
        return boat
    }

}