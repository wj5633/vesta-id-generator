package com.wj5633.vesta.bean;

/**
 * Created at 2019/7/16 14:29.
 *
 * @author wangjie
 * @version 1.0.0
 */

public class IdMetaFactory {

    private static IdMeta maxPeak = IdMeta.builder()
            .machineBits((byte) 10)
            .seqBits((byte) 20)
            .timeBits((byte) 30)
            .genMethodBits((byte) 2)
            .typeBits((byte) 1)
            .versionBits((byte) 1)
            .build();
    private static IdMeta minGranularity = IdMeta.builder()
            .machineBits((byte) 10)
            .seqBits((byte) 10)
            .timeBits((byte) 40)
            .genMethodBits((byte) 2)
            .typeBits((byte) 1)
            .versionBits((byte) 1)
            .build();

    public static IdMeta getIdMeta(IdType idType) {
        if (IdType.MAX_PEAK == idType) {
            return maxPeak;
        } else if (IdType.MIN_GRANULARITY == idType) {
            return minGranularity;
        }
        return null;
    }
}
