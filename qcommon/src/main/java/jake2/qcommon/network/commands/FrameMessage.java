package jake2.qcommon.network.commands;

import jake2.qcommon.MSG;
import jake2.qcommon.SZ;
import jake2.qcommon.network.NetworkCommandType;
import jake2.qcommon.sizebuf_t;

import java.util.Arrays;

import static jake2.qcommon.Defines.MAX_MAP_AREAS;

public class FrameMessage extends NetworkMessage {
    public int frameNumber;
    public int lastFrame;
    public int suppressCount;
    // todo: make private
    public int areaBitsLength;
    public byte[] areaBits;

    public FrameMessage() {
        super(NetworkCommandType.svc_frame);
    }

    /**
     * @param lastFrame what we are delta'ing from
     * @param suppressCount rate dropped packets
     */
    public FrameMessage(int frameNumber, int lastFrame, int suppressCount, int areaBitsLength, byte[] areaBits) {
        this();
        this.frameNumber = frameNumber;
        this.lastFrame = lastFrame;
        this.suppressCount = suppressCount;
        this.areaBitsLength = areaBitsLength;
        this.areaBits = areaBits;
    }

    @Override
    protected void writeProperties(sizebuf_t buffer) {
        MSG.WriteLong(buffer, frameNumber);
        MSG.WriteLong(buffer, lastFrame); // what we are delta'ing from
        MSG.WriteByte(buffer, suppressCount); // rate dropped packets
        MSG.WriteByte(buffer, areaBitsLength);
        SZ.Write(buffer, areaBits, areaBitsLength);
    }

    @Override
    void parse(sizebuf_t buffer) {
        frameNumber = MSG.ReadLong(buffer);
        lastFrame = MSG.ReadLong(buffer);
        // BIG HACK to let old demos continue to work
        // if (ClientGlobals.cls.serverProtocol != 26)
        // fixme: do not read otherwise?
        suppressCount = MSG.ReadByte(buffer);
        areaBitsLength = MSG.ReadByte(buffer);
        areaBits = new byte[MAX_MAP_AREAS / 8];
        MSG.ReadData(buffer, areaBits, areaBitsLength);
    }

    @Override
    public String toString() {
        return "FrameMessage{" +
                "frameNumber=" + frameNumber +
                ", lastFrame=" + lastFrame +
                ", suppressCount=" + suppressCount +
                ", areaBitsLength=" + areaBitsLength +
                ", areaBits=" + Arrays.toString(areaBits) +
                '}';
    }
}