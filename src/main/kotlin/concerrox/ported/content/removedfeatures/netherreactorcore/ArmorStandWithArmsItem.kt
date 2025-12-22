package concerrox.ported.content.removedfeatures.netherreactorcore

import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ArmorStandItem
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3

class ArmorStandWithArmsItem(properties: Properties) : ArmorStandItem(properties) {

    override fun useOn(context: UseOnContext): InteractionResult {
        val direction = context.clickedFace
        if (direction == Direction.DOWN) {
            return InteractionResult.FAIL
        } else {
            val level = context.level
            val blockplacecontext = BlockPlaceContext(context)
            val blockpos = blockplacecontext.clickedPos
            val itemstack = context.itemInHand
            val vec3 = Vec3.atBottomCenterOf(blockpos)
            val aabb = EntityType.ARMOR_STAND.dimensions.makeBoundingBox(vec3.x(), vec3.y(), vec3.z())
            if (level.noCollision(null, aabb) && level.getEntities(null, aabb).isEmpty()) {
                if (level is ServerLevel) {
                    val consumer =
                        EntityType.createDefaultStackConfig<ArmorStand>(level, itemstack, context.player)
                    val armorstand = EntityType.ARMOR_STAND.create(
                        level, consumer, blockpos, MobSpawnType.SPAWN_EGG, true, true
                    )
                    if (armorstand == null) {
                        return InteractionResult.FAIL
                    }

                    val f =
                        Mth.floor((Mth.wrapDegrees(context.rotation - 180f) + 22.5f) / 45f).toFloat() * 45.0f
                    armorstand.moveTo(armorstand.getX(), armorstand.getY(), armorstand.getZ(), f, 0.0f)
                    armorstand.isShowArms = true
                    level.addFreshEntityWithPassengers(armorstand)
                    level.playSound(
                        null,
                        armorstand.getX(),
                        armorstand.getY(),
                        armorstand.getZ(),
                        SoundEvents.ARMOR_STAND_PLACE,
                        SoundSource.BLOCKS,
                        0.75f,
                        0.8f
                    )
                    armorstand.gameEvent(GameEvent.ENTITY_PLACE, context.player)
                }

                itemstack.shrink(1)
                return InteractionResult.sidedSuccess(level.isClientSide)
            } else {
                return InteractionResult.FAIL
            }
        }
    }

}