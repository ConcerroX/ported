package concerrox.ported.mixin;

import concerrox.ported.mixininterface.BundleContentsExtensions;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BundleContents.class)
public abstract class BundleContentsMixin implements BundleContentsExtensions {

    @Shadow
    public abstract int size();

    @Unique
    private int ported$selectedItem = -1;

    @Override
    @Unique
    public int getNumberOfItemsToShow() {
        int i = this.size();
        int j = i > 12 ? 11 : 12;
        int k = i % 4;
        int l = k == 0 ? 0 : 4 - k;
        return Math.min(i, j - l);
    }

    @Override
    @Unique
    public int getSelectedItem() {
        return ported$selectedItem;
    }

    @Override
    @Unique
    public void setSelectedItem(int item) {
        ported$selectedItem = item;
    }

    @Override
    @Unique
    public boolean hasSelectedItem() {
        return ported$selectedItem != -1;
    }

}
