package kotl.serialization.extensions

import kotl.serialization.annotation.Crc32
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor

@OptIn(ExperimentalSerializationApi::class)
internal val SerialDescriptor.crc32: Crc32?
    get() = annotations.filterIsInstance<Crc32>().firstOrNull()
