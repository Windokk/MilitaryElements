
package com.windokkstudio.militaryelements.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Collections;

import static com.windokkstudio.militaryelements.blocks.CamouflageNet.NetEnum.DIRT;
import static net.minecraft.tags.BlockTags.SAND;

public class CamouflageNet extends Block {

    public static final EnumProperty NET_ENUM_BLOCKSTATE = EnumProperty.create("net_enum_property", NetEnum.class);
    public CamouflageNet() {
        super(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.GRAVEL).strength(1f, 10f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NET_ENUM_BLOCKSTATE, DIRT));

    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 0;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return box(0, 0, 0, 16, 2, 16);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(blockstate, world, pos, oldState, moving);
        double x  = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        if ((world.getBlockState(new BlockPos(x, y - 3, z))).getBlock() == Blocks.DIRT){
            world.setBlock(pos, blockstate.setValue(NET_ENUM_BLOCKSTATE,NetEnum.DIRT),3);
        } else if ((world.getBlockState(new BlockPos(x, y - 3, z))).getBlock() == Blocks.SAND) {
            world.setBlock(pos, blockstate.setValue(NET_ENUM_BLOCKSTATE,NetEnum.SAND),3);
        }
    }




    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NET_ENUM_BLOCKSTATE);
    }

    public static enum NetEnum implements StringRepresentable{
        DIRT("dirt"),
        SAND("sand");
        private final String name;

        NetEnum(String p_122456_) {
            this.name = p_122456_;
        }

        public String getName() {
            return this.name;
        }
        public String toString() {
            return this.name;
        }
        public String getSerializedName() {
            return this.name;
        }
    }
}
