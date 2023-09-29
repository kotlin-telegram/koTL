package kotl.core.descriptor

public data object TLStringDescriptor : TLPrimitiveDescriptor {
    /**
     * The values of type string look differently depending on the
     * length L of the string being serialized:
     *
     * If L <= 253, the serialization contains one byte with the value of L,
     * then L bytes of the string followed by 0 to 3 characters containing 0,
     * such that the overall length of the value be divisible by 4, whereupon
     * all of this is interpreted as a sequence of int(L/4)+1 32-bit numbers.
     *
     * If L >= 254, the serialization contains byte 254, followed by 3 bytes
     * with the string length L, followed by L bytes of the string,
     * further followed by 0 to 3 null padding bytes.
     */
    public const val STRING_SIZE_MAGIC_NUMBER: UByte = 254_u
}
