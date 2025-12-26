package concerrox.ported.content.springtolife.testblock

import com.mojang.logging.LogUtils
import concerrox.ported.registry.ModBlockEntityTypes
import concerrox.ported.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.slf4j.Logger

class TestBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(ModBlockEntityTypes.TEST_BLOCK.get(), pos, state) {

    private var mode: TestBlockMode
    var message: String = ""
    var isPowered: Boolean = false
    private var triggered = false

    init {
        mode = state.getValue(TestBlock.MODE)
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        TestBlockMode.CODEC.encodeStart(NbtOps.INSTANCE, mode).ifSuccess {
            tag.put("mode", it)
            tag.putString("message", message)
            tag.putBoolean("powered", isPowered)
        }
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        TestBlockMode.CODEC.decode(NbtOps.INSTANCE, tag.getCompound("mode")).ifSuccess {
            mode = it.first
            message = tag.getString("message")
            isPowered = tag.getBoolean("powered")
        }
    }

    private fun updateBlockState() {
        if (level != null) {
            val pos = blockPos
            val blockstate = level!!.getBlockState(pos)
            if (blockstate.`is`(ModBlocks.TEST_BLOCK)) {
                level!!.setBlock(pos, blockstate.setValue(TestBlock.MODE, mode), 2)
            }
        }
    }

    override fun getUpdatePacket(): ClientboundBlockEntityDataPacket {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    override fun getUpdateTag(provider: HolderLookup.Provider): CompoundTag {
        return saveCustomOnly(provider)
    }

    fun getMode(): TestBlockMode {
        return mode
    }

    fun setMode(mode: TestBlockMode) {
        this.mode = mode
        updateBlockState()
    }

    fun getBlockType(): Block = blockState.block

    fun reset() {
        this.triggered = false
        if (this.mode === TestBlockMode.START && this.level != null) {
            this.isPowered = false
            this.level!!.updateNeighborsAt(blockPos, getBlockType())
        }
    }

    fun trigger() {
        if (this.mode === TestBlockMode.START && this.level != null) {
            this.isPowered = true
            val blockpos = this.blockPos
            this.level!!.updateNeighborsAt(blockpos, getBlockType())
            this.level!!.blockTicks.willTickThisTick(blockpos, getBlockType())
            this.log()
        } else {
            if (this.mode === TestBlockMode.LOG) {
                this.log()
            }

            this.triggered = true
        }
    }

    fun log() {
        if (!this.message.isBlank()) {
            LOGGER.info("Test {} (at {}): {}", mode.getSerializedName(), this.blockPos, this.message)
        }
    }

    fun hasTriggered(): Boolean {
        return triggered
    }

    companion object {
        private val LOGGER: Logger = LogUtils.getLogger()
        private const val DEFAULT_MESSAGE = ""
        private const val DEFAULT_POWERED = false
    }

}