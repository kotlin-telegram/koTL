package kotl.serialization.extensions

import kotl.serialization.annotation.TLFunction
import kotl.serialization.annotation.TLType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor

@OptIn(ExperimentalSerializationApi::class)
internal val SerialDescriptor.tlFunction: TLFunction?
    get() = annotations.filterIsInstance<TLFunction>().firstOrNull()

@OptIn(ExperimentalSerializationApi::class)
internal val SerialDescriptor.tlType: TLType?
    get() = annotations.filterIsInstance<TLType>().firstOrNull()
