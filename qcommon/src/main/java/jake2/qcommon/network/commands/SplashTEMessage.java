package jake2.qcommon.network.commands;

import jake2.qcommon.MSG;
import jake2.qcommon.sizebuf_t;

import java.util.Collection;
import java.util.Set;

import static jake2.qcommon.Defines.*;

public class SplashTEMessage extends TEMessage {

    public static final Collection<Integer> SUBTYPES = Set.of(
            TE_SPLASH,
            TE_LASER_SPARKS,
            TE_WELDING_SPARKS,
            TE_TUNNEL_SPARKS
    );

    public SplashTEMessage(int style) {
        super(style);
    }

    public SplashTEMessage(int style, int count, float[] position, float[] direction, int param) {
        super(style);
        this.count = count;
        this.position = position;
        this.direction = direction;
        this.param = param;
    }

    public int count;
    public float[] position;
    public float[] direction;
    public int param;

    @Override
    protected void writeProperties(sizebuf_t buffer) {
        super.writeProperties(buffer);
        MSG.WriteByte(buffer, count);
        MSG.WritePos(buffer, position);
        MSG.WriteDir(buffer, direction);
        MSG.WriteByte(buffer, param);
    }

    @Override
    void parse(sizebuf_t buffer) {
        super.parse(buffer);
        count = MSG.ReadByte(buffer);
        position = new float[3];
        MSG.ReadPos(buffer, position);
        direction = new float[3];
        MSG.ReadDir(buffer, direction);
        param = MSG.ReadByte(buffer);
    }
}