package concerrox.ported.content.thegardenawakens.palemoss;

import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.Mth;

public enum Quadrant {
    R0(0),
    R90(1),
    R180(2),
    R270(3);

    public static final Codec<Quadrant> CODEC = Codec.INT.comapFlatMap((p_405170_) -> {
        DataResult var10000;
        switch (Mth.positiveModulo(p_405170_, 360)) {
            case 0 -> var10000 = DataResult.success(R0);
            case 90 -> var10000 = DataResult.success(R90);
            case 180 -> var10000 = DataResult.success(R180);
            case 270 -> var10000 = DataResult.success(R270);
            default -> var10000 = DataResult.error(() -> "Invalid rotation " + p_405170_ + " found, only 0/90/180/270 allowed");
        }

        return var10000;
    }, (p_405217_) -> {
        Integer var10000;
        switch (p_405217_.ordinal()) {
            case 0 -> var10000 = 0;
            case 1 -> var10000 = 90;
            case 2 -> var10000 = 180;
            case 3 -> var10000 = 270;
            default -> throw new MatchException((String)null, (Throwable)null);
        }

        return var10000;
    });
    public final int shift;

    private Quadrant(int shift) {
        this.shift = shift;
    }

    /** @deprecated */
    @Deprecated
    public static Quadrant parseJson(int rotation) {
        Quadrant var10000;
        switch (Mth.positiveModulo(rotation, 360)) {
            case 0 -> var10000 = R0;
            case 90 -> var10000 = R90;
            case 180 -> var10000 = R180;
            case 270 -> var10000 = R270;
            default -> throw new JsonParseException("Invalid rotation " + rotation + " found, only 0/90/180/270 allowed");
        }

        return var10000;
    }

    public int rotateVertexIndex(int shift) {
        return (shift + this.shift) % 4;
    }
}
