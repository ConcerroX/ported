package concerrox.ported.content.springtolife.fallentree

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator

class FallenTreeConfiguration private constructor(
    val trunkProvider: BlockStateProvider,
    val logLength: IntProvider,
    val stumpDecorators: MutableList<TreeDecorator>,
    val logDecorators: MutableList<TreeDecorator>
) : FeatureConfiguration {

    class FallenTreeConfigurationBuilder(
        private val trunkProvider: BlockStateProvider, private val logLength: IntProvider
    ) {

        private var stumpDecorators = mutableListOf<TreeDecorator>()
        private var logDecorators = mutableListOf<TreeDecorator>()

        fun stumpDecorators(stumpDecorators: MutableList<TreeDecorator>): FallenTreeConfigurationBuilder {
            this.stumpDecorators = stumpDecorators
            return this
        }

        fun logDecorators(logDecorators: MutableList<TreeDecorator>): FallenTreeConfigurationBuilder {
            this.logDecorators = logDecorators
            return this
        }

        fun build(): FallenTreeConfiguration {
            return FallenTreeConfiguration(trunkProvider, logLength, stumpDecorators, logDecorators)
        }

    }

    companion object {
        val CODEC: Codec<FallenTreeConfiguration> = RecordCodecBuilder.create { builder ->
            builder.group(
                BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter { it.trunkProvider },
                IntProvider.codec(0, 16).fieldOf("log_length").forGetter { it.logLength },
                TreeDecorator.CODEC.listOf().fieldOf("stump_decorators").forGetter { it.stumpDecorators },
                TreeDecorator.CODEC.listOf().fieldOf("log_decorators").forGetter { it.logDecorators })
                .apply(builder, ::FallenTreeConfiguration)
        }
    }

}