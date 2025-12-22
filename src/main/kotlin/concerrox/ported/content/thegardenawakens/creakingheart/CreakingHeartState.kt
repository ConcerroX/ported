package concerrox.ported.content.thegardenawakens.creakingheart

import net.minecraft.util.StringRepresentable

enum class CreakingHeartState(private val id: String) : StringRepresentable {

    UPROOTED("uprooted"),
    DORMANT("dormant"),
    AWAKE("awake");

    override fun toString() = id
    override fun getSerializedName() = id

}