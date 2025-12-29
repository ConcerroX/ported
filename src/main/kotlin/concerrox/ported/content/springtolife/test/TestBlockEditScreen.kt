package concerrox.ported.content.springtolife.test

import concerrox.ported.registry.ModBlocks
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.CycleButton
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class TestBlockEditScreen(blockEntity: TestBlockEntity) : Screen(TITLE) {

    private val position = blockEntity.blockPos
    private var mode: TestBlockMode
    private var message: String
    private var messageEdit: EditBox? = null

    public override fun init() {
        this.messageEdit =
            EditBox(this.font, this.width / 2 - 152, 80, 240, 20, Component.translatable("test_block.message"))
        this.messageEdit!!.setMaxLength(128)
        this.messageEdit!!.value = this.message
        this.addRenderableWidget(messageEdit!!)
        this.updateMode(this.mode)
        this.addRenderableWidget(
            CycleButton.builder(TestBlockMode::displayName).withValues(
                MODES
            ).displayOnlyValue().create(
                this.width / 2 - 4 - 150, 185, 50, 20, TITLE
            ) { _, mode -> this.updateMode(mode) })
        this.addRenderableWidget(
            Button.builder(
                CommonComponents.GUI_DONE
            ) { _ -> this.onDone() }.bounds(this.width / 2 - 4 - 150, 210, 150, 20).build()
        )
        this.addRenderableWidget(
            Button.builder(CommonComponents.GUI_CANCEL) { onCancel() }.bounds(this.width / 2 + 4, 210, 150, 20).build()
        )
    }

    override fun setInitialFocus() {
        if (this.messageEdit != null) {
            this.setInitialFocus(this.messageEdit!!)
        } else {
            super.setInitialFocus()
        }
    }

    override fun render(p_397107_: GuiGraphics, p_397213_: Int, p_397981_: Int, p_397804_: Float) {
        super.render(p_397107_, p_397213_, p_397981_, p_397804_)
        p_397107_.drawCenteredString(this.font, this.title, this.width / 2, 10, -1)
        if (this.mode !== TestBlockMode.START) {
            p_397107_.drawString(this.font, MESSAGE_LABEL, this.width / 2 - 153, 70, -6250336)
        }

        p_397107_.drawString(this.font, this.mode.detailedMessage, this.width / 2 - 153, 174, -6250336)
    }

    override fun isPauseScreen(): Boolean {
        return false
    }

    val isInGameUi: Boolean
        get() = true

    private fun onDone() {
        this.message = this.messageEdit!!.value
        this.minecraft!!.connection?.send(ServerboundSetTestBlockPacket(position, this.mode, this.message))
        this.onClose()
    }

    override fun onClose() {
        this.onCancel()
    }

    private fun onCancel() {
        this.minecraft!!.setScreen(null as Screen?)
    }

    private fun updateMode(mode: TestBlockMode) {
        this.mode = mode
        this.messageEdit!!.visible = mode !== TestBlockMode.START
    }

    init {
        this.mode = blockEntity.getMode()
        this.message = blockEntity.message
    }

    companion object {
        private val MODES = TestBlockMode.entries.toTypedArray().toList()
        private val TITLE = Component.translatable(ModBlocks.TEST_BLOCK.get().descriptionId)
        private val MESSAGE_LABEL = Component.translatable("test_block.message")
    }

}