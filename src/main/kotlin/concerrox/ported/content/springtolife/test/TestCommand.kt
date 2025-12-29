//package concerrox.ported.content.springtolife.test
//
//import com.mojang.brigadier.Command
//import com.mojang.brigadier.CommandDispatcher
//import com.mojang.brigadier.arguments.BoolArgumentType
//import com.mojang.brigadier.arguments.IntegerArgumentType
//import com.mojang.brigadier.arguments.StringArgumentType
//import com.mojang.brigadier.builder.ArgumentBuilder
//import com.mojang.brigadier.builder.LiteralArgumentBuilder
//import com.mojang.brigadier.builder.RequiredArgumentBuilder
//import com.mojang.brigadier.context.CommandContext
//import com.mojang.brigadier.exceptions.CommandSyntaxException
//import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType
//import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
//import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
//import com.mojang.brigadier.suggestion.SuggestionProvider
//import com.mojang.brigadier.suggestion.Suggestions
//import com.mojang.brigadier.suggestion.SuggestionsBuilder
//import net.minecraft.ChatFormatting
//import net.minecraft.SharedConstants
//import net.minecraft.commands.CommandBuildContext
//import net.minecraft.commands.CommandSourceStack
//import net.minecraft.commands.Commands
//import net.minecraft.commands.SharedSuggestionProvider
//import net.minecraft.commands.arguments.ResourceArgument
//import net.minecraft.core.*
//import net.minecraft.core.registries.Registries
//import net.minecraft.gametest.framework.*
//import net.minecraft.gametest.framework.TestCommand
//import net.minecraft.network.chat.Component
//import net.minecraft.network.chat.ComponentUtils
//import net.minecraft.network.chat.Style
//import net.minecraft.util.Mth
//import net.minecraft.world.level.block.Blocks
//import net.minecraft.world.level.block.Rotation
//import net.minecraft.world.level.block.entity.BlockEntityType
//import net.minecraft.world.level.levelgen.Heightmap
//import net.minecraft.world.phys.BlockHitResult
//import org.apache.commons.lang3.mutable.MutableInt
//import java.util.*
//import java.util.concurrent.CompletableFuture
//import java.util.function.Consumer
//import java.util.function.Function
//import java.util.function.Supplier
//import java.util.function.UnaryOperator
//import java.util.stream.Stream
//
//object TestCommand {
//    const val TEST_NEARBY_SEARCH_RADIUS: Int = 15
//    const val TEST_FULL_SEARCH_RADIUS: Int = 250
//    const val VERIFY_TEST_GRID_AXIS_SIZE: Int = 10
//    const val VERIFY_TEST_BATCH_SIZE: Int = 100
//    private const val DEFAULT_CLEAR_RADIUS = 250
//    private const val MAX_CLEAR_RADIUS = 1024
//    private const val TEST_POS_Z_OFFSET_FROM_PLAYER = 3
//    private const val DEFAULT_X_SIZE = 5
//    private const val DEFAULT_Y_SIZE = 5
//    private const val DEFAULT_Z_SIZE = 5
//    private val CLEAR_NO_TESTS =
//        SimpleCommandExceptionType(Component.translatable("commands.test.clear.error.no_tests"))
//    private val RESET_NO_TESTS =
//        SimpleCommandExceptionType(Component.translatable("commands.test.reset.error.no_tests"))
//    private val TEST_INSTANCE_COULD_NOT_BE_FOUND =
//        SimpleCommandExceptionType(Component.translatable("commands.test.error.test_instance_not_found"))
//    private val NO_STRUCTURES_TO_EXPORT =
//        SimpleCommandExceptionType(Component.literal("Could not find any structures to export"))
//    private val NO_TEST_INSTANCES =
//        SimpleCommandExceptionType(Component.translatable("commands.test.error.no_test_instances"))
//    private val NO_TEST_CONTAINING = Dynamic3CommandExceptionType { p_396466_, p_396467_, p_396468_ ->
//        Component.translatableEscape(
//            "commands.test.error.no_test_containing_pos", p_396466_, p_396467_, p_396468_
//        )
//    }
//    private val TOO_LARGE = DynamicCommandExceptionType { p_399389_ ->
//        Component.translatableEscape(
//            "commands.test.error.too_large", p_399389_
//        )
//    }
//
//    @Throws(CommandSyntaxException::class)
//    private fun reset(testFinder: TestFinder<*>): Int {
//        TestCommand.stopTests()
//        val i = TestCommand.toGameTestInfos(testFinder.source(), RetryOptions.noRetries(), testFinder)
//            .map<Int?> { p_396435_: GameTestInfo? -> TestCommand.resetGameTestInfo(testFinder.source(), p_396435_) }
//            .toList().size
//        if (i == 0) {
//            throw TestCommand.CLEAR_NO_TESTS.create()
//        } else {
//            testFinder.source()
//                .sendSuccess(Supplier { Component.translatable("commands.test.reset.success", *arrayOf<Any>(i)) }, true)
//            return i
//        }
//    }
//
//    @Throws(CommandSyntaxException::class)
//    private fun clear(testFinder: TestFinder<*>): Int {
//        TestCommand.stopTests()
//        val commandsourcestack = testFinder.source()
//        val serverlevel = commandsourcestack.getLevel()
//        val list: MutableList<TestInstanceBlockEntity> = testFinder.findTestPos().flatMap({ p_404172_ ->
//            serverlevel.getBlockEntity<T?>(p_404172_, BlockEntityType.TEST_INSTANCE_BLOCK).stream()
//        }).toList()
//
//        for (testinstanceblockentity in list) {
//            StructureUtils.clearSpaceForStructure(testinstanceblockentity.getStructureBoundingBox(), serverlevel)
//            testinstanceblockentity.removeBarriers()
//            serverlevel.destroyBlock(testinstanceblockentity.getBlockPos(), false)
//        }
//
//        if (list.isEmpty()) {
//            throw TestCommand.CLEAR_NO_TESTS.create()
//        } else {
//            commandsourcestack.sendSuccess(Supplier {
//                Component.translatable(
//                    "commands.test.clear.success", *arrayOf<Any>(list.size)
//                )
//            }, true)
//            return list.size
//        }
//    }
//
//    @Throws(CommandSyntaxException::class)
//    private fun export(testFinder: TestFinder<*>): Int {
//        val commandsourcestack = testFinder.source()
//        val serverlevel = commandsourcestack.getLevel()
//        var i = 0
//        var flag = true
//
//        val iterator: MutableIterator<BlockPos?> = testFinder.findTestPos().iterator()
//        while (iterator.hasNext()) {
//            val blockpos = iterator.next() as BlockPos
//            val var8 = serverlevel.getBlockEntity(blockpos)
//            if (var8 !is TestInstanceBlockEntity) {
//                throw TestCommand.TEST_INSTANCE_COULD_NOT_BE_FOUND.create()
//            }
//
//            val testinstanceblockentity: TestInstanceBlockEntity? = var8 as TestInstanceBlockEntity?
//            Objects.requireNonNull<CommandSourceStack?>(commandsourcestack)
//            if (!testinstanceblockentity.exportTest({ message: Component? ->
//                    commandsourcestack.sendSystemMessage(
//                        message
//                    )
//                })) {
//                flag = false
//            }
//            ++i
//        }
//
//        if (i == 0) {
//            throw TestCommand.NO_STRUCTURES_TO_EXPORT.create()
//        } else {
//            val s = "Exported " + i + " structures"
//            testFinder.source().sendSuccess(Supplier { Component.literal(s) }, true)
//            return if (flag) 0 else 1
//        }
//    }
//
//    private fun verify(testFinder: TestFinder<*>): Int {
//        TestCommand.stopTests()
//        val commandsourcestack = testFinder.source()
//        val serverlevel = commandsourcestack.getLevel()
//        val blockpos = TestCommand.createTestPositionAround(commandsourcestack)
//        val collection: MutableCollection<GameTestInfo> = Stream.concat<GameTestInfo?>(
//            TestCommand.toGameTestInfos(
//                commandsourcestack, RetryOptions.noRetries(), testFinder
//            ), TestCommand.toGameTestInfo(commandsourcestack, RetryOptions.noRetries(), testFinder, 0)
//        ).toList()
//        FailedTestTracker.forgetFailedTests()
//        val collection1: MutableCollection<GameTestBatch?> = ArrayList<Any?>()
//
//        for (gametestinfo in collection) {
//            for (rotation in Rotation.entries) {
//                val collection2: MutableCollection<GameTestInfo?> = ArrayList<Any?>()
//
//                for (i in 0..99) {
//                    val gametestinfo1 =
//                        GameTestInfo(gametestinfo.getTestHolder(), rotation, serverlevel, RetryOptions(1, true))
//                    gametestinfo1.setTestBlockPos(gametestinfo.getTestBlockPos())
//                    collection2.add(gametestinfo1)
//                }
//
//                val gametestbatch = GameTestBatchFactory.toGameTestBatch(
//                    collection2, gametestinfo.getTest().batch(), rotation.ordinal.toLong()
//                )
//                collection1.add(gametestbatch)
//            }
//        }
//
//        val structuregridspawner = StructureGridSpawner(blockpos, 10, true)
//        val gametestrunner: GameTestRunner = GameTestRunner.Builder.fromBatches(collection1, serverlevel)
//            .batcher(GameTestBatchFactory.fromGameTestInfo(100)).newStructureSpawner(structuregridspawner)
//            .existingStructureSpawner(structuregridspawner).haltOnError().clearBetweenBatches().build()
//        return TestCommand.trackAndStartRunner(commandsourcestack, gametestrunner)
//    }
//
//    private fun run(testFinder: TestFinder<*>, retryOptions: RetryOptions, rotationSteps: Int, testsPerRow: Int): Int {
//        TestCommand.stopTests()
//        val commandsourcestack = testFinder.source()
//        val serverlevel = commandsourcestack.getLevel()
//        val blockpos = TestCommand.createTestPositionAround(commandsourcestack)
//        val collection: MutableCollection<GameTestInfo?> = Stream.concat<GameTestInfo?>(
//            TestCommand.toGameTestInfos(commandsourcestack, retryOptions, testFinder),
//            TestCommand.toGameTestInfo(commandsourcestack, retryOptions, testFinder, rotationSteps)
//        ).toList()
//        if (collection.isEmpty()) {
//            commandsourcestack.sendSuccess(Supplier { Component.translatable("commands.test.no_tests") }, false)
//            return 0
//        } else {
//            FailedTestTracker.forgetFailedTests()
//            commandsourcestack.sendSuccess(Supplier {
//                Component.translatable(
//                    "commands.test.run.running", *arrayOf<Any>(collection.size)
//                )
//            }, false)
//            val gametestrunner = GameTestRunner.Builder.fromInfo(collection, serverlevel)
//                .newStructureSpawner(StructureGridSpawner(blockpos, testsPerRow, false)).build()
//            return TestCommand.trackAndStartRunner(commandsourcestack, gametestrunner)
//        }
//    }
//
//    @Throws(CommandSyntaxException::class)
//    private fun locate(testFinder: TestFinder<*>): Int {
//        testFinder.source().sendSystemMessage(Component.translatable("commands.test.locate.started"))
//        val mutableint = MutableInt(0)
//        val blockpos = BlockPos.containing(testFinder.source().getPosition())
//        testFinder.findTestPos().forEach({ p_396478_ ->
//            val `patt0$temp` = testFinder.source().getLevel().getBlockEntity(p_396478_)
//            if (`patt0$temp` is TestInstanceBlockEntity) {
//                val direction: Direction = `patt0$temp`.getRotation().rotate(Direction.NORTH)
//                val `$$8` = `patt0$temp`.getBlockPos().relative(direction, 2)
//                val `$$9` = direction.getOpposite().toYRot().toInt()
//                val `$$10` =
//                    String.format(Locale.ROOT, "/tp @s %d %d %d %d 0", `$$8`.getX(), `$$8`.getY(), `$$8`.getZ(), `$$9`)
//                val `$$11`: Int = blockpos.getX() - p_396478_.getX()
//                val `$$12`: Int = blockpos.getZ() - p_396478_.getZ()
//                val `$$13` = Mth.floor(Mth.sqrt((`$$11` * `$$11` + `$$12` * `$$12`).toFloat()))
//                val `$$14` = ComponentUtils.wrapInSquareBrackets(
//                    Component.translatable(
//                        "chat.coordinates", *arrayOf<Any?>(p_396478_.getX(), p_396478_.getY(), p_396478_.getZ())
//                    )
//                ).withStyle(UnaryOperator { p_396438_: Style? ->
//                    p_396438_!!.withColor(ChatFormatting.GREEN).withClickEvent(SuggestCommand(`$$10`)).withHoverEvent(
//                        ShowText(
//                            Component.translatable("chat.coordinates.tooltip")
//                        )
//                    )
//                })
//                testFinder.source().sendSuccess(Supplier {
//                    Component.translatable(
//                        "commands.test.locate.found", *arrayOf<Any>(`$$14`, `$$13`)
//                    )
//                }, false)
//                mutableint.increment()
//            }
//        })
//        val i = mutableint.toInt()
//        if (i == 0) {
//            throw TestCommand.NO_TEST_INSTANCES.create()
//        } else {
//            testFinder.source()
//                .sendSuccess(Supplier { Component.translatable("commands.test.locate.done", *arrayOf<Any>(i)) }, true)
//            return i
//        }
//    }
//
//    private fun runWithRetryOptions(
//        argumentBuilder: ArgumentBuilder<CommandSourceStack?, *>,
//        finderGetter: InCommandFunction<CommandContext<CommandSourceStack?>?, TestFinder<*>?>,
//        modifier: Function<ArgumentBuilder<CommandSourceStack?, *>?, ArgumentBuilder<CommandSourceStack?, *>?>
//    ): ArgumentBuilder<CommandSourceStack?, *>? {
//        return argumentBuilder.executes(Command { p_396451_: CommandContext<CommandSourceStack?>? ->
//            TestCommand.run(
//                finderGetter.apply(p_396451_) as TestFinder<*>?, RetryOptions.noRetries(), 0, 8
//            )
//        }).then(
//            (Commands.argument<Int?>("numberOfTimes", IntegerArgumentType.integer(0)).executes(
//                Command { p_396459_: CommandContext<CommandSourceStack?>? ->
//                    TestCommand.run(
//                        finderGetter.apply(
//                            p_396459_
//                        ) as TestFinder<*>?,
//                        RetryOptions(IntegerArgumentType.getInteger(p_396459_, "numberOfTimes"), false),
//                        0,
//                        8
//                    )
//                }) as RequiredArgumentBuilder<*, *>).then(
//                modifier.apply(
//                    Commands.argument<Boolean?>("untilFailed", BoolArgumentType.bool()).executes(
//                        Command { p_396440_: CommandContext<CommandSourceStack?>? ->
//                            TestCommand.run(
//                                finderGetter.apply(
//                                    p_396440_
//                                ) as TestFinder<*>?, RetryOptions(
//                                    IntegerArgumentType.getInteger(p_396440_, "numberOfTimes"),
//                                    BoolArgumentType.getBool(p_396440_, "untilFailed")
//                                ), 0, 8
//                            )
//                        })
//                ) as ArgumentBuilder<*, *>?
//            )
//        )
//    }
//
//    private fun runWithRetryOptions(
//        argumentBuilder: ArgumentBuilder<CommandSourceStack?, *>,
//        finderGetter: InCommandFunction<CommandContext<CommandSourceStack?>?, TestFinder<*>?>
//    ): ArgumentBuilder<CommandSourceStack?, *>? {
//        return TestCommand.runWithRetryOptions(
//            argumentBuilder,
//            finderGetter,
//            Function { p_319485_: ArgumentBuilder<CommandSourceStack?, *>? -> p_319485_ })
//    }
//
//    private fun runWithRetryOptionsAndBuildInfo(
//        argumentBuilder: ArgumentBuilder<CommandSourceStack?, *>,
//        finderGetter: InCommandFunction<CommandContext<CommandSourceStack?>?, TestFinder<*>?>
//    ): ArgumentBuilder<CommandSourceStack?, *>? {
//        return TestCommand.runWithRetryOptions(
//            argumentBuilder, finderGetter, Function { p_319482_: ArgumentBuilder<CommandSourceStack?, *>? ->
//                p_319482_.then(
//                    (Commands.argument<Int?>("rotationSteps", IntegerArgumentType.integer()).executes(
//                        Command { p_396457_: CommandContext<CommandSourceStack?>? ->
//                            TestCommand.run(
//                                finderGetter.apply(
//                                    p_396457_
//                                ) as TestFinder<*>?, RetryOptions(
//                                    IntegerArgumentType.getInteger(p_396457_, "numberOfTimes"),
//                                    BoolArgumentType.getBool(p_396457_, "untilFailed")
//                                ), IntegerArgumentType.getInteger(p_396457_, "rotationSteps"), 8
//                            )
//                        }) as RequiredArgumentBuilder<*, *>).then(
//                        Commands.argument<Int?>("testsPerRow", IntegerArgumentType.integer()).executes(
//                            Command { p_396448_: CommandContext<CommandSourceStack?>? ->
//                                TestCommand.run(
//                                    finderGetter.apply(
//                                        p_396448_
//                                    ) as TestFinder<*>?,
//                                    RetryOptions(
//                                        IntegerArgumentType.getInteger(p_396448_, "numberOfTimes"),
//                                        BoolArgumentType.getBool(p_396448_, "untilFailed")
//                                    ),
//                                    IntegerArgumentType.getInteger(p_396448_, "rotationSteps"),
//                                    IntegerArgumentType.getInteger(p_396448_, "testsPerRow")
//                                )
//                            })
//                    )
//                )
//            })
//    }
//
//    fun register(dispatcher: CommandDispatcher<CommandSourceStack?>, buildContext: CommandBuildContext) {
//        val argumentbuilder = TestCommand.runWithRetryOptionsAndBuildInfo(
//            Commands.argument<Boolean?>(
//                "onlyRequiredTests", BoolArgumentType.bool()
//            ), InCommandFunction { p_396444_ ->
//                TestFinder.builder().failedTests(p_396444_, BoolArgumentType.getBool(p_396444_, "onlyRequiredTests"))
//            })
//        var var10000 = ((Commands.literal("test")
//            .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)) as LiteralArgumentBuilder<*>).then(
//            Commands.literal("run").then(
//                TestCommand.runWithRetryOptionsAndBuildInfo(
//                    Commands.argument<T?>(
//                        "tests", ResourceSelectorArgument.resourceSelector(buildContext, Registries.TEST_INSTANCE)
//                    ), InCommandFunction { p_425189_ ->
//                        TestFinder.builder().byResourceSelection(
//                            p_425189_, ResourceSelectorArgument.getSelectedResources(p_425189_, "tests")
//                        )
//                    })
//            )
//        ) as LiteralArgumentBuilder<*>).then(
//            Commands.literal("runmultiple").then(
//                (Commands.argument<T?>(
//                    "tests", ResourceSelectorArgument.resourceSelector(buildContext, Registries.TEST_INSTANCE)
//                ).executes(
//                    Command { p_425190_: CommandContext<CommandSourceStack?>? ->
//                        TestCommand.run(
//                            TestFinder.builder().byResourceSelection(
//                                p_425190_, ResourceSelectorArgument.getSelectedResources(p_425190_, "tests")
//                            ), RetryOptions.noRetries(), 0, 8
//                        )
//                    }) as RequiredArgumentBuilder<*, *>).then(
//                    Commands.argument<Int?>("amount", IntegerArgumentType.integer()).executes(
//                        Command { p_425191_: CommandContext<CommandSourceStack?>? ->
//                            TestCommand.run(
//                                TestFinder.builder()
//                                    .createMultipleCopies(IntegerArgumentType.getInteger(p_425191_, "amount"))
//                                    .byResourceSelection(
//                                        p_425191_, ResourceSelectorArgument.getSelectedResources(p_425191_, "tests")
//                                    ), RetryOptions.noRetries(), 0, 8
//                            )
//                        })
//                )
//            )
//        ) as LiteralArgumentBuilder<*>
//        var var10001: LiteralArgumentBuilder<*> = Commands.literal("runthese")
//        var var10002: TestFinder.Builder<*>? = TestFinder.builder()
//        Objects.requireNonNull<TestFinder.Builder<*>?>(var10002)
//        var10000 = var10000.then(
//            TestCommand.runWithRetryOptions(
//                var10001,
//                InCommandFunction { context: CommandContext<CommandSourceStack?>? -> var10002!!.allNearby(context) })
//        ) as LiteralArgumentBuilder<*>
//        var10001 = Commands.literal("runclosest")
//        var10002 = TestFinder.builder()
//        Objects.requireNonNull<TestFinder.Builder<*>?>(var10002)
//        var10000 = var10000.then(
//            TestCommand.runWithRetryOptions(
//                var10001,
//                InCommandFunction { context: CommandContext<CommandSourceStack?>? -> var10002!!.nearest(context) })
//        ) as LiteralArgumentBuilder<*>
//        var10001 = Commands.literal("runthat")
//        var10002 = TestFinder.builder()
//        Objects.requireNonNull<TestFinder.Builder<*>?>(var10002)
//        var10000 = var10000.then(
//            TestCommand.runWithRetryOptions(
//                var10001,
//                InCommandFunction { context: CommandContext<CommandSourceStack?>? -> var10002!!.lookedAt(context) })
//        ) as LiteralArgumentBuilder<*>
//        val var9: ArgumentBuilder<*, *> = Commands.literal("runfailed").then(argumentbuilder)
//        var10002 = TestFinder.builder()
//        Objects.requireNonNull<TestFinder.Builder<*>?>(var10002)
//        var literalargumentbuilder: LiteralArgumentBuilder<CommandSourceStack?> = (((((((((((var10000.then(
//            TestCommand.runWithRetryOptionsAndBuildInfo(
//                var9, var10002::failedTests
//            )
//        ) as LiteralArgumentBuilder<*>).then(
//            Commands.literal("verify").then(
//                Commands.argument<T?>(
//                    "tests", ResourceSelectorArgument.resourceSelector(buildContext, Registries.TEST_INSTANCE)
//                ).executes(
//                    Command { p_425188_: CommandContext<CommandSourceStack?>? ->
//                        TestCommand.verify(
//                            TestFinder.builder().byResourceSelection(
//                                p_425188_, ResourceSelectorArgument.getSelectedResources(p_425188_, "tests")
//                            )
//                        )
//                    })
//            )
//        ) as LiteralArgumentBuilder<*>).then(
//            Commands.literal("locate").then(
//                Commands.argument<T?>(
//                    "tests", ResourceSelectorArgument.resourceSelector(buildContext, Registries.TEST_INSTANCE)
//                ).executes(
//                    Command { p_425187_: CommandContext<CommandSourceStack?>? ->
//                        TestCommand.locate(
//                            TestFinder.builder().byResourceSelection(
//                                p_425187_, ResourceSelectorArgument.getSelectedResources(p_425187_, "tests")
//                            )
//                        )
//                    })
//            )
//        ) as LiteralArgumentBuilder<*>).then(
//            Commands.literal("resetclosest").executes(Command { p_396461_: CommandContext<CommandSourceStack?>? ->
//                TestCommand.reset(
//                    TestFinder.builder().nearest(p_396461_)
//                )
//            })
//        ) as LiteralArgumentBuilder<*>).then(
//            Commands.literal("resetthese").executes(Command { p_396453_: CommandContext<CommandSourceStack?>? ->
//                TestCommand.reset(
//                    TestFinder.builder().allNearby(p_396453_)
//                )
//            })
//        ) as LiteralArgumentBuilder<*>).then(
//            Commands.literal("resetthat").executes(Command { p_396462_: CommandContext<CommandSourceStack?>? ->
//                TestCommand.reset(
//                    TestFinder.builder().lookedAt(p_396462_)
//                )
//            })
//        ) as LiteralArgumentBuilder<*>).then(
//            Commands.literal("clearthat").executes(Command { p_396455_: CommandContext<CommandSourceStack?>? ->
//                TestCommand.clear(
//                    TestFinder.builder().lookedAt(p_396455_)
//                )
//            })
//        ) as LiteralArgumentBuilder<*>).then(
//            Commands.literal("clearthese").executes(Command { p_396442_: CommandContext<CommandSourceStack?>? ->
//                TestCommand.clear(
//                    TestFinder.builder().allNearby(p_396442_)
//                )
//            })
//        ) as LiteralArgumentBuilder<*>).then(
//            (Commands.literal("clearall").executes(
//                Command { p_396431_: CommandContext<CommandSourceStack?>? ->
//                    TestCommand.clear(
//                        TestFinder.builder().radius(p_396431_, 250)
//                    )
//                }) as LiteralArgumentBuilder<*>).then(
//                Commands.argument<Int?>("radius", IntegerArgumentType.integer()).executes(
//                    Command { p_396425_: CommandContext<CommandSourceStack?>? ->
//                        TestCommand.clear(
//                            TestFinder.builder().radius(
//                                p_396425_, Mth.clamp(IntegerArgumentType.getInteger(p_396425_, "radius"), 0, 1024)
//                            )
//                        )
//                    })
//            )
//        ) as LiteralArgumentBuilder<*>).then(
//            Commands.literal("stop")
//                .executes(Command { p_319497_: CommandContext<CommandSourceStack?>? -> TestCommand.stopTests() })
//        ) as LiteralArgumentBuilder<*>).then(
//            (Commands.literal("pos").executes(
//                Command { p_128023_: CommandContext<CommandSourceStack?>? ->
//                    TestCommand.showPos(
//                        p_128023_!!.getSource(), "pos"
//                    )
//                }) as LiteralArgumentBuilder<*>).then(
//                Commands.argument<String?>("var", StringArgumentType.word()).executes(
//                    Command { p_128021_: CommandContext<CommandSourceStack?>? ->
//                        TestCommand.showPos(
//                            p_128021_!!.getSource(), StringArgumentType.getString(p_128021_, "var")
//                        )
//                    })
//            )
//        ) as LiteralArgumentBuilder<*>).then(
//            Commands.literal("create").then(
//                (Commands.argument<T?>("id", IdentifierArgument.id())
//                    .suggests(SuggestionProvider { obj: CommandContext<CommandSourceStack?>?, context: SuggestionsBuilder? ->
//                        TestCommand.suggestTestFunction(context)
//                    }).executes(
//                        Command { p_466098_: CommandContext<CommandSourceStack?>? ->
//                            TestCommand.createNewStructure(
//                                p_466098_!!.getSource(), IdentifierArgument.getId(p_466098_, "id"), 5, 5, 5
//                            )
//                        }) as RequiredArgumentBuilder<*, *>).then(
//                    (Commands.argument<Int?>("width", IntegerArgumentType.integer()).executes(
//                        Command { p_466097_: CommandContext<CommandSourceStack?>? ->
//                            TestCommand.createNewStructure(
//                                p_466097_!!.getSource(),
//                                IdentifierArgument.getId(p_466097_, "id"),
//                                IntegerArgumentType.getInteger(p_466097_, "width"),
//                                IntegerArgumentType.getInteger(p_466097_, "width"),
//                                IntegerArgumentType.getInteger(p_466097_, "width")
//                            )
//                        }) as RequiredArgumentBuilder<*, *>).then(
//                        Commands.argument<Int?>("height", IntegerArgumentType.integer()).then(
//                            Commands.argument<Int?>("depth", IntegerArgumentType.integer())
//                                .executes(Command { p_466096_: CommandContext<CommandSourceStack?>? ->
//                                    TestCommand.createNewStructure(
//                                        p_466096_!!.getSource(),
//                                        IdentifierArgument.getId(p_466096_, "id"),
//                                        IntegerArgumentType.getInteger(p_466096_, "width"),
//                                        IntegerArgumentType.getInteger(p_466096_, "height"),
//                                        IntegerArgumentType.getInteger(p_466096_, "depth")
//                                    )
//                                })
//                        )
//                    )
//                )
//            )
//        ) as LiteralArgumentBuilder<*>
//        if (SharedConstants.IS_RUNNING_IN_IDE) {
//            literalargumentbuilder = (((literalargumentbuilder.then(
//                Commands.literal("export").then(
//                    Commands.argument<Holder.Reference<Any?>?>(
//                        "test", ResourceArgument.resource<Any?>(buildContext, Registries.TEST_INSTANCE)
//                    ).executes(
//                        Command { p_396460_: CommandContext<CommandSourceStack?>? ->
//                            TestCommand.exportTestStructure(
//                                p_396460_!!.getSource(), ResourceArgument.getResource<GameTestInstance?>(
//                                    p_396460_, "test", Registries.TEST_INSTANCE
//                                )
//                            )
//                        })
//                )
//            ) as LiteralArgumentBuilder<*>).then(
//                Commands.literal("exportclosest").executes(Command { p_396473_: CommandContext<CommandSourceStack?>? ->
//                    TestCommand.export(
//                        TestFinder.builder().nearest(p_396473_)
//                    )
//                })
//            ) as LiteralArgumentBuilder<*>).then(
//                Commands.literal("exportthese").executes(Command { p_396433_: CommandContext<CommandSourceStack?>? ->
//                    TestCommand.export(
//                        TestFinder.builder().allNearby(p_396433_)
//                    )
//                })
//            ) as LiteralArgumentBuilder<*>).then(
//                Commands.literal("exportthat").executes(Command { p_396423_: CommandContext<CommandSourceStack?>? ->
//                    TestCommand.export(
//                        TestFinder.builder().lookedAt(p_396423_)
//                    )
//                })
//            ) as LiteralArgumentBuilder<*>
//        }
//
//        dispatcher.register(literalargumentbuilder)
//    }
//
//    fun suggestTestFunction(
//        context: CommandContext<CommandSourceStack?>, builder: SuggestionsBuilder
//    ): CompletableFuture<Suggestions?> {
//        val stream =
//            (context.getSource() as CommandSourceStack).registryAccess().lookupOrThrow<Any?>(Registries.TEST_FUNCTION)
//                .listElements().map<String?> { obj: Holder.Reference<Any?>? -> obj!!.getRegisteredName() }
//        return SharedSuggestionProvider.suggest(stream, builder)
//    }
//
//    private fun resetGameTestInfo(source: CommandSourceStack?, testInfo: GameTestInfo): Int {
//        val testinstanceblockentity: TestInstanceBlockEntity = testInfo.getTestInstanceBlockEntity()
//        Objects.requireNonNull<CommandSourceStack?>(source)
//        testinstanceblockentity.resetTest({ message: Component? -> source!!.sendSystemMessage(message) })
//        return 1
//    }
//
//    private fun toGameTestInfos(
//        source: CommandSourceStack, retryOptions: RetryOptions, posFinder: TestPosFinder
//    ): Stream<GameTestInfo?> {
//        return posFinder.findTestPos()
//            .map({ p_396472_ -> TestCommand.createGameTestInfo(p_396472_, source, retryOptions) })
//            .flatMap({ obj: Optional<*>? -> obj!!.stream() })
//    }
//
//    private fun toGameTestInfo(
//        source: CommandSourceStack, retryOptions: RetryOptions, finder: TestInstanceFinder, rotationSteps: Int
//    ): Stream<GameTestInfo?> {
//        return finder.findTests().filter({ p_466100_ ->
//            TestCommand.verifyStructureExists(
//                source, (p_466100_.value() as GameTestInstance).structure()
//            )
//        }).map({ p_396422_ ->
//            GameTestInfo(
//                p_396422_, StructureUtils.getRotationForRotationSteps(rotationSteps), source.getLevel(), retryOptions
//            )
//        })
//    }
//
//    private fun createGameTestInfo(
//        pos: BlockPos, source: CommandSourceStack, retryOptions: RetryOptions
//    ): Optional<GameTestInfo?> {
//        val serverlevel = source.getLevel()
//        val var5 = serverlevel.getBlockEntity(pos)
//        if (var5 is TestInstanceBlockEntity) {
//            val var10000: Optional<*> = var5.test()
//            val var10001: Registry<*> = source.registryAccess().lookupOrThrow<Any?>(Registries.TEST_INSTANCE)
//            Objects.requireNonNull<Registry<*>?>(var10001)
//            val optional: Optional<Holder.Reference<GameTestInstance?>?> = var10000.flatMap(var10001::get)
//            if (optional.isEmpty()) {
//                source.sendFailure(
//                    Component.translatable(
//                        "commands.test.error.non_existant_test", *arrayOf<Any?>(var5.getTestName())
//                    )
//                )
//                return Optional.empty<GameTestInfo?>()
//            } else {
//                val reference: Holder.Reference<GameTestInstance?> = optional.get() as Holder.Reference<*>
//                val gametestinfo = GameTestInfo(reference, var5.getRotation(), serverlevel, retryOptions)
//                gametestinfo.setTestBlockPos(pos)
//                return if (!TestCommand.verifyStructureExists(
//                        source, gametestinfo.getStructure()
//                    )
//                ) Optional.empty<GameTestInfo?>() else Optional.of<GameTestInfo?>(gametestinfo)
//            }
//        } else {
//            source.sendFailure(
//                Component.translatable(
//                    "commands.test.error.test_instance_not_found.position",
//                    *arrayOf<Any>(pos.getX(), pos.getY(), pos.getZ())
//                )
//            )
//            return Optional.empty<GameTestInfo?>()
//        }
//    }
//
//    @Throws(CommandSyntaxException::class)
//    private fun createNewStructure(
//        source: CommandSourceStack, id: Identifier?, width: Int, height: Int, depth: Int
//    ): Int {
//        if (width <= 48 && height <= 48 && depth <= 48) {
//            val serverlevel = source.getLevel()
//            val blockpos = TestCommand.createTestPositionAround(source)
//            val testinstanceblockentity: TestInstanceBlockEntity =
//                StructureUtils.createNewEmptyTest(id, blockpos, Vec3i(width, height, depth), Rotation.NONE, serverlevel)
//            val blockpos1: BlockPos = testinstanceblockentity.getStructurePos()
//            val blockpos2 = blockpos1.offset(width - 1, 0, depth - 1)
//            BlockPos.betweenClosedStream(blockpos1, blockpos2).forEach { p_414995_: BlockPos? ->
//                serverlevel.setBlockAndUpdate(
//                    p_414995_, Blocks.BEDROCK.defaultBlockState()
//                )
//            }
//            source.sendSuccess(Supplier {
//                Component.translatable(
//                    "commands.test.create.success", *arrayOf<Any?>(testinstanceblockentity.getTestName())
//                )
//            }, true)
//            return 1
//        } else {
//            throw TestCommand.TOO_LARGE.create(48)
//        }
//    }
//
//    @Throws(CommandSyntaxException::class)
//    private fun showPos(source: CommandSourceStack, variableName: String?): Int {
//        val serverplayer = source.getPlayerOrException()
//        val blockhitresult = serverplayer.pick(10.0, 1.0f, false) as BlockHitResult
//        val blockpos = blockhitresult.getBlockPos()
//        val serverlevel = source.getLevel()
//        var optional: Optional<BlockPos?> = StructureUtils.findTestContainingPos(blockpos, 15, serverlevel)
//        if (optional.isEmpty()) {
//            optional = StructureUtils.findTestContainingPos(blockpos, 250, serverlevel)
//        }
//
//        if (optional.isEmpty()) {
//            throw TestCommand.NO_TEST_CONTAINING.create(blockpos.getX(), blockpos.getY(), blockpos.getZ())
//        } else {
//            val var8 = serverlevel.getBlockEntity(optional.get())
//            if (var8 is TestInstanceBlockEntity) {
//                val testinstanceblockentity: TestInstanceBlockEntity? = var8 as TestInstanceBlockEntity?
//                val blockpos2: BlockPos = testinstanceblockentity.getStructurePos()
//                val blockpos1 = blockpos.subtract(blockpos2)
//                val var10000 = blockpos1.getX()
//                val `$$11` = var10000.toString() + ", " + blockpos1.getY() + ", " + blockpos1.getZ()
//                val `$$12`: String? = testinstanceblockentity.getTestName().getString()
//                val `$$13` = Component.translatable(
//                    "commands.test.coordinates", *arrayOf<Any>(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ())
//                ).setStyle(
//                    Style.EMPTY.withBold(true).withColor(ChatFormatting.GREEN)
//                        .withHoverEvent(ShowText(Component.translatable("commands.test.coordinates.copy")))
//                        .withClickEvent(CopyToClipboard("final BlockPos " + variableName + " = new BlockPos(" + `$$11` + ");"))
//                )
//                source.sendSuccess(Supplier {
//                    Component.translatable(
//                        "commands.test.relative_position", *arrayOf<Any?>(`$$12`, `$$13`)
//                    )
//                }, false)
//                serverplayer.connection.send(ClientboundGameTestHighlightPosPacket(blockpos, blockpos1))
//                return 1
//            } else {
//                throw TestCommand.TEST_INSTANCE_COULD_NOT_BE_FOUND.create()
//            }
//        }
//    }
//
//    private fun stopTests(): Int {
//        GameTestTicker.SINGLETON.clear()
//        return 1
//    }
//
//    fun trackAndStartRunner(source: CommandSourceStack?, testRunner: GameTestRunner): Int {
//        testRunner.addListener(TestBatchSummaryDisplayer(source))
//        val multipletesttracker = MultipleTestTracker(testRunner.getTestInfos())
//        multipletesttracker.addListener(TestSummaryDisplayer(source, multipletesttracker))
//        multipletesttracker.addFailureListener(Consumer { p_396449_: GameTestInfo? ->
//            FailedTestTracker.rememberFailedTest(
//                p_396449_.getTestHolder()
//            )
//        })
//        testRunner.start()
//        return 1
//    }
//
//    private fun exportTestStructure(source: CommandSourceStack, testInstance: Holder<GameTestInstance?>): Int {
//        val var10000 = source.getLevel()
//        val var10001: Identifier? = (testInstance.value() as GameTestInstance).structure()
//        Objects.requireNonNull<CommandSourceStack?>(source)
//        return if (!TestInstanceBlockEntity.export(
//                var10000, var10001, { message: Component? -> source.sendSystemMessage(message) })
//        ) 0 else 1
//    }
//
//    private fun verifyStructureExists(source: CommandSourceStack, structure: Identifier): Boolean {
//        if (source.getLevel().getStructureManager().get(structure).isEmpty()) {
//            source.sendFailure(
//                Component.translatable(
//                    "commands.test.error.structure_not_found", *arrayOf<Any?>(
//                        Component.translationArg(structure)
//                    )
//                )
//            )
//            return false
//        } else {
//            return true
//        }
//    }
//
//    private fun createTestPositionAround(source: CommandSourceStack): BlockPos {
//        val blockpos = BlockPos.containing(source.getPosition())
//        val i = source.getLevel().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, blockpos).getY()
//        return BlockPos(blockpos.getX(), i, blockpos.getZ() + 3)
//    }
//
//    @JvmRecord
//    internal data class TestBatchSummaryDisplayer(val source: CommandSourceStack?) : GameTestBatchListener {
//        override fun testBatchStarting(p_319827_: GameTestBatch) {
//            this.source!!.sendSuccess(Supplier {
//                Component.translatable(
//                    "commands.test.batch.starting",
//                    *arrayOf<Any?>(p_319827_.environment().getRegisteredName(), p_319827_.index())
//                )
//            }, true)
//        }
//
//        override fun testBatchFinished(p_320779_: GameTestBatch) {
//        }
//    }
//
//    @JvmRecord
//    data class TestSummaryDisplayer(val source: CommandSourceStack?, val tracker: MultipleTestTracker?) :
//        GameTestListener {
//        override fun testStructureLoaded(p_128064_: GameTestInfo) {
//        }
//
//        override fun testPassed(p_177797_: GameTestInfo, p_320726_: GameTestRunner) {
//            this.showTestSummaryIfAllDone()
//        }
//
//        override fun testFailed(p_128066_: GameTestInfo, p_320567_: GameTestRunner) {
//            this.showTestSummaryIfAllDone()
//        }
//
//        override fun testAddedForRerun(p_319856_: GameTestInfo, p_320528_: GameTestInfo, p_319832_: GameTestRunner) {
//            this.tracker!!.addTestToTrack(p_320528_)
//        }
//
//        private fun showTestSummaryIfAllDone() {
//            if (this.tracker!!.isDone()) {
//                this.source!!.sendSuccess(Supplier {
//                    Component.translatable(
//                        "commands.test.summary", *arrayOf<Any>(this.tracker.getTotalCount())
//                    ).withStyle(ChatFormatting.WHITE)
//                }, true)
//                if (this.tracker.hasFailedRequired()) {
//                    this.source.sendFailure(
//                        Component.translatable(
//                            "commands.test.summary.failed", *arrayOf<Any>(this.tracker.getFailedRequiredCount())
//                        )
//                    )
//                } else {
//                    this.source.sendSuccess(Supplier {
//                        Component.translatable("commands.test.summary.all_required_passed")
//                            .withStyle(ChatFormatting.GREEN)
//                    }, true)
//                }
//
//                if (this.tracker.hasFailedOptional()) {
//                    this.source.sendSystemMessage(
//                        Component.translatable(
//                            "commands.test.summary.optional_failed",
//                            *arrayOf<Any>(this.tracker.getFailedOptionalCount())
//                        )
//                    )
//                }
//            }
//        }
//    }
//}